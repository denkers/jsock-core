//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.core;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Maintains the client sockets and output stream
 * Comparable to io/UserSocket in jsockchat-server
 */
public class SocketManager 
{
    private static SocketManager instance;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;
    private ObjectOutputStream tcpOutputStream;
    private String userID;

    private SocketManager() {}
    
    public synchronized void initSockets(String host, int port)
    {
        try
        {
            tcpSocket       =   new Socket(host, port);
            udpSocket       =   new DatagramSocket();
            tcpOutputStream =   null;
            ClientManager.getInstance().startServers();
        }

        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to connect to server", "Connection failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public int getUdpPort()
    {
        if(udpSocket == null) return -1;
        return udpSocket.getLocalPort();
    }
    
    public ObjectOutputStream getTCPOutputStream() throws IOException
    {
        if(tcpSocket == null) return null;
        
        if(tcpOutputStream == null) tcpOutputStream = new ObjectOutputStream(tcpSocket.getOutputStream());
        return tcpOutputStream;
    }

    /**
     * flushes and closes output streams
     * closes TCP and UDP sockets
     */
    public synchronized void cleanUp()
    {
        try
        {
            if(tcpOutputStream != null)
            {
                tcpOutputStream.flush();
                tcpOutputStream.close();
            }

            if(tcpSocket != null) tcpSocket.close();
            if(udpSocket != null) udpSocket.close();
        }
        
        catch(IOException e)
        {
            System.out.println("[SocketManager@cleanUp]: " + e.getMessage());
        }
        
        finally
        {
            ClientManager.getInstance().disconnectUser();
        }
    }
    
    public Socket getTcpSocket()
    {
        return tcpSocket;
    }
    
    public DatagramSocket getUdpSocket()
    {
        return udpSocket;
    }
    
    public String getUserID()
    {
        return userID;
    }
    
    public String getActiveUser()
    {
        if(userID == null)
            return tcpSocket.getLocalSocketAddress().toString();
        else return userID;
    }
    
    public void setUserID(String userID)
    {
        this.userID =   userID;
    }
    
    public static SocketManager getInstance()
    {
        if(instance == null) instance = new SocketManager();
        return instance;
    }
    
}
