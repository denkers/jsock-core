//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple bean used for maintaining message content and timestamps
 * Should be coupled with a useful implementation
 */
public abstract class AbstractMessage implements Message
{
    private Date timeSent;
    private String description;
    private Map properties;
    
    public AbstractMessage()
    {
        timeSent    =   new Date();
        description =   "";
        properties  =   new HashMap<>();
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
    
    
    
    @Override
    public String toString()
    {
        return "[Message]\nTime sent: " + timeSent + "\nDescription: " + description;
    }

    @Override
    public Map getProperties()
    {
        return properties;
    }

    @Override
    public Object getProperty(String propertyName) 
    {
        return properties.get(propertyName);
    }

    @Override
    public void setProperty(String propertyName, Object propertyValue) 
    {
        properties.put(propertyName, propertyValue);
    }

    @Override
    public void setProperties(Map properties) 
    {
        this.properties =   properties;
    }
}
