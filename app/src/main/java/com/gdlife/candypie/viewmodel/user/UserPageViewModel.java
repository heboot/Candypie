package com.gdlife.candypie.viewmodel.user;

import android.arch.lifecycle.ViewModel;

import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.user.UserInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserPageViewModel extends ViewModel {

    public Observable<BaseBean<UserInfoBean>> getUserProfile(String userId) {
        Map params = SignUtils.getNormalParams();
        params.put(MKey.UID, userId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        return HttpClient.Builder.getGuodongServer().servicer_profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

}
