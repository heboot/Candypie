package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/23.
 */

public class ApplyOrderBean extends BaseBeanEntity {

    private PostVideoChatBean chat_room_config;

    private String user_service_id;

    public PostVideoChatBean getChat_room_config() {
        return chat_room_config;
    }

    public void setChat_room_config(PostVideoChatBean chat_room_config) {
        this.chat_room_config = chat_room_config;
    }

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }
}
