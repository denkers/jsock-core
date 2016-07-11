//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.io;

import com.kyleruss.jsockchat.server.core.ServerConfig;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import com.kyleruss.jsockchat.server.gui.ServerStatusPanel;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server for handling incomming messages
 * Initializes the servers TCP/UDP sockets
 * 
 */
public final class MessageServer extends SyncedServer
{
    private static MessageServer instance;
    private ServerSocket serverSocket;
    
    private MessageServer()
    {
        initSocket();
    }
    
    /**
     * Attempts to initialize the sockets
     * Notifies user if initialization fails
     */
    protected void initSocket()
    {
        try
        {
            serverSocket    =   new ServerSocket(ServerConfig.MESSAGE_SERVER_PORT);
            serverSocket.setSoTimeout(ServerConfig.MESSAGE_SERVER_TIMEOUT);
            
            LoggingList.sendLogMessage(new LogMessage("[Message Server] Initialized sever socket on port " + ServerConfig.MESSAGE_SERVER_PORT, 
            AppResources.getInstance().getServerOkImage()));
        }
        
        catch(IOException e)
        {
            LoggingList.sendLogMessage(new LogMessage("[Message Server] Failed to initialize sever socket on port " + ServerConfig.MESSAGE_SERVER_PORT, 
            AppResources.getInstance().getServerOkImage()));
        }
    }
    
    @Override
    public synchronized void stopServer()
    {
        if(isStopped || serverSocket == null || serverSocket.isClosed())
            return;
        
        try
        {
            serverSocket.close();
            isStopped = true;
        }
        
        catch(IOException e)
        {
            System.out.println("[MessageServer@stopServer]: " + e.getMessage());
        }
    }
    
    
    /**
     * Creates a new listener that listens on the passed socket
     * @param socket The socket instance to listen on in the new listener
     */
    private void handleClientSocket(Socket socket)
    {
        ServerMessageListener messageHandler =   new ServerMessageListener(socket);
        messageHandler.start();
    }

    @Override
    public boolean isStopped() 
    {
        return serverSocket == null || serverSocket.isClosed();
    }

    /**
     * Waits for a new socket
     * Creates a new listener that listens on the socket
     */
    @Override
    protected synchronized void runServerOperations() 
    {
        try
        {
            Socket clientSocket =   serverSocket.accept();
            handleClientSocket(clientSocket);
        }
            
        catch(IOException e)
        {
            System.out.println("[MessageServer@runServerOperations]: " + e.getMessage());
        }
    }
    
    /**
     * Sets isServing to the passed serving
     * @see com.kyleruss.jsockchat.server.io.SyncedServer#SyncedServer() 
     * @param serving true to enable listening and false to pause it
     */
    @Override
    public synchronized void setServingSync(boolean serving)
    {
        if(isServing == serving) return;
        
        super.setServingSync(serving);
        ServerStatusPanel.getInstance().setServerStatus(serving, ServerConfig.MESSAGE_LISTEN_SERVER_CODE);
        
        LoggingList.sendLogMessage(new LogMessage("[Message Server] Server has " + (serving? "resumed" : "paused"), 
            AppResources.getInstance().getServerOkImage()));
    }
    
    public static MessageServer getInstance()
    {
        if(instance == null) instance = new MessageServer();
        return instance;
    }
}
