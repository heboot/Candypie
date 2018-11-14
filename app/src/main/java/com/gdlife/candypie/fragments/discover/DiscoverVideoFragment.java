package com.gdlife.candypie.fragments.discover;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.discover.DiscoverVPAdapter;
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
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.index.IndexV5Bean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnInfoListener;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT;

public class DiscoverVideoFragment extends BaseFragment<FragmentDiscoverVideoBinding> {

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
    public DiscoverVideoFragment(int position, List<User> users, int sp, int totalPage) {
        this.userClickPosition = position;
        this.users = users;
        this.sp = sp;
        this.totalPage = totalPage;
    }


    public DiscoverVideoFragment() {
    }


//    private RelativeLayout mRoomContainer;
//    private FrameLayout mFragmentContainer;
//    private PlayFragment mRoomFragment = PlayFragment.newInstance();


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
//        initDiscoverData(1);

        videoChatService = new VideoChatService();

        currentShowIndex = userClickPosition;

        discoverVideoAdapter = new DiscoverVPAdapter(getContext(), users);

        binding.vvp.setAdapter(discoverVideoAdapter);

        binding.vvp.setCurrentItem(currentShowIndex);

        user = users.get(currentShowIndex);

    }


    private void initDiscoverData(int sp) {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().index_v5(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<IndexV5Bean>() {
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


    //    {
//        @Override
//        public void handleMessage(Message msg) {
//
//            showVideoProgressInfo();
//        }
//    };
//
    private static class MHandler extends Handler {

        private WeakReference<DiscoverVideoFragment> weakReference;

        public MHandler(WeakReference<DiscoverVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference != null && weakReference.get() != null) {
                weakReference.get().showVideoProgressInfo();
            }

        }
    }

    private void showVideoProgressInfo() {

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
//                if (o instanceof DiscoverEvent.DiscoverUpdateUserEvent) {
//                    user = ((DiscoverEvent.DiscoverUpdateUserEvent) o).getUser();
//                    binding.ivCover.setVisibility(View.VISIBLE);
//                    ImageUtils.showImage(binding.ivCover, user.getMain_video_list().get(0).getCover_img());
//                    if (mPlayer != null) {
//                        doPlay(user);
//                    }
//                }
                if (o.equals(DISCOVER_PAUSE_PLAY_EVENT)) {
                    if (layoutDiscoverVideoBinding != null) {
                        if (layoutDiscoverVideoBinding.ivPlay.getVisibility() == View.VISIBLE) {
                            layoutDiscoverVideoBinding.ivPlay.setVisibility(View.GONE);
                            if (tempView != null && tempView.getmPlayer() != null) {
                                tempView.getmPlayer().start();
                            }

                        } else {
                            layoutDiscoverVideoBinding.ivPlay.setVisibility(View.VISIBLE);
                            if (tempView != null && tempView.getmPlayer() != null) {
                                tempView.getmPlayer().pause();
                            }
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


        binding.vvp.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {


                ViewGroup viewGroup = (ViewGroup) page;


//
                user = users.get(currentShowIndex);
                RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(users.get(currentShowIndex)));


                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == currentShowIndex && position == 0 && currentShowIndex != mRoomId) {// && currentShowIndex != mRoomId
//                    if (((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.getVisibility() != View.GONE) {
//                        ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.setVisibility(View.GONE);
//                    }
                    if (tempView != null && tempView.getParent() != null && tempView.getParent() instanceof ViewGroup) {
                        LogUtil.e("播放器视图", "remove" + currentShowIndex);
                        ((ViewGroup) (tempView.getParent())).removeView(tempView);
                    }
                    layoutDiscoverVideoBinding = DataBindingUtil.bind(page);
                    loadVideoAndChatRoom(user.getMain_video_list().get(0).getPath(), currentShowIndex, (ViewGroup) viewGroup.findViewById(R.id.clyt_child_container));
                } else if (position == 1 || position == -1) {//if(currentShowIndex != mRoomId)
                    LogUtil.e("切换检测 two", currentShowIndex + ">>" + mRoomId + ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).tvName.getText());
                    if (((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.getVisibility() != View.VISIBLE) {
                        ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.setVisibility(View.VISIBLE);
                    }

                }

            }
        });

    }

    private int mRoomId = -1;

    private boolean mInit = false;

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

        if (UserService.getInstance().checkTourist(getContext())) {
            return;
        }

        if (!permissionUtils.hasCameraPermission(MAPP.mapp)) {
            if (tipDialog != null) {
                tipDialog.dismiss();
            }
            permissionUtils.getCameraPermission(getActivity());
            return;
        }


        int minCoin = ThemeService.getVideoMinReqCoin(ThemeService.getVideoServiceMinPrice());

        if (Integer.parseInt(UserService.getInstance().getUser().getCoin()) < minCoin) {

            if (UserService.getInstance().getVideo_first_order() != null && UserService.getInstance().getVideo_first_order().getIs_first_order() == 1) {
                ServiceTipDialog serviceTipDialog = new ServiceTipDialog.Builder(getContext(), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            IntentUtils.toAccountActivity(getContext());
                        }
                    }
                }, MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER, null, user.getAvatar(), UserService.getInstance().getVideo_first_order().getInit_recharge_amount()).create();
                serviceTipDialog.show();
                return;
            } else {
                // TODO: 2018/3/22 提示去充值钻石
                if (coinDialog == null) {
                    coinDialog = new TipDialog.Builder(getContext(), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if (integer == 1) {
                                IntentUtils.toAccountActivity(getContext());
                            }

                        }
                    }, getString(R.string.new_video_service_coin_tip_title)
                            + "\n"
                            + getString(R.string.new_video_service_coin_tip_content)
                            + minCoin
                            + getString(R.string.unit_coin) + ","
                            + getString(R.string.new_video_service_coin_tip_content2)

                    ).create();
                    coinDialog.show();
                    return;
                } else {
                    coinDialog.show();
                }
            }


        }


        tipDialog = DialogUtils.getLoadingDialog(getContext(), "", false);
        params = SignUtils.getNormalParams();
        params.put(MKey.SELECT_UID, user.getId());
        params.put(MKey.SERVICE_ID, "1");
        params.put(MKey.PRICE, ThemeService.getVideoServiceMinPrice());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().video_chat(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PostThemeBean>() {
            @Override
            public void onSuccess(BaseBean<PostThemeBean> baseBean) {
                tipDialog.dismiss();
                IntentUtils.toVideoChatActivity(getContext(), baseBean.getData().getUser_service_id(), baseBean.getData().getChat_room_config(), UserService.getInstance().isServicer() ? VideoChatFrom.SERVICER : VideoChatFrom.USER, true);
            }

            @Override
            public void onError(BaseBean<PostThemeBean> baseBean) {
                tipDialog.dismiss();
                tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                tipDialog.show();
            }
        });

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (layoutDiscoverVideoBinding != null) {
                layoutDiscoverVideoBinding.ivPlay.setVisibility(View.VISIBLE);
            }
            if (tempView != null && tempView.getmPlayer() != null) {
                tempView.getmPlayer().pause();
            }
        }
    }

    private int currentShowIndex = 0;

    /**
     * 处理播放
     */
    protected void doPlayAction(User u, Fragment root) {
        //第一次初始化为空
        if (layoutDiscoverVideoBinding == null) {
            layoutDiscoverVideoBinding = DataBindingUtil.bind(((Fragment) discoverVideoAdapter.instantiateItem(binding.vvp, currentShowIndex)).getView());
        } else {
            //移除之前播放页面的播放器
            layoutDiscoverVideoBinding.clytChildContainer.removeView(tempView);
            //更新持有的页面为最新显示的页面
            layoutDiscoverVideoBinding = DataBindingUtil.bind(((Fragment) discoverVideoAdapter.instantiateItem(binding.vvp, currentShowIndex)).getView());
        }

        user = u;
        RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(u));

        //之前播放页面的封面 显示出来
        layoutDiscoverVideoBinding.ivCover.setVisibility(View.VISIBLE);
        //在新的播放页面增加播放器
        layoutDiscoverVideoBinding.clytChildContainer.addView(tempView);
        //播放器更新操作
        tempView.updatePlayerUrl(user.getMain_video_list().get(0).getPath(), layoutDiscoverVideoBinding.clytChildContainer);
    }

    private void onCompleted() {
//        isCompleted = true;
        showVideoProgressInfo();
        stopUpdateTimer();
        tempView.getmPlayer().start();
    }

    private void stopUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
    }


    public static class MOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private WeakReference<DiscoverVideoFragment> weakReference;

        public MOnPageChangeListener(WeakReference<DiscoverVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            weakReference.get().currentShowIndex = position;


        }

        @Override
        public void onPageSelected(int position) {

            if ((weakReference.get().sp * 10) - weakReference.get().currentShowIndex < 3) {
                LogUtil.e("请求下一页 1/", weakReference.get().currentShowIndex + "请求下一页" + weakReference.get().sp);
                if (weakReference.get().sp < weakReference.get().totalPage) {
                    weakReference.get().sp = weakReference.get().sp + 1;
                    LogUtil.e("请求下一页 2/", "请求下一页" + weakReference.get().sp);
                    weakReference.get().initDiscoverData(weakReference.get().sp);
                }
            }


            RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(weakReference.get().users.get(weakReference.get().currentShowIndex)));
            RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdatePositionEvent(position));


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public static class MInfoLis implements PLOnInfoListener {

        private WeakReference<DiscoverVideoFragment> weakReference;

        public MInfoLis(WeakReference<DiscoverVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onInfo(int i, int i1) {
            if (i == MEDIA_INFO_VIDEO_RENDERING_START) {
                weakReference.get().layoutDiscoverVideoBinding.ivPlay.setVisibility(View.GONE);
                weakReference.get().layoutDiscoverVideoBinding.ivCover.setVisibility(View.GONE);
                weakReference.get().showVideoProgressInfo();
            }
        }
    }

    public static class MComLis implements PLOnCompletionListener {
        private WeakReference<DiscoverVideoFragment> vodModeActivityWeakReference;

        public MComLis(DiscoverVideoFragment vodModeActivity) {
            vodModeActivityWeakReference = new WeakReference<DiscoverVideoFragment>(vodModeActivity);
        }


        @Override
        public void onCompletion() {
            DiscoverVideoFragment vodModeActivity = vodModeActivityWeakReference.get();
            if (vodModeActivity != null) {
                vodModeActivity.onCompleted();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (tempView != null && tempView.getmPlayer() != null) {
//            if (layoutDiscoverVideoBinding != null && layoutDiscoverVideoBinding.ivPlay.getVisibility() == View.VISIBLE) {
//                tempView.getmPlayer().start();
//                layoutDiscoverVideoBinding.ivPlay.setVisibility(View.GONE);
//            }
//
//        }
    }

    @Override
    public void onPause() {
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().pause();
            if (layoutDiscoverVideoBinding != null) {
                layoutDiscoverVideoBinding.ivPlay.setVisibility(View.VISIBLE);
            }

        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        layoutDiscoverVideoBinding = null;
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().stopPlayback();
        }
        super.onDestroy();
    }
}
