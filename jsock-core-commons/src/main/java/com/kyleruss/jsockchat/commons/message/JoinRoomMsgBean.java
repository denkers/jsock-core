//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

public class JoinRoomMsgBean implements RoomBean
{
    private String room;
    private String attemptedPassword;

    public JoinRoomMsgBean(String room, String password)
    {
        this.room                   =   room;
        this.attemptedPassword      =   password;
    }

    @Override
    public String getRoom() 
    {
        return room;
    }

    public String getAttemptedPassword() 
    {
        return attemptedPassword;
    }
    
    public void setRoom(String room)
    {
        this.room = room;
    }

    public void setAttemptedPassword(String attemptedPassword) 
    {
        this.attemptedPassword = attemptedPassword;
    }
}
