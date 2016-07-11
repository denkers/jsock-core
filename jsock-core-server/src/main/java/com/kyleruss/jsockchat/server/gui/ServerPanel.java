//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.gui;

import com.kyleruss.jsockchat.commons.gui.RoomTree;
import com.kyleruss.jsockchat.commons.gui.UserList;
import com.kyleruss.jsockchat.server.core.RoomManager;
import com.kyleruss.jsockchat.server.core.ServerConfig;
import com.kyleruss.jsockchat.server.io.MessageServer;
import com.kyleruss.jsockchat.server.io.ServerMessageSender;
import com.kyleruss.jsockchat.server.io.UpdateBroadcastServer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

public class ServerPanel extends JPanel
{
    private static ServerPanel instance;
    private final LoggingList loggingList;
    private final ServerStatusPanel statusPanel;
    private final JScrollPane userScrollPane, roomScrollPane;
    private final JPanel leftPanel, rightPanel;
    private final JSplitPane treeServerSplit;
    private final JPanel userPanel;
    private final RoomTree roomTree;
    private final UserList userList;
    
    private ServerPanel()
    {
        setPreferredSize(new Dimension(ServerConfig.WINDOW_WIDTH, ServerConfig.WINDOW_HEIGHT));
        setLayout(new BorderLayout());
        
        loggingList         =   LoggingList.getInstance();
        statusPanel         =   ServerStatusPanel.getInstance();
        leftPanel           =   new JPanel(new GridLayout(2, 1));
        rightPanel          =   new JPanel(new BorderLayout());
        treeServerSplit     =   new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        userPanel           =   new JPanel(new BorderLayout());
        roomTree            =   new RoomTree("JSockchat Server");
        userList            =   new UserList();
        userScrollPane      =   new JScrollPane(userList);
        roomScrollPane      =   new JScrollPane(roomTree);
        
        roomTree.setFocusable(false);
        userList.setFocusable(false);
        loggingList.getList().setSelectionModel(new NonSelectionModel());
        userList.setSelectionModel(new NonSelectionModel());
        
        final int HOR_POLICY        =     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        final int VERT_POLICY       =     ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        userScrollPane.setHorizontalScrollBarPolicy(HOR_POLICY);
        roomScrollPane.setHorizontalScrollBarPolicy(HOR_POLICY);
        userScrollPane.setVerticalScrollBarPolicy(VERT_POLICY);
        roomScrollPane.setVerticalScrollBarPolicy(VERT_POLICY);
        
        AppResources resources  =   AppResources.getInstance();
        roomTree.setServerIcon(new ImageIcon(resources.getServerImage()));
        roomTree.setRoomIcon(new ImageIcon(resources.getChatImage()));
        roomTree.setLockedRoomIcon(new ImageIcon(resources.getLockedChatImage()));
        roomTree.setUserIcon(new ImageIcon(resources.getUserImage()));
        userList.setUserIcon(new ImageIcon(resources.getUserImage()));
        roomTree.initRooms(new ArrayList<>(RoomManager.getInstance().getDataValues()));
        
        JPanel treeWrapper              =   new JPanel(new BorderLayout());
        JPanel leftContentWrapper       =   new JPanel(new BorderLayout());
        
        Border leftPanelStatusBorder    =   BorderFactory.createEmptyBorder(5, 5, 5, 5); 
        roomTree.setBorder(leftPanelStatusBorder);
        userList.setBorder(leftPanelStatusBorder);
        treeWrapper.setBorder(BorderFactory.createTitledBorder("Rooms"));
        userPanel.setBorder(BorderFactory.createTitledBorder("Online users"));
        leftContentWrapper.setBorder(BorderFactory.createEmptyBorder(8, 3, 3, 3));
        leftContentWrapper.setPreferredSize(new Dimension(350, 0));
        
        Color statusBackground  =   new Color(237, 237, 237);
        roomTree.setBackground(statusBackground);
        userList.setBackground(statusBackground);
        
        userPanel.add(userScrollPane);
        treeWrapper.add(roomScrollPane);
        leftPanel.add(treeWrapper);
        leftPanel.add(userPanel);
        leftContentWrapper.add(leftPanel);
        
        rightPanel.add(statusPanel, BorderLayout.NORTH);
        rightPanel.add(loggingList, BorderLayout.CENTER);
        treeServerSplit.setLeftComponent(leftContentWrapper);
        treeServerSplit.setRightComponent(rightPanel);
        add(treeServerSplit);
        
        ServerMenuBar menuBar   =   ServerMenuBar.getInstance();
        menuBar.setListener(new MenuActionListener());
    }
    
    public LoggingList getLoggingList()
    {
        return loggingList;
    }

    public ServerStatusPanel getStatusPanel() 
    {
        return statusPanel;
    }

    public RoomTree getRoomTree() 
    {
        return roomTree;
    }

    public UserList getUserList() 
    {
        return userList;
    }
    
    private class MenuActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src              =   e.getSource();
            ServerMenuBar menu      =   ServerMenuBar.getInstance();
            
            if(src == menu.getItem("msgSvStopItem") || src == menu.getItem("msgSvStartItem"))
                MessageServer.getInstance().setServingSync(src == menu.getItem("msgSvStartItem"));
            
            else if(src == menu.getItem("msgSendSvStopItem") || src == menu.getItem("msgSendSvStartItem"))
                ServerMessageSender.getInstance().setSending(src == menu.getItem("msgSendSvStartItem"));
            
            else if(src == menu.getItem("updateSvStartItem") || src == menu.getItem("updateSvStopItem"))
                UpdateBroadcastServer.getInstance().setServingSync(src == menu.getItem("updateSvStartItem"));
            
            else if(src == menu.getItem("authorItem"))
                JOptionPane.showMessageDialog(null, ServerConfig.AUTHOR_MESSAGE, "Software Author", JOptionPane.INFORMATION_MESSAGE);
            
            else if(src == menu.getItem("logClearItem"))
                ((DefaultListModel) loggingList.getList().getModel()).clear();
        }
    }
    
    private class NonSelectionModel extends DefaultListSelectionModel
    {
        @Override
        public void setSelectionInterval(int indexA, int indexB)
        {
            super.setSelectionInterval(-1, -1);
        }
    }
    
    public static ServerPanel getInstance()
    {
        if(instance == null) instance = new ServerPanel();
        return instance;
    }
}
