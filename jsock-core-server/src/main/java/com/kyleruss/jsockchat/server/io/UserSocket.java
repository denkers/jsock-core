//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Maintains a servingUser's sockets
 * Has functions for cleaning up its own sockets
 */
public class UserSocket
{
    private Socket socket;
    private ObjectOutputStream sockOutputStream;
    private int udpPort;
    private String address;
    
    public UserSocket(Socket socket)
    {
        this.socket =   socket;
        udpPort     =   -1;
    }
    
    /**
     * flushes & closes the output stream & closes the socket
     */
    public void cleanUp()
    {
        try
        {
            if(sockOutputStream != null)
            {
                sockOutputStream.flush();
                sockOutputStream.close();
            }
            
            if(socket != null && !socket.isClosed())
                socket.close();
        }
        
        catch(IOException e)
        {
            System.out.println("[UserSocket@cleanUp]: " + e.getMessage());
        }
    }
    
    public Socket getSocket()
    {
        return socket;
    }
    
    public void setSocket(Socket socket)
    {
        this.socket  =   socket;
    }
    
    public void setUdpPort(int udpPort)
    {
        this.udpPort    =   udpPort;
    }
    
    public int getUdpPort()
    {
        return udpPort;
    }
    
    public String getAddress()
    {
        if(address == null && socket != null)
            address = socket.getInetAddress().getHostAddress();
        
        return address;
    }
    
    public ObjectOutputStream getSocketOutputStream() throws IOException
    {
        if(socket == null) return null;
        
        if(sockOutputStream == null) sockOutputStream = new ObjectOutputStream(socket.getOutputStream());
        return sockOutputStream;
    }
}
