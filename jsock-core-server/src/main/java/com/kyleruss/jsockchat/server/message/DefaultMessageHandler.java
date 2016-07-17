package com.kyleruss.jsockchat.server.message;

import com.kyleruss.jsockchat.commons.io.MessageHandler;
import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.BroadcastMsgBean;
import com.kyleruss.jsockchat.commons.message.CreateRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.DisconnectMsgBean;
import com.kyleruss.jsockchat.commons.message.JoinRoomMsgBean;
import com.kyleruss.jsockchat.commons.message.MessageBean;
import com.kyleruss.jsockchat.commons.message.PrivateMsgBean;

 public class DefaultMessageHandler implements MessageHandler
{
    /**
    * Interprets a passed MessageBean and creates a handler based on the type of bean
    * @param bean The bean that's type is associated with a handler
    * @return A handler thats associated with the passed bean; null otherwise
    */
     @Override
     public ActionHandler getActionHandler(MessageBean bean)
     {
         ActionHandler handler    =   null;


         if(bean instanceof DisconnectMsgBean)
             handler     =   new DisconnectMessageHandler();

         else if(bean instanceof JoinRoomMsgBean)
             handler     =   new JoinRoomMessageHandler();

         else if(bean instanceof PrivateMsgBean)
             handler     =   new PrivateMessageHandler();

         else if(bean instanceof BroadcastMsgBean)
             handler     =   new BroadcastMessageHandler();

         else if(bean instanceof CreateRoomMsgBean)
             handler     =   new CreateRoomMessageHandler();


         return handler;
     }
}
