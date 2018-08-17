package com.gdlife.candypie.serivce;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MValue;
import com.heboot.bean.pay.VipLevelBean;

import java.util.List;

/**
 * Created by heboot on 2018/3/26.
 */

public class VipService {

    /**
     * 获取配置的所有的卡配置数据
     *
     * @return
     */
    public static List<VipLevelBean> getAllVipLevelBean() {
        List<VipLevelBean> levelBeans = MAPP.mapp.getConfigBean().getVip_level();
        return levelBeans;
    }

    /**
     * 根据当前的卡等级ID获取卡等级数据
     *
     * @param levelId
     * @return
     */
    public static VipLevelBean getCurrentVipLevel(Integer levelId) {
        if (levelId == null) {
            return null;
        }
        List<VipLevelBean> levelBeans = getAllVipLevelBean();
        for (VipLevelBean vipLevelBean : levelBeans) {
            if ((vipLevelBean.getId().equals(String.valueOf(levelId)))) {
                return vipLevelBean;
            }
        }
        return null;
    }

    public static VipLevelBean getGoldVipLevel() {
        List<VipLevelBean> levelBeans = getAllVipLevelBean();
        for (VipLevelBean vipLevelBean : levelBeans) {
            if ((vipLevelBean.getId().equals("2"))) {
                return vipLevelBean;
            }
        }
        return null;
    }


    public static VipLevelBean getNextVipLevel(Integer levelId) {
        String nextId = String.valueOf(levelId + 1);
        List<VipLevelBean> levelBeans = getAllVipLevelBean();
        for (VipLevelBean vipLevelBean : levelBeans) {
            if ((vipLevelBean.getId().equals(nextId))) {
                return vipLevelBean;
            }
        }
        return null;
    }

    public static String getNextVipLevelName(String levelId) {
        String nextId = String.valueOf(Integer.parseInt(levelId) + 1);
        List<VipLevelBean> levelBeans = getAllVipLevelBean();
        for (VipLevelBean vipLevelBean : levelBeans) {
            if ((vipLevelBean.getId().equals(nextId))) {
                return vipLevelBean.getTitle();
            }
        }
        return null;
    }


}
