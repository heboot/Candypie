package com.gdlife.candypie.activitys.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.CropKey;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.auth.AuthSkillPhotoAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityAuthSkillBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.serivce.VideoService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.FrameUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.VideoUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.config.ConfigBean;
import com.heboot.event.UserEvent;
import com.heboot.event.VideoEvent;
import com.heboot.req.UploadAvatarReq;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.PublishAlbumDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/5.
 */

public class AuthSkillActivity extends BaseActivity<ActivityAuthSkillBinding> {

    private AuthSkillPhotoAdapter authSkillPhotoAdapter;

    private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

    private Uri cropUri;

    private BottomSheetDialog avatarBottomSheet;

    private BottomSheetDialog videoBottomSheet;

    private Consumer<Integer> avatarConsumer, videoConsumer;


    FrameUtils frameUtils;

    private String videoPath, videoId;

    @Inject
    VideoService videoService;

    private PublishAlbumDialog publishAlbumDialog;

    private List<UploadAvatarReq> uploadAvatarReqs;

    private String imagePath;

    private VideoUtils videoUtils;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_auth_skill));
        binding.btnBottom.setText(getString(R.string.affirm_choose_commit));

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

    }

    @Override
    public void initData() {

        frameUtils = new FrameUtils();

        DaggerServiceComponent.builder().build().inject(this);

        videoUtils = videoService.getVideoUtils();


        listBean = (ConfigBean.ServiceItemsConfigBean.ListBean) getIntent().getExtras().get(MKey.SERVICE_ITEM);

        binding.includeToolbar.tvTitle.setText(listBean.getTitle());

        authSkillPhotoAdapter = new AuthSkillPhotoAdapter(new ArrayList<>(), new WeakReference<AuthSkillActivity>(this));
        binding.rvList.setAdapter(authSkillPhotoAdapter);
    }

    public void choosePic() {
        cropUri = ImageUtils.getCropPhotoUri();
        if (avatarBottomSheet == null) {
            avatarBottomSheet = DialogUtils.getAvatarBottomSheet(AuthSkillActivity.this, avatarConsumer);
        }
        avatarBottomSheet.show();
    }

    @Override
    public void initListener() {
        rxObservable = RxBus.getInstance().toObserverable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread());
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
//              LogUtil.e("发送刷新用户视频集事件3", o.toString());
                if (o instanceof VideoEvent.VideoUploadSkillEvent) {
                    // TODO: 2018/3/27 videoId
                    videoId = ((VideoEvent.VideoUploadSkillEvent) o).getVideoId();
                    doSkillAuth();
                } else if (o instanceof VideoEvent.VideoUploadProgressEvent) {
                    int progress = ((VideoEvent.VideoUploadProgressEvent) o).getProgress();
                    publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_ing));
                    publishAlbumDialog.getBinding().pb.setProgress(progress);
                    if (progress >= 100) {
                        publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_suc));
                        publishAlbumDialog.dismiss();
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
            finish();
        });

        binding.svPlayer.setOnClickListener((v) -> {
            chooseVideo();
        });

        avatarConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                avatarBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        PictureSelector.create(AuthSkillActivity.this)
                                .openCamera(PictureMimeType.ofImage()).setOutputCameraPath(cropUri.getPath()).enableCrop(false)
                                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                                .minimumCompressSize(200)// 小于100kb的图片不压缩
                                .hideBottomControls(true)
                                .previewImage(false)
                                .maxSelectNum(1)
                                .showCropGrid(false)
                                .rotateEnabled(false)
                                .forResult(PictureConfig.CAMERA);
                        break;
                    case 1:
                        PictureSelector.create(AuthSkillActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(false)
                                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                                .minimumCompressSize(200)// 小于100kb的图片不压缩
                                .hideBottomControls(true)
                                .previewImage(false)
                                .isCamera(false)
                                .maxSelectNum(1)
                                .rotateEnabled(false)
                                .previewEggs(false)
                                .showCropGrid(false).maxSelectNum(1).forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                }
            }
        };

        videoConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                videoBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        videoUtils.toRecordVideo(AuthSkillActivity.this);
                        break;
                    case 1:
                        videoUtils.toSelectVideo(AuthSkillActivity.this);
                        break;
                }
            }
        };

        binding.btnBottom.setOnClickListener((v) -> {
            commitSkillAuth();
        });

    }

    private void commitSkillPic() {

        if (authSkillPhotoAdapter != null && authSkillPhotoAdapter.getData() != null && authSkillPhotoAdapter.getData().size() > 0 && !StringUtils.isEmpty(authSkillPhotoAdapter.getData().get(0))) {
            uploadAvatarReqs = new ArrayList<>();
            for (String localP : authSkillPhotoAdapter.getData()) {
                UploadService.doUploadAvatar(localP, new Observer<UploadAvatarReq>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UploadAvatarReq req) {
                        uploadAvatarReqs.add(req);
                        if (uploadAvatarReqs.size() == authSkillPhotoAdapter.getData().size() - 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    doUpload(videoPath);
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tipDialog = DialogUtils.getFailDialog(AuthSkillActivity.this, getString(R.string.tip_pic_upload_fail), true);
                        tipDialog.show();
                        return;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        } else {
            doUpload(videoPath);
        }
    }

    private void commitSkillAuth() {

        if (StringUtils.isEmpty(videoPath)) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.tip_upload_skill_video), true);
            tipDialog.show();
            return;
        }

        commitSkillPic();

    }

    private void doSkillAuth() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SERVICE_ID, listBean.getId());
        params.put(MKey.VIDEO_ID, videoId);
        if (uploadAvatarReqs != null && uploadAvatarReqs.size() > 0) {
            params.put(MKey.IMGS, JSON.toJSONString(uploadAvatarReqs));
        }

        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().skill_auth(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                tipDialog = DialogUtils.getSuclDialog(AuthSkillActivity.this, baseBean.getMessage(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                tipDialog.show();
                RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }

    private void chooseVideo() {
        if (videoBottomSheet == null) {
            videoBottomSheet = DialogUtils.getAuthSkillBottomSheet(AuthSkillActivity.this, videoConsumer);
        }
        videoBottomSheet.show();
    }


    private void doUpload(String path) {
        videoPath = path;

        try {

            publishAlbumDialog = new PublishAlbumDialog.Builder(this).create();
            publishAlbumDialog.show();
            publishAlbumDialog.showAni();
            videoService.doUploadVideo(imagePath, null, 0, VideoUploadType.SKILL, videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MCode.REQUEST_CODE.CHOOSE_VIDEO_CODE || requestCode == MCode.REQUEST_CODE.CHOOSE_VIDEO_CODE || requestCode == MCode.REQUEST_CODE.RECORD_VIDEO_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
//                    doUpload(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
                    videoPath = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                } else if (type == AliyunVideoRecorder.RESULT_TYPE_CROP) {
//                    doUpload(data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH));
                    videoPath = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
                }

                Bitmap bitmap = null;
                try {
                    bitmap = frameUtils.getFrameByVideo(videoPath);
                    imagePath = ImageUtils.saveBitmap(bitmap);
                    ImageUtils.showImage(binding.svPlayer, imagePath);
                    binding.btnBottom.setSelected(true);
                } catch (Exception e) {
                }


            }
        } else {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PictureConfig.CHOOSE_REQUEST:
                        selectList = PictureSelector.obtainMultipleResult(data);
                        if (selectList != null && selectList.size() > 0) {
                            doGetPic(selectList.get(0));
                        } else {
                            doGetPic(null);
                        }
                        break;
                    case PictureConfig.CAMERA:
                        if (selectList != null && selectList.size() > 0) {
                            doGetPic(selectList.get(0));
                        } else {
                            doGetPic(null);
                        }
                        break;
                }
            }
        }
    }

    private void doGetPic(LocalMedia localMedia) {
        if (localMedia != null) {
//            if (authSkillPhotoAdapter.getData().size() == 3) {
//                authSkillPhotoAdapter.getData().add(0, localMedia.getPath());
//            } else {
            authSkillPhotoAdapter.getData().add(0, localMedia.getPath());
//            }
            authSkillPhotoAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_skill;
    }
}
