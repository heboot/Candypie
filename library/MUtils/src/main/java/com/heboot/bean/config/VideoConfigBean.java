package com.heboot.bean.config;

import java.io.Serializable;
import java.util.List;

public class VideoConfigBean implements Serializable {

    private List<String> price;

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }
}
