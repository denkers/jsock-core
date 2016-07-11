//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage 
{
    private String sender;
    private String content;
    private Date dateRecieved;
    private final boolean isServerMessage;
    
    public ChatMessage(String sender, String content, boolean isServerMessage)
    {
        this.sender             =   sender;
        this.content            =   content;
        this.isServerMessage    =   isServerMessage;
        dateRecieved            =   new Date();
    }
    
    public void setDateReceived(Date dateRecieved)
    {
        this.dateRecieved   =   dateRecieved;
    }
    
    public Date getDateRecieved()
    {
        return dateRecieved;
    }
    
    public boolean isServerMessage()
    {
        return isServerMessage;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender) 
    {
        this.sender = sender;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }
    
    @Override
    public String toString()
    {
        SimpleDateFormat formatter  =   new SimpleDateFormat("HH:mm:ss");
        String recDate              =   formatter.format(dateRecieved);
        String message              =   MessageFormat.format("[{0} {1}]: {2}", recDate, sender, content);
        return message;
    }
}
