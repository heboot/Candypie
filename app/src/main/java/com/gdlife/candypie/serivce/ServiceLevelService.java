package com.gdlife.candypie.serivce;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MValue;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.bean.pay.VipLevelBean;

import java.util.List;

/**
 * Created by heboot on 2018/3/26.
 */

public class ServiceLevelService {

    /**
     * 获取配置的所有的服务等级配置数据
     *
     * @return
     */
    public static List<ServiceLevelBean> getAllServceLevelBean() {
        List<ServiceLevelBean> levelBeans = MAPP.mapp.getConfigBean().getService_level();
        return levelBeans;
    }

    /**
     * 根据当前的服务等级ID获取服务等级数据
     *
     * @param levelId
     * @return
     */
    public static ServiceLevelBean getCurrentServiceLevel(String levelId) {
        List<ServiceLevelBean> levelBeans = getAllServceLevelBean();
        for (ServiceLevelBean vipLevelBean : levelBeans) {
            if ((vipLevelBean.getId().equals(levelId))) {
                return vipLevelBean;
            }
        }
        return null;
    }

}
