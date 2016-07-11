//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

import java.util.Date;

/**
 * A simple bean used for maintaining message content and timestamps
 * Should be coupled with a useful implementation
 */
public abstract class AbstractMessage implements Message
{
    private Date timeSent;
    private String description;
    
    public AbstractMessage()
    {
        timeSent    =   new Date();
        description =   "";
    }
    
    @Override
    public Date getTimeSent()
    {
        return timeSent;
    }
    
    public void setTimeSent(Date timeSent)
    {
        this.timeSent   =   timeSent;
    }
    
    @Override
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description    =   description;
    }
}
