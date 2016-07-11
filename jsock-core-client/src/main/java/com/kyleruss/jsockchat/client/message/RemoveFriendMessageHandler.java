//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.core.UserManager;
import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.RemoveFriendMsgBean;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.user.IUser;
import java.util.Map;
import javax.swing.JOptionPane;

public class RemoveFriendMessageHandler implements ClientMessageHandler
{
    @Override
    public void clientAction(ResponseMessage response)
    {
        RemoveFriendMsgBean bean    =   (RemoveFriendMsgBean) response.getRequestMessage().getMessageBean();
        ChatHomePanel.getInstance().toggleFriendRemoveProcessing(false);
        
        if(response.getStatus())
        {
            String removedFriend        =   bean.getFriendUsername();
            Map<String, IUser> friends  =   UserManager.getInstance().getFriendsBean().getData();
            
            if(friends.containsKey(removedFriend))
            {
                friends.remove(removedFriend);
                ChatHomePanel chatPanel =   ChatHomePanel.getInstance();
                chatPanel.removeChatTab(removedFriend, true);
                chatPanel.setFriendList(UserManager.getInstance().getFriendsBean().getDataList());
            }
        }
        
        JOptionPane.showMessageDialog(null, response.getDescription());
    }

    @Override
    public void witnessAction(ResponseMessage response)
    {
    }
    
}
