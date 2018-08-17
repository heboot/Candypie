package com.heboot.bean.user;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/2.
 */

public class UserSkillBean implements Serializable {
    private String id;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
