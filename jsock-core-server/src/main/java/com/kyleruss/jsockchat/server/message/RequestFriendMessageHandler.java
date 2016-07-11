//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.server.core.UserManager;
import com.kyleruss.jsockchat.server.db.DBFriends;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import java.io.IOException;

public class RequestFriendMessageHandler implements ServerMessageHandler
{
    @Override
    public void serverAction(RequestMessage request) 
    {
        RequestFriendMsgBean bean       =   (RequestFriendMsgBean) request.getMessageBean();
        String source                   =   request.getUserSource();
        ResponseMessage response        =   new ResponseMessage(request);
        ResponseMessage sendResponse    =   null;
        
        if(DBFriends.getInstance().friendRequestExists(bean.getFriendA(), bean.getFriendB()))
        {
            response.setStatus(false);
            response.setDescription("There already exists a pending friend request");
        }
        
        else
        {
            boolean result      =   DBFriends.getInstance().addFriendRequest(bean);
            String responseMsg  =   result? "Successfully sent " + bean.getFriendB() + " a friend request" : "Failed to send friend request";
            response.setStatus(result);
            response.setDescription(responseMsg);
            
            if(result)
            {
                sendResponse    =   new ResponseMessage(request);
                sendResponse.setStatus(true);
                sendResponse.setDescription(source + " has sent you a friend request");
            }
            
            LoggingList.sendLogMessage(new LogMessage("[Friend request] User '" + source + "' has friend requested user '" + bean.getFriendB() + "'", 
            AppResources.getInstance().getServerOkImage()));
        }
        
        try 
        { 
            UserManager.getInstance().sendMessageToUser(source, response); 
            if(sendResponse != null) UserManager.getInstance().sendMessageToUser(source, sendResponse); 
            
        }
        catch(IOException e)
        {
            System.out.println("[RequestFriendMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
