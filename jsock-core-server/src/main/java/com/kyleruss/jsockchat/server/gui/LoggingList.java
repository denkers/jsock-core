//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.gui;

import com.kyleruss.jsockchat.server.core.ServerConfig;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class LoggingList extends JScrollPane
{
    private static LoggingList instance;
    private final DefaultListModel model;
    private final JList list;
    
    private LoggingList()
    {
        model   =   new DefaultListModel();
        list    =   new JList(model);
        
        list.setFocusable(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new MessageCellRenderer());
        list.setBackground(Color.BLACK);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setBorder(null);
        getViewport().add(list);
    }
    
    public void logMessage(LogMessage message)
    {
        if(model.size() >= ServerConfig.MAX_LOG_CAPACITY)
            model.removeRange(0, 40);
        
        model.addElement(message);
        getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum());
    }
    
    public static void sendLogMessage(LogMessage message)
    {
        getInstance().logMessage(message);
    }
    
    public static LoggingList getInstance()
    {
        if(instance == null) instance = new LoggingList();
        return instance;
    }
    
    public JList getList()
    {
        return list;
    }
    
    private class MessageCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JLabel label        =   (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            LogMessage message  =   (LogMessage) value;  
            
            if(label != null && message != null)
            {
                label.setIcon(new ImageIcon(message.getMessageImage()));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 12));
            }
            
            return label;
        }
    }
}
