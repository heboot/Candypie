package com.heboot.bean.message;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

public class SystemMessageBean extends BaseBeanEntity {

    private List<SystemMessage> list;

    public List<SystemMessage> getList() {
        return list;
    }

    public void setList(List<SystemMessage> list) {
        this.list = list;
    }
}
