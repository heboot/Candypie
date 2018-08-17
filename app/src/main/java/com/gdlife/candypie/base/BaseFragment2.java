package com.gdlife.candypie.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.gdlife.candypie.utils.SignUtils;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by heboot on 2018/3/1.
 */

public abstract class BaseFragment2<T extends ViewDataBinding> extends com.trello.rxlifecycle2.components.support.RxFragment implements BaseUIInterface {

    protected String TAG = this.getClass().getName();

    protected T binding;

    private CompositeDisposable compositeDisposable;

    protected Observable<Object> rxObservable;

    protected Map<String, Object> params;

    protected int pageSize = 15, sp = 1, total;

    protected QMUITipDialog tipDialog;

    protected LayoutInflater inflater;


    protected FragmentActivity _mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        params = SignUtils.getNormalParams();
        this.inflater = inflater;
        initContentView(inflater, container);
        initUI();
        initData();
        rxBusObservers();
        initListener();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    private void rxBusObservers() {
        rxObservable = RxBus.getInstance().toObserverable();
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    protected void initContentView(LayoutInflater inflater, ViewGroup con) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
    }

    @LayoutRes
    protected abstract int getLayoutId();


    public void addDisposable(Disposable disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        this.compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        if (this.compositeDisposable != null && !compositeDisposable.isDisposed()) {
            this.compositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
