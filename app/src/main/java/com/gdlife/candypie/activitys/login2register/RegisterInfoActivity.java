package com.gdlife.candypie.activitys.login2register;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.LoginType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityRegisterInfoBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.DownloadService;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.NimChatService;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.dialog.login2register.ChooseSexTipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.bean.user.UserInfoEditBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.req.UploadAvatarReq;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;

/**
 * Created by heboot on 2018/2/5.
 */

public class RegisterInfoActivity extends BaseActivity<ActivityRegisterInfoBinding> {

    @Inject
    CheckUtils checkUtils;

    private int currentSelect = -1;

    private ChooseSexTipDialog chooseSexTipDialog;

    private Consumer<Integer> avatarConsumer;

    private BottomSheetDialog avatarBottomSheet;

    private Uri cropUri;

    private String sync_login_id;

    private LoginType sync_login_type;

    private HashMap hashMap;

    private String syncHeadUrl;

    private DownloadService downloadService;


    private QMUITipDialog loadingDialog;

    private UploadAvatarReq uploadAvatarReq;

    @Override
    public void initUI() {
        MobclickAgent.onEvent(RegisterInfoActivity.this, NumEventKeys.register_view.toString());
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.register_info));
        binding.btnBottom.setText(getString(R.string.complete));
        binding.includeSex.tvWoman.setSelected(false);
        binding.includeSex.tvMan.setSelected(false);

    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);
        checkUtils.setBottomEnable(binding.btnBottom, true);
        if (getIntent() != null && getIntent().getExtras() != null) {
            sync_login_id = (String) getIntent().getExtras().get(MKey.ACTION_ID);
        }
        initOtherLoginData();

        chooseSexTipDialog = new ChooseSexTipDialog(currentSelect == 1 ? "男" : "女");

        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
    }

    private void initOtherLoginData() {
        if (!StringUtils.isEmpty(sync_login_id)) {
            sync_login_type = (LoginType) getIntent().getExtras().get(MKey.TYPE);
            hashMap = (HashMap) getIntent().getExtras().get(MKey.MAP);
            if (sync_login_type == LoginType.WX) {
                syncHeadUrl = (String) hashMap.get("headimgurl");
                ImageUtils.showImage(binding.ivAvatar, (String) hashMap.get("headimgurl"));
                binding.etNick.setText((String) hashMap.get("nickname"));
                if ((int) hashMap.get("sex") == 1) {
                    currentSelect = 1;
                    binding.includeSex.tvMan.setSelected(true);
                    binding.includeSex.tvWoman.setSelected(false);
                } else {
                    currentSelect = 0;
                    binding.includeSex.tvMan.setSelected(false);
                    binding.includeSex.tvWoman.setSelected(true);
                }
            } else if (sync_login_type == LoginType.QQ) {
                syncHeadUrl = (String) hashMap.get("figureurl_qq_2");
                ImageUtils.showImage(binding.ivAvatar, (String) hashMap.get("figureurl_qq_2"));
                binding.etNick.setText((String) hashMap.get("nickname"));
                int qqsex = hashMap.get("gender").equals("男") ? 1 : 0;
                if (qqsex == 1) {
                    currentSelect = 1;
                    binding.includeSex.tvMan.setSelected(true);
                    binding.includeSex.tvWoman.setSelected(false);
                } else {
                    currentSelect = 0;
                    binding.includeSex.tvMan.setSelected(false);
                    binding.includeSex.tvWoman.setSelected(true);
                }
            }

        }
    }

    @Override
    public void initListener() {

        binding.ivAvatar.setOnClickListener((v) -> {
            cropUri = ImageUtils.getCropPhotoUri();
            if (avatarBottomSheet == null) {
                avatarBottomSheet = DialogUtils.getAvatarBottomSheet(RegisterInfoActivity.this, avatarConsumer);
            }
            avatarBottomSheet.show();
        });


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });


        binding.includeSex.tvMan.setOnClickListener((v) -> {
            currentSelect = 1;
            binding.includeSex.tvMan.setSelected(true);
            binding.includeSex.tvWoman.setSelected(false);
            chooseSexTipDialog.setSex("男");
            chooseSexTipDialog.show(getSupportFragmentManager(), "");

        });

        binding.includeSex.tvWoman.setOnClickListener((v) -> {
            currentSelect = 0;
            binding.includeSex.tvMan.setSelected(false);
            binding.includeSex.tvWoman.setSelected(true);
            chooseSexTipDialog.setSex("女");
            chooseSexTipDialog.show(getSupportFragmentManager(), "");
        });

        binding.vClear.setOnClickListener((v) -> {
            binding.etNick.setText("");
        });


        RxView.clicks(binding.btnBottom).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                checkInfoData();
            }
        });
