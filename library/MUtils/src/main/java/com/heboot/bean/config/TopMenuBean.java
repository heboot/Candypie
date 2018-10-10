package com.heboot.bean.config;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopMenuBean implements Serializable {

    private String title;
    private String name;
    private int is_default;

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
