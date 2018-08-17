package com.gdlife.candypie.activitys.user;

import android.support.v7.widget.GridLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.auth.AuthSkillsAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.databinding.ActivitySkillListBinding;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.UserService;
import com.heboot.bean.config.ConfigBean;
import com.heboot.event.VideoEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/5.
 */

public class UserSkillListActivity extends BaseActivity<ActivitySkillListBinding> {

    private AuthSkillsAdapter homepageSkillAdapter;

    private List<ConfigBean.ServiceItemsConfigBean.ListBean> list;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_auth_skill));

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 3));

    }

    @Override
    public void initData() {

        list = AuthService.getUserAuthUserSkills(UserService.getInstance().getUser());

        if (list != null && list.size() > 0) {
            homepageSkillAdapter = new AuthSkillsAdapter(list);
            binding.rvList.setAdapter(homepageSkillAdapter);
        }


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
//              LogUtil.e("发送刷新用户视频集事件3", o.toString());
                if (o instanceof VideoEvent.VideoUploadSkillEvent) {
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


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_skill_list;
    }
}
