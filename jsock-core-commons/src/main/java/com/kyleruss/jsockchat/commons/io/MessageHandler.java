package com.kyleruss.jsockchat.commons.io;

import com.kyleruss.jsockchat.commons.message.ActionHandler;
import com.kyleruss.jsockchat.commons.message.MessageBean;

public interface MessageHandler 
{
    public ActionHandler getActionHandler(MessageBean bean);
}
