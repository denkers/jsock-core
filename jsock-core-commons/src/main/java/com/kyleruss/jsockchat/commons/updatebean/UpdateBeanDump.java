//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.updatebean;

import java.io.Serializable;
import java.util.Date;

/**
 * A bean that maintains all beans available
 * Has friends, rooms and users beans
 */
public class UpdateBeanDump implements Serializable
{
    private FriendsUpdateBean friendsBean;
    
    private RoomsUpdateBean roomsBean;
    
    private UsersUpdateBean usersBean;
    
    private Date updateTime;
    
    
    public UpdateBeanDump()
    {
        this(new FriendsUpdateBean(), new RoomsUpdateBean(), new UsersUpdateBean());
    }
    
    public UpdateBeanDump(FriendsUpdateBean friendsBean, RoomsUpdateBean roomsBean, UsersUpdateBean usersBean)
    {
        updateTime          =   new Date();
        this.friendsBean    =   friendsBean;
        this.roomsBean      =   roomsBean;
        this.usersBean      =   usersBean;
    }
    
    public FriendsUpdateBean getFriendsBean() 
    {
        return friendsBean;
    }

    public void setFriendsBean(FriendsUpdateBean friendsBean)
    {
        this.friendsBean = friendsBean;
    }

    public RoomsUpdateBean getRoomsBean()
    {
        return roomsBean;
    }

    public void setRoomsBean(RoomsUpdateBean roomsBean)
    {
        this.roomsBean = roomsBean;
    }
    
    public UsersUpdateBean getUsersBean()
    {
        return usersBean;
    }
    
    public void setUsersBean(UsersUpdateBean usersBean)
    {
        this.usersBean  =   usersBean;
    }
    
    public Date getUpdateTime()
    {
        return updateTime;
    }
}
