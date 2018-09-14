package com.heboot.bean.search;

import com.heboot.entity.User;

import java.util.List;

public class SearchHotRecmmendChildBean {

    private String title;

    private List<User> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }
}
