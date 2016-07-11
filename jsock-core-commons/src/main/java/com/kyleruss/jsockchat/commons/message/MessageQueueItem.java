//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

import java.io.ObjectOutputStream;

/**
 * Maintains a message and outputStream for output/read later
 */
public class MessageQueueItem
{
    private final ObjectOutputStream destOutputStream;
    private Message message;
    
    public MessageQueueItem(ObjectOutputStream destination, Message message)
    {
        this.destOutputStream       =   destination;
        this.message                =   message;    
    }
    
    public ObjectOutputStream getDestinationOutputStream()
    {
        return destOutputStream;
    }
    
    public Message getMessage()
    {
        return message;
    }
    
    public void setMessage(Message message)
    {
        this.message    =   message;
    }
}
