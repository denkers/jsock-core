//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.db;

import com.kyleruss.jsockchat.commons.user.User;
import com.kyleruss.jsockchat.server.core.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers extends DBModel<User>
{
    private static DBUsers instance;
    
    private DBUsers()
    {
        tableName   =   "Users";
        primaryKey  =   "username";
    }
    
    public User fetchVerifiedUser(String username, String password)
    {
        String query    =   "SELECT * FROM Users WHERE username = ? AND password = ?;";
        
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement =   conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            
            ResultSet rs    =   statement.executeQuery();
            
            if(rs.next())
            {
                String displayName  =   rs.getString("display_name");
                User user           =   new User(username, displayName);
                return user;
            }
            
            else return null;
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBUsers@fetchVerifiedUser]: " + e.getMessage());
            return null;
        }
    }
    
    public User getuser(String username)
    {
        String query    =   "SELECT * FROM Users WHERE username = ?";
        
        try(Connection conn =   DBManager.getConnection())
        {
            PreparedStatement statement =   conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs    =   statement.executeQuery();
            
            if(rs.next()) return new User(username, rs.getString("display_name"));
            else return null;
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBUsers@getUser]: " + e.getMessage());
            return null;
        }
    }
    
    public boolean createUser(String username, String password, String displayname)
    {
        String update       =   "INSERT INTO Users VALUES(?, ?, ?);";
        
        try(Connection conn =   DBManager.getConnection())
        {   
            PreparedStatement statement  =   conn.prepareStatement(update);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, displayname);
            
            int rowCount    =   statement.executeUpdate();
            return rowCount > 0;
        }
        
        catch(SQLException e)
        {
            System.out.println("[DBUsers@verifyUser]: " + e.getMessage());
            return false;
        }
    }
    
    public static DBUsers getInstance()
    {
        if(instance == null) instance = new DBUsers();
        return instance;
    }
}
