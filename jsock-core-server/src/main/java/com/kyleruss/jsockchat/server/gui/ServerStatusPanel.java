//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.gui;

import com.kyleruss.jsockchat.server.core.ServerConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class ServerStatusPanel extends JPanel
{
    private static ServerStatusPanel instance;
    private final JPanel msgServerPanel, msgSendServerPanel, updateServerPanel;
    private final JLabel msgServerLabel, updateServerLabel, msgSendServerLabel;
    
    private ServerStatusPanel()
    {
        setLayout(new GridLayout(1, 3));
        setPreferredSize(new Dimension(200, 65));
        
        msgServerPanel      =   new JPanel(new MigLayout("fillx"));
        updateServerPanel   =   new JPanel(new MigLayout("fillx"));
        msgSendServerPanel  =   new JPanel(new MigLayout("fillx"));
        msgServerLabel      =   new JLabel();
        updateServerLabel   =   new JLabel();
        msgSendServerLabel  =   new JLabel();
        
        JLabel msgServerTitleLabel      =   new JLabel("MESSAGE LISTEN SERVER");
        JLabel updateServerTitleLabel   =   new JLabel("UPDATE SERVER");
        JLabel msgSendServerTitleLabel  =   new JLabel("MESSAGE SEND SERVER");
        Font titleFont                  =   new Font("SansSerif", Font.BOLD, 14);
        Font statusFont                 =   new Font("SansSerif", Font.PLAIN, 12);
        msgServerTitleLabel.setFont(titleFont);
        updateServerTitleLabel.setFont(titleFont);
        msgSendServerTitleLabel.setFont(titleFont);
        msgServerLabel.setFont(statusFont);
        updateServerLabel.setFont(statusFont);
        msgSendServerLabel.setFont(statusFont);
        updateServerLabel.setForeground(Color.WHITE);
        msgServerLabel.setForeground(Color.WHITE);
        msgSendServerLabel.setForeground(Color.WHITE);
        msgServerTitleLabel.setForeground(Color.WHITE);
        updateServerTitleLabel.setForeground(Color.WHITE);
        msgSendServerTitleLabel.setForeground(Color.WHITE);
        msgServerTitleLabel.setIcon(new ImageIcon(AppResources.getInstance().getMsgServerImage()));
        updateServerTitleLabel.setIcon(new ImageIcon(AppResources.getInstance().getUpdateServerImage()));
        msgSendServerTitleLabel.setIcon(new ImageIcon(AppResources.getInstance().getMsgSendServerImage()));
        
        msgServerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
        msgSendServerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
        updateServerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        msgServerPanel.add(msgServerTitleLabel, "al center, wrap");
        msgServerPanel.add(msgServerLabel, "al center");
        msgSendServerPanel.add(msgSendServerTitleLabel, "al center, wrap");
        msgSendServerPanel.add(msgSendServerLabel, "al center");
        updateServerPanel.add(updateServerTitleLabel, "al center, wrap");
        updateServerPanel.add(updateServerLabel, "al center");
        
        add(msgServerPanel);
        add(msgSendServerPanel);
        add(updateServerPanel);
        
        setServerStatus(true, ServerConfig.MESSAGE_LISTEN_SERVER_CODE);
        setServerStatus(true, ServerConfig.MESSAGE_SEND_SERVER_CODE);
        setServerStatus(true, ServerConfig.UPDATE_BROADCAST_SERVER_CODE);
    }
    
    
    public void setServerStatus(boolean status, int serverCode)
    {
        SwingUtilities.invokeLater(()->
        {
            ServerMenuBar menuBar   =   ServerMenuBar.getInstance();
            JPanel serverStatusPanel;
            JLabel serverStatusLabel;
            JMenuItem startItem, stopItem;

            switch(serverCode)
            {
                case ServerConfig.MESSAGE_LISTEN_SERVER_CODE:
                    serverStatusPanel   =   msgServerPanel;
                    serverStatusLabel   =   msgServerLabel;
                    stopItem            =   menuBar.getItem("msgSvStopItem");
                    startItem           =   menuBar.getItem("msgSvStartItem");
                    break;

                case ServerConfig.MESSAGE_SEND_SERVER_CODE:
                    serverStatusPanel   =   msgSendServerPanel;
                    serverStatusLabel   =   msgSendServerLabel;
                    stopItem            =   menuBar.getItem("msgSendSvStopItem");
                    startItem           =   menuBar.getItem("msgSendSvStartItem");
                    break;

                case ServerConfig.UPDATE_BROADCAST_SERVER_CODE:
                    serverStatusPanel       =   updateServerPanel;
                    serverStatusLabel       =   updateServerLabel;
                    startItem           =   menuBar.getItem("updateSvStartItem");
                    stopItem            =   menuBar.getItem("updateSvStopItem");
                    break;

                default: return;
            }

            startItem.setEnabled(!status);
            stopItem.setEnabled(status);
            
            serverStatusPanel.setBackground(status? new Color(52, 201, 57) : new Color(212, 42, 42));
            serverStatusLabel.setText(status? "ONLINE" : "OFFLINE");
        });
    }
    
    public static ServerStatusPanel getInstance()
    {
        if(instance == null) instance = new ServerStatusPanel();
        return instance;
    }
}
