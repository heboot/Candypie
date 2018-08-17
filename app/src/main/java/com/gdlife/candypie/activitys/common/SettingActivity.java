package com.gdlife.candypie.activitys.common;

import android.view.ViewGroup;

import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.databinding.ActivitySettingBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialog;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialogHehe;
import com.gdlife.candypie.widget.gift.GiftPlayView;
import com.heboot.bean.gift.GiftBean;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/9.
 */

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {
    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        if (BuildConfig.DEBUG) {
            binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_setting) + getString(R.string.test_server));
        } else {
            binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_setting));
        }


    }

    @Override
    public void initData() {

        binding.includeNotification.includeTitle.setTitle(getString(R.string.setting_notification));
//        binding.includeRecord.includeTitle.setTitle(getString(R.string.setting_record));
//        binding.includeCache.includeTitle.setTitle(getString(R.string.setting_cache));
        binding.includeAbout.includeTitle.setTitle(getString(R.string.setting_about));
        binding.includeBlack.includeTitle.setTitle(getString(R.string.black_user_list));
        binding.includeReport.includeTitle.setTitle(getString(R.string.left_menu_feeback_text));
//        binding.includeCache.setContent(CacheService.getAppCache());


    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
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

        binding.includeNotification.getRoot().setOnClickListener((v) -> {
            IntentUtils.toSettingNotificationActivity(this);
        });


        binding.btnBottom.setOnClickListener((v) -> {
            UserService.getInstance().logout(this);
        });
        binding.includeAbout.getRoot().setOnClickListener((v) -> {
//            IntentUtils.toAboutctivity(this);
            IntentUtils.toHTMLActivity(this, "", MAPP.mapp.getConfigBean().getStatic_url_config().getAbout());
        });

        binding.includeReport.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            IntentUtils.toReportActivity(binding.getRoot().getContext(), UserService.getInstance().getUser() == null ? null : String.valueOf(UserService.getInstance().getUser().getId()), ReportFromType.FEEBACK);
        });


        binding.includeBlack.getRoot().setOnClickListener((v) -> {
            IntentUtils.toUserBlackListActivity(this);
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }
}
