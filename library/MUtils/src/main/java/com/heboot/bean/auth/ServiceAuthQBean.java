package com.heboot.bean.auth;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

/**
 * Created by heboot on 2018/3/21.
 */

public class ServiceAuthQBean extends BaseBeanEntity {

    private User user_profile;




    public User getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(User user_profile) {
        this.user_profile = user_profile;
    }
}
