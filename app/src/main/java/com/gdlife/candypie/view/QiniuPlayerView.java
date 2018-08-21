package com.gdlife.candypie.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gdlife.candypie.R;
import com.gdlife.candypie.utils.FileUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.utils.LogUtil;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class QiniuPlayerView extends FrameLayout {

//    private ViewPlayerBinding binding;

    private PLVideoTextureView mPlayer;


    private String playUrl;

    private ViewGroup parent;

    private boolean first = true;

    private PLOnInfoListener listener;

    private PLOnCompletionListener cl;

    private FrameLayout container;


    public QiniuPlayerView(Context context, String url, PLOnInfoListener pl, PLOnCompletionListener cl) {
        super(context);
        this.playUrl = url;
        this.listener = pl;
        this.cl = cl;
        init(context);
    }

    public QiniuPlayerView(Context context) {
        super(context);
    }

    public QiniuPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QiniuPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QiniuPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_player, this, false);
        View view = LayoutInflater.from(context).inflate(R.layout.view_player_qiniu, this);
//        ViewUtils.setViewHeight(view, QMUIDisplayHelper.getScreenHeight(context));
//        ViewUtils.setViewWidth(view, QMUIDisplayHelper.getScreenWidth(context));
//        binding = DataBindingUtil.getBinding(view);
        mPlayer = view.findViewById(R.id.PLVideoView);
        container = view.findViewById(R.id.flyt_container);
//        container.setLayoutParams(
//                new FrameLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(context), QMUIDisplayHelper.getScreenHeight(context)));
//        mPlayer.setLayoutParams(new FrameLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(context), QMUIDisplayHelper.getScreenHeight(context)));
        initPlayer();


    }

    public void updatePlayerUrl(String url, ViewGroup p) {
//        LogUtil.e("player",mPlayer.surface);
        playUrl = url;
        if (mPlayer != null) {
            mPlayer.setVideoPath(playUrl);

//            Observable.just("Amit")
//                    //延时两秒，第一个参数是数值，第二个参数是事件单位
//                    .delay(300, TimeUnit.MILLISECONDS)
//                    // Run on a background thread
//                    .subscribeOn(AndroidSchedulers.mainThread())
//                    // Be notified on the main thread
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String s) throws Exception {
            mPlayer.start();
//                        }
//                    });//这里的观察者依然不重要


        } else {
            //创建播放器的实例
            initPlayer();
        }
    }

    public PLVideoTextureView getmPlayer() {
        return mPlayer;
    }

    private void initPlayer() {

        if (!StringUtils.isEmpty(playUrl)) {
            mPlayer.setVideoPath(playUrl);

        }
        AVOptions avOptions = new AVOptions();
        avOptions.setString(AVOptions.KEY_CACHE_DIR, SDCardUtils.getRootPathPrivateVideo());
        avOptions.setInteger(AVOptions.KEY_FAST_OPEN, 1);
        avOptions.setInteger(AVOptions.KEY_PREFER_FORMAT, 2);
        mPlayer.setAVOptions(avOptions);
        mPlayer.setOnInfoListener(listener);
        mPlayer.setOnCompletionListener(cl);
        mPlayer.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
    }


}
