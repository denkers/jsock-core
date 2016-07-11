//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.commons.updatebean.FriendsUpdateBean;
import com.kyleruss.jsockchat.commons.updatebean.RoomsUpdateBean;
import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import com.kyleruss.jsockchat.commons.updatebean.UsersUpdateBean;
import com.kyleruss.jsockchat.commons.user.IUser;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import com.kyleruss.jsockchat.server.io.MessageServer;
import com.kyleruss.jsockchat.server.io.ServerMessageSender;
import com.kyleruss.jsockchat.server.io.UpdateBroadcastServer;

/**
 * Main class of the jsockchat-server
 * initializes servers and prepares updates
 */
public class ServerManager 
{
    private static ServerManager instance;
    
    private ServerManager() {} 
    
    /**
     * Initializes and starts listening and send message servers as well as update the broadcast server
     */
    private void startServers()
    {
        MessageServer messageServer     =   MessageServer.getInstance();
        messageServer.start();
        LoggingList.sendLogMessage(new LogMessage("[Server manager] Started Message server", AppResources.getInstance().getServerOkImage()));
        
        ServerMessageSender sendServer  =   ServerMessageSender.getInstance();
        sendServer.start();
        LoggingList.sendLogMessage(new LogMessage("[Server manager] Started Message broadcast server", AppResources.getInstance().getServerOkImage()));
        
        UpdateBroadcastServer broadcastServer =   UpdateBroadcastServer.getInstance();
        broadcastServer.start();
        LoggingList.sendLogMessage(new LogMessage("[Server manager] Started Update broadcast server", AppResources.getInstance().getServerOkImage()));
    }
    
    /**
     * Creates the MessageBeans and organizes them into a update dump
     * @param user The user who updates will be prepared for
     * @return A update dump from the created beans
     */
    public UpdateBeanDump prepareUpdates(IUser user)
    {
        RoomsUpdateBean roomsBean       =   RoomManager.getInstance().createRoomsBean();
        FriendsUpdateBean freindsBean   =   UserManager.getInstance().createFriendsBean(user.getUsername());
        UsersUpdateBean usersBean       =   UserManager.getInstance().createUsersBean();
        UpdateBeanDump beanDump         =   new UpdateBeanDump(freindsBean, roomsBean, usersBean);
        
        return beanDump;
    }
    
    public static ServerManager getInstance()
    {
        if(instance == null) instance = new ServerManager();
        return instance;
    }
    
    public static void main(String[] args)
    {
        ServerGUIManager guiManager   =   ServerGUIManager.getInstance();
        guiManager.display();
        ServerManager manager   =   getInstance();
        manager.startServers();
    }
}
