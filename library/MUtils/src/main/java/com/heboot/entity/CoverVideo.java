package com.heboot.entity;

/**
 * Created by heboot on 2018/3/10.
 */

public class CoverVideo {


    /**
     * id : 28
     * path : http://v.guodonglife.cn/sv/4b359f3-1613070daef/4b359f3-1613070daef.mp4
     * size : 10556690
     * time_size : 71
     * cover_img : http://127.0.0.1/candypie/public/uploads/images/20180208/944f485a03ad0d8d3801a650c3ca80d1.png
     * status : 1
     */

    private int id;
    private String path;
    private int size;
    private int time_size;
    private String cover_img;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTime_size() {
        return time_size;
    }

    public void setTime_size(int time_size) {
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
