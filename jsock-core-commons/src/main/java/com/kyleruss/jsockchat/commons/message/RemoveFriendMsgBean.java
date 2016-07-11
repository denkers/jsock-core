//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;


public class RemoveFriendMsgBean implements MessageBean
{
    private String friendUsername;
    private String sourceUsername;
    
    public RemoveFriendMsgBean(String friendUsername, String sourceUsername)
    {
        this.friendUsername =   friendUsername;
        this.sourceUsername =   sourceUsername;
    }
    
    public String getFriendUsername()
    {
        return friendUsername;
    }
    
    public void setFriendUsername(String friendUsername)
    {
        this.friendUsername =   friendUsername;
    }

    public String getSourceUsername() 
    {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) 
    {
        this.sourceUsername = sourceUsername;
    }
    
    
}
