//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Has definitions for getting timestamps and content of message
 */
public interface Message extends Serializable
{
    /**
     * @return The time the message was sent
     */
    public Date getTimeSent();
    
    /**
     * @return The message content
     */
    public String getDescription();
    
    public Map getProperties();
    
    public Object getProperty(String propertyName);
    
    public void setProperty(String propertyName, Object propertyValue);
    
    public void setProperties(Map properties);
}
