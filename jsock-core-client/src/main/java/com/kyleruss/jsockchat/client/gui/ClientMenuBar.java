//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.commons.gui.MappedMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ClientMenuBar extends MappedMenuBar
{
    private static ClientMenuBar instance;
    private final JMenu aboutMenu, fileMenu, accountMenu;

    private ClientMenuBar()
    {
        aboutMenu   =   new JMenu("About");
        fileMenu    =   new JMenu("File");
        accountMenu =   new JMenu("Account");

        addItem("dcItem", new JMenuItem("Disconnect"), fileMenu);
        addItem("miniItem", new JMenuItem("Minimize"), fileMenu);
        addItem("exitItem", new JMenuItem("Exit"), fileMenu);
        addItem("loginItem", new JMenuItem("Login"), accountMenu);
        addItem("registerItem", new JMenuItem("Register"), accountMenu);
        addItem("logoutItem", new JMenuItem("Logout"), accountMenu);
        addItem("authorItem", new JMenuItem("Software author"), aboutMenu);
        
        
        add(fileMenu);
        add(accountMenu);
        add(aboutMenu);
        
        getItem("loginItem").setEnabled(false);
        getItem("registerItem").setEnabled(false);
        getItem("logoutItem").setEnabled(false);
        getItem("dcItem").setEnabled(false);
    }
    
    public JMenu getAboutMenu() 
    {
        return aboutMenu;
    }

    public JMenu getFileMenu() 
    {
        return fileMenu;
    }

    public JMenu getAccountMenu()
    {
        return accountMenu;
    }

    public static ClientMenuBar getInstance()
    {
        if(instance == null) instance = new ClientMenuBar();
        return instance;
    }
}
