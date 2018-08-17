package com.heboot.bean.login2register;


import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/2/6.
 */

public class SendSMSBean extends BaseBeanEntity {

    private int interval_time;

    public int getInterval_time() {
        return interval_time;
    }

    public void setInterval_time(int interval_time) {
        this.interval_time = interval_time;
    }
}
