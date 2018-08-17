package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

public class UpdatePaymentStatusBean extends BaseBeanEntity {

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
