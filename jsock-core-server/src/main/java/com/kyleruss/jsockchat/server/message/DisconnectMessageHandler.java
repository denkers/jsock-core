//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import java.util.List;

public class DisconnectMessageHandler implements ActionHandler
{
    @Override
    public void performAction(Message message) 
    {
        RequestMessage request      =   (RequestMessage) message;
        String source               =   request.getUserSource();
        DisconnectMsgBean bean      =   (DisconnectMsgBean) request.getMessageBean();
        ResponseMessage response    =   new ResponseMessage(request);
        RoomManager roomManager     =   RoomManager.getInstance();
        
        response.setDescription(source + " has left the room");
        response.setStatus(true);
        
        if(bean.getDisconnectType() == DisconnectMsgBean.ROOM_LEAVE)
        {
            String roomName   =   bean.getRoom();
            roomManager.leaveRoom(source, roomName, request);
        }
        
        else
        {
            roomManager.leaveAllRooms(source, request);
            
            List<String> rooms  =   roomManager.getUsersRooms(source);
            for(String roomName : rooms)
                roomManager.sendMessageToRoom(roomName, response, RoomManager.createExclusions(source));

            if(bean.getDisconnectType() == DisconnectMsgBean.CLIENT_CLOSE)
                SocketManager.getInstance().clientExit(source);
            
            else
                SocketManager.getInstance().processLogout(source);
        }
    }
}

