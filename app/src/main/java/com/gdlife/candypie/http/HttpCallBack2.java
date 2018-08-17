package com.gdlife.candypie.http;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/1/17.
 */

public interface HttpCallBack2<T> {

    void onSubscribe(Disposable disposable);

    void onSuccess(Observable<T> t);

    void onError(Throwable throwable);

    void onError(String errorMsg);


}
