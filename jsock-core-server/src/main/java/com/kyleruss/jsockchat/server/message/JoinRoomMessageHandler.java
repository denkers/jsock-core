//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.JoinRoomMsgBean;
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


public class JoinRoomMessageHandler implements ServerMessageHandler
{
    @Override
    public void serverAction(RequestMessage request) 
    {
        JoinRoomMsgBean bean        =   (JoinRoomMsgBean) request.getMessageBean();
        String source               =   request.getUserSource();
        String roomName             =   bean.getRoom();
        Room room                   =   RoomManager.getInstance().get(roomName);
        IUser user                  =   UserManager.getInstance().get(source);
        ResponseMessage response    =   new ResponseMessage(request);

        try 
        { 
            if(room == null)
            {
                response.setStatus(false);
                response.setDescription("Room does not exist");
                UserManager.getInstance().sendMessageToUser(source, response);
            }
            
            else if(user.getCurrentRooms().contains(roomName) || room.hasUser(user))
            {
                response.setStatus(false);
                response.setDescription("You are already connected to this room");
                UserManager.getInstance().sendMessageToUser(source, response); 
            }
            
            else
            {
                 if(room.isPassProtected() && !room.getRoomPassword().equals(bean.getAttemptedPassword()))
                 {
                     response.setStatus(false);
                     response.setDescription("Invalid room password");
                     UserManager.getInstance().sendMessageToUser(source, response);
                 }
                 
                 else
                 {
                     user.getCurrentRooms().add(roomName);
                     room.joinRoom(user);
                     
                     response.setStatus(true);
                     response.setDescription(source + " (" + user.getDisplayName() + ") has joined the room");
                     RoomManager.getInstance().sendMessageToRoom(roomName, response, null);
                     
                     LoggingList.sendLogMessage(new LogMessage("[Join room] User '" + source + "' has joined room '" + roomName + "'", 
                     AppResources.getInstance().getAddImage()));
                 }
            }
        }
        
        catch(IOException e)
        {
            System.out.println("[JoinRoomMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
