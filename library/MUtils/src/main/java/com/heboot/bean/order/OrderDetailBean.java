package com.heboot.bean.order;

import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.theme.OrderListBean;

/**
 * Created by heboot on 2018/3/16.
 */

public class OrderDetailBean extends BaseBeanEntity {

    private OrderListBean.ListBean info;

    public OrderListBean.ListBean getInfo() {
        return info;
    }

    public void setInfo(OrderListBean.ListBean info) {
        this.info = info;
    }
}
