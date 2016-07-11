//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.core;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.CreateRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.JoinRoomMsgBean;
import com.kyleruss.jsockchat.commons.updatebean.RoomsUpdateBean;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Maintains a bean for rooms
 * Has some utility methods for room operations
 */
public class RoomManager 
{
    private static RoomManager instance;
    private RoomsUpdateBean roomsBean;
    
    private RoomManager() {}
   
    public void sendLeaveRoomRequest(String roomName)
    {
        try
        {
            DisconnectMsgBean bean  =   new DisconnectMsgBean(DisconnectMsgBean.ROOM_LEAVE);
            bean.setRoom(roomName);
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to send leave room request", "Failed request", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void sendJoinRoomRequest(String roomName, String password)
    {
        try
        {
            JoinRoomMsgBean bean    =   new JoinRoomMsgBean(roomName, password);
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to send join request", "Failed request", JOptionPane.ERROR_MESSAGE);
            ChatHomePanel.getInstance().toggleJoinRoomProcessing(false);
        }
    }
    
    public void sendCreateRoomRequest(String roomName, String password)
    {
        try
        {
            CreateRoomMsgBean bean  =   new CreateRoomMsgBean(roomName, password, false);
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to send create request", "Failed request", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public synchronized void setRoomsBean(RoomsUpdateBean roomsBean)
    {
        this.roomsBean  =   roomsBean;
    }
    
    public synchronized RoomsUpdateBean getRoomsBean()
    {
        return roomsBean;
    }
    
    
    public static RoomManager getInstance()
    {
        if(instance == null) instance = new RoomManager();
        return instance;
    }
}
