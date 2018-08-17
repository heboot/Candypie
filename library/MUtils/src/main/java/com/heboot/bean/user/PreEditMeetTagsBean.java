package com.heboot.bean.user;

import com.heboot.base.BaseBeanEntity;

import java.io.Serializable;
import java.util.List;

public class PreEditMeetTagsBean extends BaseBeanEntity implements Serializable {

    private String title;

    private String tip;

    private List<TagsChildBean> select_items;

    private List<TagsChildBean> items;

    private int max_select;

    public int getMax_select() {
        return max_select;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setMax_select(int max_select) {
        this.max_select = max_select;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TagsChildBean> getSelect_items() {
        return select_items;
    }

    public void setSelect_items(List<TagsChildBean> select_items) {
        this.select_items = select_items;
    }

    public List<TagsChildBean> getItems() {
        return items;
    }

    public void setItems(List<TagsChildBean> items) {
        this.items = items;
    }
}
