package com.heboot.bean.pay;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/26.
 */

public class VipLevelBean implements Serializable {


    /**
     * id : 1
     * title : 普通会员
     * amount : 0
     * icon : http://img.guodonglife.cn/public/static/admin/img/none.png
     * cancel_nums : 2
     * online_apply_nums : 5
     * meet_apply_nums : 10
     * favs : 10
     */

    private String id;
    private String title;
    private String amount;
    private String icon;
    private String bg_img;
    private String font_color;
    private String up_img;
    private String cancel_nums;
    private String online_apply_nums;
    private String meet_apply_nums;
    private String favs;
    private String detail_url;
    private String rights_url;

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCancel_nums() {
        return cancel_nums;
    }

    public void setCancel_nums(String cancel_nums) {
        this.cancel_nums = cancel_nums;
    }

    public String getOnline_apply_nums() {
        return online_apply_nums;
    }

    public void setOnline_apply_nums(String online_apply_nums) {
        this.online_apply_nums = online_apply_nums;
    }

    public String getMeet_apply_nums() {
        return meet_apply_nums;
    }

    public void setMeet_apply_nums(String meet_apply_nums) {
        this.meet_apply_nums = meet_apply_nums;
    }

    public String getFavs() {
        return favs;
    }

    public void setFavs(String favs) {
        this.favs = favs;
    }

    public String getBg_img() {
        return bg_img;
    }

    public void setBg_img(String bg_img) {
        this.bg_img = bg_img;
    }

    public String getUp_img() {
        return up_img;
    }

    public void setUp_img(String up_img) {
        this.up_img = up_img;
    }

    public String getFont_color() {
        return font_color;
    }

    public void setFont_color(String font_color) {
        this.font_color = font_color;
    }

    public String getRights_url() {
        return rights_url;
    }

    public void setRights_url(String rights_url) {
        this.rights_url = rights_url;
    }
}
