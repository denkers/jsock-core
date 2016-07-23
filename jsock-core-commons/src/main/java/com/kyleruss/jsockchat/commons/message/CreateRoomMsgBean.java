//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

public class CreateRoomMsgBean implements MessageBean
{
    private final String name;
    private final String password;
    private final boolean isPrivate;
    
    public CreateRoomMsgBean(String name, String password, boolean isPrivate)
    {
        this.name       =   name;
        this.password   =   password;
        this.isPrivate  =   isPrivate;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public boolean isPrivate()
    {
        return isPrivate;
    }
}
