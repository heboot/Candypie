package com.gdlife.candypie.activitys.theme;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityServiceCancelBinding;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.theme.CancelServiceBean;
import com.heboot.event.OrderEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/16.
 */

public class ServiceCancelActivity extends BaseActivity<ActivityServiceCancelBinding> {

    private String user_service_id;

    private CancelServiceBean cancelServiceBean;

    @Inject
    OrderService orderService;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_cancel));
    }

    @Override
    public void initData() {
        DaggerServiceComponent.builder().build().inject(this);
        cancelServiceBean = (CancelServiceBean) getIntent().getExtras().get(MKey.CANCEL_SERVICE_BEAN);
        user_service_id = getIntent().getStringExtra(MKey.USER_SERVICE_ID);


        ThemeService.setCancelPageText(binding.tvCancel, String.valueOf(Integer.parseInt(cancelServiceBean.getCancel_val_time()) / 60));
    }

    @Override
    public void initListener() {

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(OrderEvent.CANCEL_ORDER_ENENT)) {
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

        binding.btnBottom.setOnClickListener((v) -> {
            finish();
        });
        binding.btnCancel.setOnClickListener((v) -> {
            orderService.cancelOrder(user_service_id);
        });

        binding.tvInfo.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, null, MAPP.mapp.getConfigBean().getStatic_url_config().getCancel_rule());
        });


    }


//    private void initCancelData() {
//        params.put(MKey.USER_SERVICE_ID, user_service_id);
//        String sign = SignUtils.doSign(params);
//        params.put(MKey.SIGN, sign);
//        HttpClient.Builder.getGuodongServer().cancel_tip(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<CancelServiceBean>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                addDisposable(disposable);
//            }
//
//            @Override
//            public void onSuccess(CancelServiceBean cancelServiceBean) {
//
//            }
//
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//
//        }));
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_cancel;
    }
}
