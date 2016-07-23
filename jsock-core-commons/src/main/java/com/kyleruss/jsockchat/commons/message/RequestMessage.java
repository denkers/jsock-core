//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.message;

public class RequestMessage<T extends MessageBean> extends AbstractMessage
{
    private final String source;
    private final T messageBean;
    
    public RequestMessage(String source, T messageBean)
    {
        this.source         =   source;
        this.messageBean    =   messageBean;   
    }
    
    public T getMessageBean()
    {
        return messageBean;
    }
    
    public String getUserSource()
    {
        return source;
    }
    
    public boolean hasSource()
    {
        return source != null;
    }
    
    public boolean isWitness(String username)
    {
        return source != null && !source.equals(username);
    }
}
