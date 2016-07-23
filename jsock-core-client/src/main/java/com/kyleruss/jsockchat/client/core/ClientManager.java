//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.client.core;

import com.kyleruss.jsockchat.client.io.ClientMessageListener;
import com.kyleruss.jsockchat.client.io.ClientMessageSender;
import com.kyleruss.jsockchat.client.io.ListUpdateListener;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.MessageBean;
import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
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
        SocketManager sockMgr  =    SocketManager.getInstance();
        listener               =    new ClientMessageListener(sockMgr.getTcpSocket());
        listener.start();
        
        sender                 =    new ClientMessageSender();
        sender.start();
        
        listUpdateListener     =    new ListUpdateListener(sockMgr.getUdpSocket());
        listUpdateListener.start();
    }
    
    public ClientMessageListener getMessageListener()
    {
        return listener;
    }
    
    public ClientMessageSender getMessageSender()
    {
        return sender;
    }
    
    public ListUpdateListener getUpdateListener()
    {
        return listUpdateListener;
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
        /*init update beans and perform their actions
        
        Thread updateThread =   new Thread(()->
        {
            
        });
        
        updateThread.start(); */
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
                RequestMessage request  =   new RequestMessage(SocketManager.getInstance().getActiveUser(), bean);
                sendRequest(request);
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
        SocketManager.getInstance().setUserID(null);
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
        sendBean(bean, new HashMap<>());
    }
    
    public void sendBean(MessageBean bean, Map propertes) throws IOException
    {
        RequestMessage message  =   prepareMessage(bean);
        message.setProperties(propertes);
        sendRequest(message);
    }
    
    public RequestMessage prepareMessage(MessageBean bean)
    {
        String id   =   SocketManager.getInstance().getActiveUser();
        return new RequestMessage(id, bean);
    }
}
