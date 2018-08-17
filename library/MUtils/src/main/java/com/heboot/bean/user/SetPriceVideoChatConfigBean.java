package com.heboot.bean.user;

import java.util.List;

public class SetPriceVideoChatConfigBean {

    private String title;
    private String price;
    private List<TagsChildBean> select_items;
    private String tip;
    private String icon;
    private List<TagsChildBean> items;

    public List<TagsChildBean> getSelect_items() {
        return select_items;
    }

    public void setSelect_items(List<TagsChildBean> select_items) {
        this.select_items = select_items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public List<TagsChildBean> getItems() {
        return items;
    }

    public void setItems(List<TagsChildBean> items) {
        this.items = items;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
