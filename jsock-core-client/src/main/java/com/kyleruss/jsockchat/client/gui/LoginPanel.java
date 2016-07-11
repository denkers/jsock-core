//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import com.kyleruss.jsockchat.client.core.ClientManager;
import com.kyleruss.jsockchat.client.core.SocketManager;
import com.kyleruss.jsockchat.commons.message.AuthMsgBean;
import com.kyleruss.jsockchat.commons.message.RequestMessage;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.prompt.PromptSupport;

public class LoginPanel extends JPanel implements ActionListener
{
    protected JTextField usernameField;
    protected JPasswordField passwordField;
    protected JButton submitButton;
    protected JPanel fieldPanel;
    protected JLabel titleLabel;
    protected Border fieldBorder;
    protected JPanel processingPanel;
    protected JLabel processingLabel;
    
    protected final String PROCESS_CARD =   "process_c";
    protected final String CONTROL_CARD =   "control_c";
    
    public LoginPanel()
    {
        setBackground(Color.WHITE);
        
        fieldPanel      =   new JPanel(new MigLayout());
        passwordField   =   new JPasswordField();
        usernameField   =   new JTextField();
        submitButton    =   new JButton(new ImageIcon(AppResources.getInstance().getLoginImage()));
        
        fieldBorder     =   BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY);
        fieldPanel.setBorder(fieldBorder);
        usernameField.setBorder(fieldBorder);
        passwordField.setBorder(fieldBorder);
        fieldPanel.setPreferredSize(new Dimension(300, 180));
        PromptSupport.setPrompt(" Enter existing username", usernameField);
        PromptSupport.setPrompt(" Enter account password", passwordField);
        
        ClientPanel.removeBorder(submitButton);
        titleLabel   =   new JLabel("Sign in");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getLockImage()));
        
        processingPanel =   new JPanel(new CardLayout());
        processingLabel =   new JLabel("Processing");
        processingLabel.setHorizontalAlignment(JLabel.CENTER);
        processingLabel.setIcon(new ImageIcon(ClientConfig.IMAGES_DIR + "spinner_small.gif"));
        processingPanel.add(submitButton, CONTROL_CARD);
        processingPanel.add(processingLabel, PROCESS_CARD);
        processingPanel.setBackground(Color.WHITE);
        
        usernameField.setPreferredSize(new Dimension(200, 28));
        passwordField.setPreferredSize(new Dimension(200, 28));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.add(titleLabel, "wrap");
        fieldPanel.add(new JLabel("Username"));
        fieldPanel.add(usernameField, "wrap");
        fieldPanel.add(new JLabel("Password"));
        fieldPanel.add(passwordField, "wrap");
        fieldPanel.add(processingPanel, "span 2, al center, gapy 15");
        
        
        JPanel fieldWrapperPanel    =   new JPanel();
        fieldWrapperPanel.add(fieldPanel);
        fieldWrapperPanel.setBackground(Color.WHITE);
        fieldWrapperPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        
        add(fieldWrapperPanel);
        submitButton.addActionListener(this);
    }
    
    public void showProcessing(boolean process)
    {
        CardLayout cLayout  =   (CardLayout) processingPanel.getLayout();
        cLayout.show(processingPanel, process? PROCESS_CARD : CONTROL_CARD);
    }
    
    
    
    public JTextField getUsernameField() 
    {
        return usernameField;
    }

    public JPasswordField getPasswordField() 
    {
        return passwordField;
    }
    
    protected void submit()
    {
        Thread authThread       =   new Thread(()->
        {
            try
            {
                showProcessing(true);
                String username         =   usernameField.getText();
                String password         =   new String(passwordField.getPassword());
                AuthMsgBean bean        =   new AuthMsgBean(username, password, SocketManager.getInstance().getUdpPort());
                RequestMessage request  =   new RequestMessage(null, bean);   
                ClientManager.getInstance().sendRequest(request);
            }
            
            catch(IOException e)
            {   
                JOptionPane.showMessageDialog(null, "Failed to send authentication request", "Authentication failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        authThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
       
        if(src == submitButton)
            submit();
    }
}
