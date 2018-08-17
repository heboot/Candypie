package com.heboot.bean.user;

import com.heboot.base.BaseBeanEntity;

public class SetPriceInitBean extends BaseBeanEntity {

    private SetPriceVideoChatConfigBean video_chat_config;

    private SetPriceVideoChatConfigBean video_tags_config;

    public SetPriceVideoChatConfigBean getVideo_chat_config() {
        return video_chat_config;
    }

    public void setVideo_chat_config(SetPriceVideoChatConfigBean video_chat_config) {
        this.video_chat_config = video_chat_config;
    }

    public SetPriceVideoChatConfigBean getVideo_tags_config() {
        return video_tags_config;
    }

    public void setVideo_tags_config(SetPriceVideoChatConfigBean video_tags_config) {
        this.video_tags_config = video_tags_config;
    }
}
