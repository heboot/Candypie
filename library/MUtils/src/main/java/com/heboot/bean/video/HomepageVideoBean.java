package com.heboot.bean.video;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/2.
 */

public class HomepageVideoBean implements Serializable {

    private String id;
    private String path;
    private String size;
    private String time_size;
    private String cover_img;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime_size() {
        return time_size;
    }

    public void setTime_size(String time_size) {
        this.time_size = time_size;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
