package com.heboot.bean.config;

import java.io.Serializable;

public class ServicerAuthConfigBean implements Serializable {

    private String video;

    private String cover_img;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }
}
