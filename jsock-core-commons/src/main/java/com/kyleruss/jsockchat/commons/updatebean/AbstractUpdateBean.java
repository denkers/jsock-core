//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.updatebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T> The bean map value type
 */
public class AbstractUpdateBean<T extends Serializable> implements UpdateBean<T>
{
    protected Map<String, T> data;
    
    public AbstractUpdateBean()
    {
        data    =   new HashMap<>();
    }
    
    @Override
    public List<T> getDataList()
    {
        return new ArrayList<>(data.values());
    }
    
    @Override
    public void setData(Map<String, T> data)
    {
        this.data   =   data;
    }

    @Override
    public Map<String, T> getData() 
    {
        return data;
    }
}
