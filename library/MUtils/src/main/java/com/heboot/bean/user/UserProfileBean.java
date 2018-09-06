package com.heboot.bean.user;

import java.io.Serializable;
import java.util.List;

public class UserProfileBean implements Serializable {

    private String title;

    private List<TagsChildBean> list;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TagsChildBean> getList() {
        return list;
    }

    public void setList(List<TagsChildBean> list) {
        this.list = list;
    }
}
