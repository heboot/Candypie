package com.gdlife.candypie.fragments.discover;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.discover.DiscoverVPAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.databinding.FragmentDiscoverVideoBinding;
import com.gdlife.candypie.databinding.LayoutDiscoverVideoBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserInfoService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.view.QiniuPlayerView;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.dialog.ServiceTipDialog;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialogHehe;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.index.IndexV5Bean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.ViewUtils;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT;

public class DiscoverVideoFragment4 extends BaseFragment<FragmentDiscoverVideoBinding> {

    private DiscoverVPAdapter discoverVideoAdapter;

    private User user;

    private List<User> users;

    private LayoutDiscoverVideoBinding layoutDiscoverVideoBinding;

    private MInfoLis firstFrametListener;

    private MOnPageChangeListener onPageChangeListener;

    private MComLis completedListener;

    private int totalPage;

    private TipDialog coinDialog;

    private BottomSheetDialog bottomSheetDialog;

    private ShareDialog shareDialog;

    private PermissionUtils permissionUtils;

    private QiniuPlayerView tempView;

    private int userClickPosition;

    private VideoChatService videoChatService;


    /**
     * 需要知道当前点击的位置
     *
     * @param position
     * @param users    列表数据
     */
    @SuppressLint("ValidFragment")
    public DiscoverVideoFragment4(int position, List<User> users, int sp, int totalPage) {
        this.userClickPosition = position;
        this.users = users;
        this.sp = sp;
        this.totalPage = totalPage;

    }


    public DiscoverVideoFragment4() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover_video;
    }

    @Override
    public void initUI() {
        binding.includeToolbar.setWhiteBack(true);
        binding.vvp.setOffscreenPageLimit(1);
        firstFrametListener = new MInfoLis(new WeakReference<>(this));
        onPageChangeListener = new MOnPageChangeListener(new WeakReference<>(this));
        completedListener = new MComLis(this);
        permissionUtils = new PermissionUtils();


    }

    @Override
    public void initData() {

        videoChatService = new VideoChatService();

        currentShowIndex = userClickPosition;

        discoverVideoAdapter = new DiscoverVPAdapter(getContext(), users);

        binding.vvp.setAdapter(discoverVideoAdapter);

        binding.vvp.setCurrentItem(currentShowIndex);

        user = users.get(currentShowIndex);

        binding.tvShareContent.setText(MAPP.mapp.getConfigBean().getShare_config().getVideo_share_config().getTip());

    }


    private void initDiscoverData(int sp) {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.TYPE, "r");
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().index_v8(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<IndexV5Bean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(IndexV5Bean indexBean) {
                totalPage = indexBean.getTotalPages();
                if (indexBean.getList() != null && indexBean.getList().size() > 0) {
                    discoverVideoAdapter.getUserList().addAll(indexBean.getList());
                    discoverVideoAdapter.notifyDataSetChanged();
                    RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateDatasEvent(indexBean.getList(), currentShowIndex, sp));
                }


            }


            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onError(BaseBean<IndexV5Bean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(getContext(), basebean.getMessage(), true);
                tipDialog.show();
            }

        }));
    }

    private MHandler progressUpdateTimer = new MHandler(new WeakReference<>(this));

    private static class MHandler extends Handler {

        private WeakReference<DiscoverVideoFragment4> weakReference;

        public MHandler(WeakReference<DiscoverVideoFragment4> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference != null && weakReference.get() != null) {
                weakReference.get().showVideoProgressInfo();
            }

        }
    }

    private boolean showTip = true;

    private void showVideoProgressInfo() {

//        if (layoutDiscoverVideoBinding != null) {
//
//            int curPosition = (int) layoutDiscoverVideoBinding.PLVideoView.getCurrentPosition();
//            int duration = (int) layoutDiscoverVideoBinding.PLVideoView.getDuration();
//            if (duration <= 0) {
//                return;
//            }
//            long pos = 1000L * curPosition / duration;
//            if (layoutDiscoverVideoBinding != null) {
//                layoutDiscoverVideoBinding.pb.setMax(1000);
//                layoutDiscoverVideoBinding.pb.setProgress((int) pos);
//            }
//
//        }

        if (tempView != null && tempView.getmPlayer() != null) {

            int curPosition = (int) tempView.getmPlayer().getCurrentPosition();
            int duration = (int) tempView.getmPlayer().getDuration();
            long pos = 1000L * curPosition / duration;
            Log.d("lfj0929", "curPosition = " + curPosition + " , duration = " + duration + " ， inSeek = ");

//                positionTxt.setText(AliPlayerFormatter.formatTime(curPosition));
//                durationTxt.setText(AliPlayerFormatter.formatTime(duration));
            if (layoutDiscoverVideoBinding != null) {
                layoutDiscoverVideoBinding.pb.setMax(1000);
//                layoutDiscoverVideoBinding.pb.setSecondaryProgress(duration);
                layoutDiscoverVideoBinding.pb.setProgress((int) pos);
            }

            if (pos > 900 && showTip) {
                showTip = false;
                if (videoChatService == null) {
                    videoChatService = new VideoChatService();
                }
                videoChatService.video_chat_tip(user);
            }

        }

        startUpdateTimer();
    }

    private void startUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
        progressUpdateTimer.sendEmptyMessageDelayed(0, 100);
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
                if (o.equals(DISCOVER_PAUSE_PLAY_EVENT)) {
                    if (layoutDiscoverVideoBinding != null) {
                        if (layoutDiscoverVideoBinding.ivPlay.getVisibility() == View.VISIBLE) {
                            doPlayAction();
                        } else {
                            doPauseAction();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        binding.vMore.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            bottomSheetDialog = DialogUtils.getHomepageBottomSheet(getContext(), new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        if (UserService.getInstance().checkTourist(getContext())) {
                            return;
                        }
                        bottomSheetDialog.dismiss();
                        params = SignUtils.getNormalParams();
                        params.put(MKey.BLACK_UID, user.getId());
                        String sign = SignUtils.doSign(params);
                        params.put(MKey.SIGN, sign);
                        if (user.getIs_black() == 1) {
                            HttpClient.Builder.getGuodongServer().un_black(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();
                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        } else {
                            HttpClient.Builder.getGuodongServer().black_user(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();

                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        }


                    } else if (integer == 0) {
                        bottomSheetDialog.dismiss();
                        IntentUtils.toReportActivity(getContext(), String.valueOf(user.getId()), ReportFromType.REPROT);
                    }
                }
            }, user.getIs_black() == 1);

            bottomSheetDialog.show();


        });

        binding.ivShare.setOnClickListener((v) -> {
            if (shareDialog == null) {
                shareDialog = new ShareDialog.Builder(getContext(), String.valueOf(user.getId()), user.getAvatar(), user.getNickname(), MAPP.mapp.getConfigBean().getShare_config().getProfile_share_config()).create();
            }
            shareDialog.show();
        });


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            getActivity().finish();
        });

        binding.vvp.setOnPageChangeListener(onPageChangeListener);

