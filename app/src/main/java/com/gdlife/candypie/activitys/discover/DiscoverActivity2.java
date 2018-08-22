package com.gdlife.candypie.activitys.discover;

import android.media.AudioManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.discover.DiscoverFragmentAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentDiscoverBinding;
import com.gdlife.candypie.fragments.discover.DiscoverVideoFragment4;
import com.gdlife.candypie.fragments.homepage.HomepageBottomFragment;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DiscoverActivity2 extends BaseActivity<FragmentDiscoverBinding> {

    private DiscoverVideoFragment4 discoverVideoFragment;

    private HomepageBottomFragment homepageBottomFragment;

    private List<Fragment> fragmentList = new ArrayList<>();

    private DiscoverFragmentAdapter discoverFragmentAdapter;

    private int userClickPosition;

    private List<User> list;

    private User currentUser;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        setSwipeBackEnable(false);
    }

    @Override
    public void initData() {

        userClickPosition = (int) getIntent().getExtras().get(MKey.INDEX);
        list = (List<User>) getIntent().getExtras().get(MKey.USER_LIST);
        sp = (int) getIntent().getExtras().get(MKey.SP);
        total = (int) getIntent().getExtras().get(MKey.TOTAL_PAGES);

        discoverVideoFragment = new DiscoverVideoFragment4(userClickPosition, list, sp, total);
        homepageBottomFragment = new HomepageBottomFragment();
        fragmentList.add(discoverVideoFragment);
        fragmentList.add(homepageBottomFragment);
        discoverFragmentAdapter = new DiscoverFragmentAdapter(getSupportFragmentManager(), fragmentList);
        binding.vp.setAdapter(discoverFragmentAdapter);
//        binding.vp.setOffscreenPageLimit(0);
//        binding.vp.setCurrentItem(0);

        if (UserService.getInstance().getFirstDiscover()) {
            binding.ivWelcome.setVisibility(View.VISIBLE);
            binding.ivWelcome1.setVisibility(View.VISIBLE);
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
                if (o.equals(DiscoverEvent.DISCOVER_TO_VIDEOPAGE_EVENT)) {
                    binding.vp.setCurrentItem(0);
                } else if (o.equals(DiscoverEvent.DISCOVER_TO_USERINFO_EVENT)) {
                    binding.vp.setCurrentItem(1);
                } else if (o instanceof DiscoverEvent.DiscoverUpdateUserEvent) {
                    initLogData(String.valueOf(((DiscoverEvent.DiscoverUpdateUserEvent) o).getUser().getId()));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        binding.ivWelcome.setOnClickListener((v) -> {
            binding.ivWelcome.setVisibility(View.GONE);
            binding.ivWelcome2.setVisibility(View.VISIBLE);
        });

        binding.ivWelcome2.setOnClickListener((v) -> {
            binding.ivWelcome2.setVisibility(View.GONE);
            binding.ivWelcome1.setVisibility(View.GONE);
            UserService.getInstance().setFirstDiscover();
        });

        binding.ivWelcome1.setOnClickListener((v) -> {
            binding.ivWelcome.setVisibility(View.GONE);
            if (binding.ivWelcome2.getVisibility() == View.VISIBLE) {
                binding.ivWelcome1.setVisibility(View.GONE);
                binding.ivWelcome2.setVisibility(View.GONE);
            } else {
                binding.ivWelcome2.setVisibility(View.VISIBLE);
            }

        });

    }

    private void initLogData(String to_uid) {
        params.put(MKey.TO_UID, to_uid);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().visit_log(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {

            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {

            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        discoverVideoFragment.onPause();
    }

    @Override
    protected void onDestroy() {
        if (discoverVideoFragment != null) {
            discoverVideoFragment.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.vp.getCurrentItem() == 1) {
                binding.vp.setCurrentItem(0);
            } else {
                if (discoverVideoFragment != null) {
                    discoverVideoFragment.onDestroy();
                }
                finish();
            }
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
        return true;
    }
}
