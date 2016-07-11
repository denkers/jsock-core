//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.client.updatebean;

import com.kyleruss.jsockchat.client.core.UserManager;
import com.kyleruss.jsockchat.commons.updatebean.UsersUpdateBean;

public class UsersUpdateBeanHandler extends UpdateBeanHandler<UsersUpdateBean>
{
    public UsersUpdateBeanHandler(UsersUpdateBean bean) 
    {
        super(bean);
    }

    @Override
    public void beanAction() 
    {
        UserManager userManager =   UserManager.getInstance();
        userManager.setUsersBean(bean);
    }
}
