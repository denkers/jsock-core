//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.BroadcastMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.user.IUser;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.UserManager;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;

public class BroadcastMessageHandler implements ServerMessageHandler
{

    @Override
    public void serverAction(RequestMessage request)
    {
        BroadcastMsgBean bean       =   (BroadcastMsgBean) request.getMessageBean();
        String room                 =   bean.getRoom();
        ResponseMessage response    =   new ResponseMessage(request);
        response.setStatus(true);
        
        IUser user                  =   UserManager.getInstance().get(request.getUserSource());
        RoomManager.getInstance().sendMessageToRoom(room, response, RoomManager.createExclusions(user));
        LoggingList.sendLogMessage(new LogMessage("[Message broadcast] User '" + user.getUsername() + "' has broadcasted to room '" + room + "'", 
        AppResources.getInstance().getBroadcastImage()));
    }
}
