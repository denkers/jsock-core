//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;


public class DefaultActionHandler implements ActionHandler
{
    @Override
    public void performAction(Message message) 
    {
        System.out.println(message);
    }
}