//        binding.btnBottom.setOnClickListener((v) -> {
//            checkInfoData();
////            Intent intent = new Intent(this, IndexActivity.class);
////            startActivity(intent);
//        });

        avatarConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                avatarBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        PictureSelector.create(RegisterInfoActivity.this)
                                .openCamera(PictureMimeType.ofImage()).setOutputCameraPath(cropUri.getPath()).enableCrop(true)
                                .cropWH(300, 300).withAspectRatio(1, 1)
                                .cropCompressQuality(70)// 裁剪压缩质量 默认90 int
                                .minimumCompressSize(200)// 小于100kb的图片不压缩
                                .hideBottomControls(true)
                                .previewImage(false)
                                .showCropGrid(false)
                                .rotateEnabled(false)
                                .forResult(PictureConfig.CAMERA);
                        break;
                    case 1:
                        PictureSelector.create(RegisterInfoActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(true)
                                .cropWH(300, 300).withAspectRatio(1, 1)
                                .cropCompressQuality(70)// 裁剪压缩质量 默认90 int
                                .minimumCompressSize(200)// 小于100kb的图片不压缩
                                .hideBottomControls(true)
                                .previewImage(false)
                                .isCamera(false)
                                .rotateEnabled(false)
                                .previewEggs(false)
                                .showCropGrid(false).maxSelectNum(1).forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                }
            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean submitFlag = false;

    private void doSubmit() {

        submitFlag = true;
//
//        tipDialog = DialogUtils.getLoadingDialog(this, "", false);
//        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
        loadingDialog.show();

        params = SignUtils.getNormalParams();
        params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().user_check_nick(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                params = SignUtils.getNormalParams();
                if (StringUtils.isEmpty(sync_login_id)) {
                    params.put(MKey.MOBILE, UserService.getInstance().getUser().getMobile());
                    params.put(MKey.CODE, UserService.getInstance().getUser().getSmscode());
                    params.put(MKey.PASSWORD, UserService.getInstance().getUser().getPwd());
                }

                params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
                params.put(MKey.SEX, currentSelect);
                params.put(MKey.AVATAR, uploadAvatarReq == null ? "" : JSON.toJSONString(uploadAvatarReq));
                if (!StringUtils.isEmpty(sync_login_id)) {
                    params.put(MKey.SYNC_LOGIN_ID, sync_login_id);
                }


                String sign = SignUtils.doSign(params);
                params.put(MKey.SIGN, sign);
                HttpClient.Builder.getGuodongServer().user_register(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RegisterBean>() {
                    @Override
                    public void onSuccess(BaseBean<RegisterBean> baseBean) {
                        MobclickAgent.onEvent(RegisterInfoActivity.this, NumEventKeys.register_success.toString());
                        User user = baseBean.getData().getUser();
                        if (StringUtils.isEmpty(sync_login_id)) {
                            user.setPwd(UserService.getInstance().getUser().getPwd());
                            user.setMobile(UserService.getInstance().getUser().getMobile().replaceAll(" ", ""));
                        }
                        UserService.getInstance().setUser(user);
                        UserService.getInstance().putSPUser(user);
                        RxBus.getInstance().post(NormalEvent.FINISH_PAGE);
                        LoginService loginService = new LoginService();
                        loginService.doLoginAgora(baseBean.getData().getVideo_user(), baseBean.getData().getIm_user());

                        RxBus.getInstance().post(UserEvent.LOGIN_SUC);
//                        if (MValue.IS_FROM_TOURIST) {
//                            MValue.IS_FROM_TOURIST = false;
//                            finish();
//                        } else {

                        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                            IntentUtils.toIndexActivity(RegisterInfoActivity.this);
                        } else {
                            IntentUtils.toMainActivity(RegisterInfoActivity.this);
                        }
                        loadingDialog.dismiss();
//                        Intent intent = new Intent(RegisterInfoActivity.this, IndexActivity.class);
//                        startActivity(intent);
                        finish();
//                        }
                    }

                    @Override
                    public void onError(BaseBean<RegisterBean> baseBean) {
                        submitFlag = false;
                        loadingDialog.dismiss();
                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, baseBean.getMessage(), true);
                        tipDialog.show();
                    }
                });
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {
                submitFlag = false;
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });


