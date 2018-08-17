package com.gdlife.candypie.activitys.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityAuthIdBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.LocationUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.auth.UserAuthIDBean;
import com.heboot.bean.auth.UserAuthQBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.req.UploadAvatarReq;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.yalantis.dialog.TipCustomDialog;

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

public class AuthIDActivity extends BaseActivity<ActivityAuthIdBinding> {


    private Uri cropUri;

    private BottomSheetDialog avatarBottomSheet;

    private Consumer<Integer> avatarConsumer;

    private boolean picIDFaceFlag, picIDFlag = false;

    private String picIDPath, picIDFacePath;

    private TipDialog mtipDialog, delIDFaceDialog, delIDDialog;

    private String picIdUrl, picIdFaceUrl;


    private UploadAvatarReq picIDReq, picIDFaceReq;

    private MValue.AUTH_ID_FROM from;

    private TipCustomDialog tipCustomDialog;

    @Inject
    CheckUtils checkUtils;

    @Inject
    LocationUtils locationUtils;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.tvRight.setText(getString(R.string.after_auth));
        binding.includeToolbar.tvRight.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_auth_id));
    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);

        locationUtils.requestLocation();

        from = (MValue.AUTH_ID_FROM) getIntent().getExtras().get(MKey.FROM);

        //单纯的身份认证
        if (from == MValue.AUTH_ID_FROM.USER_AUTH) {
            //不显示稍后认证
            binding.includeToolbar.setShowRight(false);
            if (UserService.getInstance().getUser().getUser_auth_status() != null && UserService.getInstance().getUser().getUser_auth_status() == MValue.AUTH_STATUS_ING) {
                binding.ivId.getRoot().setEnabled(false);
                binding.ivIdface.getRoot().setEnabled(false);
                binding.btnBottom.setEnabled(false);
                binding.btnBottom.setText(getString(R.string.ver_ing));
            }
        } else if (from == MValue.AUTH_ID_FROM.EDIT) {
            binding.includeToolbar.setShowRight(false);
            binding.includeToolbar.tvRight.setVisibility(View.GONE);
            binding.btnBottom.setSelected(true);
            binding.btnBottom.setText(getString(R.string.save));
        } else if (from == MValue.AUTH_ID_FROM.SERVICE_AUTH) {
            binding.includeToolbar.setShowRight(false);
            binding.includeToolbar.tvRight.setVisibility(View.GONE);
            binding.btnBottom.setSelected(true);
            binding.btnBottom.setText(getString(R.string.commit_authid));
        }


        q_user_auth();

    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            showExitDialog();
        });


        avatarConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                avatarBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        PictureSelector.create(AuthIDActivity.this)
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
                        PictureSelector.create(AuthIDActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(false)
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
        binding.ivIdface.getRoot().setOnClickListener((v) -> {
            picIDFaceFlag = true;
            picIDFlag = false;
            cropUri = ImageUtils.getCropPhotoUri();
            if (avatarBottomSheet == null) {
                avatarBottomSheet = DialogUtils.getAvatarBottomSheet(AuthIDActivity.this, avatarConsumer);
            }
            avatarBottomSheet.show();
        });

        binding.ivId.getRoot().setOnClickListener((v) -> {
            picIDFaceFlag = false;
            picIDFlag = true;
            cropUri = ImageUtils.getCropPhotoUri();
            if (avatarBottomSheet == null) {
                avatarBottomSheet = DialogUtils.getAvatarBottomSheet(AuthIDActivity.this, avatarConsumer);
            }
            avatarBottomSheet.show();
        });

        binding.ivIdface.ivClose.setOnClickListener((v) -> {


            if (delIDFaceDialog == null) {
                delIDFaceDialog = new TipDialog.Builder(this, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            binding.ivIdface.getRoot().setEnabled(true);
                            binding.ivIdface.ivImg.setVisibility(View.INVISIBLE);
                            binding.ivIdface.ivClose.setVisibility(View.INVISIBLE);
                        }
                    }
                }, getString(R.string.del_tip)).create();
            }

            delIDFaceDialog.show();


        });

        binding.ivId.ivClose.setOnClickListener((v) -> {

            if (delIDDialog == null) {
                delIDDialog = new TipDialog.Builder(this, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            binding.ivId.getRoot().setEnabled(true);
                            binding.ivId.ivImg.setVisibility(View.INVISIBLE);
                            binding.ivId.ivClose.setVisibility(View.INVISIBLE);
                        }
                        checkBottom();
                    }
                }, getString(R.string.del_tip)).create();
            }

            delIDDialog.show();


        });

        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            if (mtipDialog == null) {
                mtipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {

                            IntentUtils.toAuthCommitActivity(AuthIDActivity.this);
                        }
                    }
                }, getString(R.string.after_auth_tips)).create();
                checkBottom();
            }
            mtipDialog.show();
        });

        binding.btnBottom.setOnClickListener((v) -> {
            if (StringUtils.isEmpty(picIDPath) && picIDReq == null) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_id), true);
                tipDialog.show();
                return;
            }
            if (StringUtils.isEmpty(picIDFacePath) && picIDFaceReq == null) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_idface), true);
                tipDialog.show();
                return;
            }
            doUPload();
        });

    }

    private void doUPload() {
        UploadService.doUploadAvatar(picIDPath, new Observer<UploadAvatarReq>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UploadAvatarReq req) {
                picIDReq = req;
                if (picIDFaceReq != null && req != null) {


                    commitAuth();
                }
            }

            @Override
            public void onError(Throwable e) {
                CrashReport.postCatchedException(e);
            }

            @Override
            public void onComplete() {

            }
        });
        UploadService.doUploadAvatar(picIDFacePath, new Observer<UploadAvatarReq>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UploadAvatarReq req) {
                picIDFaceReq = req;
                if (picIDReq != null && req != null) {
                    commitAuth();
                }
            }

            @Override
            public void onError(Throwable e) {
                CrashReport.postCatchedException(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void commitAuth() {
        params = SignUtils.getNormalParams();
        params.put(MKey.ID_FACE, JSON.toJSONString(picIDReq));
        params.put(MKey.HAND_ID_FACE, JSON.toJSONString(picIDFaceReq));
        params.put(MKey.TYPE, from.toString());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().user_auth(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserAuthIDBean>() {
            @Override
            public void onSuccess(BaseBean<UserAuthIDBean> baseBean) {

                UserAuthIDBean userAuthIDBean = baseBean.getData();

                if (userAuthIDBean != null) {

                    if (from == MValue.AUTH_ID_FROM.EDIT) {
                        IntentUtils.toAuthCommitActivity(AuthIDActivity.this);
                        finish();
                    } else if (from == MValue.AUTH_ID_FROM.SERVICE_AUTH) {

                        if (userAuthIDBean.getService_auth_status() != null) {
                            UserService.getInstance().getUser().setService_auth_status(userAuthIDBean.getService_auth_status());
                        }
                        RxBus.getInstance().post(NormalEvent.FINISH_AUTH_INDEX_PAGE);
                        RxBus.getInstance().post(NormalEvent.FINISH_AUTH_WELCOME_PAGE);
//                        IntentUtils.toAuthIndexActivity(AuthIDActivity.this, RecordVideoFrom.AUTH);

                        tipDialog = DialogUtils.getSuclDialog(AuthIDActivity.this, baseBean.getMessage(), true);
                        tipDialog.show();
                        tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                IntentUtils.toAuthIndexActivity(AuthIDActivity.this, RecordVideoFrom.AUTH);
                                finish();
                            }
                        });


                    } else {
                        UserService.getInstance().getUser().setUser_auth_status(userAuthIDBean.getUser_auth_status());
                        RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
                        finish();
                    }


                }
            }

            @Override
            public void onError(BaseBean<UserAuthIDBean> baseBean) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(AuthIDActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });


//        HttpClient.Builder.getGuodongServer().user_auth(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserAuthIDBean>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                addDisposable(disposable);
//            }
//
//            @Override
//            public void onSuccess(UserAuthIDBean userAuthIDBean) {
//
//                if (userAuthIDBean != null) {
//
//                    if (from == MValue.AUTH_ID_FROM.EDIT) {
//                        IntentUtils.toAuthCommitActivity(AuthIDActivity.this);
//                        finish();
//                    } else if (from == MValue.AUTH_ID_FROM.SERVICE_AUTH) {
//
//                        if (userAuthIDBean.getService_auth_status() != null) {
//                            UserService.getInstance().getUser().setService_auth_status(userAuthIDBean.getService_auth_status());
//                        }
//                        RxBus.getInstance().post(NormalEvent.FINISH_AUTH_INDEX_PAGE);
////                        IntentUtils.toAuthIndexActivity(AuthIDActivity.this, RecordVideoFrom.AUTH);
//
//                        tipDialog = DialogUtils.getSuclDialog(this,userAuthIDBean)
//
//                        finish();
//                    } else {
//                        UserService.getInstance().getUser().setUser_auth_status(userAuthIDBean.getUser_auth_status());
//                        RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
//                        finish();
//                    }
//
//
//                }
//            }
//
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onError(BaseBean<UserAuthIDBean> basebean) {
//
//            }
//
//        }));


    }

    private void checkBottom() {
        if (binding.ivIdface.ivImg.getVisibility() == View.VISIBLE && binding.ivId.ivImg.getVisibility() == View.VISIBLE
                && (!StringUtils.isEmpty(picIDPath) || !StringUtils.isEmpty(picIDFacePath))
                ) {
            checkUtils.setBottomEnable(binding.btnBottom, true);
        } else {
            checkUtils.setBottomEnable(binding.btnBottom, false);
        }
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
        if (picIDFlag) {
            if (localMedia == null) {
                picIDFlag = false;
                return;
            }

            picIDPath = localMedia.getPath();
            ImageUtils.showImage(binding.ivId.ivImg, picIDPath);
            binding.ivId.getRoot().setEnabled(false);
            binding.ivId.ivImg.setVisibility(View.VISIBLE);
            binding.ivId.ivClose.setVisibility(View.VISIBLE);
            checkBottom();
            picIDFlag = false;
        } else if (picIDFaceFlag) {
            if (localMedia == null) {
                picIDFaceFlag = false;
                return;
            }
            picIDFacePath = localMedia.getPath();
            ImageUtils.showImage(binding.ivIdface.ivImg, picIDFacePath);
            binding.ivIdface.getRoot().setEnabled(false);
            binding.ivIdface.ivImg.setVisibility(View.VISIBLE);
            binding.ivIdface.ivClose.setVisibility(View.VISIBLE);
            checkBottom();
            picIDFaceFlag = false;
        }
    }

    private void q_user_auth() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().q_user_auth(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserAuthQBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(UserAuthQBean userAuthQBean) {
                if (userAuthQBean != null && userAuthQBean.getConfig() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageUtils.showImage(binding.ivIdface.ivImg, userAuthQBean.getConfig().getHand_id_face());
                            ImageUtils.showImage(binding.ivId.ivImg, userAuthQBean.getConfig().getId_face());
                        }
                    });
                    if (from.equals(MValue.AUTH_ID_FROM.USER_AUTH) && UserService.getInstance().getUser().getUser_auth_status() != null && UserService.getInstance().getUser().getUser_auth_status() == MValue.AUTH_STATUS_ING) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.ivIdface.ivClose.setVisibility(View.INVISIBLE);
                                binding.ivId.ivClose.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        if (!StringUtil.isEmpty(userAuthQBean.getConfig().getHand_id_face())) {
                            binding.ivIdface.ivClose.setVisibility(View.VISIBLE);
                        }
                        if (!StringUtil.isEmpty(userAuthQBean.getConfig().getId_face())) {
                            binding.ivId.ivClose.setVisibility(View.VISIBLE);
                        }

                    }

                }
            }

            @Override
            public void onError(BaseBean<UserAuthQBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(AuthIDActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }

            @Override
            public void onError(Throwable throwable) {
            }

        }));
    }

    private void showExitDialog() {

        if (!StringUtils.isEmpty(picIDFacePath) || !StringUtils.isEmpty(picIDPath)) {
            tipCustomDialog = new TipCustomDialog.Builder(this, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        finish();
                    }
                }
            }, getString(R.string.auth_id_no_save_tip), getString(R.string.mcancel), getString(R.string.abandon)).create();
            tipCustomDialog.show();
        } else {
            finish();
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        DialogUtils.dimissDialog(tipDialog);
        DialogUtils.dimissDialog(delIDDialog);
        DialogUtils.dimissDialog(delIDFaceDialog);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_id;
    }
}
