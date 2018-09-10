package com.heboot.bean.video;

import com.heboot.base.BaseBeanEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heboot on 2018/3/21.
 */

public class UserVideosBean extends BaseBeanEntity implements Serializable {

    private String title;

    private String nums;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    private List<HomepageVideoBean> list;

    public List<HomepageVideoBean> getList() {
        return list;
    }

    public void setList(List<HomepageVideoBean> list) {
        this.list = list;
    }

}
