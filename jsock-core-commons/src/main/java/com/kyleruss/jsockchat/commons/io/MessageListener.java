//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.io;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.DefaultActionHandler;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.MessageBean;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * A server that listens for incomming messages
 * Should handle messages based on their bean type
 * @param <T> client -> RequestMessage, server -> ResponseMessage
 */
public abstract class MessageListener<T extends Message> extends Thread implements MessageHandler
{
    protected Socket socket;
    protected MessageHandler messageHandler;
    
    public MessageListener(Socket socket)
    {
        this.socket =   socket;
    }
    
    @Override
    public ActionHandler getActionHandler(MessageBean bean)
    {
        if(messageHandler != null)
            return messageHandler.getActionHandler(bean);
        else 
            return new DefaultActionHandler();
    }
    
    public void setMessageHandler(MessageHandler messageHandler)
    {
        this.messageHandler =   messageHandler;
    }
    
    /**
     * Should perform the action on the passed mesage
     * @param message The message to be handled
     */
    protected abstract void handleReceivedMessage(T message);
    
    /**
     * Should block and listen for incomming messages
     * @param inputStream The input stream to read from
     * @return A The read message; null if exception
     */
    protected abstract T getMessage(ObjectInputStream inputStream);
    
    /**
     * Performs some cleanup duties on the stream and its socket
     * @param inputStream The current reading inputStream
     */
    protected abstract void handleCleanup(ObjectInputStream inputStream);
    
    public Socket getSocket()
    {
        return socket;
    }
    
    @Override
    public void run()
    {
        if(socket == null) return;
            
        try(ObjectInputStream inputStream   =   new ObjectInputStream(socket.getInputStream()))
        {
            T message;
            while((message = getMessage(inputStream)) != null)
            {
                MessageRunner handler  =   new MessageRunner(message);
                handler.start();
            }
            
            handleCleanup(inputStream);
        }
        
        catch(IOException e)
        {
            System.out.println("[MessageListener@run]: " + e.getMessage());
            handleCleanup(null);
        }
    }
    
    protected class MessageRunner extends Thread
    {
        private final T message;
        
        public MessageRunner(T message)
        {
            this.message    =   message;
        }
        
        @Override
        public void run()
        {
            handleReceivedMessage(message);
        }
    }
}
