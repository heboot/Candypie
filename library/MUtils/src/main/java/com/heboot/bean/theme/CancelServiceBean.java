package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/19.
 */

public class CancelServiceBean extends BaseBeanEntity implements Serializable {

    private String cancel_val_time;

    private String cancel_rate;

    public String getCancel_val_time() {
        return cancel_val_time;
    }

    public void setCancel_val_time(String cancel_val_time) {
        this.cancel_val_time = cancel_val_time;
    }

    public String getCancel_rate() {
        return cancel_rate;
    }

    public void setCancel_rate(String cancel_rate) {
        this.cancel_rate = cancel_rate;
    }
}
