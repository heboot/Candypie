package com.gdlife.candypie.activitys.user;

import android.view.KeyEvent;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ActivityUserVideoAutoPlayBinding;
import com.gdlife.candypie.fragments.homepage.HomepageVideoFragment;
import com.heboot.entity.User;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.List;

public class UserVideoAudioPlayActivity extends BaseActivity<ActivityUserVideoAutoPlayBinding> {

    private int position;

    private List<User> users;

    private int totalPage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_video_auto_play;
    }

    @Override
    public void initUI() {

        position = (int) getIntent().getExtras().get(MKey.INDEX);
//        users = (List<User>) getIntent().getExtras().get(MKey.USER_LIST);
        users = MValue.currentUserVideosList;
        totalPage = (int) getIntent().getExtras().get(MKey.TOTAL_PAGES);

        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);

        mDelegate.loadRootFragment(binding.flytContainer.getId(), new HomepageVideoFragment(position, users, 1, totalPage));

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        MValue.currentUserVideosList = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return true;
    }


}