//        HttpClient.Builder.getGuodongServer().user_check_nick(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                addDisposable(disposable);
//            }
//
//            @Override
//            public void onSuccess(BaseBeanEntity o) {
//                params = SignUtils.getNormalParams();
//                if (StringUtils.isEmpty(sync_login_id)) {
//                    params.put(MKey.MOBILE, UserService.getInstance().getUser().getMobile());
//                    params.put(MKey.CODE, UserService.getInstance().getUser().getSmscode());
//                    params.put(MKey.PASSWORD, UserService.getInstance().getUser().getPwd());
//                }
//
//                params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
//                params.put(MKey.SEX, currentSelect);
//                params.put(MKey.AVATAR, uploadAvatarReq == null ? "" : JSON.toJSONString(uploadAvatarReq));
//                if (!StringUtils.isEmpty(sync_login_id)) {
//                    params.put(MKey.SYNC_LOGIN_ID, sync_login_id);
//                }
//
//
//                String sign = SignUtils.doSign(params);
//                params.put(MKey.SIGN, sign);
//                HttpClient.Builder.getGuodongServer().user_register(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<RegisterBean>() {
//                    @Override
//                    public void onSubscribe(Disposable disposable) {
//                        addDisposable(disposable);
//                    }
//
//                    @Override
//                    public void onSuccess(RegisterBean sendSMSBeanBaseBean) {
//                        MobclickAgent.onEvent(RegisterInfoActivity.this, NumEventKeys.register_success.toString());
//                        User user = sendSMSBeanBaseBean.getUser();
//                        if (StringUtils.isEmpty(sync_login_id)) {
//                            user.setPwd(UserService.getInstance().getUser().getPwd());
//                            user.setMobile(UserService.getInstance().getUser().getMobile().replaceAll(" ", ""));
//                        }
//                        UserService.getInstance().setUser(user);
//                        UserService.getInstance().putSPUser(user);
//                        RxBus.getInstance().post(NormalEvent.FINISH_PAGE);
//                        LoginService loginService = new LoginService();
//                        loginService.doLoginAgora(sendSMSBeanBaseBean.getVideo_user(), sendSMSBeanBaseBean.getIm_user());
//                        loadingDialog.dismiss();
//                        RxBus.getInstance().post(UserEvent.LOGIN_SUC);
////                        if (MValue.IS_FROM_TOURIST) {
////                            MValue.IS_FROM_TOURIST = false;
////                            finish();
////                        } else {
//
//                        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
//                            IntentUtils.toIndexActivity(RegisterInfoActivity.this);
//                        } else {
//                            IntentUtils.toMainActivity(RegisterInfoActivity.this);
//                        }
//
////                        Intent intent = new Intent(RegisterInfoActivity.this, IndexActivity.class);
////                        startActivity(intent);
//                        finish();
////                        }
//
//                    }
//
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        loadingDialog.dismiss();
//                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, throwable.getMessage(), true);
//                        tipDialog.show();
//                    }
//
//                    @Override
//                    public void onError(BaseBean<RegisterBean> basebean) {
//                        if (tipDialog != null && tipDialog.isShowing()) {
//                            tipDialog.dismiss();
//                        }
//
//                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, basebean.getMessage(), true);
//                        tipDialog.show();
//                    }
//
//
//                }));
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                if (tipDialog != null && tipDialog.isShowing()) {
//                    tipDialog.dismiss();
//                }
//                tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, throwable.getMessage(), true);
//                tipDialog.show();
//            }
//
//            @Override
//            public void onError(BaseBean<BaseBeanEntity> basebean) {
//
//            }
//        }));
    }

    private void checkInfoData() {

        if (currentSelect < 0) {
            tipDialog = DialogUtils.getFailDialog(this, "请先选择性别", true);
            tipDialog.show();
            return;
        }

        if (!checkUtils.checkNick(binding.etNick.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_nick), true);
            tipDialog.show();
            return;
        }

        //先下载头像
        if (!StringUtils.isEmpty(sync_login_id)) {
            loadingDialog.show();
            if (downloadService == null) {
                downloadService = new DownloadService();
            }
            String downloadedVideoPath = sync_login_id + "avatar";
            File file = new File(SDCardUtils.getRootPathPrivatePic() + "/" + downloadedVideoPath);

            if (!file.exists()) {
                downloadService.downlaodAvatar(syncHeadUrl, downloadedVideoPath, new Consumer<Status>() {
                    @Override
                    public void accept(Status status) {
                        try {
                            if (status instanceof Succeed) {
                                if (!submitFlag) {
                                    UploadService.doUploadAvatar(SDCardUtils.getRootPathPrivatePic() + "/" + downloadedVideoPath, new Observer<UploadAvatarReq>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            addDisposable(d);
                                        }

                                        @Override
                                        public void onNext(UploadAvatarReq uploadAvatarReq) {
                                            loadingDialog.dismiss();
                                            if (uploadAvatarReq != null) {
                                                RegisterInfoActivity.this.uploadAvatarReq = uploadAvatarReq;
                                            }
                                            if (!submitFlag) {
                                                doSubmit();
                                            }

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            loadingDialog.dismiss();
                                            ToastUtils.showToast(e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                                }
                            } else if (status instanceof Failed) {
                                loadingDialog.dismiss();
                                ToastUtils.showToast("下载失败，请稍后重试");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                RxDownload.INSTANCE.start(syncHeadUrl).observeOn(AndroidSchedulers.mainThread()).
                        subscribe();

            } else {
                if (!submitFlag) {
                    UploadService.doUploadAvatar(SDCardUtils.getRootPathPrivatePic() + "/" + downloadedVideoPath, new Observer<UploadAvatarReq>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(UploadAvatarReq uploadAvatarReq) {
                            loadingDialog.dismiss();
                            if (uploadAvatarReq != null) {
                                RegisterInfoActivity.this.uploadAvatarReq = uploadAvatarReq;
                            }
                            if (!submitFlag) {
                                doSubmit();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            loadingDialog.dismiss();
                            ToastUtils.showToast(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
            }

        } else {
            doSubmit();
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_info;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (localMedia == null) {
            return;
        }

        ImageUtils.showImage(binding.ivAvatar, localMedia.getCutPath());

        UploadService.doUploadAvatar(localMedia.getCutPath(), new Observer<UploadAvatarReq>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(UploadAvatarReq uploadAvatarReq) {
                if (uploadAvatarReq != null) {
                    RegisterInfoActivity.this.uploadAvatarReq = uploadAvatarReq;
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });

    }


}
