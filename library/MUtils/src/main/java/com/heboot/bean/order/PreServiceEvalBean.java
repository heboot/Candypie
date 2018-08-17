package com.heboot.bean.order;

import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.TagsChildBean;

import java.util.List;

public class PreServiceEvalBean extends BaseBeanEntity {

    private List<TagsChildBean> eval_items;

    public List<TagsChildBean> getEval_items() {
        return eval_items;
    }

    public void setEval_items(List<TagsChildBean> eval_items) {
        this.eval_items = eval_items;
    }
}
