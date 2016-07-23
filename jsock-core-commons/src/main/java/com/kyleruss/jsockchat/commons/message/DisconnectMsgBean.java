//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

public class DisconnectMsgBean implements RoomBean
{
    public static final int ROOM_LEAVE      =   0;
    public static final int CLIENT_LOGOUT   =   1;
    public static final int CLIENT_CLOSE    =   2;
    
    private String room;
    private int disconnectType;

    public DisconnectMsgBean(int disconnectType)
    {
        this.disconnectType     =   disconnectType;
    }

    public String getRoom() 
    {
        return room;
    }

    public int getDisconnectType() 
    {
        return disconnectType;
    }
    
    public void setRoom(String room) 
    {    
        this.room   =   room;
    }
    
    public void setDisconnectType(int disconnectType)
    {
        this.disconnectType =   disconnectType;
    }
}
