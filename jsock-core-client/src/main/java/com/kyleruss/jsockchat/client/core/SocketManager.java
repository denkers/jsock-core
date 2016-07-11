//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.core;

import com.kyleruss.jsockchat.client.gui.ClientMenuBar;
import com.kyleruss.jsockchat.client.gui.ClientPanel;
import com.kyleruss.jsockchat.client.gui.ConnectPanel;
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

    private SocketManager() {}
    
    public void initSockets(String host, int port)
    {
        Thread sockThread   =   new Thread(()->
        {
            ConnectPanel connectView =   ClientPanel.getInstance().getConnectView();
            
            try
            {
                tcpSocket       =   new Socket(host, port);
                udpSocket       =   new DatagramSocket();
                tcpOutputStream =   null;
                ClientManager.getInstance().startServers();
                connectView.showProcessing(false);
                ClientPanel.getInstance().changeView(ClientConfig.LOGIN_VIEW_CARD);
                
                ClientMenuBar menu  =   ClientMenuBar.getInstance();
                menu.getItem("loginItem").setEnabled(true);
                menu.getItem("registerItem").setEnabled(true);
                menu.getItem("logoutItem").setEnabled(false);
                menu.getItem("dcItem").setEnabled(true);
            }
        
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(null, "Failed to connect to server", "Connection failed", JOptionPane.ERROR_MESSAGE);
                connectView.showProcessing(false);
            }
            
        });
        
        sockThread.start();
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
    
    public static SocketManager getInstance()
    {
        if(instance == null) instance = new SocketManager();
        return instance;
    }
    
}
