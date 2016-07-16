
package com.kyleruss.jsockchat.commons.message;


public class DefaultActionHandler implements ActionHandler
{
    @Override
    public void performAction(Message message) 
    {
        System.out.println(message);
    }
}
