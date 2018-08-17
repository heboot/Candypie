package com.heboot.bean.pay;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/13.
 */

public class CouponsBeanModel implements Serializable {
    /**
     * id : 10001
     * title : 约会代金券
     * value : 20
     * money : 0
     * start_time : 1520243956
     * valid_time : 1520783999
     * status : 0
     * type : 1
     * desc : 只可在线下约会中使用
     */

    private int id;
    private String title;
    private String value;
    private int money;
    private int start_time;
    private int valid_time;
    private int status;
    private int type;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
