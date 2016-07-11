//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public abstract class GUIManager 
{
    protected JPanel panel;
    protected JFrame frame;
    
    public GUIManager() {}
    
    protected void initFrame(String title)
    {
        frame   =   new JFrame(title);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    protected void initLookAndFeel(String path)
    {
        try
        {
            UIManager.setLookAndFeel(path);
        } 
        
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to initialize application look and feel ");
        }
    }
    
    protected void attachMenubar(JMenuBar menuBar)
    {
        frame.setJMenuBar(menuBar);
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
    
    public JPanel getPanel()
    {
        return panel;
    }
    
    public void display()
    {
        frame.setVisible(true);
    }   
}
