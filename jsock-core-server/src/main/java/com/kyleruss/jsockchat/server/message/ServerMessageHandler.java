//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.RequestMessage;

/**
 * Defines an entity that can process a client request
 */
public interface ServerMessageHandler
{
    /**
     * Should perform some task on or for the request
     * @param request The request to handle
     */
    public abstract void serverAction(RequestMessage request);
}
