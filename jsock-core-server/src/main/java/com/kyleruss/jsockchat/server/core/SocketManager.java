//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.commons.message.Message;
import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.server.io.ServerMessageSender;
import com.kyleruss.jsockchat.server.io.UserSocket;
import java.io.IOException;

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
            LoggingManager.log("[Socket manager] Client '" + username + "' has disconnected, cleaning up resources");
            
            UserSocket userSocket   =   get(username);
            userSocket.cleanUp();
            remove(username);
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
            
            LoggingManager.log("[Logout] User '" + username + "' has logged out");
        }
    }
    
       /**
     * Removes the user from the managed map
     * Notifies room users of users exit
     * @param client The user who is exiting
     */
    public synchronized void clientExit(String client)
    {
        if(client == null) return;
        
        LoggingManager.log("[User manager] User '" + client + "' has exited");
        
        RoomManager.getInstance().leaveAllRooms(client);
        cleanUp(client);
    }
    
    
    /**
     * Sends a direct message to the passed user
     * @param username The username to send to
     * @param message The message being sent
     */
    public synchronized void sendMessageToUser(String username, Message message) throws IOException
    {
        UserSocket userSocket     =   SocketManager.getInstance().get(username);

        if(userSocket != null)
        {
            MessageQueueItem messageItem    =   new MessageQueueItem(userSocket.getSocketOutputStream(), message);
            ServerMessageSender.getInstance().addMessage(messageItem);
        }
    }
    
    public static SocketManager getInstance()
    {
        if(instance == null) instance = new SocketManager();
        return instance;
    }
}
