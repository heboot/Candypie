package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/3/26.
 */

public class InComeBean extends BaseBeanEntity {

    private List<InComeListBean> list;

    public List<InComeListBean> getList() {
        return list;
    }

    public void setList(List<InComeListBean> list) {
        this.list = list;
    }
}
