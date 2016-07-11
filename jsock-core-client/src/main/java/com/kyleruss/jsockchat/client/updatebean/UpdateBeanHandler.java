//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.updatebean;

import com.kyleruss.jsockchat.commons.updatebean.UpdateBean;

/**
 * Wraps an update bean can be used to perform an appropriate action with it
 * @param <T> The bean type to be wrapping
 */
public abstract class UpdateBeanHandler<T extends UpdateBean>
{
    protected final T bean;
    
    public UpdateBeanHandler(T bean)
    {
        this.bean   =   bean;
    }
    
    public T getBean()
    {
        return bean;
    }
    
    public abstract void beanAction();
}
