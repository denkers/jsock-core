//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import com.kyleruss.jsockchat.server.io.UserSocket;

/**
 * Manages UserSockets and their associated serveringUser keys
 */
public class SocketManager extends AbstractManager<String, UserSocket>
{
    private static SocketManager instance;
    
    /**
     * Removes a users UserSocket and cleans up its socket
     * @param username The user who's socket to clean up
     */
    public void cleanUp(String username)
    {
        if(find(username))
        {
            LoggingList.sendLogMessage(new LogMessage("[Socket manager] Client '" + username + "' has disconnected, cleaning up resources", 
                AppResources.getInstance().getDcImage()));
            
            UserSocket userSocket   =   get(username);
            userSocket.cleanUp();
            remove(username);
            UserManager.getInstance().remove(username);
        }
    }
    
    /**
     * Changes the associated key for the passed users UserSocket
     * Goes back to using the user's remote IP to identify
     * @param username 
     */
    public void processLogout(String username)
    {
        if(find(username))
        {
            UserSocket userSocket   =   get(username);
            remove(username);
            add(userSocket.getSocket().getRemoteSocketAddress().toString(), userSocket);
            
            UserManager.getInstance().remove(username);
            LoggingList.sendLogMessage(new LogMessage("[Logout] User '" + username + "' has logged out", AppResources.getInstance().getDcImage()));
        }
    }
    
    public static SocketManager getInstance()
    {
        if(instance == null) instance = new SocketManager();
        return instance;
    }
}
