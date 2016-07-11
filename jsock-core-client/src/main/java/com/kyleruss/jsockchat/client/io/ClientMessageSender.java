//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.io;

import com.kyleruss.jsockchat.client.core.SocketManager;
import com.kyleruss.jsockchat.commons.io.MessageSender;

/**
 * A mesage sending server 
 * Adopts implementation from MessageSender
 */
public class ClientMessageSender extends MessageSender
{
    @Override
    protected void cleanUp(String source)
    {
        SocketManager.getInstance().cleanUp();
    }
    
    @Override
    protected boolean isStopped() 
    {
        return false;
    }
}
