package com.gdlife.candypie.activitys.video;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityPlayer2Binding;
import com.gdlife.candypie.serivce.DownloadService;
import com.gdlife.candypie.serivce.VideoService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.FrameUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.common.CropAction;
import com.heboot.event.UserEvent;
import com.heboot.event.VideoEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.ValueUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.yalantis.dialog.PublishAlbumDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivityByFrame;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;

import static com.heboot.event.NormalEvent.FINISH_AUTH_INDEX_PAGE;

/**
 * Created by heboot on 2018/1/17.
 */

public class PlayerActivity2 extends BaseActivity<ActivityPlayer2Binding> {


    AliVcMediaPlayer mPlayer;


    private Observable<Integer> timeObservable;

    private Observer<Integer> progressObserver;

    private Disposable disposable;

    private VideoPreviewFrom from;

    private PublishAlbumDialog publishAlbumDialog;

    private String videoPath;

    private String videoId;

    private List<String> frames = new ArrayList();


    @Inject
    VideoService videoService;

    @Inject
    DownloadService downloadService;

    FrameUtils frameUtils;

    private Status currentStatus = new Status();

    private String downloadedVideoPath;

    private String cover;

    private boolean isOvalBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player2;
    }


    @Override
    public void initUI() {

        //设置全屏
        QMUIDisplayHelper.setFullScreen(this);


        videoPath = (String) getIntent().getExtras().get(MKey.URL);

        from = (VideoPreviewFrom) getIntent().getExtras().get(MKey.FROM);

        videoId = (String) getIntent().getExtras().get(MKey.VIDEO_ID);

        isOvalBack = getIntent().getExtras().getBoolean(MKey.TYPE, false);

        cover = getIntent().getStringExtra(MKey.COVER_URL);

        if (isOvalBack) {
            binding.ivBack.setVisibility(View.GONE);
            binding.ovBack.setVisibility(View.VISIBLE);
        } else {
            binding.ivBack.setVisibility(View.VISIBLE);
            binding.ovBack.setVisibility(View.GONE);
        }

        if (from == VideoPreviewFrom.USER) {
            binding.tvNext.setVisibility(View.GONE);
        }


        if (!StringUtils.isEmpty(cover)) {
            ImageUtils.showImage(binding.ivCover, cover);
        } else {
            binding.ivCover.setVisibility(View.GONE);
        }

        //创建播放器的实例
        mPlayer = new AliVcMediaPlayer(this, binding.surfaceView);

        if (mPlayer != null) {
            mPlayer.prepareAndPlay(videoPath);
        }

        mPlayer.setCirclePlay(true);

        //开始播放
        mPlayer.play();
        binding.ivPlay.setVisibility(View.GONE);
        setProgress();

        binding.surfaceView.getHolder().addCallback(new SurfaceHolderCallBck(new WeakReference<>(this)));
    }

    @Override
    public void initData() {
        frameUtils = new FrameUtils();
        DaggerServiceComponent.builder().build().inject(this);
        binding.includeHomepageProgress.pb.setMax(mPlayer.getDuration());

    }

    private void setProgress() {
        timeObservable = ObserableUtils.countdownByMILLISECONDS(mPlayer.getDuration());
        progressObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                addDisposable(d);
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
    public void initListener() {

        binding.vBack.setOnClickListener((v) -> {
            finish();
        });

        rxObservable = RxBus.getInstance().toObserverable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread());
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
//              LogUtil.e("发送刷新用户视频集事件3", o.toString());
                if (o instanceof VideoEvent.VideoUploadProgressEvent) {
                    int progress = ((VideoEvent.VideoUploadProgressEvent) o).getProgress();
                    if (publishAlbumDialog == null) {
                        return;
                    }
                    publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_ing));
                    publishAlbumDialog.getBinding().pb.setProgress(progress);
                    if (progress >= 100) {
                        publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_suc));
                        publishAlbumDialog.dismiss();
                        if (from == VideoPreviewFrom.USER) {
                            finish();
                        } else if (from == VideoPreviewFrom.AUTH) {
                            RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
                            finish();
                        } else if (from == VideoPreviewFrom.COMMIT) {
                            finish();
                        }
                    }


                } else if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_USERVIDEOS)) {
                    finish();
                } else if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH) || o.equals(VideoEvent.REPLACE_SUC_EVENT)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });

                } else if (o.equals(VideoEvent.VIDEO_UPLOAD_ERROR_EVENT)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (publishAlbumDialog != null) {
                                publishAlbumDialog.dismiss();
                            }
                            ToastUtils.showToast("上传失败，请稍后重试");
                        }
                    });
                } else if (o.equals(VideoEvent.UPDATE_AVATAR_SUC_EVENT)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            publishAlbumDialog.showAniTo100();
                        }
                    });
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.surfaceView.setOnClickListener((v) -> {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                disposable.dispose();
                binding.ivPlay.setVisibility(View.VISIBLE);
//                binding.ivOption.setVisibility(View.VISIBLE);
            } else {
//                mPlayer.seekTo(0);
                mPlayer.play();
                binding.ivPlay.setVisibility(View.GONE);
                setProgress();
//                binding.ivOption.setVisibility(View.GONE);
            }

        });


        mPlayer.setPreparedListener(new MPlayerPreparedListener(new WeakReference<>(this)));

        mPlayer.setCircleStartListener(new MPlayerCircleStartListener(new WeakReference<>(this)));

        mPlayer.setFrameInfoListener(new MPlayerFirstFrametListener(new WeakReference<>(this)));

        binding.tvNext.setOnClickListener((v -> {
            if (from == VideoPreviewFrom.ADD) {
                RxBus.getInstance().post(FINISH_AUTH_INDEX_PAGE);
                doUpload(videoPath);
            }
            //替换主视频
            else if (from == VideoPreviewFrom.CHOOSE) {

                tipDialog = DialogUtils.getLoadingDialog(this, "", false);
                tipDialog.setCancelable(true);
                tipDialog.show();

                downloadedVideoPath = videoId + "video";

                File file = new File(SDCardUtils.getRootPathPrivateVideo() + "/" + downloadedVideoPath);

                if (!file.exists()) {
                    //                doUpload(videoPath);
                    // TODO: 2018/3/24 先进行下载
                    downloadService.downlaodVideo(videoPath, downloadedVideoPath, new Consumer<Status>() {
                        @Override
                        public void accept(Status status) throws Exception {
                            currentStatus = status;
                            if (status instanceof Succeed) {
                                toFrame(SDCardUtils.getRootPathPrivateVideo() + "/" + downloadedVideoPath);
                            } else if (status instanceof Failed) {
                                tipDialog.dismiss();
                                ToastUtils.showToast("下载失败，请稍后重试");
                            }
                        }
                    });
                    RxDownload.INSTANCE.start(videoPath).
                            subscribe();
                } else {
                    toFrame(SDCardUtils.getRootPathPrivateVideo() + "/" + downloadedVideoPath);
                }


            }
            //提交认证
            else if (from == VideoPreviewFrom.AUTH) {

                RxBus.getInstance().post(FINISH_AUTH_INDEX_PAGE);

                tipDialog = DialogUtils.getLoadingDialog(this, "", false);
                tipDialog.setCancelable(true);
                tipDialog.show();
                toFrame(videoPath);
            }
            //中断提交认证
            else if (from == VideoPreviewFrom.COMMIT) {

                RxBus.getInstance().post(FINISH_AUTH_INDEX_PAGE);

                MValue.COMMIT_UPLOAD_VIDEO = true;

                tipDialog = DialogUtils.getLoadingDialog(this, "", false);
                tipDialog.setCancelable(true);
                tipDialog.show();
                toFrame(videoPath);

            }
        }));

    }

    private void toFrame(String toFramePath) {

        publishAlbumDialog = new PublishAlbumDialog.Builder(this).create();
        publishAlbumDialog.show();
        publishAlbumDialog.showAni();


        Bitmap bitmap = null;
        String imagePath = null;
        try {
            bitmap = frameUtils.getFrameByVideo(videoPath);
            imagePath = ImageUtils.saveBitmap(bitmap);
        } catch (Exception e) {
        }


        RxBus.getInstance().post(new VideoEvent.VideoUploadEvent(toFramePath, imagePath, ""));

//        Observable.create(new ObservableOnSubscribe<Object>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
//                FrameUtils frameUtils = new FrameUtils();
//                String fristImage = ImageUtils.saveBitmap(frameUtils.getFrameByVideo(toFramePath));
//
//                ValueUtils.currentVideoPath = toFramePath;
//
//                UCrop.Options options = new UCrop.Options();
//
//                options.setShowCropGrid(false);
//                options.setFreeStyleCropEnabled(false);
//                options.setHideBottomControls(true);
//                options.setToolbarTitle("裁剪");
//                options.setToolbarColor(Color.TRANSPARENT);
//                options.setToolbarWidgetColor(Color.WHITE);
//
//                tipDialog.dismiss();
//
//                if (from == VideoPreviewFrom.CHOOSE) {
//                    CropAction.CROP_ACTION = CropAction.CROP_ACTION_VALUE.CHOOSE.toString();
//                } else {
//                    CropAction.CROP_ACTION = CropAction.CROP_ACTION_VALUE.UPLOAD_VIDEO.toString();
//                }
//
//
//                MValue.CURRENT_UPDATE_AVATAR_VIDEO_ID = videoId;
////                    options.getOptionBundle().putSerializable("frames", (Serializable) frames);
//
//                options.getOptionBundle().putSerializable("path", toFramePath);
//                options.getOptionBundle().putSerializable("outPath", SDCardUtils.getRootPathPrivateFrame());
//
//                UCrop.of(Uri.fromFile(new File(fristImage)), ImageUtils.getCropPhotoUri())
//                        .withOptions(options)
//                        .withAspectRatio(1, 1)
//                        .startByFrame(PlayerActivity2.this);
//
//                finish();
//            }
////            }
//        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();


    }

    public static class MPlayerFirstFrametListener implements MediaPlayer.MediaPlayerFrameInfoListener {

        private WeakReference<PlayerActivity2> weakReference;

        public MPlayerFirstFrametListener(WeakReference<PlayerActivity2> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onFrameInfoListener() {
            weakReference.get().binding.ivCover.setVisibility(View.GONE);
        }
    }


    public static class MPlayerCircleStartListener implements MediaPlayer.MediaPlayerCircleStartListener {

        private WeakReference<PlayerActivity2> weakReference;

        public MPlayerCircleStartListener(WeakReference<PlayerActivity2> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onCircleStart() {
            weakReference.get().setProgress();
            weakReference.get().mPlayer.seekTo(0);
        }
    }

    public static class MPlayerPreparedListener implements MediaPlayer.MediaPlayerPreparedListener {

        private WeakReference<PlayerActivity2> weakReference;

        public MPlayerPreparedListener(WeakReference<PlayerActivity2> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onPrepared() {
            if (weakReference == null || weakReference.get() == null || weakReference.get().mPlayer == null) {
                return;
            }
            weakReference.get().mPlayer.play();
            weakReference.get().binding.ivPlay.setVisibility(View.GONE);
            weakReference.get().setProgress();
            //准备完成时触发
            weakReference.get().binding.includeHomepageProgress.pb.setMax(weakReference.get().mPlayer.getDuration());
        }
    }


    private static class SurfaceHolderCallBck implements SurfaceHolder.Callback {

        private WeakReference<PlayerActivity2> weakReference;

        public SurfaceHolderCallBck(WeakReference<PlayerActivity2> weakReference) {
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
        binding.ivPlay.setVisibility(View.VISIBLE);
        if (disposable != null) {
            disposable.dispose();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        mPlayer.stop();
        if (disposable != null) {
            disposable.dispose();
        }
        super.onStop();
    }

    private void doUpload(String path) {
        videoPath = path;

        try {
            Bitmap bitmap = frameUtils.getFrameByVideo(videoPath);
            String imagePath = ImageUtils.saveBitmap(bitmap);
            publishAlbumDialog = new PublishAlbumDialog.Builder(this).create();
            publishAlbumDialog.show();
            publishAlbumDialog.showAni();
            videoService.doUploadVideo(imagePath, null, 0, VideoUploadType.USER, videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
