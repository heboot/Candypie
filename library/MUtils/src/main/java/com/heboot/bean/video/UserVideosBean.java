package com.heboot.bean.video;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/3/21.
 */

public class UserVideosBean extends BaseBeanEntity {


    private List<HomepageVideoBean> list;

    public List<HomepageVideoBean> getList() {
        return list;
    }

    public void setList(List<HomepageVideoBean> list) {
        this.list = list;
    }

}
