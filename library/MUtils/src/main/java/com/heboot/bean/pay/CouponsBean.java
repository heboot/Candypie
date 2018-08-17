package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/3/13.
 */

public class CouponsBean extends BaseBeanEntity {

    private List<CouponsBeanModel> list;

    public List<CouponsBeanModel> getList() {
        return list;
    }

    public void setList(List<CouponsBeanModel> list) {
        this.list = list;
    }
}
