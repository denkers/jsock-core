//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.updatebean;

import com.kyleruss.jsockchat.client.core.RoomManager;
import com.kyleruss.jsockchat.client.gui.ChatHomePanel;
import com.kyleruss.jsockchat.commons.updatebean.RoomsUpdateBean;

public class RoomsUpdateBeanHandler extends UpdateBeanHandler<RoomsUpdateBean>
{
    public RoomsUpdateBeanHandler(RoomsUpdateBean bean) 
    {
        super(bean);
    }
    
    @Override
    public void beanAction() 
    {
        RoomManager roomManager =   RoomManager.getInstance();
        roomManager.setRoomsBean(bean);
        
        ChatHomePanel.getInstance().updateUserRoomLists(bean.getDataList());
        ChatHomePanel.getInstance().getRoomTree().getRootNode().setUserObject(bean.getServerNodeName());
    }
}
