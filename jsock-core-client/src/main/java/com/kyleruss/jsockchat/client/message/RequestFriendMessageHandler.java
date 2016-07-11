//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import javax.swing.JOptionPane;

public class RequestFriendMessageHandler implements ClientMessageHandler
{

    @Override
    public void clientAction(ResponseMessage response)
    {
        ChatHomePanel.getInstance().toggleFriendRequestProcessing(false);
        JOptionPane.showMessageDialog(null, response.getDescription());
    }

    @Override
    public void witnessAction(ResponseMessage response) {}
}
