package com.heboot.bean.login2register;


import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.order.RunServiceTipBean;
import com.heboot.entity.IMUser;
import com.heboot.entity.User;
import com.heboot.entity.VideoFirstOrder;
import com.heboot.entity.VideoUser;

/**
 * Created by heboot on 2018/2/6.
 */

public class RegisterBean extends BaseBeanEntity {


    private IMUser im_user;

    //声网需要的东西
    private VideoUser video_user;

    private VideoFirstOrder video_first_order;


    public IMUser getIm_user() {
        return im_user;
    }

    public void setIm_user(IMUser im_user) {
        this.im_user = im_user;
    }

    public VideoUser getVideo_user() {
        return video_user;
    }

    public void setVideo_user(VideoUser video_user) {
        this.video_user = video_user;
    }

    public VideoFirstOrder getVideo_first_order() {
        return video_first_order;
    }

    public void setVideo_first_order(VideoFirstOrder video_first_order) {
        this.video_first_order = video_first_order;
    }
}
