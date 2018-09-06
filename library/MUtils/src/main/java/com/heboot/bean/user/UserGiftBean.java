package com.heboot.bean.user;

import java.io.Serializable;
import java.util.List;

public class UserGiftBean implements Serializable {

    private String title;

    private List<UserGiftChildBean> list;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserGiftChildBean> getList() {
        return list;
    }

    public void setList(List<UserGiftChildBean> list) {
        this.list = list;
    }
}
