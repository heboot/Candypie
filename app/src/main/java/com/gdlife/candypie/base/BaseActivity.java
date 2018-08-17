package com.gdlife.candypie.base;

import android.app.Notification;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.PopupNotificationSnack;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by heboot on 2018/1/17.
 */

public abstract class BaseActivity<T extends ViewDataBinding> extends SwipeBackActivity implements BaseUIInterface, ISupportActivity {

    protected String TAG = this.getClass().getName();

    protected T binding;

    private CompositeDisposable compositeDisposable;

    protected Map<String, Object> params;

    public QMUITipDialog tipDialog;

    protected Observable<Object> rxObservable;

    public int pageSize = 15, sp = 1, total;

    String title = "";

    private Notification notification;

    private PopupNotificationSnack popupNotificationSnack;

    private TipCustomOneDialog tipCustomDialog;

    protected final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        params = SignUtils.getNormalParams();
        initContentView();
        initUI();
        initData();
        rxBusObservers();
        initListener();
        EventBus.getDefault().register(this);
    }

    protected void initContentView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
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
    protected void onDestroy() {

        mDelegate.onDestroy();
        DialogUtils.dimissDialog(tipDialog);
        DialogUtils.dimissDialog(tipCustomDialog);

        EventBus.getDefault().unregister(this);

        if (this.compositeDisposable != null && !compositeDisposable.isDisposed()) {
            this.compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void rxBusObservers() {
        rxObservable = RxBus.getInstance().toObserverable();
        rxObservable.subscribe(new Observer<Object>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
                this.disposable = d;
            }

            @Override
            public void onNext(Object o) {
//                if (o.equals(NormalEvent.FINISH_PAGE)) {
//                    finish();
//                }

                if (o.equals(UserEvent.LOGOUT)) {
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Subscribe
    public void onOtherLoginEvent(String o) {
        if (o.equals(UserEvent.OTHER_LOGIN)) {

            tipCustomDialog = new TipCustomOneDialog.Builder(this, "您的账号在其他设备登录", "重新登录", new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    UserService.getInstance().logout(BaseActivity.this);
//                    IntentUtils.toSplashActivity(BaseActivity.this);
                    finish();
                }
            }).create();

            tipCustomDialog.show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected MAPP getMAPP() {
        return (MAPP) getApplication();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }
}
