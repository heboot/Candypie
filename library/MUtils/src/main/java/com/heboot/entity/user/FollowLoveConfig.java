package com.heboot.entity.user;

import java.io.Serializable;

public class FollowLoveConfig implements Serializable {

    private int status;

    private String price;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
