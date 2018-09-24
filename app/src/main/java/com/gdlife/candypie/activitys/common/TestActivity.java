package com.gdlife.candypie.activitys.common;

import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.databinding.ActivityTestBinding;
import com.gdlife.candypie.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

public class TestActivity extends BaseActivity<ActivityTestBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initUI() {
        setSwipeBackEnable(false);
        QMUIStatusBarHelper.translucent(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initData() {
        binding.localVideoViewContainer.setOnClickListener((v) -> {
            ToastUtils.showToast("hehehe");
        });
        binding.userAvatar.setOnClickListener((v) -> {
            ToastUtils.showToast("6666");
        });
    }

    @Override
    public void initListener() {

    }
}
