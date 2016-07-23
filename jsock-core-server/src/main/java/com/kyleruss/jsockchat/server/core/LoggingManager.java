//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.server.core;


public class LoggingManager 
{
    public static void log(String message)
    {
        System.out.println(message);
    }
    
    public static void logError(String message, Exception e)
    {
        System.out.println(message + "\n" + e.getMessage());
    }
}
