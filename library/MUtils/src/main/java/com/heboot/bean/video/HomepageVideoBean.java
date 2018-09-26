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

    //收费视频新加的字段
    private String price;
    private String unlock_nums;
    private String unlock;
    private int is_main_video;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnlock_nums() {
        return unlock_nums;
    }

    public void setUnlock_nums(String unlock_nums) {
        this.unlock_nums = unlock_nums;
    }

    public String getUnlock() {
        return unlock;
    }

    public void setUnlock(String unlock) {
        this.unlock = unlock;
    }

    public int getIs_main_video() {
        return is_main_video;
    }

    public void setIs_main_video(int is_main_video) {
        this.is_main_video = is_main_video;
    }

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
