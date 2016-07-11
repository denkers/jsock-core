//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import com.kyleruss.jsockchat.client.core.RoomManager;
import com.kyleruss.jsockchat.client.core.UserManager;
import com.kyleruss.jsockchat.commons.gui.RoomTree;
import com.kyleruss.jsockchat.commons.gui.UserList;
import com.kyleruss.jsockchat.commons.message.RequestFriendMsgBean;
import com.kyleruss.jsockchat.commons.room.Room;
import com.kyleruss.jsockchat.commons.user.IUser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class ChatHomePanel extends JPanel
{
    private static ChatHomePanel instance;
    private RoomTree roomTree;
    private UserList friendList;
    private JList pendingFriendList;
    private JSplitPane homeSplit;
    private ChatContentPanel rightPanel;
    private UserContentPanel leftPanel;
    private Map<String, ChatPanel> roomTabs;
    private Map<String, ChatPanel> privateTabs;
    
    private ChatHomePanel()
    {
        setLayout(new BorderLayout());
        roomTabs    =   new LinkedHashMap<>();
        privateTabs =   new LinkedHashMap<>();
        homeSplit   =   new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanel   =   new UserContentPanel();
        rightPanel  =   new ChatContentPanel();
        
        JPanel listContentWrapper   =   new JPanel(new BorderLayout());
        listContentWrapper.setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
        listContentWrapper.setPreferredSize(new Dimension(200, 0));
        
        listContentWrapper.add(leftPanel);
        homeSplit.setLeftComponent(listContentWrapper);
        homeSplit.setRightComponent(rightPanel);
        
        add(homeSplit);
    }
    
    public ChatPanel getRoomChat(String roomName)
    {
        return roomTabs.get(roomName);
    }
    
    public ChatPanel getPrivateChat(String user)
    {
        return privateTabs.get(user);
    }
    
    public UserList getFriendList()
    {
        return friendList;
    }
    
    public JList getPendingFriendList()
    {
        return pendingFriendList;
    }
    
    public RoomTree getRoomTree()
    {
        return roomTree;
    }
    
    public void updateUserRoomLists(List<Room> rooms)
    {
        roomTree.initRooms(rooms);
        
        for(Room room : rooms)
        {
            String roomName =   room.getRoomName();
            ChatPanel chat  =   roomTabs.get(roomName);
            
            if(chat != null)
                chat.getRoomUsers().initUsers(room.getUserList());
        }
    }
    
    public void addChatTab(String name, boolean privateChat)
    {
        Map tabMap  =   privateChat? privateTabs : roomTabs;

        if(tabMap.containsKey(name)) return;
        
        List<IUser> roomUsers;
        if(!privateChat)
        {
            Room room   =   RoomManager.getInstance().getRoomsBean().getData().get(name);
            if(room != null)
                roomUsers   =   room.getUserList();
            else
            {
                roomUsers   =   new ArrayList<>();
                roomUsers.add(UserManager.getInstance().getActiveUser());
            }
        }
        
        else
        {
            roomUsers   =   new ArrayList<>();
            roomUsers.add(UserManager.getInstance().getActiveUser());
            roomUsers.add(UserManager.getInstance().getUsersBean().getData().get(name));
        }
        
        ChatPanel chatPanel     =   new ChatPanel(name, privateChat);
        tabMap.put(name, chatPanel);
        chatPanel.getRoomUsers().initUsers(roomUsers);
        
        String title    =   "[" + name + "] - " + (privateChat? "Private" : "Public");
        rightPanel.chatTabPane.addTab(title, chatPanel);
        
        int index   =   rightPanel.chatTabPane.getTabCount() - 1;
        rightPanel.chatTabPane.setTabComponentAt(index, new TabPanel(title, name, privateChat));
        notifyChatTab(name, privateChat);
    }
    
    public void notifyFriendReqTab()
    {
        int pendingTabIndex =   1;
        notifyTab(leftPanel.userTabbedPane, pendingTabIndex);
    }
    
    public void notifyChatTab(String name, boolean isPrivate)
    {
        ChatPanel chat  =   isPrivate? privateTabs.get(name) : roomTabs.get(name);
        if(chat != null)
        {
            int chatIndex   =   rightPanel.chatTabPane.indexOfComponent(chat);
            if(chatIndex >= 0)
                notifyTab(rightPanel.chatTabPane, chatIndex);
        }
    }
    
    public void notifyTab(JTabbedPane tabPane, int reqTabIndex)
    {
        int currentIndex    =   tabPane.getSelectedIndex();
        if(currentIndex == reqTabIndex || currentIndex == -1) return;
        
        if(tabPane == rightPanel.chatTabPane)
            ((TabPanel) tabPane.getTabComponentAt(reqTabIndex)).tabTitleLabel.setIcon(new ImageIcon(AppResources.getInstance().getNoticeImage()));
        else
            tabPane.setIconAt(reqTabIndex, new ImageIcon(AppResources.getInstance().getNoticeImage()));
    }
    
    public void removeChatTabClient(String name, boolean isPrivate)
    {
        Map tabMap  =   isPrivate? privateTabs : roomTabs;
        if(!tabMap.containsKey(name)) return;
        
        int index   =   new ArrayList<>(tabMap.keySet()).indexOf(name);
        rightPanel.chatTabPane.remove(index);
        tabMap.remove(name);
    }
    
    public void removeChatTab(String name, boolean isPrivate)
    {
        removeChatTabClient(name, isPrivate);
        
        if(!isPrivate)
            RoomManager.getInstance().sendLeaveRoomRequest(name);
    }
    
    public void removeAllChats()
    {
        Object[] privateRooms   =   privateTabs.keySet().toArray();
        for(Object privateRoom : privateRooms)
            removeChatTabClient(privateRoom.toString(), true);
        
        Object[] publicRooms    =   roomTabs.keySet().toArray();
        for(Object publicRoom : publicRooms)
            removeChatTabClient(publicRoom.toString(), false);
    }
    
    public void broadcastMessage(ChatMessage message, String roomName, boolean isPrivate)
    {
        Map<String, ChatPanel> tabMap   =   isPrivate? privateTabs : roomTabs;
        ChatPanel chat =   tabMap.get(roomName);
        if(chat == null) addChatTab(roomName, isPrivate);
        chat   =   tabMap.get(roomName);
        
        chat.addMessage(message);
        notifyChatTab(roomName, isPrivate);
    }
    
    public void setFriendList(List<IUser> friends)
    {
        UserManager.getInstance().sortFriends(friends);
        getFriendList().initUsers(friends);
    }
    
    public void setPendingFriendList(List<RequestFriendMsgBean> pendingFriends)
    {
        UserList.initUsers(pendingFriends, (DefaultListModel) getPendingFriendList().getModel());
    }
    
    public void toggleJoinRoomProcessing(boolean process)
    {
        leftPanel.roomJoinButton.setEnabled(!process);
    }
    
    public void toggelCreateRoomProcessing(boolean process)
    {
        leftPanel.roomCreateButton.setEnabled(!process);
    }
    
    public void toggleFriendRequestProcessing(boolean process)
    {
        leftPanel.addFriendButton.setEnabled(!process);
    }
    
    public void toggleFriendRemoveProcessing(boolean process)
    {
        leftPanel.removeFriendButton.setEnabled(!process);
    }
    
    public void toggleFriendResponseProcessing(boolean process)
    {
        leftPanel.acceptFriendButton.setEnabled(!process);
        leftPanel.declineFriendButton.setEnabled(!process);
    }
    
    public void addFriend()
    {
        String username =   JOptionPane.showInputDialog(null, "Enter the friend's username", "Add friend", JOptionPane.OK_CANCEL_OPTION);
        if(username != null)
        {
            if(username.equals("") || username.equals(UserManager.getInstance().getSource())) 
                JOptionPane.showMessageDialog(null, "Please enter valid username");
            
            else
            {
                toggleFriendRequestProcessing(true);
                UserManager.getInstance().sendFriendRequest(username);
            }
        }
    }
    
    public void removeFriend()
    {
        IUser removeFriend  =   (IUser) friendList.getSelectedValue();
        if(removeFriend == null)
            JOptionPane.showMessageDialog(null, "Please select a friend to remove");
        else
        {
            toggleFriendRemoveProcessing(true);
            UserManager.getInstance().sendRemoveFriendRequest(removeFriend.getUsername());
        }
    }
    
    public void messageFriend()
    {
        IUser friend    =   (IUser) friendList.getSelectedValue();
        if(friend == null) 
            JOptionPane.showMessageDialog(null, "Please select a friend to message");
        
        else if(!UserManager.getInstance().getUsersBean().getData().containsKey(friend.getUsername()))
            JOptionPane.showMessageDialog(null, "Friend is not online");
        
        else
        {
            if(privateTabs.containsKey(friend.getUsername()))
                rightPanel.chatTabPane.setSelectedComponent(privateTabs.get(friend.getUsername()));
            
            else addChatTab(friend.getUsername(), true);
        }
    }
    
    public void leaveRoom()
    {
        DefaultMutableTreeNode node  = (DefaultMutableTreeNode) roomTree.getSelectionPath().getLastPathComponent();
        
        if(node == null)
            JOptionPane.showMessageDialog(null, "Please select a room to leave");
        else
        {
            Room nodeRoom   =   (Room) node.getUserObject();
            if(!nodeRoom.getUserList().contains(UserManager.getInstance().getActiveUser()))
                JOptionPane.showMessageDialog(null, "You cannot leave a room you are not in");
            
            else removeChatTab(nodeRoom.getRoomName(), false);
        }
    }
    
    public void respondToFriend(boolean accept)
    {
        RequestFriendMsgBean req    =   (RequestFriendMsgBean) pendingFriendList.getSelectedValue();
        
        if(req == null) JOptionPane.showMessageDialog(null, "Please select a friend request to respond to");
        else
        {
            toggleFriendResponseProcessing(true);
            UserManager.getInstance().sendFriendResponse(req, accept);
        }
    }
    
    public void joinRoom()
    {
        TreePath path   =   roomTree.getSelectionPath();
        if(path == null) 
        {
            JOptionPane.showMessageDialog(null, "Please select a room to join");
            return;
        }
        
        DefaultMutableTreeNode node =   (DefaultMutableTreeNode) path.getLastPathComponent();
        if(node == null || node.isRoot()) JOptionPane.showMessageDialog(null, "Please select a room to join");
        else
        {
            Room room   =   (Room) node.getUserObject();
            String pass =   null;
            
            if(room.isPassProtected())
            {
                pass = JOptionPane.showInputDialog("Enter room password");
                if(pass == null) return;
            }
            
            toggleJoinRoomProcessing(true);
            RoomManager.getInstance().sendJoinRoomRequest(room.getRoomName(), pass);
        }
    }
    
    public void sendMessage()
    {
        ChatPanel currentChat   =   (ChatPanel) rightPanel.chatTabPane.getSelectedComponent();
        String message          =   currentChat.getEnteredText();
        String sender           =   UserManager.getInstance().getSource();
        String room             =   currentChat.getRoomName();
        boolean isPrivate       =   currentChat.isPrivate();
        
        ChatMessage chatMsg     =   new ChatMessage(sender, message, false);
        broadcastMessage(chatMsg, room, isPrivate);
        
        if(isPrivate)
            UserManager.getInstance().sendPrivateMessage(room, message);
        else
            UserManager.getInstance().sendBroadcastMessage(room, message);
    }
    
    public void createRoom()
    {
        JPanel createRoomPanel      =   new JPanel(new GridLayout(2, 1));
        JTextField roomField        =   new JTextField();
        JPasswordField passField    =   new JPasswordField();
        
        createRoomPanel.add(new JLabel("Name"));
        createRoomPanel.add(roomField);
        createRoomPanel.add(new JLabel("Password"));
        createRoomPanel.add(passField);
        
        int option  =   JOptionPane.showConfirmDialog(null, createRoomPanel, "Create room", JOptionPane.OK_CANCEL_OPTION);
        
        if(option == JOptionPane.OK_OPTION)
        {
            String name         =   roomField.getText();
            String password     =   new String(passField.getPassword());
            System.out.println(password);
            
            if(name.equals("")) JOptionPane.showMessageDialog(null, "Please enter a valid room name");
            else RoomManager.getInstance().sendCreateRoomRequest(name, password);
        }
    }
    
    private class TabPanel extends JPanel implements ActionListener
    {
        private JButton tabCloseBtn;
        private JLabel tabTitleLabel;
        private String name;
        private boolean isPrivate;
        
        public TabPanel(String title, String name, boolean isPrivate)
        {
            tabTitleLabel   =   new JLabel(title);
            tabCloseBtn     =   new JButton("x");
            this.name       =   name;
            this.isPrivate  =   isPrivate;
            
            
            setLayout(new FlowLayout(FlowLayout.LEFT));
            ClientPanel.removeBorder(tabCloseBtn);
            
            setIcon();
            
            add(tabTitleLabel);
            add(tabCloseBtn);
            
            tabCloseBtn.addActionListener(this);
        }
        
        private void setIcon()
        {
            if(isPrivate) tabTitleLabel.setIcon(new ImageIcon(AppResources.getInstance().getLockImage()));
            else tabTitleLabel.setIcon(new ImageIcon(AppResources.getInstance().getChatImage()));
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() == tabCloseBtn)
                removeChatTab(name, isPrivate);
        }
    }
    
    private class ChatContentPanel extends JPanel implements ChangeListener
    {
        private JTabbedPane chatTabPane;
        
        public ChatContentPanel()
        {
            setLayout(new BorderLayout());
            
            chatTabPane =   new JTabbedPane();
            chatTabPane.addChangeListener(this);
            
            add(chatTabPane);
        }

        @Override
        public void stateChanged(ChangeEvent e)
        {
            JTabbedPane source  =   (JTabbedPane) e.getSource();
            int index           =   source.getSelectedIndex();
            
            if(index != -1)
            {
                TabPanel tab        =   (TabPanel) source.getTabComponentAt(index);
                if(tab != null) tab.setIcon();
            }
        }
    }
    
    private class UserContentPanel extends JPanel implements ActionListener, ChangeListener
    {
        private final JScrollPane friendScroll, roomsScrollPane, pendingFriendScroll;
        private final JTabbedPane userTabbedPane;
        private final JPanel friendPanel, friendRequestPanel;
        
        private final JButton addFriendButton, removeFriendButton, msgFriendButton;
        private final JPanel friendControlPanel;
        
        private final JButton acceptFriendButton, declineFriendButton;
        private final JPanel friendReqControlPanel;
        
        private final JButton roomJoinButton, roomCreateButton, roomLeaveButton;
        private final JPanel roomControlPanel, roomPanel;
        private final ImageIcon processIcon;
        
        public UserContentPanel()
        {
            setPreferredSize(new Dimension(300, 0));
            setLayout(new GridLayout(2, 1));
            
            processIcon         =   new ImageIcon(ClientConfig.IMAGES_DIR + "spinner_small.gif");
            roomTree            =   new RoomTree("");
            friendList          =   new UserList();
            pendingFriendList   =   new UserList();
            userTabbedPane      =   new JTabbedPane();
            friendScroll        =   new JScrollPane(friendList);
            pendingFriendScroll =   new JScrollPane(pendingFriendList);
            roomsScrollPane     =   new JScrollPane(roomTree);
            
            roomTree.setBackground(Color.WHITE);
            friendList.setCellRenderer(new OnlineFriendCellRenderer());
            pendingFriendList.setCellRenderer(new FriendRequestCellRenderer());
            
            AppResources resources  =   AppResources.getInstance();
            roomTree.setServerIcon(new ImageIcon(resources.getServerImage()));
            roomTree.setRoomIcon(new ImageIcon(resources.getChatImage()));
            roomTree.setLockedRoomIcon(new ImageIcon(resources.getLockedChatImage()));
            roomTree.setUserIcon(new ImageIcon(resources.getUserImage()));
            friendList.setUserIcon(new ImageIcon(resources.getUserImage()));
            
            
            final int HOR_POLICY        =     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
            final int VERT_POLICY       =     ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
            
            friendScroll.setHorizontalScrollBarPolicy(HOR_POLICY);
            roomsScrollPane.setHorizontalScrollBarPolicy(HOR_POLICY);
            pendingFriendScroll.setHorizontalScrollBarPolicy(HOR_POLICY);
            friendScroll.setVerticalScrollBarPolicy(VERT_POLICY);
            roomsScrollPane.setVerticalScrollBarPolicy(VERT_POLICY);
            pendingFriendScroll.setVerticalScrollBarPolicy(VERT_POLICY);
            
            friendPanel         =   new JPanel(new BorderLayout());
            addFriendButton     =   new JButton("Add");
            removeFriendButton  =   new JButton("Remove");
            msgFriendButton     =   new JButton("Message");
            friendControlPanel  =   new JPanel(new GridLayout(1, 3));
            
            addFriendButton.setIcon(new ImageIcon(resources.getAddPersonImage()));
            removeFriendButton.setIcon(new ImageIcon(resources.getRemoveFriendImage()));
            msgFriendButton.setIcon(new ImageIcon(resources.getPmImage()));
                
            
            friendControlPanel.add(addFriendButton);
            friendControlPanel.add(removeFriendButton);
            friendControlPanel.add(msgFriendButton);
            friendPanel.add(friendControlPanel, BorderLayout.NORTH);
            friendPanel.add(friendScroll, BorderLayout.CENTER);
            
            friendRequestPanel      =   new JPanel(new BorderLayout());
            acceptFriendButton      =   new JButton("Accept");
            declineFriendButton     =   new JButton("Decline");
            friendReqControlPanel   =   new JPanel(new GridLayout(1, 2));
            
            acceptFriendButton.setIcon(new ImageIcon(resources.getTickImage()));
            declineFriendButton.setIcon(new ImageIcon(resources.getRemoveImage()));
            
            friendReqControlPanel.add(acceptFriendButton);
            friendReqControlPanel.add(declineFriendButton);
            friendRequestPanel.add(friendReqControlPanel, BorderLayout.NORTH);
            friendRequestPanel.add(pendingFriendScroll, BorderLayout.CENTER);
            
            userTabbedPane.addTab("Friends", friendPanel);
            userTabbedPane.addTab("Requests", friendRequestPanel);
            userTabbedPane.addChangeListener(this);
            
            roomControlPanel    =   new JPanel(new GridLayout(1, 3));
            roomPanel           =   new JPanel(new BorderLayout());
            roomJoinButton      =   new JButton("Join");
            roomCreateButton    =   new JButton("Create");
            roomLeaveButton     =   new JButton("Leave");
            
            roomJoinButton.setIcon(new ImageIcon(resources.getGroupImage()));
            roomCreateButton.setIcon(new ImageIcon(resources.getAddImage()));
            roomLeaveButton.setIcon(new ImageIcon(resources.getLeaveRoomImage()));
            
            roomJoinButton.setDisabledIcon(processIcon);
            roomCreateButton.setDisabledIcon(processIcon);
            roomLeaveButton.setDisabledIcon(processIcon);
            
            roomControlPanel.add(roomJoinButton);
            roomControlPanel.add(roomLeaveButton);
            roomControlPanel.add(roomCreateButton);
            roomPanel.add(roomControlPanel, BorderLayout.NORTH);
            roomPanel.add(roomsScrollPane, BorderLayout.CENTER);
            
            JPanel friendsWrapper   =       new JPanel(new BorderLayout());
            friendsWrapper.add(userTabbedPane);
            friendsWrapper.setBorder(BorderFactory.createTitledBorder("Friends"));
            
            JPanel roomsWrapper     =   new JPanel(new BorderLayout());
            roomsWrapper.add(roomPanel);
            roomsWrapper.setBorder(BorderFactory.createTitledBorder("Rooms"));
            
            add(roomsWrapper);
            add(friendsWrapper);
            
            roomJoinButton.addActionListener(this);
            addFriendButton.addActionListener(this);
            removeFriendButton.addActionListener(this);
            msgFriendButton.addActionListener(this);
            acceptFriendButton.addActionListener(this);
            declineFriendButton.addActionListener(this);
            roomCreateButton.addActionListener(this);
            roomLeaveButton.addActionListener(this);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            
            if(src == roomJoinButton)
                joinRoom();
            
            else if(src == roomLeaveButton)
                leaveRoom();
            
            else if(src == roomCreateButton)
                createRoom();
            
            else if(src == addFriendButton)
                addFriend();
            
            else if(src == removeFriendButton)
                removeFriend();
            
            else if(src == acceptFriendButton)
                respondToFriend(true);
            
            else if(src == declineFriendButton)
                respondToFriend(false);
            
            else if(src == msgFriendButton)
                messageFriend();
            
        }

        @Override
        public void stateChanged(ChangeEvent e)
        {
            JTabbedPane source  =   (JTabbedPane) e.getSource();
            int selectedIndex   =   source.getSelectedIndex();
            source.setIconAt(selectedIndex, null);
        }
        
        private class FriendRequestCellRenderer extends DefaultListCellRenderer
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel component    =   (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                component.setIcon(new ImageIcon(AppResources.getInstance().getAddPersonImage()));
                return component;
            }
        }
        
        private class OnlineFriendCellRenderer extends DefaultListCellRenderer
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel component    =   (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                IUser user          =   (IUser) value;
                
                if(UserManager.getInstance().getUsersBean().getData().containsKey(user.getUsername()))
                    component.setIcon(new ImageIcon(AppResources.getInstance().getOnlineImage()));
                else
                    component.setIcon(new ImageIcon(AppResources.getInstance().getOfflineImage()));
                
                return component;
            }
        }
    }

    
    
    
    public static ChatHomePanel getInstance()
    {
        if(instance == null) instance = new ChatHomePanel();
        return instance;
    }
}
