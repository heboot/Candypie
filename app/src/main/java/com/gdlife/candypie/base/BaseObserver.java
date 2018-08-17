package com.gdlife.candypie.base;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.NetWorkUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/1/17.
 */

public class BaseObserver<T> implements Observer<BaseBean<T>> {

    private String TAG = this.getClass().getName();

    private HttpCallBack<T> httpCallBack;

    private QMUITipDialog qmuiTipDialog;

    private Disposable disposable;

    public BaseObserver(HttpCallBack<T> callBack) {
        this.httpCallBack = callBack;
    }

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

//    @Override
//    public void onNext(T t) {
//        if (BuildConfig.DEBUG) {
//            LogUtil.e(TAG, JSON.toJSONString(((BaseBean<BaseBeanEntity>) t)));
//        }
//        if (((BaseBean<BaseBeanEntity>) t).getError_code() != MCode.HTTP_CODE.SUCCESS || ((BaseBean<BaseBeanEntity>) t) == null) {
////            ToastUtils.showToast(baseBean.getMessage());
//            this.disposable.dispose();
//            httpCallBack.onError(new Throwable(((BaseBean<BaseBeanEntity>) t).getMessage()));
//            return;
//        }
//        UserService.getInstance().setSign_key(((BaseBean<BaseBeanEntity>) t).getSign_key());
//
//        try {
//            UserService.getInstance().setToken(((BaseBean<BaseBeanEntity>) t).getData().getToken());
//        } catch (Exception e) {
//        }
//
////        httpCallBack.onSuccess(baseBean.getData());
//
//        httpCallBack.onSuccess(t);
//    }


    @Override
    public void onNext(BaseBean<T> baseBean) {
        if (BuildConfig.DEBUG) {
            LogUtil.e(TAG, JSON.toJSONString(baseBean));
        }
        if (baseBean.getError_code() != MCode.HTTP_CODE.SUCCESS || baseBean == null) {
            if (baseBean != null && baseBean.getError_code() == MCode.ERROR_CODE.PLASE_LOGIN) {
//                IntentUtils.toLoginActivity(MAPP.mapp);
                EventBus.getDefault().post(UserEvent.OTHER_LOGIN);
            }
            CrashReport.postCatchedException(new Throwable(baseBean.getMessage()));
            this.disposable.dispose();
            try {
                httpCallBack.onError(baseBean);
            } catch (Exception ex) {

            }
            httpCallBack.onError(new Throwable(baseBean.getMessage()));
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
            LogUtil.e("BaseObserver", "BaseObserver出错");
        }

        httpCallBack.onSuccess(baseBean.getData());

    }

    @Override
    public void onError(Throwable e) {
        CrashReport.postCatchedException(e);
        httpCallBack.onError(e);
    }

    @Override
    public void onComplete() {
        this.disposable.dispose();
    }
}
