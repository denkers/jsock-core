//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.message;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import com.kyleruss.jsockchat.client.gui.ClientPanel;
import com.kyleruss.jsockchat.client.gui.RegisterPanel;
import com.kyleruss.jsockchat.commons.message.ResponseMessage;
import javax.swing.JOptionPane;

public class RegisterMessageHandler implements ClientMessageHandler 
{

    @Override
    public void clientAction(ResponseMessage response) 
    {
        RegisterPanel registerView =   ClientPanel.getInstance().getRegisterView();
        
        registerView.showProcessing(false);
        JOptionPane.showMessageDialog(null, response.getDescription(), "Registration message", 
        (response.getStatus()? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE));
        
        if(response.getStatus())
            ClientPanel.getInstance().changeView(ClientConfig.LOGIN_VIEW_CARD);
    }

    @Override
    public void witnessAction(ResponseMessage response) {
    }
    
}
