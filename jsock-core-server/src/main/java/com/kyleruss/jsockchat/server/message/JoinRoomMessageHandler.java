//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.JoinRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.room.Room;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JoinRoomMessageHandler implements ActionHandler
{
    @Override
    public void performAction(Message message)
    {
        RequestMessage request      =   (RequestMessage) message;
        JoinRoomMsgBean bean        =   (JoinRoomMsgBean) request.getMessageBean();
        String source               =   request.getUserSource();
        String roomName             =   bean.getRoom();
        Room room                   =   RoomManager.getInstance().get(roomName);
        ResponseMessage response    =   new ResponseMessage(request);
        List<String> usersRooms     =   RoomManager.getInstance().getUsersRooms(source);

        try 
        { 
            if(room == null)
            {
                room   =   new Room(roomName, false, null, false);
                RoomManager.getInstance().add(roomName, room);
                room.joinRoom(source);
                
                response.setStatus(true);
                response.setDescription("You have connected to room: " + roomName);
                SocketManager.getInstance().sendMessageToUser(source, response);
            }
            
            else if(room.hasUser(source) || usersRooms.contains(roomName))
            {
                response.setStatus(false);
                response.setDescription("You are already connected to this room");
                SocketManager.getInstance().sendMessageToUser(source, response); 
            }
            
            else
            {
                 if(room.isPassProtected() && !room.getRoomPassword().equals(bean.getAttemptedPassword()))
                 {
                     response.setStatus(false);
                     response.setDescription("Invalid room password");
                     SocketManager.getInstance().sendMessageToUser(source, response);
                 }
                 
                 else
                 {
                    room.joinRoom(source);

                    response.setStatus(true);
                    response.setDescription(source + " (" + source + ") has joined the room");

                    List<String> exclusions    =   new ArrayList<>();
                    exclusions.add(source);
                    RoomManager.getInstance().sendMessageToRoom(roomName, response, exclusions);

                    ResponseMessage directResponse    =   new ResponseMessage(request);
                    directResponse.setStatus(true);
                    directResponse.setDescription("You have connected to room: " + roomName);
                    SocketManager.getInstance().sendMessageToUser(source, directResponse);
                     
                    LoggingManager.log("[Join room] User '" + source + "' has joined room '" + roomName + "'");
                 }
            }
        }
        
        catch(IOException e)
        {
            LoggingManager.log("[JoinRoomMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
