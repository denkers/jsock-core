//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.AcceptFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.server.core.UserManager;
import com.kyleruss.jsockchat.server.db.DBFriends;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import java.io.IOException;

public class AcceptFriendMessageHandler implements ServerMessageHandler
{
    @Override
    public void serverAction(RequestMessage request) 
    {
        AcceptFriendMsgBean bean    =   (AcceptFriendMsgBean) request.getMessageBean();
        String source               =   request.getUserSource();
        boolean result              =   DBFriends.getInstance().respondToFriendRequest(bean);
        
        ResponseMessage response    =   new ResponseMessage(request);
        String responseMsg          =   result? "Successfully responded to pending request" : "Failed to respond to pending request";
        response.setStatus(result);
        response.setDescription(responseMsg);
        
        try 
        { 
            UserManager.getInstance().sendMessageToUser(source, response); 
            
            if(result)
            {
                ResponseMessage senderResponse  =   new ResponseMessage(request);
                senderResponse.setStatus(true);
                senderResponse.setDescription(source + " has accepted your friend request");
                UserManager.getInstance().sendMessageToUser(bean.getFromUser(), senderResponse); 
            }
        } 
        catch(IOException e)
        {
            System.out.println("[AcceptFriendMessageHandler@serverAction]: " + e.getMessage());
        }
        
        if(result)
        {
            LoggingList.sendLogMessage(new LogMessage("[Friend response] User '" + source + "' has " + (bean.isAcceptRequest()? "accepted" : "declined") + " the friend request "
            + "from user '" + bean.getFromUser() + "'", 
            AppResources.getInstance().getAddFriendImage()));
        }
    }
}
