//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.db;

import com.kyleruss.jsockchat.commons.message.AcceptFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RemoveFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.server.core.DBManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.kyleruss.jsockchat.commons.user.IUser;
import com.kyleruss.jsockchat.commons.user.User;
import com.kyleruss.jsockchat.server.core.UserManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFriends extends DBModel
{
    private static DBFriends instance;
    
    private DBFriends()
    {
        tableName   =   "Friends";
        primaryKey  =   "id";
    }
    
    public Map<String, IUser> getUsersFriends(String username)
    {
        Map<String, IUser> friends  =   new HashMap<>();
        String query        =   
        "SELECT friend.* \n" +
        "FROM Friends, Users friend\n" +
        "WHERE Friends.friend_b = friend.username\n" +
        "AND Friends.friend_a = ?\n" +
        "AND confirmed = 'TRUE'\n" + 
        "UNION\n" +
        "SELECT friend.*\n" +
        "FROM Friends, Users friend\n" +
        "WHERE Friends.friend_a = friend.username\n" +
        "AND Friends.friend_b = ?" +
        "AND confirmed = 'TRUE'";
        
        try(Connection conn     =   DBManager.getConnection())
        {
            PreparedStatement statement =   conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, username);
            
            ResultSet results           =   statement.executeQuery();
            
            while(results.next())
            {
                String rsUsername       =   results.getString("username");
                String rsDisplayname    =   results.getString("display_name");
                
                User user               =   new User(rsUsername, rsDisplayname);
                friends.put(user.getUsername(), user);
            }
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBFriends@getUsersFriends]: " + e.getMessage());
        }
        
        return friends;
    }
    
    public Map<String, IUser> getUsersOnlineFriends(String username)
    {
        Map<String, IUser> friends         =   getUsersFriends(username);
        return getUsersOnlineFriends(username, friends);
    }
    
    public Map<String, IUser> getUsersOnlineFriends(String username, Map<String, IUser>  friends)
    {
        Map<String, IUser> onlineFriends    =   new HashMap<>();
        UserManager userMgr                 =   UserManager.getInstance();
        
        for(String friendUsername : friends.keySet())
        {
            if(userMgr.find(friendUsername))
                onlineFriends.put(friendUsername, friends.get(friendUsername));
        }
        
        return onlineFriends;
    }
    
    public List<RequestFriendMsgBean> getPendingFriendRequests(String username)
    {
        String query    =   
        "SELECT * FROM Friends\n" +
        "WHERE friend_b = ?\n" +
        "AND confirmed = 'FALSE'";
        
        List<RequestFriendMsgBean> pendingRequests  =   new ArrayList<>();
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement =   conn.prepareStatement(query);
            SimpleDateFormat formatter  =   new SimpleDateFormat("yyyy-MM-dd");
            statement.setString(1, username);
            
            ResultSet rs    =   statement.executeQuery();
            while(rs.next())
            {
                int friendshipID                =   rs.getInt("id");
                String friendA                  =   rs.getString("friend_a");
                String friendB                  =   rs.getString("friend_b");
                Date reqDate                    =   formatter.parse(rs.getString("befriend_date"));
                
                RequestFriendMsgBean reqBean    =   new RequestFriendMsgBean(friendA, friendB, reqDate);
                reqBean.setFriendshipID(friendshipID);
                pendingRequests.add(reqBean);
            }
        }
        
        catch(SQLException | ParseException e)
        {
            System.out.println("[DBFriends@getPendingFriendRequests]: " + e.getMessage());
        }
        
        return pendingRequests;
    }
    
    public boolean addFriendRequest(RequestFriendMsgBean request)
    {
        String query    =  "INSERT INTO Friends (friend_a, friend_b) VALUES (?, ?)";
        
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement      =   conn.prepareStatement(query);
            statement.setString(1, request.getFriendA());
            statement.setString(2, request.getFriendB());
            
            return statement.executeUpdate() > 0;
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBFriends@addAFriendRequest]: " + e.getMessage());
            return false;
        }
    }
    
    public boolean removeFriend(RemoveFriendMsgBean removeFriendBean)
    {
        String friendA  =   removeFriendBean.getFriendUsername();
        String friendB  =   removeFriendBean.getSourceUsername();
        
        String query    =   
        "DELETE FROM Friends "
        + "WHERE (friend_a = ? AND friend_b = ?) "
        + "OR (friend_a = ? AND friend_b = ?);";
        
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement =   conn.prepareStatement(query);
            statement.setString(1, friendA);
            statement.setString(2, friendB);
            statement.setString(3, friendB);
            statement.setString(4, friendA);
            
            return statement.executeUpdate() > 0;
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBFriends@removeFriend]: " + e.getMessage());
            return false;
        }
    }
    
    public boolean respondToFriendRequest(AcceptFriendMsgBean requestBean)
    {
        boolean isAccepting =   requestBean.isAcceptRequest();
        int friendshipID    =   requestBean.getFriendshipID();
        String query;
        
        if(isAccepting)
        {
            query   =   
            "UPDATE Friends "
            + "SET confirmed = '" + (isAccepting? "TRUE" : "FALSE") + "' "
            + "WHERE id = ?";
        }
        
        else
        {
            query   =   
            "DELETE FROM Friends WHERE id = ?";
        }
        
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement  =       conn.prepareStatement(query);
            
            if(isAccepting)
            {
                statement.setInt(1, friendshipID);
            }
            
            else statement.setInt(1, friendshipID);
            
            return statement.executeUpdate() > 0;
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBFriends@respondToFriendRequest]: " + e.getMessage());
            return false;
        }
    }
    
    public boolean friendRequestExists(String friendA, String friendB)
    {
        String query    =   
        "SELECT  * FROM Friends "
        + "WHERE (friend_a = ? AND friend_b = ?) "
        + "OR (friend_a = ? AND friend_b = ?)";
        
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement  =   conn.prepareStatement(query);
            statement.setString(1, friendA);
            statement.setString(2, friendB);
            statement.setString(3, friendB);
            statement.setString(4, friendA);
            
            ResultSet rs    =   statement.executeQuery();
            return rs.next();
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBFriends@friendRequestExists]: " + e.getMessage());
            return false;
        }
    }
    
    public static DBFriends getInstance()
    {
        if(instance == null) instance = new DBFriends();
        return instance;
    }
}
