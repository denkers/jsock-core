//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.core;


public class ServerConfig 
{
    //---------------------------------------------------------
    //  SERVER CONSTS
    //---------------------------------------------------------
    public static final int MESSAGE_SERVER_PORT             =   8890;
    public static final int MESSAGE_SERVER_TIMEOUT          =   0;
    public static final int BROADCAST_PORT                  =   8891;
    public static final int BROADCAST_DELAY                 =   5000;
    public static final String FIXED_ROOMS_PATH             =   "data/rooms.xml";
    public static final int MESSAGE_LISTEN_SERVER_CODE      =   0;
    public static final int MESSAGE_SEND_SERVER_CODE        =   1;
    public static final int UPDATE_BROADCAST_SERVER_CODE    =   2;
    //---------------------------------------------------------
    
    
    //---------------------------------------------------------
    //  GUI CONSTS
    //---------------------------------------------------------
    public static final String WINDOW_TITLE         =   "JSockChat Server";
    public static final int WINDOW_WIDTH            =   1000;
    public static final int WINDOW_HEIGHT           =   600;
    public static final String LOOKNFEEL_PACKAGE    =   "de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel";
    public static final String IMAGES_DIR           =   "data/resources/";
    public static final String AUTHOR_MESSAGE       =   "Kyle Russell\nAUT University\nwww.github.com/denkers";
    public static final int MAX_LOG_CAPACITY        =   100;
    //---------------------------------------------------------
    
    
    //---------------------------------------------------------
    //  DATABASE CONSTS
    //---------------------------------------------------------
    public static final String JDBC_CLASS   =   "org.sqlite.JDBC";
    public static final String JDBC_DRIVER  =   "jdbc:sqlite:";
    public static final String DB_FILE      =   "data/db/jsockchat.db";
    public static final String CONN_URL     =   JDBC_DRIVER + DB_FILE;
    //---------------------------------------------------------
}
