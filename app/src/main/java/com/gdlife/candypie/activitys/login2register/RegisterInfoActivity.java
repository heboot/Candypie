package com.gdlife.candypie.activitys.login2register;

import android.content.Intent;
import android.net.Uri;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityRegisterInfoBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/5.
 */

public class RegisterInfoActivity extends BaseActivity<ActivityRegisterInfoBinding> {

    @Inject
    CheckUtils checkUtils;

    private int currentSelect = -1;

    private TipCustomOneDialog chooseSexTipDialog;

    private Consumer<Integer> avatarConsumer;

    private BottomSheetDialog avatarBottomSheet;

    private Uri cropUri;


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

        chooseSexTipDialog = new TipCustomOneDialog.Builder(this, "性别设定后不可更改", "知道了").create();

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
            chooseSexTipDialog.show();

        });

        binding.includeSex.tvWoman.setOnClickListener((v) -> {
            currentSelect = 0;
            binding.includeSex.tvMan.setSelected(false);
            binding.includeSex.tvWoman.setSelected(true);
            chooseSexTipDialog.show();
        });


        binding.btnBottom.setOnClickListener((v) -> {
            checkInfoData();
//            Intent intent = new Intent(this, IndexActivity.class);
//            startActivity(intent);
        });

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


        tipDialog = DialogUtils.getLoadingDialog(this, "", false);
        tipDialog.show();

        params = SignUtils.getNormalParams();
        params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().user_check_nick(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(BaseBeanEntity o) {
                params = SignUtils.getNormalParams();
                params.put(MKey.MOBILE, UserService.getInstance().getUser().getMobile());
                params.put(MKey.CODE, UserService.getInstance().getUser().getSmscode());
                params.put(MKey.PASSWORD, UserService.getInstance().getUser().getPwd());
                params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
                params.put(MKey.SEX, currentSelect);
                params.put(MKey.AVATAR, uploadAvatarReq == null ? "" : JSON.toJSONString(uploadAvatarReq));
                String sign = SignUtils.doSign(params);
                params.put(MKey.SIGN, sign);
                HttpClient.Builder.getGuodongServer().user_register(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<RegisterBean>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(RegisterBean sendSMSBeanBaseBean) {
                        MobclickAgent.onEvent(RegisterInfoActivity.this, NumEventKeys.register_success.toString());
                        User user = sendSMSBeanBaseBean.getUser();
                        user.setPwd(UserService.getInstance().getUser().getPwd());
                        user.setMobile(UserService.getInstance().getUser().getMobile().replaceAll(" ", ""));
                        UserService.getInstance().setUser(user);
                        UserService.getInstance().putSPUser(user);
                        RxBus.getInstance().post(NormalEvent.FINISH_PAGE);
                        LoginService loginService = new LoginService();
                        loginService.doLoginAgora(sendSMSBeanBaseBean.getVideo_user(), sendSMSBeanBaseBean.getIm_user());
                        tipDialog.dismiss();
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

//                        Intent intent = new Intent(RegisterInfoActivity.this, IndexActivity.class);
//                        startActivity(intent);
                        finish();
//                        }

                    }


                    @Override
                    public void onError(Throwable throwable) {
                        tipDialog.dismiss();
                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, throwable.getMessage(), true);
                        tipDialog.show();
                    }

                    @Override
                    public void onError(BaseBean<RegisterBean> basebean) {
                        if (tipDialog != null && tipDialog.isShowing()) {
                            tipDialog.dismiss();
                        }

                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, basebean.getMessage(), true);
                        tipDialog.show();
                    }


                }));
            }

            @Override
            public void onError(Throwable throwable) {
                tipDialog.dismiss();
                tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, throwable.getMessage(), true);
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> basebean) {

            }
        }));


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
