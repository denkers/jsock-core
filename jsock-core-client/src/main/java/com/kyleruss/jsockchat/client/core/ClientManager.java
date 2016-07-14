//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.core;

import com.kyleruss.jsockchat.client.io.ClientMessageListener;
import com.kyleruss.jsockchat.client.io.ClientMessageSender;
import com.kyleruss.jsockchat.client.io.ListUpdateListener;
import com.kyleruss.jsockchat.client.updatebean.FriendsUpdateBeanHandler;
import com.kyleruss.jsockchat.client.updatebean.RoomsUpdateBeanHandler;
import com.kyleruss.jsockchat.client.updatebean.UsersUpdateBeanHandler;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.MessageBean;
import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

/**
 * Responsible for starting the servers, handeling updates + more
 * Main class of jsockchat-client
 */
public class ClientManager 
{
    private static ClientManager instance;
    private ClientMessageSender sender;
    private ClientMessageListener listener;
    private ListUpdateListener listUpdateListener;
    
    private ClientManager() {}
    
    /**
     * Initializes listener, sender & listUpdateListener servers
     * Starts the initialized servers
     */
    public void startServers()
    {
        SocketManager sockMgr  =   SocketManager.getInstance();
        listener    =   new ClientMessageListener(sockMgr.getTcpSocket());
        listener.start();
        
        sender      =   new ClientMessageSender();
        sender.start();
        
        listUpdateListener = new ListUpdateListener(sockMgr.getUdpSocket());
        listUpdateListener.start();
    }
    
    public static ClientManager getInstance()
    {
        if(instance == null) instance = new ClientManager();
        return instance;
    }
    
    /**
     * Applies the updates in the passed UpdateBeanDump
     * @param update The update to apply to the client
     */
    public void handleUpdates(UpdateBeanDump update)
    {
        Thread updateThread =   new Thread(()->
        {
            FriendsUpdateBeanHandler friendsHandler =   new FriendsUpdateBeanHandler(update.getFriendsBean());
            RoomsUpdateBeanHandler roomsHandler     =   new RoomsUpdateBeanHandler(update.getRoomsBean());
            UsersUpdateBeanHandler usersHandler     =   new UsersUpdateBeanHandler(update.getUsersBean());

            usersHandler.beanAction();
            roomsHandler.beanAction();
            friendsHandler.beanAction();
        });
        
        updateThread.start();
    }
    
    /**
     * Removes beans in the client
     */
    public void clearUpdates()
    {
        RoomManager.getInstance().setRoomsBean(null);
    }
    
    /**
     * Logs out the activeUser 
     * Cleans up resources and moves user back to the login view
     */
    public void logoutUser()
    {
        
        Thread thread   = new Thread(()->
        {
            
            try
            {
                DisconnectMsgBean bean  =   new DisconnectMsgBean(DisconnectMsgBean.CLIENT_LOGOUT);
                RequestMessage request  =   new RequestMessage(userManager.getActiveUser().getUsername(), bean);
                ClientManager.getInstance().sendRequest(request);
                clearUpdates();
            }
            
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(null, "Failed to send logout request", "Logout request message", JOptionPane.ERROR_MESSAGE);
            }
            
        });
        
        thread.start();
    }
    
    /**
     * DC's the active user
     * Moves user back to the connect view
     */
    public void disconnectUser()
    {
        clearUpdates();
        UserManager.getInstance().setActiveUser(null);
        JOptionPane.showMessageDialog(null, "You have disconnected from the server", "Connection failed", JOptionPane.ERROR_MESSAGE);
        
    }
    
    /**
     * Creates a queue item and adds it to the sending server queue
     * @param request The request to send
     */
    public void sendRequest(RequestMessage request) throws IOException
    {
        ObjectOutputStream oos  =   SocketManager.getInstance().getTCPOutputStream();
        MessageQueueItem item   =   new MessageQueueItem(oos, request);
        sender.addMessage(item);
    }
    
    /**
     * Creates a request from the passed bean and sends it
     * @param bean the bean to send
     */
    public void sendBean(MessageBean bean) throws IOException
    {
        RequestMessage request  =   new RequestMessage(UserManager.getInstance().getActiveUser().getUsername(), bean);
        sendRequest(request);
    }
}
