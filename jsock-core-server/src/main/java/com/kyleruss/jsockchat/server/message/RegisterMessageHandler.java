//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.message.MessageQueueItem;
import com.kyleruss.jsockchat.commons.message.RegisterMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.user.User;
import com.kyleruss.jsockchat.server.core.SocketManager;
import com.kyleruss.jsockchat.server.db.DBUsers;
import com.kyleruss.jsockchat.server.gui.AppResources;
import com.kyleruss.jsockchat.server.gui.LogMessage;
import com.kyleruss.jsockchat.server.gui.LoggingList;
import com.kyleruss.jsockchat.server.io.ServerMessageSender;
import com.kyleruss.jsockchat.server.io.UserSocket;
import java.io.IOException;

public class RegisterMessageHandler implements ServerMessageHandler
{
    private final String servingUser;
    
    public RegisterMessageHandler(String servingUser)
    {
        this.servingUser =   servingUser;
    }
    
    @Override
    public void serverAction(RequestMessage request) 
    {
        ResponseMessage response        =   new ResponseMessage(request);
        RegisterMsgBean registerBean    =   (RegisterMsgBean) request.getMessageBean();
        String username                 =   registerBean.getUsername();
        String password                 =   registerBean.getPassword();
        String displayname              =   registerBean.getDisplayName();
        
        DBUsers userModel               =   DBUsers.getInstance();
        User existingUser               =   userModel.getuser(username);
        
        if(existingUser != null)
        {
            response.setStatus(false);
            response.setDescription("Failed to register: Username already exists");
        }
        
        else
        {
            boolean result      =   userModel.createUser(username, password, displayname);
            String responseMsg  =   result? "Your account has been successfully created" : "Failed to create account";   
            response.setStatus(result);
            response.setDescription(responseMsg);
        }
        
        UserSocket userSocket   =   SocketManager.getInstance().get(servingUser);
        
        try
        {
            MessageQueueItem messageItem   =   new MessageQueueItem(userSocket.getSocketOutputStream(), response);
            ServerMessageSender.getInstance().addMessage(messageItem);
            LoggingList.sendLogMessage(new LogMessage("[Registration] User '" + username + "' has created an account", 
            AppResources.getInstance().getAddFriendImage()));
        }

        catch(IOException e)
        {
            System.out.println("[RegisterMessageHandler@serverAction]: " + e.getMessage());
        }
    }
}
