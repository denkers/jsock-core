//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.commons.gui.UserList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class ChatPanel extends JPanel implements ActionListener
{
    private final String roomName;
    private UserList roomUsers;
    private JPanel chatControls;
    private JList chatArea;
    private JTextArea chatEnterArea;
    private JScrollPane chatEnterScroll;
    private DefaultListModel chatModel;
    private JScrollPane chatScroll;
    private JScrollPane userScroll;
    private JButton messageSubmitBtn;
    private JButton changeFGButton, changeBGButton;
    private boolean isPrivate;
    private Color fgColour;
    
    public ChatPanel(String roomName, boolean isPrivate)
    {
        this.roomName   =   roomName;
        this.isPrivate  =   isPrivate;
        setLayout(new BorderLayout());
        
        roomUsers           =   new UserList();
        userScroll          =   new JScrollPane(roomUsers);
        chatControls        =   new JPanel(new BorderLayout());
        chatModel           =   new DefaultListModel();
        chatArea            =   new JList(chatModel);
        chatScroll          =   new JScrollPane(chatArea);
        chatEnterArea       =   new JTextArea();
        chatEnterScroll     =   new JScrollPane(chatEnterArea);
        messageSubmitBtn    =   new JButton("SEND");
        changeFGButton      =   new JButton("FG");
        changeBGButton      =   new JButton("BG");
        fgColour            =   Color.BLACK;
        
        ImageIcon bgIcon    =   new ImageIcon(AppResources.getInstance().getColourImage());
        changeFGButton.setIcon(bgIcon);
        changeBGButton.setIcon(bgIcon);
        messageSubmitBtn.setIcon(new ImageIcon(AppResources.getInstance().getSendImage()));
        
        roomUsers.setUserIcon(new ImageIcon(AppResources.getInstance().getUserImage()));
        userScroll.setPreferredSize(new Dimension(150, 0));
        chatControls.setPreferredSize(new Dimension(0, 70));
        messageSubmitBtn.setPreferredSize(new Dimension(100, 0));

        chatArea.setFocusable(false);
        chatArea.setSelectionModel(new NonSelectionModel());
        chatArea.setCellRenderer(new MessageCellRenderer());
        
        chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatEnterScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatEnterScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        userScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        userScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JPanel colourPanel  =   new JPanel(new GridLayout(2, 1));
        colourPanel.add(changeBGButton);
        colourPanel.add(changeFGButton);
        
        JPanel eastControls =   new JPanel(new BorderLayout());
        eastControls.add(colourPanel, BorderLayout.EAST);
        eastControls.add(messageSubmitBtn, BorderLayout.CENTER);
        
        changeBGButton.addActionListener(this);
        changeFGButton.addActionListener(this);
        messageSubmitBtn.addActionListener(this);
        
        chatControls.add(eastControls, BorderLayout.EAST);
        chatControls.add(chatEnterScroll, BorderLayout.CENTER);
        
        JPanel controlsWrapper  =   new JPanel();
        controlsWrapper.add(chatControls);
        
        JPanel usersWrapper     =   new JPanel(new BorderLayout());
        usersWrapper.add(userScroll);
        
        JPanel chatAreaWrapper  =   new JPanel(new BorderLayout());
        chatAreaWrapper.add(chatScroll);
        
        add(usersWrapper, BorderLayout.EAST);
        add(chatControls, BorderLayout.SOUTH);
        add(chatAreaWrapper, BorderLayout.CENTER);
    }
    
    public String getEnteredText()
    {
        String text =   chatEnterArea.getText();
        chatEnterArea.setText("");
        
        return text;
    }
    
    public void changeBGColour()
    {
        Color c =   JColorChooser.showDialog(null, "Choose chat background colour", Color.WHITE);
        chatArea.setBackground(c);
    }
    
    public void changeFGColour()
    {
        fgColour     =   JColorChooser.showDialog(null, "Choose chat foreground colour", Color.BLACK);
    }
    
    public void addMessage(ChatMessage message)
    {
        chatModel.addElement(message);
        chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum());
    }
    
        public String getRoomName()
    {
        return roomName;
    }
    
    public boolean isPrivate()
    {
        return isPrivate;
    }

    public UserList getRoomUsers() 
    {
        return roomUsers;
    }

    public JList getChatArea() 
    {
        return chatArea;
    }
    
    @Override
    public int hashCode()
    {
        return roomName.hashCode();
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof ChatPanel)
        {
            ChatPanel otherChat =   (ChatPanel) other;
            return (isPrivate == otherChat.isPrivate()) &&
                    roomName.equals(otherChat.getRoomName());
        }
        
        else return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == changeFGButton)
            changeFGColour();
        
        else if(src == changeBGButton)
            changeBGColour();
        
        else if(src == messageSubmitBtn)
            ChatHomePanel.getInstance().sendMessage();
    }
    
    private class NonSelectionModel extends DefaultListSelectionModel
    {   
        @Override
        public void setSelectionInterval(int indexA, int indexB)
        {
            super.setSelectionInterval(-1, -1);
        }
    }
    
    private class MessageCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JLabel label        =   (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ChatMessage msg     =   (ChatMessage) value;
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            label.setForeground(fgColour);
            
            if(msg.isServerMessage())
            {
                label.setIcon(new ImageIcon(AppResources.getInstance().getServerImage()));
                label.setFont(new Font("Arial", Font.BOLD, 12));
            }
            
            else
                label.setIcon(new ImageIcon(AppResources.getInstance().getUserImage()));
            
            return label;
        }
    }
}
