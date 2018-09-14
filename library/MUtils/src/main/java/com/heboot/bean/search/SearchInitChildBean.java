package com.heboot.bean.search;

import com.heboot.bean.user.TagsChildBean;

import java.util.List;

public class SearchInitChildBean {

    private String title;

    private List<TagsChildBean> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TagsChildBean> getItems() {
        return items;
    }

    public void setItems(List<TagsChildBean> items) {
        this.items = items;
    }
}
