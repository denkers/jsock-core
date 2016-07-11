//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.db;

import java.io.Serializable;


public abstract class DBModel<T extends Serializable> 
{
    protected String primaryKey;
    
    protected String tableName;
    
    public String getPrimaryKey()
    {
        return primaryKey;
    }
    
    public String getTableName()
    {
        return tableName;
    }
}
