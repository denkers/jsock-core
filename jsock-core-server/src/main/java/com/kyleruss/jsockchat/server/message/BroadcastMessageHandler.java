//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.BroadcastMsgBean;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.RoomManager;

public class BroadcastMessageHandler implements ActionHandler
{

    @Override
    public void performAction(Message message)
    {
        RequestMessage request      =   (RequestMessage) message;
        BroadcastMsgBean bean       =   (BroadcastMsgBean) request.getMessageBean();
        String room                 =   bean.getRoom();
        ResponseMessage response    =   new ResponseMessage(request);
        response.setStatus(true);
        
        String user  =   request.getUserSource();
        RoomManager.getInstance().sendMessageToRoom(room, response, RoomManager.createExclusions(user));
        LoggingManager.log("[Message broadcast] User '" + user + "' has broadcasted to room '" + room + "'");
    }
}
