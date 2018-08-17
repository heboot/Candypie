package com.heboot.bean.luckypan;

import com.heboot.base.BaseBeanEntity;

public class InvitePlayBean extends BaseBeanEntity {

    private int status;

    private String status_message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
}
