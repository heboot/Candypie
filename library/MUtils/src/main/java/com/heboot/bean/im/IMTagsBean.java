package com.heboot.bean.im;

import java.io.Serializable;

public class IMTagsBean implements Serializable{

    private String title;

    private String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
