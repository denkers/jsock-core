//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.room.Room;
import com.kyleruss.jsockchat.commons.updatebean.RoomsUpdateBean;
import com.kyleruss.jsockchat.server.io.ServerMessageSender;
import com.kyleruss.jsockchat.server.io.UserSocket;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Maintains the active rooms in the server
 * Initializes static rooms from data/rooms.xml
 */
public final class RoomManager extends AbstractManager<String, Room>
{
    private static RoomManager instance;
    private String serverNodeName;
    
    private RoomManager() 
    {
        data    =   new LinkedHashMap<>();
        initFixedRooms();
    }
    
    /**
     * Gets the room with the passed roomName and returns its user list
     * @param roomName The name of the room/key in data 
     * @return A list of users in this room
     */
    public synchronized List<String> getUsersInRoom(String roomName)
    {
        Room room           =   get(roomName);
        
        if(room != null) return room.getUserList();
        else return new ArrayList<>();
    }
    
    public synchronized List<String> getUsersRooms(String user)
    {
        List<String> rooms  =   new ArrayList<>();
        for(Room room : data.values())
        {
            if(room.hasUser(user))
                rooms.add(user);
        }
        
        return rooms;
    }
    
    /**
     * Sends the passed message to all users in the room (exception those in exclusions)
     * @param roomName The name of the room to send to
     * @param message The message to send users in the room
     * @param exclusions Users in the room to ignore
     */
    public synchronized void sendMessageToRoom(String roomName, Message message, List<String> exclusions)
    {
        if(!find(roomName))
            return;
        
        List<String> roomUsers   =   getUsersInRoom(roomName);
        
        for(String user : roomUsers)
        {
            if(exclusions != null && exclusions.contains(user))
                continue;
            
            try
            {
                UserSocket userSocket           =   SocketManager.getInstance().get(user);
                MessageQueueItem messageItem    =   new MessageQueueItem(userSocket.getSocketOutputStream(), message);
                ServerMessageSender.getInstance().addMessage(messageItem);
            }
            
            catch(IOException e)
            {
                System.out.println("[RoomManager@sendMessageToRoom]: " + e.getMessage());
            }
        }
    }
    
    /**
     * Creates a list of users to be ignored
     * @param excludedUsers The users to exclude
     * @return A list of users that should be ignored
     */
    public static List<String> createExclusions(String... excludedUsers)
    {
        List<String> exclusions  =   new ArrayList<>();
        for(String user : excludedUsers)
            exclusions.add(user);
        
        return exclusions;
    }
    
    /**
     * Removes the user from the room
     * Updates clients in the room of users exist
     * @param user The user to remove from the room
     * @param roomName The room to remove user from
     */
    public synchronized void leaveRoom(String user, String roomName)
    {
        if(find(roomName))
        {
            LoggingManager.log("[Room manager] User '" + user + "' has left room '" + roomName + "'");
            
            //user.getCurrentRooms().remove(roomName);
            Room room   =   get(roomName);
            room.leaveRoom(user);
            
            if(room.isEmpty() && !room.isFixed())
            {
                data.remove(roomName);
                LoggingManager.log("[Room manager] Room '" + roomName + "' is empty, destroying...");
            }
            
            else
            {
                DisconnectMsgBean bean      =   new DisconnectMsgBean(DisconnectMsgBean.ROOM_LEAVE);
                bean.setRoom(room.getRoomName());
                RequestMessage request      =   new RequestMessage(user, bean);
                ResponseMessage response    =   new ResponseMessage(request);
                sendMessageToRoom(room.getRoomName(), response, createExclusions(user));
                
                LoggingManager.log("[Room manager] Notified witnesses of room '" + roomName + "' that user '" + user + "' has left");
            }
        }
    }
    
    /**
     * Removes the user from all rooms its involved in
     * @param user The user that's going to leave all their rooms
     */
    public synchronized void leaveAllRooms(String user)
    {
        List<String> usersRoomList  =   getUsersRooms(user);
        Object[] currentRooms   =    usersRoomList.toArray();
        
        for(Object room : currentRooms)
            leaveRoom(user, room.toString());
    }
    
    /**
     * Parses the static rooms file (default data/rooms.xml)
     * Addes rooms the file to the manager
     */
    private void initFixedRooms()
    {
        try
        {
            DocumentBuilderFactory builderFactory   =   DocumentBuilderFactory.newInstance();
            DocumentBuilder builder                 =   builderFactory.newDocumentBuilder();
            Document doc                            =   builder.parse(new File(ServerConfig.FIXED_ROOMS_PATH));
            NodeList roomTags                       =   doc.getElementsByTagName("room");
            
            for(int i = 0; i < roomTags.getLength(); i++)
            {
                Element roomTag     =   (Element) roomTags.item(i);
                String roomName     =   roomTag.getElementsByTagName("name").item(0).getTextContent();
                String password     =   roomTag.getElementsByTagName("password").item(0).getTextContent();
                Room room           =   new Room(roomName, false, password, true);
                add(roomName, room);
            }
            
            serverNodeName  =   doc.getElementsByTagName("rootRoom").item(0).getTextContent();
            
            LoggingManager.log("[Room manager] rooms.xml has been initialized");
        }
        
        catch(IOException | ParserConfigurationException | SAXException e)
        {
            System.out.println("[RoomManager@initFixedRooms]: " + e.getMessage());
            LoggingManager.log("[Room manager] Failed to initialize rooms.xml");
        }
    }
    
    public void setServerNodeName(String serverNodeName)
    {
        this.serverNodeName  =   serverNodeName;
    }
    
    public String getServerNodeName(String serverNodeName)
    {
        return serverNodeName;
    }
    
    public RoomsUpdateBean createRoomsBean()
    {
        RoomsUpdateBean bean    =   new RoomsUpdateBean();
        bean.setServerNodeName(serverNodeName);
        bean.setData(data);
        
        return bean;
    }
    
    public static RoomManager getInstance()
    {
        if(instance == null) instance = new RoomManager();
        return instance;
    }
}
