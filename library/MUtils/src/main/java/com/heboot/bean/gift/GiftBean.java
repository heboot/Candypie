package com.heboot.bean.gift;

import java.io.Serializable;

public class GiftBean  implements Serializable{

    private String id;

    private String name;

    private String img;

    private String price;

    private String play_url;

    private String tip_msg;

    private String income_msg;

    public String getTip_msg() {
        return tip_msg;
    }

    public void setTip_msg(String tip_msg) {
        this.tip_msg = tip_msg;
    }

    public String getIncome_msg() {
        return income_msg;
    }

    public void setIncome_msg(String income_msg) {
        this.income_msg = income_msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }
}