//        binding.includeBottom.setOnClickListener((v) -> {
//            if (UserService.getInstance().checkTourist(getContext())) {
//                return;
//            }
//            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
//                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
//                tipDialog.show();
//                return;
//            }
//            IntentUtils.toThemeListActivity(getContext(), true, user);
//        });

        binding.includeBottom.ivMsg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            IntentUtils.intent2ChatActivity(getContext(), MValue.CHAT_PRIEX + user.getId());
        });


        binding.includeBottom.ivSendGift.setOnClickListener(v -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            BottomVideoGiftSheetDialogHehe bottomVideoGiftSheetDialogHehe = new BottomVideoGiftSheetDialogHehe(String.valueOf(user.getId()), null);
            bottomVideoGiftSheetDialogHehe.show(getFragmentManager(), "");
        });

        binding.includeBottom.vVideoBg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            postVideoService();
        });

        binding.vvp.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {


                ViewGroup viewGroup = (ViewGroup) page;


                user = users.get(currentShowIndex);


                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == currentShowIndex && position == 0 && currentShowIndex != mRoomId) {// && currentShowIndex != mRoomId
                    if (tempView != null && tempView.getParent() != null && tempView.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (tempView.getParent())).removeView(tempView);
                    }
                    layoutDiscoverVideoBinding = DataBindingUtil.bind(page);
                    loadVideoAndChatRoom(user.getMain_video_list().get(0).getPath(), currentShowIndex, (ViewGroup) viewGroup.findViewById(R.id.clyt_child_container));
                    RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(users.get(currentShowIndex)));
