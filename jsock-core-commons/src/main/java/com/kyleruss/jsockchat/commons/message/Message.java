//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

import java.io.Serializable;
import java.util.Date;

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
}
