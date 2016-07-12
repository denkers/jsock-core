//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.io;

import com.kyleruss.jsockchat.server.message.AcceptFriendMessageHandler;
import com.kyleruss.jsockchat.commons.io.MessageListener;
import com.kyleruss.jsockchat.commons.message.AcceptFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.AuthMsgBean;
import com.kyleruss.jsockchat.commons.message.BroadcastMsgBean;
import com.kyleruss.jsockchat.commons.message.CreateRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.JoinRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.MessageBean;
import com.kyleruss.jsockchat.commons.message.PrivateMsgBean;
import com.kyleruss.jsockchat.commons.message.RegisterMsgBean;
import com.kyleruss.jsockchat.commons.message.RemoveFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.server.core.LoggingManager;
import com.kyleruss.jsockchat.server.core.SocketManager;
import com.kyleruss.jsockchat.server.message.AuthMessageHandler;
import com.kyleruss.jsockchat.server.message.BroadcastMessageHandler;
import com.kyleruss.jsockchat.server.message.CreateRoomMessageHandler;
import com.kyleruss.jsockchat.server.message.DisconnectMessageHandler;
import com.kyleruss.jsockchat.server.message.JoinRoomMessageHandler;
import com.kyleruss.jsockchat.server.message.PrivateMessageHandler;
import com.kyleruss.jsockchat.server.message.RegisterMessageHandler;
import com.kyleruss.jsockchat.server.message.RemoveFriendMessageHandler;
import com.kyleruss.jsockchat.server.message.RequestFriendMessageHandler;
import com.kyleruss.jsockchat.server.message.ServerMessageHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * A server for listening and interpreting messages from a client
 * ServerMessageListener are spawned from io/MessageServer on socket.accept()
 * Handeling of messages is performed by an appropriate message/*
 * @see com.kyleruss.jsockchat.commons.io.MessageListener
 */
public class ServerMessageListener extends MessageListener<RequestMessage>
{
    private static ServerMessageListener instance;
    protected String servingUser; //The messaging source (ip/username)
    
    public ServerMessageListener(Socket socket) 
    {
        super(socket);
        initUserSocket();
    }
    
    /**
     * Initializes a UserSocket for the listeners socket
     * Initializes the servingUser
     * Associates the created UserSocket with the servingUser
     */
    private void initUserSocket()
    {
        if(socket != null)
        {
            String address              =   socket.getRemoteSocketAddress().toString();
            SocketManager sockManager   =   SocketManager.getInstance();
            
            UserSocket userSocket       =   new UserSocket(socket);
            sockManager.add(address, userSocket);
            servingUser =   address;
            LoggingManager.log("[Message Server] Serving new client '");
        }
    }
    
    public synchronized void setServingUser(String servingUser)
    {
        this.servingUser    =   servingUser;
    }
    
    /**
     * Interprets a passed MessageBean and creates a handler based on the type of bean
     * @param bean The bean that's type is associated with a handler
     * @return A handler thats associated with the passed bean; null otherwise
     */
    private ServerMessageHandler getHandler(MessageBean bean)
    {
        ServerMessageHandler handler    =   null;
            
        if(bean instanceof AuthMsgBean)
            handler     =   new AuthMessageHandler(servingUser, this);
        
        else if(bean instanceof RegisterMsgBean)
            handler     =   new RegisterMessageHandler(servingUser);
        
        else if(bean instanceof DisconnectMsgBean)
            handler     =   new DisconnectMessageHandler();
        
        else if(bean instanceof JoinRoomMsgBean)
            handler     =   new JoinRoomMessageHandler();
        
        else if(bean instanceof PrivateMsgBean)
            handler     =   new PrivateMessageHandler();
        
        else if(bean instanceof BroadcastMsgBean)
            handler     =   new BroadcastMessageHandler();
        
        else if(bean instanceof RequestFriendMsgBean)
            handler     =   new RequestFriendMessageHandler();
        
        else if(bean instanceof AcceptFriendMsgBean)
            handler     =   new AcceptFriendMessageHandler();
        
        else if(bean instanceof CreateRoomMsgBean)
            handler     =   new CreateRoomMessageHandler();
        
        else if(bean instanceof RemoveFriendMsgBean)
            handler     =   new RemoveFriendMessageHandler();
        
        return handler;
    }
    
    /**
     * Gets a handler for the request
     * Performs the handlers  appropriate action
     * @param request The request to handle
     */
    @Override
    protected void handleReceivedMessage(RequestMessage request) 
    {
        if(request != null)
        {
            if(request.getUserSource() != null)
                servingUser = request.getUserSource();
            
            else
            {
                String address  =   socket.getRemoteSocketAddress().toString();
                servingUser     =   address;
            }
            
            MessageBean bean                =   request.getMessageBean();
            ServerMessageHandler handler    =   getHandler(bean);
            
            
            if(handler != null) handler.serverAction(request);
        }
    }

    /**
     * Listens for a new request message and returns it
     * @param inputStream The sockets input stream
     * @return The request message sent
     */
    @Override
    protected RequestMessage getMessage(ObjectInputStream inputStream)
    {
        try
        {
            RequestMessage request   =   (RequestMessage) inputStream.readObject();
            return request;
        }
        
        catch(IOException | ClassNotFoundException e)
        {
            return null;
        }
    }
    
    /**
     * Closes up sockets/streams 
     * @param inputStream The socket input stream
     */
    @Override
    protected void handleCleanup(ObjectInputStream inputStream)
    {
        try
        {
            if(inputStream != null) inputStream.close();
            
            if(SocketManager.getInstance().find(servingUser))
                SocketManager.getInstance().clientExit(servingUser);
            
            else socket.close();
        }
        
        catch(IOException e)
        {
            System.out.println("[ServerMessageListener@handleCleanup]: " + e.getMessage());
        }
    }
    
    public static ServerMessageListener getInstance(Socket socket)
    {
        if(instance == null) instance   =   new ServerMessageListener(socket);
        return instance;
    }
}
