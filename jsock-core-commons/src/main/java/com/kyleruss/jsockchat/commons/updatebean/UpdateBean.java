//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.commons.updatebean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A bean that uses a map data structure
 * @param <T> The bean's map value type
 */
public interface UpdateBean<T extends Serializable> extends Serializable
{
    public List<T> getDataList();
    
    public Map<String, T> getData();
    
    public void setData(Map<String, T> data);
}
