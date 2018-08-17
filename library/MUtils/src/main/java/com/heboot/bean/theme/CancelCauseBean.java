package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

public class CancelCauseBean extends BaseBeanEntity {

    private List<CancelCauseChildBean> content_list;

    public List<CancelCauseChildBean> getContent_list() {
        return content_list;
    }

    public void setContent_list(List<CancelCauseChildBean> content_list) {
        this.content_list = content_list;
    }
}
