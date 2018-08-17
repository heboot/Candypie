package com.heboot.bean.index;

import com.heboot.entity.User;

import java.util.List;

public class IndexRecommendConfigBean {

    private String title;
    private String content;

    private List<User> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
