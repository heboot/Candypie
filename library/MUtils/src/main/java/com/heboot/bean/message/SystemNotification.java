package com.heboot.bean.message;

public class SystemNotification {

    private String type;

    private SystemNotificationValueBean value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SystemNotificationValueBean getValue() {
        return value;
    }

    public void setValue(SystemNotificationValueBean value) {
        this.value = value;
    }
}
