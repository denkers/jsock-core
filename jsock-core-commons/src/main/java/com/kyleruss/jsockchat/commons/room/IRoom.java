//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.room;

import java.io.Serializable;
import java.util.List;

public interface IRoom extends Serializable
{
    public List<String> getUserList();
    
    public boolean isEmpty();
    
    public int getNumUsersInRoom();
    
    public boolean isPrivate();
    
    public void setPrivate(boolean isPrivate);
    
    public boolean isPassProtected();
    
    public String getRoomPassword();
    
    public String getRoomName();
    
    public void setUserList(List<String> userList);
    
    public boolean isFixed();
    
    public void leaveRoom(String user);
    
    public boolean joinRoom(String user);
    
    public boolean hasUser(String user);
}
