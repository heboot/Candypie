package com.heboot.bean.search;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.util.List;

public class SearchUserBean extends BaseBeanEntity {

    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
