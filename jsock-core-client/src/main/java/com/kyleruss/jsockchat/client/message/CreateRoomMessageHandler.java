//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.CreateRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import javax.swing.JOptionPane;

public class CreateRoomMessageHandler implements ClientMessageHandler
{
    @Override
    public void clientAction(ResponseMessage response)
    {
        CreateRoomMsgBean bean  =   (CreateRoomMsgBean) response.getRequestMessage().getMessageBean();
        
        if(response.getStatus())
        {
            String room =   bean.getName();
            ChatHomePanel.getInstance().addChatTab(room, false);
        }
        
        else JOptionPane.showMessageDialog(null, response.getDescription(), "Create room failed", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void witnessAction(ResponseMessage response) {}
}
