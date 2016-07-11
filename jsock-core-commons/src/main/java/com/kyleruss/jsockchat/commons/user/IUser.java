//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.user;

import java.io.Serializable;
import java.util.List;

/*
* A standard user definition
* Password fetch is not necessary
*/
public interface IUser extends Serializable
{
    /**
     * @return The users unique ID
     */
    public String getUsername();
    
    /**
     * @return The users custom name
     */
    public String getDisplayName();
    
    /**
     * @return A list of rooms the user is involved in
     */
    public List<String> getCurrentRooms();
}