//                    if (layoutDiscoverVideoBinding != null) {
//                        layoutDiscoverVideoBinding.PLVideoView.stopPlayback();
//                    }
//
//                    layoutDiscoverVideoBinding = DataBindingUtil.bind(page);
//                    layoutDiscoverVideoBinding.PLVideoView.setLooping(true);
//                    layoutDiscoverVideoBinding.PLVideoView.setOnInfoListener(firstFrametListener);
//                    layoutDiscoverVideoBinding.PLVideoView.setOnCompletionListener(completedListener);
//                    if (user.getMain_video_list() == null || user.getMain_video_list().size() <= 0) {
//                        return;
//                    }
//                    layoutDiscoverVideoBinding.PLVideoView.setVideoPath(user.getMain_video_list().get(0).getPath());
//                    layoutDiscoverVideoBinding.PLVideoView.start();
                } else if (position == 1 || position == -1) {//if(currentShowIndex != mRoomId)
                    if (((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.getVisibility() != View.VISIBLE) {
                        ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.setVisibility(View.VISIBLE);
                    }

                }

            }
        });

    }

    private int mRoomId = -1;


    private void loadVideoAndChatRoom(String url, int cur, ViewGroup v) {
        if (layoutDiscoverVideoBinding != null) {
            layoutDiscoverVideoBinding.pb.setMax(1000);
//                layoutDiscoverVideoBinding.pb.setSecondaryProgress(duration);
            layoutDiscoverVideoBinding.pb.setProgress(0);
        }
        LogUtil.e("播放器视图", "add" + currentShowIndex);
        if (tempView == null) {
            tempView = new QiniuPlayerView(getContext(), users.get(currentShowIndex).getMain_video_list().get(0).getPath(), firstFrametListener, completedListener);
        }
        v.addView(tempView);
        tempView.updatePlayerUrl(url, null);
        try {
            v.addView(tempView);
            tempView.updatePlayerUrl(url, null);
        } catch (Exception e) {

        }

        mRoomId = cur;
    }

    private void postVideoService() {
        videoChatService.postVideoService(permissionUtils, (BaseActivity) getActivity(), user, coinDialog);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            doPauseAction();
        }
    }

    private void doPauseAction() {
        if (layoutDiscoverVideoBinding != null) {
            layoutDiscoverVideoBinding.ivCover.setVisibility(View.VISIBLE);
            layoutDiscoverVideoBinding.ivPlay.setVisibility(View.VISIBLE);
//            layoutDiscoverVideoBinding.PLVideoView.pause();
            tempView.getmPlayer().pause();
        }
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().pause();
        }
    }

    private void doPlayAction() {
        if (layoutDiscoverVideoBinding != null) {
            layoutDiscoverVideoBinding.ivCover.setVisibility(View.GONE);
            layoutDiscoverVideoBinding.ivPlay.setVisibility(View.GONE);
//            layoutDiscoverVideoBinding.PLVideoView.start();
            tempView.getmPlayer().start();
        }
        if (tempView != null && tempView.getmPlayer() != null) {
//            tempView.getmPlayer().start();
        }
    }

    private int currentShowIndex = 0;


    private void onCompleted() {
        showVideoProgressInfo();
        stopUpdateTimer();
        if (layoutDiscoverVideoBinding != null) {
//            layoutDiscoverVideoBinding.PLVideoView.start();
            tempView.getmPlayer().start();
        }
    }

    private void stopUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
    }


    public static class MOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private WeakReference<DiscoverVideoFragment4> weakReference;

        public MOnPageChangeListener(WeakReference<DiscoverVideoFragment4> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            weakReference.get().currentShowIndex = position;


        }

        @Override
        public void onPageSelected(int position) {
            weakReference.get().showTip = true;
            if ((weakReference.get().sp * 10) - weakReference.get().currentShowIndex < 3) {
                if (weakReference.get().sp < weakReference.get().totalPage) {
                    weakReference.get().sp = weakReference.get().sp + 1;
                    weakReference.get().initDiscoverData(weakReference.get().sp);
                }
            }

//            RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(weakReference.get().users.get(weakReference.get().currentShowIndex)));
            RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdatePositionEvent(position));


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public static class MInfoLis implements PLOnInfoListener {

        private WeakReference<DiscoverVideoFragment4> weakReference;


        public MInfoLis(WeakReference<DiscoverVideoFragment4> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onInfo(int i, int i1) {

            if (i == MEDIA_INFO_VIDEO_RENDERING_START) {


                weakReference.get().showVideoProgressInfo();
                if (weakReference.get().layoutDiscoverVideoBinding != null) {
                    weakReference.get().layoutDiscoverVideoBinding.ivPlay.setVisibility(View.GONE);
                    weakReference.get().layoutDiscoverVideoBinding.ivCover.setVisibility(View.GONE);
                }


            }
        }
    }

    public static class MComLis implements PLOnCompletionListener {
        private WeakReference<DiscoverVideoFragment4> vodModeActivityWeakReference;

        public MComLis(DiscoverVideoFragment4 vodModeActivity) {
            vodModeActivityWeakReference = new WeakReference<DiscoverVideoFragment4>(vodModeActivity);
        }


        @Override
        public void onCompletion() {
            DiscoverVideoFragment4 vodModeActivity = vodModeActivityWeakReference.get();
            if (vodModeActivity != null) {
                vodModeActivity.onCompleted();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        doPauseAction();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        doPauseAction();
        layoutDiscoverVideoBinding = null;
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().stopPlayback();
        }
        super.onDestroy();
    }
}
