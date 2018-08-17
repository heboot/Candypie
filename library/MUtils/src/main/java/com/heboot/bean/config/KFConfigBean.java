package com.heboot.bean.config;

import java.io.Serializable;

public class KFConfigBean implements Serializable {


    /**
     * zs_uid : 38
     * zs_name : 蜜糖派小助手
     * vip_kf_uid : 40
     * vip_kf_name : 客服经理
     * kf_uid : 39
     * kf_name : 蜜糖派贴心助理
     * kf_tel : 400-886-6952
     */

    private String zs_uid;
    private String zs_name;
    private String vip_kf_uid;
    private String vip_kf_name;
    private String kf_uid;
    private String kf_name;
    private String kf_tel;

    public String getZs_uid() {
        return zs_uid;
    }

    public void setZs_uid(String zs_uid) {
        this.zs_uid = zs_uid;
    }

    public String getZs_name() {
        return zs_name;
    }

    public void setZs_name(String zs_name) {
        this.zs_name = zs_name;
    }

    public String getVip_kf_uid() {
        return vip_kf_uid;
    }

    public void setVip_kf_uid(String vip_kf_uid) {
        this.vip_kf_uid = vip_kf_uid;
    }

    public String getVip_kf_name() {
        return vip_kf_name;
    }

    public void setVip_kf_name(String vip_kf_name) {
        this.vip_kf_name = vip_kf_name;
    }

    public String getKf_uid() {
        return kf_uid;
    }

    public void setKf_uid(String kf_uid) {
        this.kf_uid = kf_uid;
    }

    public String getKf_name() {
        return kf_name;
    }

    public void setKf_name(String kf_name) {
        this.kf_name = kf_name;
    }

    public String getKf_tel() {
        return kf_tel;
    }

    public void setKf_tel(String kf_tel) {
        this.kf_tel = kf_tel;
    }
}
