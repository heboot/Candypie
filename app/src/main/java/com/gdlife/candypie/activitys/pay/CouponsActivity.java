package com.gdlife.candypie.activitys.pay;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.pay.CouponFragmentAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityCouponsBinding;
import com.gdlife.candypie.fragments.pay.CouponFragment;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/13.
 */

public class CouponsActivity extends BaseActivity<ActivityCouponsBinding> {

    private CouponFragment couponFragment, outCouponFragment;


    private ArrayList<String> titles;

    private List<CouponFragment> fragments = new ArrayList<>();

    private boolean use;

    private PostThemeBean postThemeBean;

    private CouponsBeanModel selectedCouponsBeanModel;

    @Inject
    UIService uiService;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_coupons));
        binding.includeToolbar.tvRight.setText(getString(R.string.rule));
        DaggerServiceComponent.builder().build().inject(this);
        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.color_343445));
    }

    @Override
    public void initData() {
        String[] ttt = null;

        use = getIntent().getBooleanExtra(MKey.USED, false);

        postThemeBean = (PostThemeBean) getIntent().getExtras().get(MKey.POST_THEME_BEAN);

        selectedCouponsBeanModel = (CouponsBeanModel) getIntent().getExtras().get(MKey.COUPONS_ID);

        couponFragment = new CouponFragment(0, use ? 1 : 0, postThemeBean, selectedCouponsBeanModel);

        fragments.add(couponFragment);
        if (use) {
            binding.stTitle.setVisibility(View.GONE);
        } else {
            initTitles();
            outCouponFragment = new CouponFragment(-1, use ? 1 : 0, null, selectedCouponsBeanModel);
            fragments.add(outCouponFragment);
            ttt = titles.toArray(new String[titles.size()]);

        }


        binding.rvList.setAdapter(new CouponFragmentAdapter(getSupportFragmentManager(), fragments));
        if (!use) {
            binding.stTitle.setViewPager(binding.rvList, ttt);
        }


    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, null, MAPP.mapp.getConfigBean().getStatic_url_config().getCoupons_rule());
        });
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
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

    public void selectCoupon(CouponsBeanModel couponsBeanModel) {
        RxBus.getInstance().post(new PayEvent.SelectCouponEvent(couponsBeanModel));
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupons;
    }

    private void initTitles() {
        titles = new ArrayList<String>();
        titles.add(getString(R.string.coupon_title_unuse));
        if (!use) {
            titles.add(getString(R.string.coupon_title_outdate));
        }

    }
}
