//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.db;

import com.kyleruss.jsockchat.server.core.DBManager;
import java.sql.SQLException;


public class DatabaseBuilder
{
    public static void createTables()
    {
        String userTableSQL     =   getUserTableSQL();
        String friendsTableSQL  =   getFriendsTableSQL();
        DBManager dbManager     =   DBManager.getInstance();
        
        try
        {
            dbManager.sendUpdates(userTableSQL, friendsTableSQL);
        }
        
        catch(SQLException e)
        {
            System.out.println("[DatabaseBuilder@createTables] Failed to create tables: " + e.getMessage());
        }
    }
    
    private static String getUserTableSQL()
    {
        return
            "CREATE TABLE IF NOT EXISTS Users"
        +   "("
                + "username VARCHAR(18) NOT NULL PRIMARY KEY, "
                + "password VARCHAR(255) NOT NULL, "
                + "display_name VARCHAR(32) NOT NULL"
        +   ");";
        
    }
    
    private static String getFriendsTableSQL()
    {
        return
            "CREATE TABLE IF NOT EXISTS Friends"
        +   "("
                + "id INTEGER PRIMARY KEY, "
                + "friend_a VARCHAR(18) NOT NULL, "
                + "friend_b VARCHAR(18) NOT NULL, "
                + "confirmed BOOLEAN DEFAULT FALSE, "
                + "befriend_date DATE DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(friend_a) REFERENCES User(username) ON DELETE CASCADE"
        + ");";
    }
    
    public static void main(String[] args)
    {
        createTables();
    }
}
