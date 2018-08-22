package com.gdlife.candypie.fragments.discover;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alivc.player.MediaPlayer;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.databinding.LayoutDiscoverVideoHomepageBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.view.AliPlayerView;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.im.ImChatStatusBean;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT;
import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS;

public class DiscoverVideoHomepageFragment extends BaseFragment<LayoutDiscoverVideoHomepageBinding> {


    private User user;


    private MPlayerFirstFrametListener firstFrametListener;

    private MyCompletedListener completedListener;

    private TipDialog coinDialog;

    private BottomSheetDialog bottomSheetDialog;

    private ShareDialog shareDialog;

    private PermissionUtils permissionUtils;

    private AliPlayerView tempView;

    private VideoChatService videoChatService;

    @SuppressLint("ValidFragment")
    public DiscoverVideoHomepageFragment(User u) {
        this.user = u;
    }

    public DiscoverVideoHomepageFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_discover_video_homepage;
    }

    @Override
    public void initUI() {
        binding.includeToolbar.setWhiteBack(true);
        firstFrametListener = new MPlayerFirstFrametListener(new WeakReference<>(this));
        completedListener = new MyCompletedListener(this);
        permissionUtils = new PermissionUtils();
    }


    @Override
    public void initData() {
        if (user != null && user.getMain_video_list() != null && user.getMain_video_list().size() > 0) {
            tempView = new AliPlayerView(getContext(), user.getMain_video_list().get(0).getPath(), firstFrametListener, completedListener);
            ImageUtils.showImage(binding.ivCover, user.getMain_video_list().get(0).getCover_img());
            binding.clytChildContainer.addView(tempView);
            binding.tvName.setText(user.getNickname());
            binding.tvCity.setText(user.getCity());
            if (user.getIs_online() == 1) {
                binding.tvOnline.setVisibility(View.VISIBLE);
            } else {
                binding.tvOnline.setVisibility(View.GONE);
            }

            if (user.getVideo_nums() > 1) {
                binding.tvVideosNum.setVisibility(View.VISIBLE);
                binding.tvVideosNum.setText(user.getVideo_nums() + "");
            } else {
                binding.tvVideosNum.setVisibility(View.GONE);
            }

        } else {
            binding.tvName.setText(user.getNickname());
            binding.tvCity.setText(user.getCity());
            if (user.getIs_online() == 1) {
                binding.tvOnline.setVisibility(View.VISIBLE);
            } else {
                binding.tvOnline.setVisibility(View.GONE);
            }

            if (user.getVideo_nums() > 1) {
                binding.tvVideosNum.setVisibility(View.VISIBLE);
                binding.tvVideosNum.setText(user.getVideo_nums() + "");
            } else {
                binding.tvVideosNum.setVisibility(View.GONE);
            }

        }

        binding.tvVideoPrice.setText(user.getVideo_chat_price());

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

        private WeakReference<DiscoverVideoHomepageFragment> weakReference;

        public MHandler(WeakReference<DiscoverVideoHomepageFragment> weakReference) {
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
            int bufferPosition = tempView.getmPlayer().getBufferPosition();
            Log.d("lfj0929", "curPosition = " + curPosition + " , duration = " + duration + " ， inSeek = ");

//                positionTxt.setText(AliPlayerFormatter.formatTime(curPosition));
//                durationTxt.setText(AliPlayerFormatter.formatTime(duration));
            if (binding != null) {
                binding.pb.setMax(duration);
                binding.pb.setSecondaryProgress(duration);
                binding.pb.setProgress(curPosition);
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
                if (o.equals(DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS)) {
                    if (binding != null) {
                        if (binding.ivPlay.getVisibility() == View.VISIBLE) {
                            binding.ivPlay.setVisibility(View.GONE);
                            if (tempView != null && tempView.getmPlayer() != null) {
                                tempView.getmPlayer().play();
                            }

                        } else {
                            binding.ivPlay.setVisibility(View.VISIBLE);
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

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            getActivity().finish();
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


        binding.ivYue.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (videoChatService == null) {
                videoChatService = new VideoChatService();
            }
            if (permissionUtils == null) {
                permissionUtils = new PermissionUtils();
            }

            videoChatService.postVideoService(permissionUtils, _mActivity, user, coinDialog);

//            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
//                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
//                tipDialog.show();
//                return;
//            }
//            IntentUtils.toThemeListActivity(getContext(), true, user);
        });

        binding.ivMsg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            toChat();
        });
        binding.tvMsg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            toChat();
        });

        binding.ivFav.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            doFav(user.getIs_favs());
        });

        binding.tvFav.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            doFav(user.getIs_favs());
        });

        binding.ivVideos.setOnClickListener((v) -> {
            toRight();
        });

        binding.tvVideos.setOnClickListener((v) -> {
            toRight();
        });

        binding.clytChildContainer.setOnClickListener((v) -> {
            RxBus.getInstance().post(DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS);
        });

    }


    private void toRight() {
        IntentUtils.toUserVideosActivity(getContext(), user, UserVideoActivityFrom.NORMAL);
    }


    private void doFav(int fav) {
        params = SignUtils.getNormalParams();
        params.put(MKey.TO_UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        if (fav == 0) {

            HttpClient.Builder.getGuodongServer().favs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    user.setIs_favs(1);
                    binding.ivFav.setBackgroundResource(R.drawable.icon_discover_fav_on);
                    binding.tvFav.setText(getString(R.string.fav_status_on));
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });


        } else {

            HttpClient.Builder.getGuodongServer().unfavs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    binding.ivFav.setBackgroundResource(R.drawable.icon_discover_fav);
                    binding.tvFav.setText(getString(R.string.fav_status_un));
                    user.setIs_favs(0);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });
        }
    }

    private void toChat() {
        RxBus.getInstance().post(new IMEvent.QUERY_IM_STAUTS_EVENT(getContext(), String.valueOf(user.getId())));
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.e(TAG, "setUserVisibleHint" + isVisibleToUser);
        if (!isVisibleToUser) {
            doPause();
        }
    }


    private void doPause() {
        if (binding != null) {
            binding.ivPlay.setVisibility(View.VISIBLE);
            binding.ivCover.setVisibility(View.VISIBLE);
        }
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().pause();
        }
    }

    private void onCompleted() {
//        isCompleted = true;
        showVideoProgressInfo();
        stopUpdateTimer();
    }

    private void stopUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
    }

    private static class MyCompletedListener implements MediaPlayer.MediaPlayerCompletedListener {

        private WeakReference<DiscoverVideoHomepageFragment> vodModeActivityWeakReference;

        public MyCompletedListener(DiscoverVideoHomepageFragment vodModeActivity) {
            vodModeActivityWeakReference = new WeakReference<DiscoverVideoHomepageFragment>(vodModeActivity);
        }

        @Override
        public void onCompleted() {
            DiscoverVideoHomepageFragment vodModeActivity = vodModeActivityWeakReference.get();
            if (vodModeActivity != null) {
                vodModeActivity.onCompleted();
            }
        }
    }


    public static class MPlayerFirstFrametListener implements MediaPlayer.MediaPlayerFrameInfoListener {

        private WeakReference<DiscoverVideoHomepageFragment> weakReference;

        public MPlayerFirstFrametListener(WeakReference<DiscoverVideoHomepageFragment> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onFrameInfoListener() {
            weakReference.get().binding.ivPlay.setVisibility(View.GONE);
            weakReference.get().binding.ivCover.setVisibility(View.GONE);
            weakReference.get().showVideoProgressInfo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        doPause();
    }

    @Override
    public void onDestroy() {
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().stop();
            tempView.getmPlayer().destroy();
        }
        super.onDestroy();
    }

}
