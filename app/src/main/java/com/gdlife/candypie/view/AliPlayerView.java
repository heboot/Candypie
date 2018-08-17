package com.gdlife.candypie.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.gdlife.candypie.R;
import com.heboot.utils.LogUtil;

import java.lang.ref.WeakReference;

public class AliPlayerView extends FrameLayout {

//    private ViewPlayerBinding binding;

    private AliVcMediaPlayer mPlayer;

    private MediaPlayer.MediaPlayerFrameInfoListener mPlayerFirstFrametListener;

    private MediaPlayer.MediaPlayerCompletedListener playerCompletedListener;

    private MPlayerPreparedListener preparedListener;

    private SurfaceHolderCallBck surfaceHolderCallBck;

    private SurfaceView surfaceView;

    private String playUrl;

    private ViewGroup parent;

    private boolean first = true;

    public AliPlayerView(Context context, String url, MediaPlayer.MediaPlayerFrameInfoListener listener, MediaPlayer.MediaPlayerCompletedListener clis) {
        super(context);
        this.mPlayerFirstFrametListener = listener;
        this.playerCompletedListener = clis;
        this.playUrl = url;
        init(context);
    }

    public AliPlayerView(Context context) {
        super(context);
    }

    public AliPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AliPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AliPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_player, this, false);
        View view = LayoutInflater.from(context).inflate(R.layout.view_player, this);
//        ViewUtils.setViewHeight(view, QMUIDisplayHelper.getScreenHeight(context));
//        ViewUtils.setViewWidth(view, QMUIDisplayHelper.getScreenWidth(context));
        surfaceView = view.findViewById(R.id.surfaceView);
//        binding = DataBindingUtil.getBinding(view);
        initPlayer();


    }

    public void updatePlayerUrl(String url, ViewGroup p) {
//        LogUtil.e("player",mPlayer.surface);
        playUrl = url;
        if (mPlayer != null) {
            this.parent = p;
            mPlayer.stop();
//            if (first) {
            mPlayer.prepareAndPlay(url);
//                first = false;
//            }
        } else {
            //创建播放器的实例
            initPlayer();
        }
    }

    public AliVcMediaPlayer getmPlayer() {
        return mPlayer;
    }

    private void initPlayer() {
        surfaceHolderCallBck = new SurfaceHolderCallBck(new WeakReference<>(this));
        preparedListener = new MPlayerPreparedListener(new WeakReference<>(this));
        //创建播放器的实例
        mPlayer = new AliVcMediaPlayer(getContext(), surfaceView);

        mPlayer.setCirclePlay(true);
        surfaceView.getHolder().addCallback(surfaceHolderCallBck);
        mPlayer.setPreparedListener(preparedListener);
        mPlayer.setFrameInfoListener(mPlayerFirstFrametListener);
        mPlayer.setCompletedListener(playerCompletedListener);
        mPlayer.prepareAndPlay(playUrl);

    }


    public static class MPlayerPreparedListener implements MediaPlayer.MediaPlayerPreparedListener {

        private WeakReference<AliPlayerView> weakReference;

        public MPlayerPreparedListener(WeakReference<AliPlayerView> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onPrepared() {
//            if(weakReference.get().getParent() != null){
//                ((ViewGroup)weakReference.get().getParent()).removeView(weakReference.get());
//            }
//            weakReference.get().parent.addView(weakReference.get());
            weakReference.get().mPlayer.play();
        }
    }

    private static class SurfaceHolderCallBck implements SurfaceHolder.Callback {

        private WeakReference<AliPlayerView> weakReference;

        public SurfaceHolderCallBck(WeakReference<AliPlayerView> weakReference) {
            this.weakReference = weakReference;
        }


        public void surfaceCreated(SurfaceHolder holder) {
//                holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);
            LogUtil.e("播放器状态", "surfaceCreated " + holder);
            // Important: surfaceView changed from background to front, we need reset surface to mediaplayer.
            // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
            if (weakReference.get().mPlayer != null) {
                weakReference.get().mPlayer.setVideoSurface(holder.getSurface());
//                weakReference.get().mPlayer.prepareAndPlay(weakReference.get().user.getMain_video_list().get(0).getPath());
            }

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

            LogUtil.e("播放器状态", "surfaceChanged ");
            if (weakReference.get().mPlayer != null) {
                weakReference.get().mPlayer.setSurfaceChanged();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtil.e("播放器状态", "surfaceDestroyed ");
//                if (mPlayer != null) {
//                    mPlayer.releaseVideoSurface();
//                }
        }
    }

}
