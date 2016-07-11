//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.user.IUser;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import com.kyleruss.jsockchat.server.core.UserManager;
import java.util.List;

public class DisconnectMessageHandler implements ServerMessageHandler
{
    @Override
    public void serverAction(RequestMessage request) 
    {
        String source               =   request.getUserSource();
        DisconnectMsgBean bean      =   (DisconnectMsgBean) request.getMessageBean();
        ResponseMessage response    =   new ResponseMessage(request);
        IUser user                  =   UserManager.getInstance().get(source);
        RoomManager roomManager     =   RoomManager.getInstance();
        
        response.setDescription(source + " (" + user.getDisplayName() + ") has left the room");
        response.setStatus(true);
        
        if(bean.getDisconnectType() == DisconnectMsgBean.ROOM_LEAVE)
        {
            String roomName   =   bean.getRoom();
            roomManager.leaveRoom(user, roomName);
        }
        
        else
        {
            roomManager.leaveAllRooms(user);
            
            List<String> rooms  =   user.getCurrentRooms();
            for(String roomName : rooms)
                roomManager.sendMessageToRoom(roomName, response, RoomManager.createExclusions(user));

            if(bean.getDisconnectType() == DisconnectMsgBean.CLIENT_CLOSE)
                UserManager.getInstance().clientExit(user);
            
            else
                SocketManager.getInstance().processLogout(user.getUsername());
        }
    }
}

