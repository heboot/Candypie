package com.gdlife.candypie.activitys.auth;

import android.support.v4.view.ViewPager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.ImageFragmentAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.databinding.ActivityAuthBootBinding;
import com.gdlife.candypie.fragments.ImageFragment;
import com.gdlife.candypie.serivce.AuthService;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/20.
 */

public class AuthBootActivity extends BaseActivity<ActivityAuthBootBinding> {


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        setSwipeBackEnable(false);
        binding.includeToolbar.setWhiteBack(true);
    }

    @Override
    public void initData() {

        List<Integer> localImages = new ArrayList<>();
        localImages.add(R.drawable.auth_boot1);
        localImages.add(R.drawable.auth_boot2);
//        localImages.add(R.drawable.auth_boot3);

        List<ImageFragment> fragments = new ArrayList<>();

        int index = 1;

        for (Integer path : localImages) {
            ImageFragment imageFragment = new ImageFragment(index, path);
            fragments.add(imageFragment);
            index = index + 1;
        }

        binding.vp.setAdapter(new ImageFragmentAdapter(getSupportFragmentManager(), fragments));
        setPoint(0);

    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });


        rxObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_AUTH_WELCOME_PAGE)) {
                    finish();
                }
                if (o.equals(NormalEvent.FINISH_AUTH_WELCOME_PAGE)) {
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
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.btnBottom.setOnClickListener((v) -> {
            AuthService.toAuthPageByAuthBoot(this);
        });
    }

    private void setPoint(int point) {
        switch (point) {
            case 0:
                binding.cb1.setChecked(true);
                binding.cb2.setChecked(false);
//                binding.cb3.setChecked(false);
                break;
            case 1:
                binding.cb1.setChecked(false);
                binding.cb2.setChecked(true);
//                binding.cb3.setChecked(false);
                break;
//            case 2:
//                binding.cb1.setChecked(false);
//                binding.cb2.setChecked(false);
//                binding.cb3.setChecked(true);
//                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_boot;
    }
}
