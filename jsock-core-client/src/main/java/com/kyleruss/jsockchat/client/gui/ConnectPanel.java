//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.client.core.SocketManager;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.prompt.PromptSupport;


public class ConnectPanel extends LoginPanel
{
    private JTextField hostField, portField;
    
    public ConnectPanel()
    {
        submitButton.setIcon(new ImageIcon(AppResources.getInstance().getConnectImage()));
        titleLabel.setText("Connect");
        titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getServerImage()));
        fieldPanel.removeAll();
        
        hostField   =   new JTextField();
        portField   =   new JTextField();
        
        PromptSupport.setPrompt(" Server hostname/address", hostField);
        PromptSupport.setPrompt(" Port number of server", portField);
        
        hostField.setPreferredSize(usernameField.getPreferredSize());
        portField.setPreferredSize(usernameField.getPreferredSize());
        hostField.setBorder(fieldBorder);
        portField.setBorder(fieldBorder);
        
        fieldPanel.add(titleLabel, "wrap");
        fieldPanel.add(new JLabel("Address"));
        fieldPanel.add(hostField, "wrap");
        fieldPanel.add(new JLabel("Port"));
        fieldPanel.add(portField, "wrap");
        fieldPanel.add(processingPanel, "span 2, al center, gapy 15");
    }
    
    @Override
    protected void submit()
    {
        showProcessing(true);
        
        String host;
        int port;
        
        try
        {
            host    =   hostField.getText();
            port    =   Integer.parseInt(portField.getText());
            
            if(port > 65535 || host.equals("")) throw new NumberFormatException();
            
            SocketManager.getInstance().initSockets(host, port); 
        }
        
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Please enter correct input");
            showProcessing(false);
        }
    }
}
