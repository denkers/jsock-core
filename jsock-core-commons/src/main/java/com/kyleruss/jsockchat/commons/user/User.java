//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.user;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @see com.kyleruss.jsockchat.commons.user.IUser
 */
public class User implements IUser, Serializable
{
    private String username;
    private String displayName;
    private List<String> currentRooms;
    
    public User(String username, String displayName)
    {
        this.username       =   username;
        this.displayName    =   displayName;
        currentRooms        =   new LinkedList<>();
    }
    
    @Override
    public String getUsername() 
    {
        return username;
    }

    @Override
    public List<String> getCurrentRooms() 
    {
        return currentRooms;
    }

    @Override
    public String getDisplayName() 
    {
        return displayName;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public void setCurrentRooms(List<String> currentRooms) 
    {
        this.currentRooms = currentRooms;
    }
    
    @Override
    public String toString()
    {
        return displayName;
    }

    @Override
    public int hashCode()
    {
        return username.hashCode();
    }
    
    @Override
    public boolean equals(Object other)
    {
        return other instanceof User && ((User) other).getUsername().equals(username);
    }
}
