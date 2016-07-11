//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.CreateRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.room.Room;
import com.kyleruss.jsockchat.commons.user.IUser;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.UserManager;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import java.io.IOException;

public class CreateRoomMessageHandler implements ServerMessageHandler
{
    @Override
    public void serverAction(RequestMessage request) 
    {
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
                IUser user  =   UserManager.getInstance().get(source);
                Room room   =   new Room(roomName, isPrivate, password, false);
                
                roomManager.add(roomName, room);
                room.joinRoom(user);
                user.getCurrentRooms().add(roomName);
                
                response.setStatus(true);
                response.setDescription("Room has been created");
                
                LoggingList.sendLogMessage(new LogMessage("[Create room] User '" + user.getUsername() + "' has created a room '" + room.getRoomName() + "'", 
                AppResources.getInstance().getAddImage()));
            }
            
            UserManager.getInstance().sendMessageToUser(source, response);
        }
        
        catch(IOException e)
        {
            System.out.println("[CreateRoomMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
