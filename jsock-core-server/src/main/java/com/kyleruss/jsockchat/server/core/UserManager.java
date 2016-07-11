//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.updatebean.FriendsUpdateBean;
import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import com.kyleruss.jsockchat.commons.updatebean.UsersUpdateBean;
import com.kyleruss.jsockchat.commons.user.AuthPackage;
import com.kyleruss.jsockchat.commons.user.IUser;
import com.kyleruss.jsockchat.commons.user.User;
import com.kyleruss.jsockchat.server.db.DBFriends;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import com.kyleruss.jsockchat.server.io.ServerMessageSender;
import com.kyleruss.jsockchat.server.io.UserSocket;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Manages users in the server
 * Responsible for creating user and friends beans
 */
public final class UserManager extends AbstractManager<String, IUser>
{
    private static UserManager instance;
    
    private UserManager() {}
    
    /**
     * Gets the passed user's friends from db query
     * Creates and returns a new UpdateBean);
     * @param username The user to fetch friends for
     * @return A friends bean containing friends and online friends
     */
    public FriendsUpdateBean createFriendsBean(String username)
    {
        FriendsUpdateBean bean                      =   new FriendsUpdateBean();
        DBFriends friendModel                       =   DBFriends.getInstance();
        Map<String, IUser> friends                  =   friendModel.getUsersFriends(username);
        List<RequestFriendMsgBean> friendRequests   =   friendModel.getPendingFriendRequests(username);
        
        bean.setData(friends);
        bean.setPendingRequests(friendRequests);
        return bean;
    }
    
    
    public UsersUpdateBean createUsersBean()
    {
        UsersUpdateBean bean    =   new UsersUpdateBean();
        bean.setData(data);
        return bean;
    }
    
    
    /**
     * Removes the user from the managed map
     * Notifies room users of users exit
     * @param client The user who is exiting
     */
    public synchronized void clientExit(IUser client)
    {
        if(client == null) return;
        
        LoggingList.sendLogMessage(new LogMessage("[User manager] User '" + client.getUsername() + "' has exited", AppResources.getInstance().getDcImage()));
        
        remove(client.getUsername());
        RoomManager.getInstance().leaveAllRooms(client);
        SocketManager.getInstance().cleanUp(client.getUsername());
    }
    
    
    /**
     * Sends a direct message to the passed user
     * @param username The username to send to
     * @param message The message being sent
     */
    public synchronized void sendMessageToUser(String username, Message message) throws IOException
    {
        UserSocket userSocket     =   SocketManager.getInstance().get(username);

        if(userSocket != null)
        {
            MessageQueueItem messageItem    =   new MessageQueueItem(userSocket.getSocketOutputStream(), message);
            ServerMessageSender.getInstance().addMessage(messageItem);
        }
    }
    
    
    public AuthPackage prepareAuthPackage(User user)
    {
        UpdateBeanDump beanDump     =   ServerManager.getInstance().prepareUpdates(user);
        return new AuthPackage(user, beanDump);
    }
    
    public static UserManager getInstance()
    {
        if(instance == null) instance = new UserManager();
        return instance;
    }
}
