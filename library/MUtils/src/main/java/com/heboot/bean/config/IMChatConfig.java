package com.heboot.bean.config;


import java.io.Serializable;

public class IMChatConfig implements Serializable {

    private String gift_amount;
    private String turntable_amount;
    private String turntable_apply_time;

    public String getGift_amount() {
        return gift_amount;
    }

    public void setGift_amount(String gift_amount) {
        this.gift_amount = gift_amount;
    }

    public String getTurntable_amount() {
        return turntable_amount;
    }

    public void setTurntable_amount(String turntable_amount) {
        this.turntable_amount = turntable_amount;
    }

    public String getTurntable_apply_time() {
        return turntable_apply_time;
    }

    public void setTurntable_apply_time(String turntable_apply_time) {
        this.turntable_apply_time = turntable_apply_time;
    }
}
