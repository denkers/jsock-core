//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.message;

public class ResponseMessage extends AbstractMessage
{
    private final RequestMessage request;
    private Object responseData;
    private boolean status;
    
    public ResponseMessage(RequestMessage request)
    {
        this.request    =   request;
    }
    
    public RequestMessage getRequestMessage()
    {
        return request;
    }
    
    public Object getResponseData()
    {
        return responseData;
    }
    
    public void setResponseData(Object responseData)
    {
        this.responseData   =   responseData;
    }
    
    public boolean getStatus()
    {
        return status;
    }
    
    public void setStatus(boolean status)
    {
        this.status =   status;
    }
}
