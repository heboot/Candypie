package com.heboot.bean.auth;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/10.
 */

public class UserAuthIDBean extends BaseBeanEntity {

    private int user_auth_status;

    private Integer service_auth_status;

    public Integer getService_auth_status() {
        return service_auth_status;
    }

    public void setService_auth_status(Integer service_auth_status) {
        this.service_auth_status = service_auth_status;
    }

    public int getUser_auth_status() {
        return user_auth_status;
    }

    public void setUser_auth_status(int user_auth_status) {
        this.user_auth_status = user_auth_status;
    }
}
