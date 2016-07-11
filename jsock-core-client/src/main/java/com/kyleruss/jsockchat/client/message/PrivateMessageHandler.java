//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.client.gui.ChatMessage;
import com.kyleruss.jsockchat.commons.message.PrivateMsgBean;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;

public class PrivateMessageHandler implements ClientMessageHandler
{
    @Override
    public void clientAction(ResponseMessage response) {}

    @Override
    public void witnessAction(ResponseMessage response) 
    {
        PrivateMsgBean bean     =   (PrivateMsgBean) response.getRequestMessage().getMessageBean();
        String source           =   response.getRequestMessage().getUserSource();
        String content          =   bean.getContent();
        
        ChatMessage chatMessage =   new ChatMessage(source, content, false);
        ChatHomePanel.getInstance().broadcastMessage(chatMessage, source, true);
    }
}
