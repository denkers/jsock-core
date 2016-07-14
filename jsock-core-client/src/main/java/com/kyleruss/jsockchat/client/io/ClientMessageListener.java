//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.io;

import com.kyleruss.jsockchat.client.core.SocketManager;
import com.kyleruss.jsockchat.client.message.ClientMessageHandler;
import com.kyleruss.jsockchat.commons.io.MessageListener;
import com.kyleruss.jsockchat.commons.message.MessageBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.user.IUser;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * A server that listens and handles server response messages
 */
public class ClientMessageListener extends MessageListener<ResponseMessage>
{
    public ClientMessageListener(Socket socket) 
    {
        super(socket);
    }

    /**
     * @param bean The request bean
     * @return An appropriate handler for the passed bean
     */
    private ClientMessageHandler getHandler(MessageBean bean)
    {
        ClientMessageHandler handler    =   null;   
        
        return handler;
    }
    
    /**
     * Handles a response message
     * Uses appropriate action function based on witness status
     * @param response The read ResponseMessage
     */
    @Override
    protected void handleReceivedMessage(ResponseMessage response)
    {
        RequestMessage request          =   response.getRequestMessage();
        MessageBean bean                =   request.getMessageBean();
        String user                     =   SocketManager.getInstance().getActiveUser();
        ClientMessageHandler handler    =   getHandler(bean);
        
        if(handler != null)
        {
            if(user == null || !request.isWitness(user))
                handler.clientAction(response);

            else
                handler.witnessAction(response); 
        }
    }
    
    /**
     * Closes the passed inputStream and cleanup in SocketManager
     * @param inputStream The client's inputstream
     */
    @Override
    protected void handleCleanup(ObjectInputStream inputStream)
    {
        try
        {
            if(inputStream != null) inputStream.close();
            SocketManager.getInstance().cleanUp();
        }
        
        catch(IOException e)
        {
            System.out.println("[ServerMessageListener@handleCleanup]: " + e.getMessage());
        }
    }

    /**
     * Waits for a ResponseMessage (blocks) and returns it
     * @param inputStream The input stream to read from
     * @return A response message from the stream
     */
    @Override
    protected ResponseMessage getMessage(ObjectInputStream inputStream) 
    {
        try
        {
            ResponseMessage response    =   (ResponseMessage) inputStream.readObject();
            return response;
        }
        
        catch(IOException | ClassNotFoundException | ClassCastException e)
        {
            System.out.println("[ClientMessageListener@getMessage]: " + e.getMessage());
            return null;
        }
    }
    
}