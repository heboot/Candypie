package com.gdlife.candypie.http;

import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;

import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/1/17.
 */

public interface HttpCallBack<T> {

    void onSubscribe(Disposable disposable);

    void onSuccess(T t);

    void onError(Throwable throwable);

    void onError(BaseBean<T> baseBeanEntity);

//    void onError(String errorMsg);


}
