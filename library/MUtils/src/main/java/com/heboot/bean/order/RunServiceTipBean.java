package com.heboot.bean.order;

import com.heboot.bean.theme.PostVideoChatBean;

public class RunServiceTipBean {


    /**
     * message : 执行中的订单等待处理
     * user_service_id : 10811
     * action : push_user_list
     */

    private String message;
    private String user_service_id;
    private String action;

    private PostVideoChatBean chat_room_config;

    public PostVideoChatBean getChat_room_config() {
        return chat_room_config;
    }

    public void setChat_room_config(PostVideoChatBean chat_room_config) {
        this.chat_room_config = chat_room_config;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
