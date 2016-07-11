//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.gui;

import com.kyleruss.jsockchat.commons.user.IUser;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class UserList extends JList
{
    private ImageIcon userIcon;
    private DefaultListModel model;
    
    public UserList()
    {
        model   =   new DefaultListModel();
        setModel(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new UserCellRenderer());
    }
    
    public void setUserIcon(ImageIcon userIcon)
    {
        this.userIcon   =   userIcon;
    }
    
    public void initUsers(List<IUser> users)
    {
        initUsers(users, model);
    }
    
    public static void initUsers(List users, DefaultListModel listModel)
    {
        listModel.clear();
        
        for(Object user : users)
            listModel.addElement(user);
    }
    
    protected class UserCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JLabel component =   (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            component.setIcon(userIcon);
            
            return component;
        }
        
    }
}
