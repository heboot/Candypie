package com.heboot.bean.user;

import java.io.Serializable;

public class UserAbstBean implements Serializable {

    private String title;

    private String abst;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }
}
