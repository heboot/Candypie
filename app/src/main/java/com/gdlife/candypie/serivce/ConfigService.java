package com.gdlife.candypie.serivce;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MValue;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.entity.User;

import java.util.Calendar;

/**
 * Created by heboot on 2018/2/8.
 */

public class ConfigService {

    private static ConfigService configService;


    private ConfigService() {

    }

    public synchronized static ConfigService getInstance() {
        if (configService == null) {
            configService = new ConfigService();
            return configService;
        }
        return configService;
    }


    public ConfigBean.KpiConfigBean getCurrentKPILevelBean(String kpi) {
        for (ConfigBean.KpiConfigBean kpiConfigBean : MAPP.mapp.getConfigBean().getKpi_config()) {
            if (kpiConfigBean.getTitle().equals(kpi)) {
                return kpiConfigBean;
            }
        }
        return null;
    }


    public ServiceLevelBean getCurrentServiceLevelBean() {
        for (ServiceLevelBean serviceLevelBean : MAPP.mapp.getConfigBean().getService_level()) {
            if (serviceLevelBean.getId().equals(UserService.getInstance().getUser().getLevel())) {
                return serviceLevelBean;
            }
        }
        return null;
    }

    public ServiceLevelBean getCurrentServiceLevelBeanByUser(User user) {
        for (ServiceLevelBean serviceLevelBean : MAPP.mapp.getConfigBean().getService_level()) {
            if (serviceLevelBean.getId().equals(user.getLevel())) {
                return serviceLevelBean;
            }
        }
        return null;
    }


    public ServiceLevelBean getCurrentNextServiceLevelBean() {

        ServiceLevelBean current = getCurrentServiceLevelBean();


        int nextId = Integer.parseInt(current.getId()) + 1;

        for (ServiceLevelBean serviceLevelBean : MAPP.mapp.getConfigBean().getService_level()) {
            if (serviceLevelBean.getId().equals(nextId)) {
                return serviceLevelBean;
            }
        }

        return null;

    }

    /**
     * 是否是小助手
     *
     * @param account
     * @return
     */
    public boolean isZs(String account) {
        String ac = account.replace(MValue.CHAT_PRIEX, "").trim();
        if (ac.equals(MAPP.mapp.getConfigBean().getKf_config().getZs_uid())) {
            return true;
        }
        return false;
    }

    public boolean isKF(String account) {
        String ac = account.replace(MValue.CHAT_PRIEX, "").trim();
        if(MAPP.mapp.getConfigBean() == null || MAPP.mapp.getConfigBean().getKf_uids() == null){
            return false;
        }
        if(MAPP.mapp.getConfigBean().getKf_uids().indexOf(ac) > -1){
            return true;
        }
        return false;
    }

    /**
     * 是否是小助理
     *
     * @param account
     * @return
     */
    public boolean isZL(String account) {
        if (account.equals(MAPP.mapp.getConfigBean().getKf_config().getKf_uid())) {
            return true;
        }
        return false;
    }

    public User getZLUser() {
        User user = new User();
        user.setId(Integer.parseInt(MAPP.mapp.getConfigBean().getKf_config().getKf_uid()));
        user.setNickname(MAPP.mapp.getConfigBean().getKf_config().getKf_name());
        user.setPwd("99899");
        return user;
    }


    /**
     * 获取最小可选身高
     *
     * @return
     */
    public int getMinHeight() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getHeight().get(0));
    }

    /**
     * 获取最小可选身高
     *
     * @return
     */
    public int getMaxHeight() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getHeight().get(1));
    }

    /**
     * 获取最小可选体重
     *
     * @return
     */
    public int getMinWeight() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getWeight().get(0));
    }

    /**
     * 获取最小可选体重
     *
     * @return
     */
    public int getMaxWeight() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getWeight().get(1));
    }

    public int getServiceRemarkMaxLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getService_remark().get(1));
    }


    /**
     * 最小注册年龄
     *
     * @return
     */
    public int getMinAgeYear() {
        Calendar selectedDate = Calendar.getInstance();
        return selectedDate.get(Calendar.YEAR) - (Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getAge().get(0)) + 1);
    }

    /**
     * 最大注册年龄
     *
     * @return
     */
    public int getMaxAgeYear() {
        Calendar selectedDate = Calendar.getInstance();
        return selectedDate.get(Calendar.YEAR) - (Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getAge().get(1)) - 1);
    }


    public int getMinAg() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getAge().get(0));
    }

    /**
     * 最大注册年龄
     *
     * @return
     */
    public int getMaxAge() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getAge().get(1));
    }

    public int getNickNameMinLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getNickname().get(0));
    }

    public int getNickNameMaxLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getNickname().get(1));
    }


    public int getPasswordMinLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getPassword().get(0));
    }

    public int getPasswordMaxLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getPassword().get(1));
    }

    /**
     * 个性签名最小长度
     */
    public int getAbstMinLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getAbst().get(0));
    }

    /**
     * 个性签名最大长度
     */
    public int getAbstMaxLength() {
        return Integer.parseInt(MAPP.mapp.getConfigBean().getInput_limit_config().getAbst().get(1));
    }
}
