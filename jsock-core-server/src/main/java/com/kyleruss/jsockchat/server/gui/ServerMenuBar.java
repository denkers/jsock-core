//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.gui;

import com.kyleruss.jsockchat.commons.gui.MappedMenuBar;
import java.util.HashMap;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ServerMenuBar extends MappedMenuBar
{
    private final JMenu aboutMenu;
    private final JMenu logsMenu;
    private final JMenu serverMenu;
    private final JMenu msgServerMenu;
    private final JMenu msgSendServerMenu;
    private final JMenu updateServerMenu;
    private static ServerMenuBar instance;
    
    public ServerMenuBar()
    {
        aboutMenu           =   new JMenu("About");
        logsMenu            =   new JMenu("Logs");
        serverMenu          =   new JMenu("Server");
        msgServerMenu       =   new JMenu("Message listen server");
        msgSendServerMenu   =   new JMenu("Message send server");
        updateServerMenu    =   new JMenu("Update broadcast server");
        items               =   new HashMap<>();
        
        serverMenu.add(msgServerMenu);
        serverMenu.add(msgSendServerMenu);
        serverMenu.add(updateServerMenu);
        
        addItem("msgSvStopItem", new JMenuItem("Pause"), msgServerMenu);
        addItem("msgSvStartItem", new JMenuItem("Start"), msgServerMenu);
        addItem("updateSvStartItem", new JMenuItem("Start"), updateServerMenu);
        addItem("updateSvStopItem", new JMenuItem("Pause"), updateServerMenu);
        addItem("authorItem", new JMenuItem("Author"), aboutMenu);
        addItem("logClearItem", new JMenuItem("Clear"), logsMenu);
        addItem("msgSendSvStopItem", new JMenuItem("Pause"), msgSendServerMenu);
        addItem("msgSendSvStartItem", new JMenuItem("Start"), msgSendServerMenu);
        
        add(serverMenu);
        add(logsMenu);
        add(aboutMenu);
    }
    
    public JMenu getAboutMenu()
    {
        return aboutMenu;
    }

    public JMenu getLogsMenu() 
    {
        return logsMenu;
    }

    public JMenu getServerMenu() 
    {
        return serverMenu;
    }
    
    public JMenu getMsgServerMenu()
    {
        return msgServerMenu;
    }

    public JMenu getUpdateServerMenu() 
    {
        return updateServerMenu;
    }
    
    public static ServerMenuBar getInstance()
    {
        if(instance == null) instance = new ServerMenuBar();
        return instance;
    }
}
