//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.client.gui.ChatMessage;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;


public class DisconnectMessageHandler implements ClientMessageHandler 
{
    @Override
    public void clientAction(ResponseMessage response) {}

    @Override
    public void witnessAction(ResponseMessage response) 
    {
        DisconnectMsgBean bean  =   (DisconnectMsgBean) response.getRequestMessage().getMessageBean();
        String sender           =   "SERVER";
        String content          =   response.getRequestMessage().getUserSource() + " has left the room";
        ChatMessage msg         =   new ChatMessage(sender, content, true);
        
        ChatHomePanel.getInstance().broadcastMessage(msg, bean.getRoom(), false);
    }
}
