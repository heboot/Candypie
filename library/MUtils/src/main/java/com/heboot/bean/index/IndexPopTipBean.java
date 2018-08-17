package com.heboot.bean.index;

import java.io.Serializable;

public class IndexPopTipBean implements Serializable {

    private String id;
    private String title;
    private String img;
    private String action;
    private String url;
    private String type;
    private String tip;
    private String params_config;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    private String video_cover_img;

    public String getVideo_cover_img() {
        return video_cover_img;
    }

    public void setVideo_cover_img(String video_cover_img) {
        this.video_cover_img = video_cover_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams_config() {
        return params_config;
    }

    public void setParams_config(String params_config) {
        this.params_config = params_config;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
