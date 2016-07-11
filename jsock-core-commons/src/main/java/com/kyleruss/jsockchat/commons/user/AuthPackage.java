//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.user;

import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import java.io.Serializable;

/**
 * An AuthPackage contains a package delivered to authenticated users
 * On successful authentication to get quick updates without waiting
 * for server's next update
 */
public class AuthPackage implements Serializable
{
    private final User authenticatedUser;
    private final UpdateBeanDump listDump;
    
    public AuthPackage(User authenticatedUser, UpdateBeanDump listDump)
    {
        this.authenticatedUser  =   authenticatedUser;
        this.listDump           =   listDump;
    }
    
    public User getAuthenticatedUser() 
    {
        return authenticatedUser;
    }
   
    public UpdateBeanDump getListDump() 
    {
        return listDump;
    }
}
