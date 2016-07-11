//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.RemoveFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.server.core.UserManager;
import com.kyleruss.jsockchat.server.db.DBFriends;
import java.io.IOException;

public class RemoveFriendMessageHandler implements ServerMessageHandler
{
    @Override
    public void serverAction(RequestMessage request) 
    {
        RemoveFriendMsgBean bean    =   (RemoveFriendMsgBean) request.getMessageBean();
        ResponseMessage response    =   new ResponseMessage(request);
        boolean removeResult        =   DBFriends.getInstance().removeFriend(bean);
        
        response.setStatus(removeResult);
        response.setDescription(removeResult? "Friend has been successfully removed" : "Failed to remove friend");
        
        try { UserManager.getInstance().sendMessageToUser(bean.getSourceUsername(), response); }
        catch(IOException e)
        {
            System.out.println("[RemoveFriendMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
