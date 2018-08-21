package com.gdlife.candypie.activitys.auth;

import android.app.Activity;
import android.content.Intent;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.CropKey;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityAuthIndexBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.LocationUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.VideoUtils;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/5.
 */

public class AuthIndexActivity extends BaseActivity<ActivityAuthIndexBinding> {

    @Inject
    VideoUtils videoUtils;

    @Inject
    PermissionUtils permissionUtils;

    @Inject
    LocationUtils locationUtils;

    private String videoPath;

    private List<String> frames = new ArrayList();

    private RecordVideoFrom recordVideoFrom;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(true);
        binding.includeToolbar.setShowRight(false);
        binding.btnBottom.setText(getString(R.string.upload_video));
    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);

        ImageUtils.showImage(binding.ivCover, MAPP.mapp.getConfigBean().getServicer_auth_config().getCover_img());

    }

    @Override
    public void initListener() {

        permissionUtils.requestLocationPermission(this);

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_AUTH_INDEX_PAGE)) {
                    finish();
                } else if (o.equals(UserEvent.AUTH_SERVICE_SUC_EVENT)) {
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

        recordVideoFrom = (RecordVideoFrom) getIntent().getExtras().get(MKey.FROM);

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.ivCover.setOnClickListener((v) -> {
            IntentUtils.toPlayerActivity2(this, MAPP.mapp.getConfigBean().getServicer_auth_config().getVideo(), VideoPreviewFrom.USER, null, MAPP.mapp.getConfigBean().getServicer_auth_config().getCover_img());

        });

        binding.btnBottom.setOnClickListener((v) -> {

            /**
             * 检查录制权限
             */
            if (permissionUtils.hasCameraPermission(MAPP.mapp)) {
                if (recordVideoFrom == RecordVideoFrom.AUTH || recordVideoFrom == RecordVideoFrom.COMMIT) {
                    if (!permissionUtils.hasNoticicationPermission()) {
                        permissionUtils.showPermissionDialog(AuthIndexActivity.this, null);
                        return;
                    }
                    if (!permissionUtils.hasLocPermission()) {
                        permissionUtils.showPermissionDialog(AuthIndexActivity.this, null);
                        return;
                    }
                }

                locationUtils.requestLocation();

                DialogUtils.getAuthSkillBottomSheet(this, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 0) {
                            videoUtils.toRecordVideo(AuthIndexActivity.this);
                        } else if (integer == 1) {
                            videoUtils.toSelectVideo(AuthIndexActivity.this);
                        }
                    }
                }).show();
            } else {
                permissionUtils.getCameraPermission(this);
            }


//            IntentUtils.toUserInfoActivity(this, MValue.USER_INFO_TYPE_AUTH, MValue.USER_INFO_TYPE_AUTH, UserService.getInstance().getUser(), null, null);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MCode.REQUEST_CODE.RECORD_VIDEO_CODE || requestCode == MCode.REQUEST_CODE.CHOOSE_VIDEO_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    videoPath = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                    if (recordVideoFrom == RecordVideoFrom.AUTH) {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.AUTH, null);
                    } else if (recordVideoFrom == RecordVideoFrom.COMMIT) {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.COMMIT, null);
                    } else {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.ADD, null);
                    }

                } else if (type == AliyunVideoRecorder.RESULT_TYPE_CROP) {
                    videoPath = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
                    if (recordVideoFrom == RecordVideoFrom.AUTH) {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.AUTH, null);
                    } else if (recordVideoFrom == RecordVideoFrom.COMMIT) {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.COMMIT, null);
                    } else {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.ADD, null);
                    }
                }

//                    IntentUtils.toVideoFrameActivity(this, data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
//                    videoService.doUploadVideo(this, imagePath, data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
//            ToastUtils.showToast("用户取消录制");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER
//                && UserService.getInstance().getUser().getService_auth_status() != null
//                && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_ING) {
        if (UserService.getInstance().getUser().getService_auth_status() != null
                && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_ING) {
            binding.btnBottom.setEnabled(false);
            binding.btnBottom.setSelected(false);
            binding.tvAuthStatus.setText(getString(R.string.auth_ing));
        } else {
            binding.btnBottom.setEnabled(true);
            binding.btnBottom.setSelected(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_index;
    }
}
