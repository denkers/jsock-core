//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.core;

public class ClientConfig 
{
    //---------------------------------------------------------
    //  IO CONSTS
    //---------------------------------------------------------
    public static int UPDATE_LIST_PORT      =   8891;
    public static int MSG_SERVER_PORT       =   8890;
    public static String MSG_SERVER_HOST    =   "localhost";
    public static int UPDATE_BUFFER_SIZE    =   8192;
    
    
    //---------------------------------------------------------
    //  GUI CONSTS
    //---------------------------------------------------------
    public static int WINDOW_HEIGHT                 =   600;
    public static int WINDOW_WIDTH                  =   900;
    public static String WINDOW_TITLE               =   "JSockChat";
    public static final String LOOKNFEEL_PACKAGE    =   "de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel";
    public static final String HOME_VIEW_CARD       =   "home_view";
    public static final String LOGIN_VIEW_CARD      =   "login_view";
    public static final String REGISTER_VIEW_CARD   =   "register_view";
    public static final String TRANSITION_VIEW_CARD =   "transition_view";
    public static final String CONNECT_VIEW_CARD    =   "connect_view";
    public static final String IMAGES_DIR           =   "data/resources/";
    public static final int TRANSITION_TIME         =   800;
}
