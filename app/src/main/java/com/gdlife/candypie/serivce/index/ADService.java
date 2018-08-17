package com.gdlife.candypie.serivce.index;

import android.content.Context;

import com.gdlife.candypie.common.IndexTopADType;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.index.IndexPopTipBean;

/**
 *
 */
public class ADService {

    public static void doADAction(Context context, IndexPopTipBean indexPopTipBean) {
        if (indexPopTipBean == null) {
            return;
        }
        if (!StringUtils.isEmpty(indexPopTipBean.getAction()) && indexPopTipBean.getAction().equals(IndexTopADType.h5.toString())) {
            String token = "";
            if (indexPopTipBean.getUrl().indexOf("?") > -1) {
                token = "&token=" + UserService.getInstance().getToken();
            } else {
                token = "?token=" + UserService.getInstance().getToken();
            }
            IntentUtils.toHTMLActivity(context, indexPopTipBean.getTitle(), indexPopTipBean.getUrl() + token);
        } else if (!StringUtils.isEmpty(indexPopTipBean.getAction()) && indexPopTipBean.getAction().equals(IndexTopADType.video.toString())) {
            IntentUtils.toPlayerActivity2(context, indexPopTipBean.getUrl(), VideoPreviewFrom.USER, null, indexPopTipBean.getVideo_cover_img(), true);
        }
    }

}
