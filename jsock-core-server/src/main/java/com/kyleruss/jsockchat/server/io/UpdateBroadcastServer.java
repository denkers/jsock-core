//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.io;

import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.ServerConfig;
import com.kyleruss.jsockchat.server.core.ServerManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A server that periodically pushes updates via UDP to clients
 * Sends a UpdateDump every ClientConfig.BROADCAST_DELAY
 */
public class UpdateBroadcastServer extends SyncedServer
{
    private static UpdateBroadcastServer instance;
    private DatagramSocket socket;
    
    private UpdateBroadcastServer()
    {
        initSocket();
    }
    
    /**
     * Initializes the servers socket
     * Server stops if failed 
     */
    private void initSocket()
    {
        try
        {
            socket  =   new DatagramSocket();
            LoggingManager.log("[Update Broadcast Server] Sending updates to online users every " + (ServerConfig.BROADCAST_DELAY / 1000) + " second(s)");
        }
        
        catch(SocketException e)
        {
            System.out.println("[ListBroadcastServer@initSocket]: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isStopped() 
    {
        return isStopped || socket == null || socket.isClosed();
    }

    protected DatagramSocket getSocket()
    {
        return socket;
    }
    
    /**
     * Sends the passed update dump to the user
     * @param updates The created update dump
     * @param user The user to send the update dump to
     */
    protected synchronized void sendUpdates(UpdateBeanDump updates, String user) throws IOException
    {
        UserSocket userSock             =       SocketManager.getInstance().get(user);
        int port                        =       userSock.getUdpPort();
        if(port == -1) return;
        
        InetAddress host                =       InetAddress.getByName(userSock.getAddress());
        ByteArrayOutputStream baos      =   new ByteArrayOutputStream();
        try(ObjectOutputStream oos      =   new ObjectOutputStream(baos))
        {
            oos.writeObject(updates);

            byte[] bData                =   baos.toByteArray();
            DatagramPacket packet       =   new DatagramPacket(bData, bData.length, host, port);
            socket.send(packet);
        }
    }
    
    protected synchronized void updateUsers()
    {
        Collection<String> users     =   SocketManager.getInstance().getDataKeys();
        
        for(String user : users)
        {
            UpdateBeanDump updates   =   ServerManager.getInstance().prepareUpdates(user);
            
            try { sendUpdates(updates, user); } 
            catch(IOException e)
            {
                LoggingManager.log("[Update Broadcast Server] Failed to send updates to user '" + user + "'");
            }
        }
    }
    
    @Override
    public synchronized void setServingSync(boolean serving)
    {
        if(isServing == serving) return;
        
        super.setServingSync(serving);
        
        LoggingManager.log("[Update broadcast Server] Server has " + (serving? "resumed" : "paused")); 
    }

    @Override
    protected synchronized void runServerOperations()
    {
        try
        {
            wait(ServerConfig.BROADCAST_DELAY);
            updateUsers();
        }
        
        catch(InterruptedException e)
        {
            System.out.println("[AbstractBroadcastServer@runServerOperations]: " + e.getMessage());
        }
    }
    
    public static UpdateBroadcastServer getInstance()
    {
        if(instance == null) instance = new UpdateBroadcastServer();
        return instance;
    }
}
