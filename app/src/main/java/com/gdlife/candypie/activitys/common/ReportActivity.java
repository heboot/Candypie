package com.gdlife.candypie.activitys.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.common.ReoprtPhotoAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.databinding.ActivityReportBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.req.UploadAvatarReq;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ReportActivity extends BaseActivity<ActivityReportBinding> {

    private Consumer<Integer> avatarConsumer;
    private BottomSheetDialog avatarBottomSheet;
    private Uri cropUri;
    private ReoprtPhotoAdapter authSkillPhotoAdapter;
    private List<UploadAvatarReq> uploadAvatarReqs;

    private String imagePath;

    private String uid;

    private ReportFromType reportFromType;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);

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

        uid = getIntent().getExtras().getString(MKey.UID);

        reportFromType = (ReportFromType) getIntent().getExtras().get(MKey.TYPE);


        if (reportFromType == ReportFromType.REPROT) {
            binding.includeToolbar.tvTitle.setText(R.string.page_title_report);
            binding.etContent.setHint(R.string.hint_report);
        } else {
            binding.includeToolbar.tvTitle.setText(R.string.page_title_suggest);
            binding.etContent.setHint(R.string.hint_suggest);
        }

        binding.tvTelnum.setText(MAPP.mapp.getConfigBean().getKf_config().getKf_tel());

        authSkillPhotoAdapter = new ReoprtPhotoAdapter(new ArrayList<>(), new WeakReference<ReportActivity>(this));
        binding.rvList.setAdapter(authSkillPhotoAdapter);
    }

    public void choosePic() {
        cropUri = ImageUtils.getCropPhotoUri();
        if (avatarBottomSheet == null) {
            avatarBottomSheet = DialogUtils.getAvatarBottomSheet(ReportActivity.this, avatarConsumer);
        }
        avatarBottomSheet.show();
    }

    private void commitSkillPic() {

        tipDialog = DialogUtils.getLoadingDialog(this, null, false);
        tipDialog.show();

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
                                    if (reportFromType == ReportFromType.REPROT) {
                                        commit_report();
                                    } else {
                                        commit_feedback();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        tipDialog.dismiss();

                        tipDialog = DialogUtils.getFailDialog(ReportActivity.this, getString(R.string.tip_pic_upload_fail), true);
                        tipDialog.show();
                        return;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        } else {
            if (reportFromType == ReportFromType.REPROT) {
                commit_report();
            } else {
                commit_feedback();
            }
        }
    }


    private void commit_report() {

        params = SignUtils.getNormalParams();
        params.put(MKey.REPORT_UID, uid);
        params.put(MKey.CONTENT, binding.etContent.getText().toString());

        if (uploadAvatarReqs != null && uploadAvatarReqs.size() > 0) {
            params.put(MKey.IMGS, JSON.toJSONString(uploadAvatarReqs));
        }

        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().post(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {

                tipDialog.dismiss();

                tipDialog = DialogUtils.getSuclDialog(ReportActivity.this, baseBean.getMessage(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {
                tipDialog.dismiss();
            }
        });
    }

    private void commit_feedback() {

        params = SignUtils.getNormalParams();
        params.put(MKey.CONTENT, binding.etContent.getText().toString());

        if (uploadAvatarReqs != null && uploadAvatarReqs.size() > 0) {
            params.put(MKey.IMGS, JSON.toJSONString(uploadAvatarReqs));
        }

        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().user_suggest_post(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {

                tipDialog.dismiss();


                tipDialog = DialogUtils.getSuclDialog(ReportActivity.this, baseBean.getMessage(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {
                tipDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


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

    private void doGetPic(LocalMedia localMedia) {
        if (localMedia != null) {
//            if (authSkillPhotoAdapter.getData().size() == 3) {
//                authSkillPhotoAdapter.getData().add(0, localMedia.getPath());
//            } else {
            authSkillPhotoAdapter.getData().add(0, localMedia.getPath());
//            }
            authSkillPhotoAdapter.notifyDataSetChanged();

            binding.btnBottom.setSelected(true);
        }
    }


    public void checkBottom(boolean select) {
        if (!select && StringUtils.isEmpty(binding.etContent.getText().toString())) {
            binding.btnBottom.setSelected(select);
        } else {
            binding.btnBottom.setSelected(select);
        }

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
//
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


        RxTextView.textChanges(binding.etContent).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (charSequence.length() > 0) {
                    binding.btnBottom.setSelected(true);
                } else {
                    binding.btnBottom.setSelected(false);
                }
                binding.tvContentnum.setText(charSequence.length() + "/150");
            }
        });

        avatarConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                avatarBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        PictureSelector.create(ReportActivity.this)
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
                        PictureSelector.create(ReportActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(false)
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

        binding.btnBottom.setOnClickListener((v) -> {
//            commitSkillAuth();
            commitSkillPic();
        });
    }
}
