package com.heboot.bean.pay;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/26.
 */

public class ServiceLevelBean implements Serializable {
    /**
     * id : 1
     * title : 1级
     * rate : 80
     * add_order_nums : 0
     * order_nums : 0
     */

    private String id;
    private String title;
    private String rate;
    private String add_order_nums;
    private String img;
    private int order_nums;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAdd_order_nums() {
        return add_order_nums;
    }

    public void setAdd_order_nums(String add_order_nums) {
        this.add_order_nums = add_order_nums;
    }

    public int getOrder_nums() {
        return order_nums;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setOrder_nums(int order_nums) {
        this.order_nums = order_nums;
    }
}
