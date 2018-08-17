package com.heboot.bean.order;

import com.heboot.base.BaseBeanEntity;

public class ServiceEvalBean extends BaseBeanEntity {

    private String eval_reward_tip;

    private String eval_title;

    public String getEval_title() {
        return eval_title;
    }

    public void setEval_title(String eval_title) {
        this.eval_title = eval_title;
    }

    public String getEval_reward_tip() {
        return eval_reward_tip;
    }

    public void setEval_reward_tip(String eval_reward_tip) {
        this.eval_reward_tip = eval_reward_tip;
    }
}
