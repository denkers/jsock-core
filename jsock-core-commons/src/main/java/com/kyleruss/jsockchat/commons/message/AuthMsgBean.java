//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

public class AuthMsgBean implements MessageBean
{
    private String username;
    private String password;
    private int udpPort;
   
    public AuthMsgBean(String username, String password, int udpPort)
    {
        this.username           =   username;
        this.password           =   password;
        this.udpPort            =   udpPort;
    }
    
    public String getUsername() 
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
    
    public int getUdpPort()
    {
        return udpPort;
    }
}
