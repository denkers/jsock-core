//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TransitionPanel extends JPanel
{
    private final JLabel spinnerLabel;
    
    public TransitionPanel()
    {
        super(new BorderLayout());
        setBackground(Color.WHITE);
        spinnerLabel    =   new JLabel(new ImageIcon(ClientConfig.IMAGES_DIR + "loadspinner.gif"));
        add(spinnerLabel);
    }
}
