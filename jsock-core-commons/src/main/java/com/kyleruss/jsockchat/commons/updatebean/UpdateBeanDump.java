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
    private RoomsUpdateBean roomsBean;
    
    
    private Date updateTime;
    
    
    public UpdateBeanDump()
    {
        this(new RoomsUpdateBean());
    }
    
    public UpdateBeanDump(RoomsUpdateBean roomsBean)
    {
        updateTime          =   new Date();
        this.roomsBean      =   roomsBean;
    }
    
    public RoomsUpdateBean getRoomsBean()
    {
        return roomsBean;
    }

    public void setRoomsBean(RoomsUpdateBean roomsBean)
    {
        this.roomsBean = roomsBean;
    }
    
    public Date getUpdateTime()
    {
        return updateTime;
    }
}
