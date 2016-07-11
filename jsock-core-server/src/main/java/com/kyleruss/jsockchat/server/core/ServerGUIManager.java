//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;

import com.kyleruss.jsockchat.commons.gui.GUIManager;
import com.kyleruss.jsockchat.server.gui.ServerMenuBar;
import com.kyleruss.jsockchat.server.gui.ServerPanel;

/**
 * A manager for initializing the servers gui
 */
public class ServerGUIManager extends GUIManager
{
    private static ServerGUIManager instance;
    
    /**
     * Initializes the LookNfeel, frame and menuBar
     */
    private ServerGUIManager()
    {
        initLookAndFeel(ServerConfig.LOOKNFEEL_PACKAGE);
        panel   =   ServerPanel.getInstance();
        initFrame(ServerConfig.WINDOW_TITLE);
        attachMenubar(ServerMenuBar.getInstance());
    }
    
    public static ServerGUIManager getInstance()
    {
        if(instance == null) instance = new ServerGUIManager();
        return instance;
    }
}
