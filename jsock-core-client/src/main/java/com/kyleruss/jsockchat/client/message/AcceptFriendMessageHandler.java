//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.AcceptFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class AcceptFriendMessageHandler implements ClientMessageHandler
{
    @Override
    public void clientAction(ResponseMessage response) 
    {
        ChatHomePanel.getInstance().toggleFriendResponseProcessing(false);
        AcceptFriendMsgBean bean            =   (AcceptFriendMsgBean) response.getRequestMessage().getMessageBean();
        JList pendingList                   =   ChatHomePanel.getInstance().getPendingFriendList();
        RequestFriendMsgBean reqBean        =   (RequestFriendMsgBean) pendingList.getSelectedValue();
        
        if(reqBean != null && reqBean.getFriendshipID() == bean.getFriendshipID())
            ((DefaultListModel) pendingList.getModel()).removeElement(reqBean);
        
        if(!response.getStatus())
            JOptionPane.showMessageDialog(null, response.getDescription());
        
        else if(bean.isAcceptRequest())
            JOptionPane.showMessageDialog(null, bean.getFromUser() + " has been added to friends");
    }

    @Override
    public void witnessAction(ResponseMessage response)
    {
        JOptionPane.showMessageDialog(null, response.getDescription());
    }
}
