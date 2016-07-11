//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.updatebean;

import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import java.util.List;
import com.kyleruss.jsockchat.commons.user.IUser;

public class FriendsUpdateBean extends AbstractUpdateBean<IUser>
{
    private List<RequestFriendMsgBean> pendingRequests;
    
    public List<RequestFriendMsgBean> getPendingRequests()
    {
        return pendingRequests;
    }
    
    public void setPendingRequests(List<RequestFriendMsgBean> pendingRequests)
    {
        this.pendingRequests    =   pendingRequests;
    }
}
