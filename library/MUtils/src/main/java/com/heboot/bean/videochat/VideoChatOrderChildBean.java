package com.heboot.bean.videochat;

import com.heboot.entity.User;

public class VideoChatOrderChildBean {

    private String id;

    private VideoChatOrderChildStatusBean status;

    private User user;

    private long post_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoChatOrderChildStatusBean getStatus() {
        return status;
    }

    public void setStatus(VideoChatOrderChildStatusBean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getPost_time() {
        return post_time;
    }

    public void setPost_time(long post_time) {
        this.post_time = post_time;
    }
}
