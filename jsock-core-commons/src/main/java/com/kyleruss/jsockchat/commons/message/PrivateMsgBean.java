//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

public class PrivateMsgBean implements MessageBean
{
    private String destinationUser;
    private String content;
    
    public PrivateMsgBean(String destinationUser, String content)
    {
        this.destinationUser    =   destinationUser;
        this.content            =   content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getDestinationUser() 
    {
        return destinationUser;
    }

    public void setDestinationUser(String destinationUser)
    {
        this.destinationUser    =   destinationUser;
    }
}
