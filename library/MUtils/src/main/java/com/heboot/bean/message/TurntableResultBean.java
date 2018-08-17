package com.heboot.bean.message;

public class TurntableResultBean {

    private String user_turntable_id;

    private int index;

    private String type;

    private String tip_msg;

    public String getTip_msg() {
        return tip_msg;
    }

    public void setTip_msg(String tip_msg) {
        this.tip_msg = tip_msg;
    }

    public String getUser_turntable_id() {
        return user_turntable_id;
    }

    public void setUser_turntable_id(String user_turntable_id) {
        this.user_turntable_id = user_turntable_id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
