package com.gdlife.candypie.activitys.video;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.video.UserVideosVPAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityAutoPlayerBinding;
import com.gdlife.candypie.databinding.ActivityPlayer2Binding;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.view.QiniuPlayerView;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.utils.LogUtil;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT_BY_USER_VIDEOS;

/**
 * Created by heboot on 2018/1/17.
 */

public class AutoPlayActivity extends BaseActivity<ActivityAutoPlayerBinding> {

    private UserVideosVPAdapter userVideosVPAdapter;

    //看的谁的视频
    private List<HomepageVideoBean> homepageVideoBeans;

    private MInfoLis firstFrametListener;

    private MOnPageChangeListener onPageChangeListener;

    private MComLis completedListener;

    private QiniuPlayerView tempView;

//    private ActivityPlayer2Binding layoutDiscoverVideoBinding;

    private int currentShowIndex = 0;

    private PermissionUtils permissionUtils;

    private int userClickPosition = -1;

    private ActivityPlayer2Binding currentPlayBinding;

    private ShareDialog shareDialog;

    private String nickName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_player;
    }

    @Override
    public void initUI() {
        //设置全屏
        QMUIDisplayHelper.setFullScreen(this);
        binding.vvp.setOffscreenPageLimit(1);
        firstFrametListener = new MInfoLis(new WeakReference<>(this));
        onPageChangeListener = new MOnPageChangeListener(new WeakReference<>(this));
        completedListener = new MComLis(this);
        permissionUtils = new PermissionUtils();

    }

    @Override
    public void initData() {
        homepageVideoBeans = (List<HomepageVideoBean>) getIntent().getExtras().get(MKey.USER);
        nickName = getIntent().getExtras().getString(MKey.NAME);
        userClickPosition = getIntent().getExtras().getInt(MKey.INDEX, -1);
        currentShowIndex = userClickPosition;
        userVideosVPAdapter = new UserVideosVPAdapter(this, homepageVideoBeans);
        binding.vvp.setAdapter(userVideosVPAdapter);

        binding.vvp.setCurrentItem(currentShowIndex);
        binding.tvShareContent.setText(MAPP.mapp.getConfigBean().getShare_config().getVideo_share_config().getTip());

    }


    @Override
    public void onPause() {
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().pause();
            binding.ivPlay.setVisibility(View.VISIBLE);

        }

        super.onPause();
    }

    @Override
    public void onStop() {
        if (tempView != null && tempView.getmPlayer() != null) {
            tempView.getmPlayer().stopPlayback();

        }
        super.onStop();
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
                if (o.equals(DISCOVER_PAUSE_PLAY_EVENT_BY_USER_VIDEOS)) {
                    if (binding != null) {
                        if (binding.ivPlay.getVisibility() == View.VISIBLE) {
                            binding.ivPlay.setVisibility(View.GONE);
                            if (tempView != null && tempView.getmPlayer() != null) {
                                tempView.getmPlayer().start();
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
        binding.vvp.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {


                ViewGroup viewGroup = (ViewGroup) page;


                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == currentShowIndex && position == 0 && currentShowIndex != mRoomId) {// && currentShowIndex != mRoomId
//                    if (((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.getVisibility() != View.GONE) {
//                        ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(page)).ivCover.setVisibility(View.GONE);
//                    }
                    if (tempView != null && tempView.getParent() != null && tempView.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (tempView.getParent())).removeView(tempView);
                    }
                    currentPlayBinding = DataBindingUtil.bind(viewGroup);
                    loadVideoAndChatRoom(homepageVideoBeans.get(currentShowIndex).getPath(), currentShowIndex, (ViewGroup) viewGroup.findViewById(R.id.clyt_child_container));
                } else if (position == 1 || position == -1) {//if(currentShowIndex != mRoomId)
                    if (((ActivityPlayer2Binding) DataBindingUtil.bind(page)).ivCover.getVisibility() != View.VISIBLE) {
                        ((ActivityPlayer2Binding) DataBindingUtil.bind(page)).ivCover.setVisibility(View.VISIBLE);
                    }

                }

            }
        });
        binding.vvp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.e(TAG, "onPageScrolled" + position);
                currentShowIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.ovBack.setOnClickListener((v) -> {
            finish();
        });
        binding.ivShare.setOnClickListener((v) -> {
            if (shareDialog == null) {
                shareDialog = new ShareDialog.Builder(this, homepageVideoBeans.get(currentShowIndex).getId(), homepageVideoBeans.get(currentShowIndex).getCover_img(), nickName, MAPP.mapp.getConfigBean().getShare_config().getVideo_share_config()).create();
            }
            shareDialog.show();
        });
    }

    private int mRoomId = -1;

    private void loadVideoAndChatRoom(String url, int cur, ViewGroup v) {
        if (tempView == null) {
            tempView = new QiniuPlayerView(this, homepageVideoBeans.get(currentShowIndex).getPath(), firstFrametListener, completedListener);
        }
        try {
            v.addView(tempView);
            tempView.updatePlayerUrl(url, null);
        } catch (Exception e) {

        }

        mRoomId = cur;
    }

    private void onCompleted() {
//        isCompleted = true;
//        showVideoProgressInfo();
//        stopUpdateTimer();
        tempView.getmPlayer().start();
    }

    public static class MOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private WeakReference<AutoPlayActivity> weakReference;

        public MOnPageChangeListener(WeakReference<AutoPlayActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            weakReference.get().currentShowIndex = position;


        }

        @Override
        public void onPageSelected(int position) {

//            if (weakReference.get().currentShowIndex < weakReference.get().total - 3) {
//                weakReference.get().sp = weakReference.get().sp + 1;
//                weakReference.get().initDiscoverData(weakReference.get().sp);
//            }


//            if (weakReference.get().tempView.getParent() != null && weakReference.get().tempView.getParent() instanceof ViewGroup) {
//                LogUtil.e("播放器视图", "remove" + weakReference.get().currentShowIndex);
//                ((ViewGroup) (weakReference.get().tempView.getParent())).removeView(weakReference.get().tempView);
//            }
//
//
//            weakReference.get().layoutDiscoverVideoBinding = DataBindingUtil.bind((View) weakReference.get().discoverVideoAdapter.instantiateItem(weakReference.get().binding.vvp, position));
//            weakReference.get().loadVideoAndChatRoom(weakReference.get().users.get(position).getMain_video_list().get(0).getPath(), weakReference.get().currentShowIndex, weakReference.get().layoutDiscoverVideoBinding.clytChildContainer);


//            if (weakReference.get().mPlayer != null) {
//                weakReference.get().mPlayer.stop();
//                weakReference.get().mPlayer.destroy();
//                weakReference.get().layoutDiscoverVideoBinding.surfaceView.getHolder().removeCallback(weakReference.get().surfaceHolderCallBck);
//                weakReference.get().layoutDiscoverVideoBinding.surfaceView.destroyDrawingCache();
//
//            }

//            weakReference.get().currentShowIndex = position;
//            if (position < weakReference.get().total - 3) {
//                weakReference.get().sp = weakReference.get().sp + 1;
//                weakReference.get().initDiscoverData(weakReference.get().sp);
//            }
////
//            weakReference.get().user = weakReference.get().users.get(position);
//            RxBus.getInstance().post(new DiscoverEvent.DiscoverUpdateUserEvent(weakReference.get().users.get(position)));
//
//            weakReference.get().layoutDiscoverVideoBinding.ivCover.setVisibility(View.VISIBLE);
//            weakReference.get().layoutDiscoverVideoBinding.clytChildContainer.removeView(weakReference.get().tempView);
//            //解决内存问题 注释
////            weakReference.get().layoutDiscoverVideoBinding = DataBindingUtil.bind(weakReference.get().binding.vvp.getChildAt(position));
//            weakReference.get().layoutDiscoverVideoBinding = DataBindingUtil.bind(((Fragment) weakReference.get().discoverVideoAdapter.instantiateItem(weakReference.get().binding.vvp, position)).getView());
//            if (position >= 1 && position < weakReference.get().total) {
//                try {
//                    ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(((Fragment) weakReference.get().discoverVideoAdapter.instantiateItem(weakReference.get().binding.vvp, position - 1)).getView())).ivCover.setVisibility(View.VISIBLE);
//                    ((LayoutDiscoverVideoBinding) DataBindingUtil.bind(((Fragment) weakReference.get().discoverVideoAdapter.instantiateItem(weakReference.get().binding.vvp, position + 1)).getView())).ivCover.setVisibility(View.VISIBLE);
//                } catch (Exception e) {
//
//                }
//
//            }
//            weakReference.get().layoutDiscoverVideoBinding.clytChildContainer.addView(weakReference.get().tempView);
//            weakReference.get().tempView.updatePlayerUrl(weakReference.get().user.getMain_video_list().get(0).getPath(), weakReference.get().layoutDiscoverVideoBinding.clytChildContainer);


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public static class MInfoLis implements PLOnInfoListener {

        private WeakReference<AutoPlayActivity> weakReference;

        public MInfoLis(WeakReference<AutoPlayActivity> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onInfo(int i, int i1) {
            if (i == MEDIA_INFO_VIDEO_RENDERING_START) {
                if (weakReference.get() != null && weakReference.get().currentPlayBinding != null) {
                    weakReference.get().currentPlayBinding.ivPlay.setVisibility(View.GONE);
                    weakReference.get().currentPlayBinding.ivCover.setVisibility(View.GONE);
                }

//                weakReference.get().showVideoProgressInfo();
            }
        }
    }

    public static class MComLis implements PLOnCompletionListener {
        private WeakReference<AutoPlayActivity> vodModeActivityWeakReference;

        public MComLis(AutoPlayActivity vodModeActivity) {
            vodModeActivityWeakReference = new WeakReference<AutoPlayActivity>(vodModeActivity);
        }


        @Override
        public void onCompletion() {
            AutoPlayActivity vodModeActivity = vodModeActivityWeakReference.get();
            if (vodModeActivity != null) {
                vodModeActivity.onCompleted();
            }
        }
    }
}
