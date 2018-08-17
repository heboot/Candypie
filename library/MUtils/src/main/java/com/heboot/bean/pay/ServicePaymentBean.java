package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/15.
 */

public class ServicePaymentBean extends BaseBeanEntity {

    private ServicePaymentBeanModel payment_params;

    public ServicePaymentBeanModel getPayment_params() {
        return payment_params;
    }

    public void setPayment_params(ServicePaymentBeanModel payment_params) {
        this.payment_params = payment_params;
    }
}
