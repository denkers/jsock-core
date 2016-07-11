//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import com.kyleruss.jsockchat.client.core.ClientManager;
import com.kyleruss.jsockchat.client.core.UserManager;
import com.kyleruss.jsockchat.client.gui.ClientMenuBar;
import com.kyleruss.jsockchat.client.gui.ClientPanel;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import com.kyleruss.jsockchat.commons.user.AuthPackage;
import com.kyleruss.jsockchat.commons.user.User;
import javax.swing.JOptionPane;

public class AuthMessageHandler implements ClientMessageHandler
{
    @Override
    public void clientAction(ResponseMessage response) 
    {
        if(response.getStatus())
        {
            AuthPackage authPackage =   (AuthPackage) response.getResponseData();
            User authUser           =   authPackage.getAuthenticatedUser();
            
            UserManager.getInstance().setActiveUser(authUser);
            ClientManager.getInstance().handleUpdates(authPackage.getListDump());
            
            ClientPanel.getInstance().getLoginView().showProcessing(false);
            ClientPanel.getInstance().changeView(ClientConfig.HOME_VIEW_CARD);
            
            ClientMenuBar menu  =   ClientMenuBar.getInstance();
            menu.getItem("loginItem").setEnabled(false);
            menu.getItem("registerItem").setEnabled(false);
        }
        
        else
        {
            ClientPanel.getInstance().getLoginView().showProcessing(false);
            JOptionPane.showMessageDialog(null, response.getDescription(), "Authentication failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void witnessAction(ResponseMessage response) {}
}
