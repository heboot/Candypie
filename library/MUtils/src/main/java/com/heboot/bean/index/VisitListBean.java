package com.heboot.bean.index;

import com.heboot.entity.User;

import java.io.Serializable;
import java.util.List;

public class VisitListBean implements Serializable {

    private String title;

    private List<User> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
