package com.gdlife.candypie.serivce.user;

import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.UserVisitListBean;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FollowService {

    /**
     * 点击守护
     *
     * @param uid
     * @param observer
     */
    public void doFollowLove(String uid, HttpObserver<BaseBeanEntity> observer) {
        Map params = SignUtils.getNormalParams();
        params.put(MKey.TO_UID, uid);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().follow_love(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    public void followList(String uid, int sp, HttpObserver<UserVisitListBean> observer) {
        Map params = SignUtils.getNormalParams();
        params.put(MKey.UID, uid);
        params.put(MKey.PAGESIZE, 10);
        params.put(MKey.SP, sp);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().follow_love_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

}
