//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.io;

import com.kyleruss.jsockchat.commons.io.MessageSender;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.SocketManager;

/**
 * A server for sending messages in a queue
 * @see com.kyleruss.jsockchat.commons.io.MessageSender
 * @author denker
 */
public class ServerMessageSender extends MessageSender
{
    private static ServerMessageSender instance;
    private boolean isSending;
    
    private ServerMessageSender()
    {
        isSending = true;
    }
    
    /**
     * gets the parent lock -> locks if the message queue is empty
     * get this lock -> if !isSending (paused)
     */
    @Override
    public synchronized void getLock() 
    {
        super.getLock();
        
        if(!isSending)
        {
            try { wait(); }

            catch(InterruptedException e)
            {
                System.out.println("[ServerMessageSender@getLock]: " + e.getMessage());
            }
        }
    }
    
    /**
     * Resumes/pauses the server 
     * @param sending The status of the server
     */
    public synchronized void setSending(boolean sending)
    {
        if(isSending == sending) return;
        
        isSending  =   sending;
        notify();
        
        
        LoggingManager.log("[Message Send Server] Server has " + (sending? "resumed" : "paused"));
    }
    
    /**
     * Cleans up the associated socket 
     * @param source The user who's socket to clean up
     */
    @Override
    protected void cleanUp(String source)
    {
        SocketManager.getInstance().clientExit(source);
    }
    
    @Override
    protected boolean isStopped() 
    {
        return false;
    }
    
    public static ServerMessageSender getInstance()
    {
        if(instance == null) instance   =   new ServerMessageSender();
        return instance;
    }
}
