//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.gui;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class AppResources
{
    private static AppResources instance;
    private BufferedImage registerImage, loginImage, addPersonImage, lockImage;
    private BufferedImage connectImage, serverImage, userImage, chatImage, noticeImage;
    private BufferedImage lockedChatImage, onlineImage, offlineImage;
    private BufferedImage removeImage, leaveRoomImage, groupImage, colourImage, addImage;
    private BufferedImage removeFriendImage, pmImage, tickImage, sendImage;

    private AppResources()
    {
        initResources();
    }
    
    private void initResources()
    {
        try
        {
            registerImage       =   getImageResource("registerbutton.png");
            loginImage          =   getImageResource("loginbutton.png");
            addPersonImage      =   getImageResource("add_friend.png");
            lockImage           =   getImageResource("lock.png");
            connectImage        =   getImageResource("connectbutton.png");
            serverImage         =   getImageResource("internet_icon.png");
            userImage           =   getImageResource("user_icon.png");
            chatImage           =   getImageResource("bubbles-alt.png");
            noticeImage         =   getImageResource("critical.png");
            lockedChatImage     =   getImageResource("locked_chat.png");
            onlineImage         =   getImageResource("bullet_blue.png");
            offlineImage        =   getImageResource("bullet_red.png");
            removeImage         =   getImageResource("removeSmallIcon.png");
            leaveRoomImage      =   getImageResource("leave_room.png");
            groupImage          =   getImageResource("group_icon.png");
            colourImage         =   getImageResource("color_icon.png");
            addImage            =   getImageResource("addSmallIcon.png");
            removeFriendImage   =   getImageResource("remove_friend.png");
            pmImage             =   getImageResource("pm_image.png");
            tickImage           =   getImageResource("tick_icon.png");
            sendImage           =   getImageResource("send_button.png");
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to load resources");
        }
    }
    
    public BufferedImage getImageResource(String name) throws IOException 
    {
        return ImageIO.read(new File(ClientConfig.IMAGES_DIR + name));
    }
    
    public BufferedImage getRegisterImage() 
    {
        return registerImage;
    }

    public BufferedImage getLoginImage() 
    {
        return loginImage;
    }

    public BufferedImage getAddPersonImage() 
    {
        return addPersonImage;
    }

    public BufferedImage getLockImage() 
    {
        return lockImage;
    }

    public BufferedImage getConnectImage() 
    {
        return connectImage;
    }

    public BufferedImage getServerImage() 
    {
        return serverImage;
    }
    
    public BufferedImage getUserImage()
    {
        return userImage;
    }
    
    public BufferedImage getChatImage()
    {
        return chatImage;
    }
    
    public BufferedImage getNoticeImage()
    {
        return noticeImage;
    }
    
    public BufferedImage getLockedChatImage()
    {
        return lockedChatImage;
    }

    public BufferedImage getOnlineImage() 
    {
        return onlineImage;
    }

    public BufferedImage getOfflineImage() 
    {
        return offlineImage;
    }

    public BufferedImage getRemoveImage() 
    {
        return removeImage;
    }

    public BufferedImage getLeaveRoomImage()
    {
        return leaveRoomImage;
    }

    public BufferedImage getGroupImage() 
    {
        return groupImage;
    }

    public BufferedImage getColourImage()
    {
        return colourImage;
    }

    public BufferedImage getAddImage()
    {
        return addImage;
    }

    public BufferedImage getRemoveFriendImage()
    {
        return removeFriendImage;
    }

    public BufferedImage getPmImage() 
    {
        return pmImage;
    }
    
    public BufferedImage getTickImage()
    {
        return tickImage;
    }
    
    public BufferedImage getSendImage()
    {
        return sendImage;
    }
    
    public static AppResources getInstance()
    {
        if(instance == null) instance = new AppResources();
        return instance;
    }
}
