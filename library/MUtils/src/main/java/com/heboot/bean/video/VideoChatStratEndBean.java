package com.heboot.bean.video;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/24.
 */

public class VideoChatStratEndBean extends BaseBeanEntity {

    private String end_time;
    private String service_time;
    private int is_im_chat;

    public int getIs_im_chat() {
        return is_im_chat;
    }

    public void setIs_im_chat(int is_im_chat) {
        this.is_im_chat = is_im_chat;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }
}
