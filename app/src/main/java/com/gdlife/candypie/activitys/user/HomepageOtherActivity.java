package com.gdlife.candypie.activitys.user;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityHomepageOtherBinding;
import com.gdlife.candypie.fragments.discover.DiscoverVideoHomepageFragment;
import com.gdlife.candypie.fragments.homepage.HomepageBottomFragment;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.view.verticalview.DragLayout;
import com.heboot.base.BaseBean;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS;

public class HomepageOtherActivity extends BaseActivity<ActivityHomepageOtherBinding> {

    private DiscoverVideoHomepageFragment discoverFragment;

    private HomepageBottomFragment homepageBottomFragment;

    private User user;

    private DragLayout.ShowNextPageNotifier nextIntf;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_homepage_other;
    }

    @Override
    public void initUI() {

        user = (User) getIntent().getExtras().get(MKey.USER);

        QMUIStatusBarHelper.translucent(this);

        initUserInfo();

        nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
//                ToastUtils.showToast("1");
                binding.dlContainer.setEnable(false);
                RxBus.getInstance().post(DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS);
            }
        };


    }

    private void initUserInfo() {
        params = SignUtils.getNormalParams();
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().servicer_profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserInfoBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(UserInfoBean sendSMSBeanBaseBean) {

                binding.plytContainer.toMain();

                user = sendSMSBeanBaseBean.getUser_profile();


                discoverFragment = new DiscoverVideoHomepageFragment(user);

                homepageBottomFragment = new HomepageBottomFragment(user, binding.dlContainer);


                getSupportFragmentManager().beginTransaction()
                        .add(R.id.first, discoverFragment)
                        .add(R.id.second, homepageBottomFragment)
                        .commit();


                RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(user));
                binding.dlContainer.setNextPageListener(nextIntf);

            }

            @Override
            public void onError(BaseBean<UserInfoBean> basebean) {

            }

            @Override
            public void onError(Throwable throwable) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(HomepageOtherActivity.this, throwable.getMessage(), true);
                if (!HomepageOtherActivity.this.isDestroyed()) {
                    tipDialog.show();
                }
            }

        }));
    }


    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
    }
}
