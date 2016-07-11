//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.core;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.AcceptFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.BroadcastMsgBean;
import com.kyleruss.jsockchat.commons.message.PrivateMsgBean;
import com.kyleruss.jsockchat.commons.message.RemoveFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.updatebean.FriendsUpdateBean;
import com.kyleruss.jsockchat.commons.updatebean.UsersUpdateBean;
import com.kyleruss.jsockchat.commons.user.IUser;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Manages the active user, user's friends, and pending friend requests
 * As well as all online users 
 */
public class UserManager 
{
    private static UserManager instance;
    private IUser activeUser;
    private FriendsUpdateBean friendsBean;
    private UsersUpdateBean usersBean;
    
    private UserManager() {}
    
    /**
     * Sorts a list in the order of online 
     * @param friendList The list of users to sort
     */
    public void sortFriends(List<IUser> friendList)
    {
        friendList.sort((IUser a, IUser b) -> 
        {
            Map<String, IUser> userData =   usersBean.getData();
            boolean aOnline             =   userData.containsKey(a.getUsername());
            boolean bOnline             =   userData.containsKey(b.getUsername());
            
            if(aOnline && !bOnline) return -1;
            else if(!aOnline && bOnline) return 1;
            else return 0;
        });
    }
    
    public void sendPrivateMessage(String toUser, String message)
    {
        try
        {
            PrivateMsgBean bean =   new PrivateMsgBean(toUser, message);
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to send private message", "Private message fail", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void sendBroadcastMessage(String room, String message)
    {
        try
        {
            BroadcastMsgBean bean   =   new BroadcastMsgBean(room, message);
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to send message in room", "Broadcast message fail", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void sendFriendRequest(String username)
    {
        try
        {
            RequestFriendMsgBean bean   =   new RequestFriendMsgBean(activeUser.getUsername(), username, new Date());
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            ChatHomePanel.getInstance().toggleFriendRequestProcessing(false);
            JOptionPane.showMessageDialog(null, "Failed to send friend request", "Request failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void sendRemoveFriendRequest(String username)
    {
        try
        {
            RemoveFriendMsgBean bean    =   new RemoveFriendMsgBean(username, activeUser.getUsername());
            ClientManager.getInstance().sendBean(bean);
        }
        
        catch(IOException e)
        {
            ChatHomePanel.getInstance().toggleFriendRemoveProcessing(false);
            JOptionPane.showMessageDialog(null, "Failed to send remove friend request", "Request failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void sendFriendResponse(RequestFriendMsgBean bean, boolean accept)
    {
        try
        {
            AcceptFriendMsgBean responseBean    =   new AcceptFriendMsgBean(accept, bean.getFriendshipID(), bean.getFriendA());
            ClientManager.getInstance().sendBean(responseBean);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to send response to friend request", "Request failed", JOptionPane.ERROR_MESSAGE);
            ChatHomePanel.getInstance().toggleFriendResponseProcessing(false);
        }
    }
    
    public IUser getActiveUser()
    {
        return activeUser;
    }
    
    public void setActiveUser(IUser activeUser)
    {
        this.activeUser =   activeUser;
    }
    
    public synchronized void setFriendsBean(FriendsUpdateBean friendsBean)
    {
        this.friendsBean    =   friendsBean;
    }
    
    public synchronized FriendsUpdateBean getFriendsBean()
    {
        return friendsBean;
    }
    
    public synchronized UsersUpdateBean getUsersBean()
    {
        return usersBean;
    }
    
    public synchronized void setUsersBean(UsersUpdateBean usersBean)
    {
        this.usersBean  =   usersBean;
    }
    
    public String getSource()
    {
        if(activeUser == null) return null;
        else return activeUser.getUsername();
    }
    
    public static UserManager getInstance()
    {
        if(instance == null) instance = new UserManager();
        return instance;
    }
}
