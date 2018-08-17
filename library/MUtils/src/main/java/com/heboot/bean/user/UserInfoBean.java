package com.heboot.bean.user;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.util.List;

/**
 * Created by heboot on 2018/3/2.
 */

public class UserInfoBean extends BaseBeanEntity {

    private User user_profile;


    public User getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(User user_profile) {
        this.user_profile = user_profile;
    }
}
