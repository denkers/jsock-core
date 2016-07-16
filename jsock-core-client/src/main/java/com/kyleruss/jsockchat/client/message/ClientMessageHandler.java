//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.Message;

/**
 * Defines two methods for response handeling 
 * Decided based on on witness status
 */
public interface ClientMessageHandler extends ActionHandler
{
    /**
     * Clients should execute this action if they are a witness
     * @param message The response to handle
     */
    public void witnessAction(Message message);
}