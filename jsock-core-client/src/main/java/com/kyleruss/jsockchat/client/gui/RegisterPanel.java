//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.client.core.ClientManager;
import com.kyleruss.jsockchat.commons.message.RegisterMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.prompt.PromptSupport;

public class RegisterPanel extends LoginPanel
{
    private JTextField displaynameField;
    
    public RegisterPanel()
    {
        fieldPanel.setPreferredSize(new Dimension(350, 200));
        fieldPanel.remove(submitButton);
        titleLabel.setText("REGISTER");
        titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getAddPersonImage()));
        
        displaynameField    =   new JTextField();
        
        submitButton.setIcon(new ImageIcon(AppResources.getInstance().getRegisterImage()));
        PromptSupport.setPrompt(" Your name in chat", displaynameField);
        PromptSupport.setPrompt(" A unique account name", usernameField);
        
        displaynameField.setBorder(fieldBorder);
        displaynameField.setPreferredSize(usernameField.getPreferredSize());
        fieldPanel.add(new JLabel("Name"));
        fieldPanel.add(displaynameField, "wrap");
        fieldPanel.add(processingPanel, "span 2, al center, gapy 20, gapx 50");
    }
    
    @Override
    protected void submit()
    {
        Thread registerThread   =   new Thread(()->
        {
            try
            {
                showProcessing(true);
                String username         =   usernameField.getText();
                String password         =   new String(passwordField.getPassword());
                String name             =   displaynameField.getText();
                RegisterMsgBean bean    =   new RegisterMsgBean(username, password, name);
                RequestMessage request  =   new RequestMessage(null, bean);
                ClientManager.getInstance().sendRequest(request);
            }
            
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(null, "Failed to send register request", "Request failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        registerThread.start();
    }
}
