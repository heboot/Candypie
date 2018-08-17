package com.heboot.bean.auth;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/10.
 */

public class UserAuthQBean extends BaseBeanEntity {

    private UserAuthQBeanModel config;

    public UserAuthQBeanModel getConfig() {
        return config;
    }

    public void setConfig(UserAuthQBeanModel config) {
        this.config = config;
    }
}
