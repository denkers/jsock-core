//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.updatebean;

import com.kyleruss.jsockchat.client.core.UserManager;
import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.updatebean.FriendsUpdateBean;
import com.kyleruss.jsockchat.commons.user.IUser;
import java.util.List;


public class FriendsUpdateBeanHandler extends UpdateBeanHandler<FriendsUpdateBean>
{
    public FriendsUpdateBeanHandler(FriendsUpdateBean bean) 
    {
        super(bean);
    }
    
    @Override
    public void beanAction() 
    {
        UserManager userManager =   UserManager.getInstance();
        boolean notifyPending   =   (userManager.getFriendsBean() == null || userManager.getFriendsBean().getDataList().size() != bean.getPendingRequests().size())
                                    && bean.getPendingRequests().size() > 0;
        
        userManager.setFriendsBean(bean);
        
        List<IUser> friends                         =   bean.getDataList();
        List<RequestFriendMsgBean> friendRequests   =   bean.getPendingRequests();
        
        ChatHomePanel.getInstance().setFriendList(friends);
        ChatHomePanel.getInstance().setPendingFriendList(friendRequests);
        
        if(notifyPending) ChatHomePanel.getInstance().notifyFriendReqTab();
    }
}
