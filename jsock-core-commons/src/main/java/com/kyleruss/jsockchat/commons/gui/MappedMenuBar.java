//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.gui;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MappedMenuBar extends JMenuBar
{
    protected Map<String, JMenuItem> items;
    
    public MappedMenuBar()
    {
        items   =   new HashMap<>();
    }
    
    public void addItem(String name, JMenuItem item, JMenu menu)
    {
        if(!items.containsKey(name))
        {
            items.put(name, item);
            menu.add(item);
        }
    }
    
    public JMenuItem getItem(String itemName)
    {
        return items.get(itemName);
    }
    
    public void setListener(ActionListener listener)
    {
        for(JMenuItem item : items.values())
            item.addActionListener(listener);
    }
}
