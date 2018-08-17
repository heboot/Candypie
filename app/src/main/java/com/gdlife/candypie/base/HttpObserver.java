package com.gdlife.candypie.base;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.NetWorkUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/1/17.
 */

public abstract class HttpObserver<T> implements Observer<BaseBean<T>> {

    private String TAG = this.getClass().getName();

    private Disposable disposable;


    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
        if (!NetWorkUtils.isConnectedByState(MAPP.mapp)) {
            onComplete();
            this.disposable.dispose();
            return;
        }
//        httpCallBack.onSubscribe(d);
    }


    @Override
    public void onNext(BaseBean<T> baseBean) {
        LogUtil.e(TAG, JSON.toJSONString(baseBean));
        if (baseBean.getError_code() != MCode.HTTP_CODE.SUCCESS || baseBean == null) {
            if (baseBean != null && baseBean.getError_code() == MCode.ERROR_CODE.PLASE_LOGIN) {
//                IntentUtils.toLoginActivity(MAPP.mapp);
                EventBus.getDefault().post(UserEvent.OTHER_LOGIN);
            }
            CrashReport.postCatchedException(new Throwable(baseBean.getMessage()));
            this.disposable.dispose();
            try {
                onError(baseBean);
            } catch (Exception ex) {
                onError(ex);
            }
            return;
        }
        UserService.getInstance().setSign_key(baseBean.getSign_key());
        try {
            if (!StringUtils.isEmpty(((BaseBean<com.heboot.base.BaseBeanEntity>) baseBean).getData().getToken())) {
                UserService.getInstance().setToken(((BaseBean<com.heboot.base.BaseBeanEntity>) baseBean).getData().getToken());
            }
            if (((BaseBean<com.heboot.base.BaseBeanEntity>) baseBean).getData().getUser() != null) {
                if (UserService.getInstance().getUser() != null) {
                    if (((BaseBean<com.heboot.base.BaseBeanEntity>) baseBean).getData().getUser().getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                        UserService.getInstance().setUser(((BaseBean<com.heboot.base.BaseBeanEntity>) baseBean).getData().getUser());
                    }
                }
            }
        } catch (Exception e) {
//            onError(e);
        }
        onSuccess(baseBean);
    }

    public abstract void onSuccess(BaseBean<T> baseBean);

    public abstract void onError(BaseBean<T> baseBean);



    @Override
    public void onError(Throwable e) {
        CrashReport.postCatchedException(e);
    }

    @Override
    public void onComplete() {
        this.disposable.dispose();
    }
}
