//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

public class BroadcastMsgBean implements MessageBean
{
    private String room;
    private String content;
    
    public BroadcastMsgBean(String room, String content)
    {
        this.room       =   room;
        this.content    =   content;
    }

    public String getRoom() 
    {
        return room;
    }

    public String getContent() 
    {
        return content;
    }
    
    public void setRoom(String room)
    {
        this.room = room;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }
}
