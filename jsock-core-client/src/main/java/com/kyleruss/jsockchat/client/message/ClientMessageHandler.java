//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.commons.message.ResponseMessage;

/**
 * Defines two methods for response handeling 
 * Decided based on on witness status
 */
public interface ClientMessageHandler
{
    /**
     * Clients should execute this action if they are not a witness
     * @param response The response to handle
     */
    public void clientAction(ResponseMessage response);
    
    /**
     * Clients should execute this action if they are a witness
     * @param response The response to handle
     */
    public void witnessAction(ResponseMessage response);
}