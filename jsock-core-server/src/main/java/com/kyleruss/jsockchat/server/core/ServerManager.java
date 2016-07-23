//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.commons.updatebean.RoomsUpdateBean;
import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
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
    public void startServers()
    {
        MessageServer messageServer     =   MessageServer.getInstance();
        messageServer.start();
        LoggingManager.log("[Server manager] Started Message server");
        
        ServerMessageSender sendServer  =   ServerMessageSender.getInstance();
        sendServer.start();
        LoggingManager.log("[Server manager] Started Message broadcast server");
        
        UpdateBroadcastServer broadcastServer =   UpdateBroadcastServer.getInstance();
        broadcastServer.start();
        LoggingManager.log("[Server manager] Started Update broadcast server");
    }
    
    /**
     * Creates the MessageBeans and organizes them into a update dump
     * @param user The user who updates will be prepared for
     * @return A update dump from the created beans
     */
    public UpdateBeanDump prepareUpdates(String user)
    {
        RoomsUpdateBean roomsBean       =   RoomManager.getInstance().createRoomsBean();
        UpdateBeanDump beanDump         =   new UpdateBeanDump(roomsBean);
        
        return beanDump;
    }
    
    public static ServerManager getInstance()
    {
        if(instance == null) instance = new ServerManager();
        return instance;
    }
}
