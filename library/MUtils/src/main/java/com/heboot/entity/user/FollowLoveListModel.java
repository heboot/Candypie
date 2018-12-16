package com.heboot.entity.user;

import com.heboot.entity.User;

import java.io.Serializable;
import java.util.List;

public class FollowLoveListModel implements Serializable {

    private String title;

    private int nums;

    private List<User> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
