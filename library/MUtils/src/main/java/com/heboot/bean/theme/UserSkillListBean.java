package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/3/19.
 */

public class UserSkillListBean extends BaseBeanEntity {


    private List<String> skill_ids;

    public List<String> getSkill_ids() {
        return skill_ids;
    }

    public void setSkill_ids(List<String> skill_ids) {
        this.skill_ids = skill_ids;
    }
}
