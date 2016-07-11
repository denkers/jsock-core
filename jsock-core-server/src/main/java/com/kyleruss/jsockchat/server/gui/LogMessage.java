//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.gui;

import java.awt.image.BufferedImage;

public class LogMessage
{
    private final BufferedImage messageImage;
    private final String message;
    
    public LogMessage(String message, BufferedImage messageImage)
    {
        this.message        =   message;
        this.messageImage   =   messageImage;
    }
    
    public BufferedImage getMessageImage()
    {
        return messageImage;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
