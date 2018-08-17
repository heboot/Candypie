package com.gdlife.candypie.activitys.theme;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.theme.ThemeListAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityThemeListBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.theme.UserSkillListBean;
import com.heboot.entity.User;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/1.
 */

public class ThemeListActivity extends BaseActivity<ActivityThemeListBinding> {

    private ThemeListAdapter themeListAdapter;

    private boolean one2one = false;

    private User user;

    private boolean isVideo;

    private VideoChatService videoChatService;

    private PermissionUtils permissionUtils;

    private TipDialog coinDialog;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.fast_yue));
        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void initData() {

        videoChatService = new VideoChatService();

        permissionUtils = new PermissionUtils();

        one2one = getIntent().getBooleanExtra(MKey.TYPE, false);

        isVideo = getIntent().getBooleanExtra(MKey.VIDEO_ID, false);

        //如果是一对一发单 要查询对方可以提供的的服务列表
        if (one2one) {
            user = (User) getIntent().getExtras().get(MKey.USER);
            binding.includeToolbar.toolbarAvatar.setVisibility(View.VISIBLE);
            ImageUtils.showAvatar(binding.includeToolbar.toolbarAvatar, user.getAvatar());
            binding.includeToolbar.tvTitle.setText(getString(R.string.service_user_agin));
            initUserSkillList();
        } else {
            binding.plytContainer.toMain();
            themeListAdapter = new ThemeListAdapter(ThemeService.getAllThemeListNoMoreNoVideo(), user);
            binding.rvList.setAdapter(themeListAdapter);
        }

    }

    private void initUserSkillList() {
        params = SignUtils.getNormalParams();
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().user_skill_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserSkillListBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(UserSkillListBean userSkillListBean) {
                binding.plytContainer.toMain();
                if (isVideo) {
                    int index = userSkillListBean.getSkill_ids().indexOf("1");
                    if (index > -1) {
                        List<String> result = userSkillListBean.getSkill_ids();
                        result.remove(index);
                        themeListAdapter = new ThemeListAdapter(ThemeService.getServiceBeanByIds(result), user);
                    } else {
                        themeListAdapter = new ThemeListAdapter(ThemeService.getServiceBeanByIds(userSkillListBean.getSkill_ids()), user);
                    }
                } else {
                    themeListAdapter = new ThemeListAdapter(ThemeService.getServiceBeanByIds(userSkillListBean.getSkill_ids()), user);
                }

                binding.rvList.setAdapter(themeListAdapter);
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<UserSkillListBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(ThemeListActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }


        }));
    }

    public void startChatVideoService() {
        videoChatService.postVideoService(permissionUtils, this, user, coinDialog);
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_theme_list;
    }
}
