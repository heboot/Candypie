package com.heboot.bean.videochat;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

public class VideoChatOrderBean extends BaseBeanEntity {

    private List<VideoChatOrderChildBean> list;

    public List<VideoChatOrderChildBean> getList() {
        return list;
    }

    public void setList(List<VideoChatOrderChildBean> list) {
        this.list = list;
    }
}
