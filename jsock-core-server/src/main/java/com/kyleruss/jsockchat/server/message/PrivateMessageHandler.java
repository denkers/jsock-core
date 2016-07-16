//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.PrivateMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import java.io.IOException;

public class PrivateMessageHandler implements ActionHandler
{
    @Override
    public void performAction(Message message) 
    {
        RequestMessage request      =   (RequestMessage) message;
        PrivateMsgBean bean         =   (PrivateMsgBean) request.getMessageBean();
        String destinationUser      =   bean.getDestinationUser();
        String source               =   request.getUserSource();
        ResponseMessage response    =   new ResponseMessage(request);
        String destination;
        
        if(destinationUser == null)
        {
            response.setStatus(false);
            response.setDescription("User not found");
            destination =   source;
        }
        
        else
        {
            response.setStatus(true);
            destination =   bean.getDestinationUser();
        }
        
        try 
        { 
            SocketManager.getInstance().sendMessageToUser(destination, response); 
            
            if(destinationUser != null)
            {
                LoggingManager.log("[Private message] User '" + source + "' has sent a private message to user '" + destinationUser); 
            }
        }
        
        catch(IOException e)
        {
            System.out.println("[PrivateMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
