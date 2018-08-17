package com.heboot.bean.user;

import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.util.List;

/**
 * Created by heboot on 2018/3/5.
 */

public class RecommendServicerListBean extends BaseBeanEntity {

    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
