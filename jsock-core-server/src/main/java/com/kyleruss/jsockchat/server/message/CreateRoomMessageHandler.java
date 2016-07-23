//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.CreateRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.room.Room;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import java.io.IOException;

public class CreateRoomMessageHandler implements ActionHandler
{
    @Override
    public void performAction(Message message) 
    {
        RequestMessage request      =   (RequestMessage) message;
        CreateRoomMsgBean bean      =   (CreateRoomMsgBean) request.getMessageBean();
        ResponseMessage response    =   new ResponseMessage(request);
        String source               =   request.getUserSource();
        String roomName             =   bean.getName();
        String password             =   bean.getPassword();
        boolean isPrivate           =   bean.isPrivate();
        RoomManager roomManager     =   RoomManager.getInstance();
        
        try
        {
            if(roomManager.find(roomName))
            {
                response.setStatus(false);
                response.setDescription("Room with that name already exists");
            }
            
            else
            {
                Room room   =   new Room(roomName, isPrivate, password, false);
                
                roomManager.add(roomName, room);
                room.joinRoom(source);
                
                response.setStatus(true);
                response.setDescription("Room has been created");
                
                LoggingManager.log("[Create room] User '" + source + "' has created a room '" + room.getRoomName() + "'");
            }
            
            SocketManager.getInstance().sendMessageToUser(source, response);
        }
        
        catch(IOException e)
        {
            System.out.println("[CreateRoomMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
