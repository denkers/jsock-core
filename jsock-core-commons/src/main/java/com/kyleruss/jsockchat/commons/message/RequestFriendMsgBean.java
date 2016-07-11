//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestFriendMsgBean implements MessageBean
{
    private final String friendA;
    private final String friendB;
    private final Date requestDate;
    private int friendshipID;
    
    public RequestFriendMsgBean(String friendA, String friendB, Date requestDate)
    {
        this.friendA        =   friendA;
        this.friendB        =   friendB;
        this.requestDate    =   requestDate;
    }
    
    public void setFriendshipID(int friendshipID)
    {
        this.friendshipID   =   friendshipID;
    }
    
    public Date getRequestDate()
    {
        return requestDate;
    }
    
    public int getFriendshipID()
    {
        return friendshipID;
    }
    
    public String getFriendA()
    {
        return friendA;
    }
    
    public String getFriendB()
    {
        return friendB;
    }
    
    @Override
    public String toString()
    {
        SimpleDateFormat formatter  =   new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate        =   formatter.format(requestDate);
        
        return "[" + formattedDate + "] Request from: " + friendA;
    }
}
