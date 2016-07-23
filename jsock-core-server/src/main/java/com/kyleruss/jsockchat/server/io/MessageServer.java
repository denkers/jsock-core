//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.io;

import com.kyleruss.jsockchat.commons.io.MessageHandler;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.ServerConfig;
import com.kyleruss.jsockchat.server.message.DefaultMessageHandler;
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
    private MessageHandler messageHandler;
    
    private MessageServer()
    {
        initSocket();
        messageHandler  =   new DefaultMessageHandler();
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
            
            LoggingManager.log("[Message Server] Initialized sever socket on port " + ServerConfig.MESSAGE_SERVER_PORT);
        }
        
        catch(IOException e)
        {
            LoggingManager.log("[Message Server] Failed to initialize sever socket on port " + ServerConfig.MESSAGE_SERVER_PORT);
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
        ServerMessageListener messageListener =   new ServerMessageListener(socket);
        messageListener.setMessageHandler(messageHandler);
        messageListener.start();
    }
    
    public void setMessageHandler(MessageHandler messageHandler)
    {
        this.messageHandler =   messageHandler;
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
        
        LoggingManager.log("[Message Server] Server has " + (serving? "resumed" : "paused"));
    }
    
    public static MessageServer getInstance()
    {
        if(instance == null) instance = new MessageServer();
        return instance;
    }
}
