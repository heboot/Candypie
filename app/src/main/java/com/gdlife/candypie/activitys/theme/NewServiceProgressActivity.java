package com.gdlife.candypie.activitys.theme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityNewServiceProgressBinding;
import com.gdlife.candypie.databinding.LayoutUserAvatarNameBinding;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;
import com.heboot.event.OrderEvent;
import com.heboot.utils.ViewUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NewServiceProgressActivity extends BaseActivity<ActivityNewServiceProgressBinding> {


    private Animation rotateAni;

    private Animation scaleAni;


    private ValueAnimator widthAni, heightAni;

    @Inject
    OrderService orderService;

    private String userServiceId;

    private MHandler timeHandler = new MHandler(new WeakReference<>(this));

    /**
     * 头像集合
     */
    private List<LayoutUserAvatarNameBinding> avatarViews = new ArrayList<>();

    private List<User> users = new ArrayList<>();

    private List<User> users2 = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_service_progress;
    }

    @Override
    public void initUI() {

        setSwipeBackEnable(false);

        QMUIStatusBarHelper.translucent(this);
//
        rotateAni = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAni.setDuration(1000);
        rotateAni.setRepeatCount(3000);//动画的重复次数
        rotateAni.setFillAfter(true);//设置为true，动画转化结束后被应用

        LinearInterpolator lir = new LinearInterpolator();
        rotateAni.setInterpolator(lir);

        binding.ivLeida.startAnimation(rotateAni);//开始动画


        scaleAni = new ScaleAnimation(1.0f, 8.0f, 1.0f, 8.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAni.setDuration(2000);
        scaleAni.setRepeatCount(2000);
        scaleAni.setRepeatMode(Animation.RESTART);
        scaleAni.setDuration(2000);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatCount(2000);
        alphaAnimation.setRepeatMode(Animation.RESTART);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                binding.ivLeidaquan.setAlpha(0.7f);
            }
        });
        alphaAnimation.setDuration(2000);
//        binding.ivLeidaquan.startAnimation(alphaAnimation);


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(2000);
        animationSet.setRepeatCount(2000);
        animationSet.setRepeatMode(Animation.RESTART);
//        animationSet.addAnimation(scaleAni);
        animationSet.addAnimation(alphaAnimation);

        binding.ivLeidaquan.startAnimation(animationSet);

        widthAni = ValueAnimator.ofInt(50, 900);
        widthAni.setRepeatMode(ValueAnimator.RESTART);
        widthAni.setRepeatCount(200);
        widthAni.setDuration(2000);

        heightAni = ValueAnimator.ofInt(50, 900);
        heightAni.setDuration(2000);
        heightAni.setRepeatMode(ValueAnimator.RESTART);
        heightAni.setRepeatCount(200);

        widthAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewUtils.setViewWidth(binding.ivLeidaquan, (Integer) animation.getAnimatedValue());

            }
        });

        heightAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewUtils.setViewHeight(binding.ivLeidaquan, (Integer) animation.getAnimatedValue());

            }
        });

        widthAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }
        });

        widthAni.start();
        heightAni.start();


    }

    @Override
    public void initData() {
        DaggerServiceComponent.builder().build().inject(this);
        userServiceId = getIntent().getExtras().getString(MKey.USER_SERVICE_ID);
        timeHandler.sendEmptyMessage(1);

        avatarViews.add(binding.includeAvatar5);
        avatarViews.add(binding.includeAvatar6);
        avatarViews.add(binding.includeAvatar3);
        avatarViews.add(binding.includeAvatar4);
        avatarViews.add(binding.includeAvatar1);
        avatarViews.add(binding.includeAvatar2);

        users.addAll(MAPP.mapp.getConfigBean().getRecommend_user_list());
        users2.addAll(MAPP.mapp.getConfigBean().getRecommend_user_list());
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
                if (o.equals(OrderEvent.CANCEL_ORDER_ENENT)) {
                    if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                        IntentUtils.toIndexActivity(NewServiceProgressActivity.this);
                    } else {
                        IntentUtils.toIndexUsersActivity(NewServiceProgressActivity.this);
                    }
//                    IntentUtils.toIndexUsersActivity(NewServiceProgressActivity.this);
                }
//                else if (o.equals(MessageEvent.REFRESH_UNREAD_NUM_ENENT)) {
//
//                    int unread = NIMClient.getService(MsgService.class)
//                            .getTotalUnreadCount();
//
//                    if (unread > 0) {
//                        binding.tvUnread.setText(String.valueOf(unread));
//                        binding.tvUnread.setVisibility(View.VISIBLE);
//                        binding.ivUnread.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.tvUnread.setVisibility(View.INVISIBLE);
//                        binding.ivUnread.setVisibility(View.INVISIBLE);
//                    }
//
//
//                    initLeftUI(false);
//
//                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.vCancel.setOnClickListener((v) -> {
            orderService.cancelOrder(userServiceId);
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }


    public static class MHandler extends Handler {

        private WeakReference<NewServiceProgressActivity> weakReference;

        private long person = 1;

        public MHandler(WeakReference<NewServiceProgressActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {

            person = person + 1;

            if (person >= 71) {

            } else if (person == 20) {
//                weakReference.get().initAvatarAni();
                weakReference.get().binding.tvPerson.setText(weakReference.get().getString(R.string.yitongzhi) + person + weakReference.get().getString(R.string.weiyonghu));
                sendEmptyMessageDelayed(1, 100);
            }
//            else if (person == 50) {
//                weakReference.get().initAvatarAni2();
//                weakReference.get().binding.tvPerson.setText(weakReference.get().getString(R.string.yitongzhi) + person + weakReference.get().getString(R.string.weiyonghu));
//                sendEmptyMessageDelayed(1, 100);
//            }
            else {
                weakReference.get().binding.tvPerson.setText(weakReference.get().getString(R.string.yitongzhi) + person + weakReference.get().getString(R.string.weiyonghu));
                sendEmptyMessageDelayed(1, 100);
            }


        }
    }

}
