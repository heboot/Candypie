package com.heboot.bean.user;

import java.io.Serializable;

public class UserGiftChildBean implements Serializable {

    private String id;

    private int nums;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
}
