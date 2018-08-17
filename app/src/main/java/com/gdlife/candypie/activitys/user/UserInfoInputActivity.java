package com.gdlife.candypie.activitys.user;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityUserInfoInputAbstBinding;
import com.gdlife.candypie.serivce.UserInfoService;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

/**
 * Created by heboot on 2018/3/2.
 */

public class UserInfoInputActivity extends BaseActivity<ActivityUserInfoInputAbstBinding> {

    private String content;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.tvRight.setText(getString(R.string.finish));
        binding.includeToolbar.tvTitle.setText(getString(R.string.infop_signature));
    }

    @Override
    public void initData() {

        content = getIntent().getExtras().getString(MKey.CONTENT);

        binding.etContent.setText(content);

        binding.tvTip.setText(UserInfoService.getInputSignTip());

    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            RxBus.getInstance().post(new UserEvent.UserInfoInputEvent(binding.etContent.getText().toString()));
            finish();
        });

        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            RxBus.getInstance().post(new UserEvent.UserInfoInputEvent(binding.etContent.getText().toString()));
            finish();
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_input_abst;
    }
}
