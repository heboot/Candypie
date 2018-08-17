package com.gdlife.candypie.fragments.homepage;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.databinding.FragmentHomepageVideoBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.heboot.event.VideoEvent;
import com.heboot.utils.LogUtil;
import com.netease.nim.uikit.common.util.media.ImageUtil;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/1.
 */

public class HomepageVideoFragment extends BaseFragment<FragmentHomepageVideoBinding> {

    private AliVcMediaPlayer mPlayer;

    private Observable<Integer> timeObservable;

    private Observer<Integer> progressObserver;

    private Disposable disposable;

    private boolean isFirst = true;

    private String url;

    private String coverUrl;


    public HomepageVideoFragment(String url, String coverUrl) {
        this.url = url;
        this.coverUrl = coverUrl;
    }

    public HomepageVideoFragment() {
    }

    @Override
    public void initUI() {
        mPlayer = new AliVcMediaPlayer(getContext(), binding.surfaceView);

        if (mPlayer != null) {
            mPlayer.prepareToPlay(url);
        }

        binding.ivPlay.setVisibility(View.GONE);

        mPlayer.setCirclePlay(true);

//
        binding.surfaceView.getHolder().addCallback(new SurfaceHolderCallBck(new WeakReference<>(this)));

    }

    private void setProgress() {

        timeObservable = ObserableUtils.countdownByMILLISECONDS(mPlayer.getDuration());

        progressObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                addDisposable(disposable);
            }

            @Override
            public void onNext(Integer integer) {
                if (integer >= mPlayer.getDuration()) {
                    binding.includeHomepageProgress.pb.setProgress(mPlayer.getDuration());
                } else {
                    binding.includeHomepageProgress.pb.setProgress(mPlayer.getCurrentPosition());
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        timeObservable.subscribe(progressObserver);
    }

    @Override
    public void initData() {

        ImageUtils.showImage(binding.ivCover, coverUrl);

    }

    @Override
    public void initListener() {


//        mPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
//            @Override
//            public void onCompleted() {
//                //视频正常播放完成时触发
//                binding.includeHomepageProgress.pb.setProgress(mPlayer.getDuration());
//                LogUtil.e(TAG, "onCompleted" + mPlayer.getDuration() + "====" + mPlayer.getCurrentPosition());
//                disposable.dispose();
//                binding.ivPlay.setVisibility(View.VISIBLE);
//                mPlayer.stop();
//            }
//        });


        rxObservable.subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(VideoEvent.MUTE_OFF)) {
                    mPlayer.setMuteMode(false);
                } else if (o.equals(VideoEvent.MUTE_ON)) {
                    mPlayer.setMuteMode(true);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

//        mPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
//            @Override
//            public void onPrepared() {
//                binding.ivCover.setVisibility(View.GONE);
//                mPlayer.play();
//                setProgress();
//                //准备完成时触发
//                binding.includeHomepageProgress.pb.setMax(mPlayer.getDuration());
//            }
//        });

        mPlayer.setPreparedListener(new MPlayerPreparedListener(new WeakReference<>(this)));

        mPlayer.setCircleStartListener(new MPlayerCircleStartListener(new WeakReference<>(this)));

        mPlayer.setFrameInfoListener(new MPlayerFirstFrametListener(new WeakReference<>(this)));

        binding.surfaceView.setOnClickListener((v) -> {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                disposable.dispose();
                binding.ivPlay.setVisibility(View.VISIBLE);
            } else {
                if (mPlayer.getCurrentPosition() == mPlayer.getDuration()) {
                    mPlayer.seekTo(0);
                }
                mPlayer.play();
                setProgress();
                binding.ivPlay.setVisibility(View.GONE);
            }

        });

        isFirst = false;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && !isFirst) {
            mPlayer.pause();
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                binding.ivPlay.setVisibility(View.VISIBLE);
            }
        } else {
            if (!isFirst) {
                binding.ivPlay.setVisibility(View.GONE);
                mPlayer.seekTo(0);
                mPlayer.play();
                setProgress();
            }
        }
    }

    public static class MPlayerFirstFrametListener implements MediaPlayer.MediaPlayerFrameInfoListener {

        private WeakReference<HomepageVideoFragment> weakReference;

        public MPlayerFirstFrametListener(WeakReference<HomepageVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onFrameInfoListener() {
            weakReference.get().binding.ivCover.setVisibility(View.GONE);
        }
    }


    public static class MPlayerCircleStartListener implements MediaPlayer.MediaPlayerCircleStartListener {

        private WeakReference<HomepageVideoFragment> weakReference;

        public MPlayerCircleStartListener(WeakReference<HomepageVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onCircleStart() {
            weakReference.get().setProgress();
            weakReference.get().mPlayer.seekTo(0);
        }
    }

    public static class MPlayerPreparedListener implements MediaPlayer.MediaPlayerPreparedListener {

        private WeakReference<HomepageVideoFragment> weakReference;

        public MPlayerPreparedListener(WeakReference<HomepageVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onPrepared() {
            if (weakReference != null && weakReference.get() != null) {
                weakReference.get().mPlayer.play();
                weakReference.get().setProgress();
                //准备完成时触发
                weakReference.get().binding.includeHomepageProgress.pb.setMax(weakReference.get().mPlayer.getDuration());
            }

        }
    }

    private static class SurfaceHolderCallBck implements SurfaceHolder.Callback {

        private WeakReference<HomepageVideoFragment> weakReference;

        public SurfaceHolderCallBck(WeakReference<HomepageVideoFragment> weakReference) {
            this.weakReference = weakReference;
        }


        public void surfaceCreated(SurfaceHolder holder) {
//                holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);
            Log.e("lfj0930", "surfaceCreated ");
            // Important: surfaceView changed from background to front, we need reset surface to mediaplayer.
            // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
            if (weakReference.get().mPlayer != null) {
                weakReference.get().mPlayer.setVideoSurface(holder.getSurface());
            }

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

            Log.e("lfj0930", "surfaceChanged ");
            if (weakReference.get().mPlayer != null) {
                weakReference.get().mPlayer.setSurfaceChanged();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e("lfj0930", "surfaceDestroyed ");
//                if (mPlayer != null) {
//                    mPlayer.releaseVideoSurface();
//                }
        }
    }


    @Override
    public void onPause() {
        mPlayer.pause();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        binding.ivPlay.setVisibility(View.VISIBLE);
        super.onPause();
    }

    @Override
    public void onStop() {
        mPlayer.stop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onStop();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage_video;
    }
}
