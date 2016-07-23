//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.io;

import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A server that sends responses from a queue of messages
 */
public abstract class MessageSender extends Thread
{
    private final Queue<MessageQueueItem> messageQueue;
    
    public MessageSender()
    {
        messageQueue    =   new LinkedList<>();   
    }
    
    /**
     * @return true when the server if finished
     */
    protected abstract boolean isStopped();
    
    
    /**
     * Performs a cleanup for the users socket
     * @param source The source whose UserSocket should be cleaned
     */
    protected abstract void cleanUp(String source);
    
    /**
     * Retrieves the lock if it is not empty
     * Otherwise block
     */
    protected synchronized void getLock()
    {
        try
        {
            if(messageQueue.isEmpty())
                wait();
        }

        catch(InterruptedException e)
        {
            System.out.println("[RequestSender@getLock]: " + e.getMessage());
        }
    }
    
    /**
     * Adds a message to the queue to be sent
     * @param message A message to be sent
     */
    public synchronized void addMessage(MessageQueueItem message)
    {
        messageQueue.add(message);
        notify();
    }
    
    /**
     * Sends a message to the message source
     * @param message The message to be sent
     * @param outputStream The users current ouputStream to write to
     */
   protected void sendMessage(Message message, ObjectOutputStream outputStream) 
   { 
        try 
        { 
            outputStream.writeObject(message); 
        }
        catch(IOException e)
        {
            String source   =   null;
            if(message instanceof RequestMessage)
                source = ((RequestMessage) message).getUserSource();

            else if(message instanceof ResponseMessage)
                source = ((ResponseMessage) message).getRequestMessage().getUserSource();

            if(source != null) cleanUp(source);
        }
    }
    
    
    @Override
    public void run()
    {
        while(!isStopped())
        {
            getLock();
            MessageQueueItem item   =   messageQueue.poll();
            Message message         =   item.getMessage();
            ObjectOutputStream oos  =   item.getDestinationOutputStream();
            sendMessage(message, oos);
        }
    }
}
