package com.heboot.bean.theme;

import com.heboot.entity.User;

import java.io.Serializable;
import java.util.List;

public class PushUserTipBean implements Serializable {

    private String user_service_id;

    private List<User> push_users;

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }

    public List<User> getPush_users() {
        return push_users;
    }

    public void setPush_users(List<User> push_users) {
        this.push_users = push_users;
    }
}
