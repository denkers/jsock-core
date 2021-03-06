//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.updatebean;

import com.kyleruss.jsockchat.commons.room.Room;

public class RoomsUpdateBean extends AbstractUpdateBean<Room>
{
    private String serverNodeName;
    
    public String getServerNodeName()
    {
        return serverNodeName;
    }
    
    public void setServerNodeName(String serverNodeName)
    {
        this.serverNodeName  =   serverNodeName;
    }
}
