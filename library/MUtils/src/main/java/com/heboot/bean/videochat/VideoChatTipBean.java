package com.heboot.bean.videochat;

import com.heboot.base.BaseBeanEntity;

public class VideoChatTipBean extends BaseBeanEntity {

    private int status;

    private String tip_message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTip_message() {
        return tip_message;
    }

    public void setTip_message(String tip_message) {
        this.tip_message = tip_message;
    }
}
