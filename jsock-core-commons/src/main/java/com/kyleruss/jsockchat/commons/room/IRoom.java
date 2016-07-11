//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.room;

import com.kyleruss.jsockchat.commons.user.IUser;
import java.io.Serializable;
import java.util.List;

public interface IRoom extends Serializable
{
    public List<IUser> getUserList();
    
    public boolean isEmpty();
    
    public int getNumUsersInRoom();
    
    public boolean isPrivate();
    
    public void setPrivate(boolean isPrivate);
    
    public boolean isPassProtected();
    
    public String getRoomPassword();
    
    public String getRoomName();
    
    public void setUserList(List<IUser> userList);
    
    public boolean isFixed();
    
    public void leaveRoom(IUser user);
    
    public boolean joinRoom(IUser user);
    
    public boolean hasUser(IUser user);
}
