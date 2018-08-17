package com.heboot.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heboot on 2018/3/2.
 */

public class UserEvalBean implements Serializable {
    private List<TagsChildBean> tag_list;
    private int nums;
    private String title;

    public List<TagsChildBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<TagsChildBean> tag_list) {
        this.tag_list = tag_list;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
