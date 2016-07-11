//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.client.gui.ChatMessage;
import com.kyleruss.jsockchat.commons.message.JoinRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import javax.swing.JOptionPane;

public class JoinRoomMessageHandler implements ClientMessageHandler
{
    @Override
    public void clientAction(ResponseMessage response) 
    {
        ChatHomePanel.getInstance().toggleJoinRoomProcessing(false);
        JoinRoomMsgBean bean    =   (JoinRoomMsgBean) response.getRequestMessage().getMessageBean();
        
        if(!response.getStatus())
            JOptionPane.showMessageDialog(null, response.getDescription(), "Join room failed", JOptionPane.ERROR_MESSAGE);
        
        else
            ChatHomePanel.getInstance().addChatTab(bean.getRoom(), false);
    }

    @Override
    public void witnessAction(ResponseMessage response)
    {
        JoinRoomMsgBean bean    =   (JoinRoomMsgBean) response.getRequestMessage().getMessageBean();
        String sender           =   "SERVER";
        String content          =   response.getRequestMessage().getUserSource() + " has joined the room";
        ChatMessage msg         =   new ChatMessage(sender, content, true);
        
        ChatHomePanel.getInstance().broadcastMessage(msg, bean.getRoom(), false);
    }
}
