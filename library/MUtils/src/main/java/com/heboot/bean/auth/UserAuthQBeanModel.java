package com.heboot.bean.auth;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/10.
 */

public class UserAuthQBeanModel extends BaseBeanEntity {


    private String id_face;
    private String hand_id_face;

    public String getId_face() {
        return id_face;
    }

    public void setId_face(String id_face) {
        this.id_face = id_face;
    }

    public String getHand_id_face() {
        return hand_id_face;
    }

    public void setHand_id_face(String hand_id_face) {
        this.hand_id_face = hand_id_face;
    }
}
