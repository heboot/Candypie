package com.gdlife.candypie.activitys.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.TagsDialogType;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityUserInfoBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.serivce.UserInfoService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.dialog.ChooseTagsDialog;
import com.gdlife.candypie.widget.homepage.HomepageSelectHeightDialog;
import com.gdlife.candypie.widget.homepage.HomepageSelectKGDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.auth.SubmitAuthBean;
import com.heboot.bean.user.PreEditMeetTagsBean;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.bean.user.UserInfoEditBean;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.req.UploadAvatarReq;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DateUtil;
import com.heboot.utils.MStatusBarUtils;
import com.heboot.utils.ViewUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.UserEvent.UPDATE_USER_INFO_BY_MEET_TAG;

/**
 * Created by heboot on 2018/2/27.
 */

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {

    private User user;

    private String from;

    private HomepageSelectHeightDialog heightDialog;
    private HomepageSelectHeightDialog.Builder heightBuilder;

    private HomepageSelectKGDialog kgDialog;
    private HomepageSelectKGDialog.Builder kgBuilder;

    private Consumer<String> kgConsumer, heightConsumer;

    private TimePickerView pvTime;

    private String birthdayDate;

    private BottomSheetDialog avatarBottomSheet;

    private Consumer<Integer> avatarConsumer;

    private Uri cropUri;

    private String tempAbst;

    private QMUITipDialog loadingDialog;

    private String height, weight;

    private ChooseTagsDialog chooseTagsDialog;

    private Consumer<List<TagsChildBean>> consumer;

    @Inject
    CheckUtils checkUtils;

    private BottomSheetDialog bottomSheetDialog;

    // TODO: 2018/7/12 dagger update
    private UserInfoService userInfoService = new UserInfoService();
    private UIService uiService = new UIService();

    /**
     * 接口返回的约会态度数据
     */
    private List<TagsChildBean> currentSelectedMeetTags;


    //认证过来的还是普通查看过来的
    private String type;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_userinfo));
    }

    @Override
    public void initData() {

        DaggerUtilsComponent.builder().build().inject(this);

        from = getIntent().getExtras().getString(MKey.FROM);

        type = getIntent().getExtras().getString(MKey.TYPE);

        user = (User) getIntent().getExtras().get(MKey.USER);

        if (type.equals(MValue.USER_INFO_TYPE_AUTH)) {
            doEdit(true);
            binding.includeAvatar.getRoot().setVisibility(View.GONE);
            binding.lineAvatar.setVisibility(View.INVISIBLE);
            binding.lineNick.setVisibility(View.INVISIBLE);
            binding.vEdit.setVisibility(View.GONE);
            binding.vEdit.setEnabled(false);
            binding.btnBottom.setText(getString(R.string.mnext));
        }

        binding.setFrom(from);

        if (user == null) {
            user = UserService.getInstance().getUser();
        }

        tempAbst = user.getAbst();

        binding.includeUserinfo.setUser(user);
        binding.includeUserinfo.includeSexage.setUser(user);
        binding.includeVer.setUser(user);


        if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
            binding.ivMore.setVisibility(View.INVISIBLE);
            binding.vMore.setVisibility(View.GONE);
            binding.includeVer.getRoot().setVisibility(View.VISIBLE);
            binding.v3.setVisibility(View.VISIBLE);
            binding.ivChat.setVisibility(View.GONE);
            binding.includeChattip.getRoot().setVisibility(View.GONE);
            initVisitLayout();
        } else {
            binding.ivMore.setVisibility(View.VISIBLE);
            binding.vMore.setVisibility(View.VISIBLE);
            binding.includeVer.getRoot().setVisibility(View.GONE);
            binding.v3.setVisibility(View.GONE);
            binding.ivChat.setVisibility(View.VISIBLE);
            binding.includeChattip.getRoot().setVisibility(View.VISIBLE);
        }


        initUserInfo();

    }


    private void setUserUI() {

        binding.includeUserinfo.ivEdit.setVisibility(View.GONE);

        binding.includeUserinfo.setUser(user);
        binding.includeUserinfo.includeSexage.setUser(user);
        binding.includeVer.setUser(user);

        if (!StringUtils.isEmpty(user.getHeight())) {
            height = user.getHeight();
        }
        if (!StringUtils.isEmpty(user.getWeight())) {
            weight = user.getWeight();
        }

        birthdayDate = user.getBirthday();

        //自己看自己
        if (from.equals(MValue.FROM_MY) || from.equals(MValue.USER_INFO_TYPE_AUTH_COMMIT)) {
            binding.includeVer.includeTitle.setTitle(getString(R.string.ver_info_my));
            binding.includeUserinfo.setUser(user);
            binding.includeUserinfo.includeSexage.setUser(user);
//            if (!StringUtils.isEmpty(user.getHeight()) && user.getHeight().indexOf(getString(R.string.unit_height)) > -1) {
//                height = user.getHeight().replace(getString(R.string.unit_height), "");
//            }
//            if (!StringUtils.isEmpty(user.getWeight()) && user.getWeight().indexOf(getString(R.string.unit_kg)) > -1) {
//                weight = user.getWeight().replace(getString(R.string.unit_kg), "");
//            }
        }
        //看别人
        else {
            binding.includeVer.includeTitle.setTitle(getString(R.string.ver_info_other));
        }


        ImageUtils.showImage(binding.includeUserinfo.avatar, user.getAvatar());

        binding.includeAge.includeTitle.setTitle(getString(R.string.age_info));

        binding.includeVer.tvVerService.setText(getString(R.string.ver_info_servicer));

//        binding.includeVer.tvVerId.setText(getString(R.string.ver_info_id));

        binding.includeHeight.includeTitle.setTitle(getString(R.string.height_info));

        binding.includeKg.includeTitle.setTitle(getString(R.string.kg_info));

        binding.includeSignature.includeTitle.setTitle(getString(R.string.infop_signature));

        binding.includeSignatureEdit.tvRight.setText(StringUtils.isEmpty(user.getAbst()) ? getString(R.string.input_abst) : user.getAbst());
        binding.includeSignature.includeContent.setTitle(StringUtils.isEmpty(user.getAbst()) ? getString(R.string.input_abst) : user.getAbst());
        if (!StringUtils.isEmpty(user.getAbst())) {
            binding.includeSignatureEdit.tvRight.setSelected(true);
        }
        binding.includeSignatureEdit.includeTitle.setTitle(getString(R.string.infop_signature));

        if (StringUtils.isEmpty(user.getBirthday())) {
            binding.includeAge.setContent(getString(R.string.un_input));
            binding.includeAge.tvRight.setSelected(false);
        } else {
            binding.includeAge.setContent(UserService.getUserAgeByBirthday(user));
            binding.includeAge.tvRight.setSelected(true);
        }


        if (StringUtils.isEmpty(user.getWeight())) {
            binding.includeHeight.setContent(getString(R.string.un_input));
            binding.includeHeight.tvRight.setSelected(false);
        } else {
            binding.includeHeight.setContent(user.getHeight() + getString(R.string.unit_height));
            binding.includeHeight.tvRight.setSelected(true);
        }


        if (StringUtils.isEmpty(user.getWeight())) {
            binding.includeKg.setContent(getString(R.string.un_input));
            binding.includeKg.tvRight.setSelected(false);
        } else {
            binding.includeKg.setContent(user.getWeight() + getString(R.string.unit_kg));
            binding.includeKg.tvRight.setSelected(true);
        }


        binding.includeNick.includeTitle.setTitle(getString(R.string.nick));
        binding.includeNick.setContent(user.getNickname());
        binding.includeAvatar.includeTitle.setTitle(getString(R.string.avatar));
        ImageUtils.showAvatar(binding.includeAvatar.tvRight, user.getAvatar());


        if (type.equals(MValue.USER_INFO_TYPE_EDIT)
                || type.equals(MValue.USER_INFO_TYPE_AUTH)
                || type.equals(MValue.USER_INFO_TYPE_AUTH_COMMIT)
                ) {
            doEdit(true);
//                binding.includeMeetTag.getRoot().setVisibility(View.VISIBLE);
//                initMeetTags();
            binding.includeMeetTag.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.includeMeetTag.getRoot().setVisibility(View.GONE);
        }

        initVisitLayout();
    }


    private void initMeetTags() {


        /**
         * meet tags
         */
        binding.includeMeetTag.tvTitle.setText(user.getMeet_tags().getTitle());
        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
            binding.includeMeetTag.tvArrow.setVisibility(View.VISIBLE);
            binding.includeMeetTag.tvRight.setVisibility(View.VISIBLE);
            initTagsView();

        } else {
            if (user.getMeet_tags() != null && user.getMeet_tags().getSelect_items().size() > 0) {
                binding.includeMeetTag.getRoot().setVisibility(View.VISIBLE);
                initTagsView();

            } else {
                binding.includeMeetTag.getRoot().setVisibility(View.GONE);
            }
            binding.includeMeetTag.tvArrow.setVisibility(View.GONE);
            binding.includeMeetTag.tvRight.setVisibility(View.GONE);
        }


    }

    private void initTagsView() {
        if (currentSelectedMeetTags != null) {
            uiService.initMeetSelectedTagsLayout(currentSelectedMeetTags, binding.includeMeetTag.qfytContainer, getResources().getDimensionPixelOffset(R.dimen.y10), getResources().getDimensionPixelOffset(R.dimen.x10));
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
                if (o instanceof UserEvent.UserInfoInputEvent) {
                    UserEvent.UserInfoInputEvent event = (UserEvent.UserInfoInputEvent) o;
                    if (!StringUtils.isEmpty(event.getContent())) {
                        binding.includeSignatureEdit.tvRight.setText(event.getContent());
                        binding.includeSignatureEdit.tvRight.setSelected(true);
                        tempAbst = event.getContent();
                    }
                } else if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
                    finish();
                } else if (o.equals(UserEvent.UPDATE_PROFILE)) {
                    user = UserService.getInstance().getUser();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.includeUserinfo.setUser(user);
                            binding.includeUserinfo.includeSexage.setUser(user);
                            binding.includeVer.setUser(user);
                        }
                    });
                } else if (o.equals(UPDATE_USER_INFO_BY_MEET_TAG)) {
                    initUserInfo();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        consumer = new Consumer<List<TagsChildBean>>() {
            @Override
            public void accept(List<TagsChildBean> tagsChildBeans) throws Exception {
                if (tagsChildBeans != null) {
                    currentSelectedMeetTags = tagsChildBeans;
                    initTagsView();
                }

            }
        };


        avatarConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                avatarBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        PictureSelector.create(UserInfoActivity.this)
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
                        PictureSelector.create(UserInfoActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(true)
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

        kgConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                weight = s;
                binding.includeKg.tvRight.setSelected(true);
                binding.includeKg.setContent(s + MAPP.mapp.getString(R.string.unit_kg));

            }
        };

        heightConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                height = s;
                binding.includeHeight.tvRight.setSelected(true);
                binding.includeHeight.setContent(s + MAPP.mapp.getString(R.string.unit_height));
            }
        };

        binding.includeAge.getRoot().setOnClickListener((v) -> {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            //startDate.set(2013,1,1);
            Calendar endDate = Calendar.getInstance();
            //endDate.set(2020,1,1);

            //正确设置方式 原因：注意事项有说明
            startDate.set(ConfigService.getInstance().getMaxAgeYear(), 0, 1);
            endDate.set(ConfigService.getInstance().getMinAgeYear(), 12, 31);

            if (pvTime == null) {

                //时间选择器
                pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        try {
                            birthdayDate = DateUtil.date2Str(date, DateUtil.FORMAT_YMD);
                            binding.includeAge.setContent(DateUtil.getCurrentAgeByBirthdate(date) + MAPP.mapp.getString(R.string.age_unit));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("年", "月", "日", null, null, null)
                        .setContentSize(22)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setRangDate(startDate, endDate).build();
            }
            pvTime.setDate(selectedDate);//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
            pvTime.show();
        });

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.vEdit.setOnClickListener((v) -> {
            doEdit(true);
        });

        binding.btnBottom.setOnClickListener((v) -> {
            editUserInfo();


//            if (type.equals(MValue.USER_INFO_TYPE_AUTH)) {
//                if (from.equals(MValue.USER_INFO_TYPE_AUTH_COMMIT)) {
//                    finish();
//                } else {
//                    IntentUtils.toAuthCommitActivity(this);
//                }
//
//            } else {
//                editUserInfo();
//            }

        });

        binding.includeKg.getRoot().setOnClickListener((v) -> {
            if (kgDialog == null) {
                if (kgBuilder == null) {
                    kgBuilder = new HomepageSelectKGDialog.Builder(UserInfoActivity.this, kgConsumer);
                }
                kgDialog = kgBuilder.create();
            }
            kgDialog.show();
        });

        binding.includeHeight.getRoot().setOnClickListener((v) -> {
            if (heightDialog == null) {
                if (heightBuilder == null) {
                    heightBuilder = new HomepageSelectHeightDialog.Builder(UserInfoActivity.this, heightConsumer);
                }
                heightDialog = heightBuilder.create();
            }
            heightDialog.show();
        });

        binding.includeSignatureEdit.getRoot().setOnClickListener((v) -> {
            IntentUtils.toUserInfoInputActivity(UserInfoActivity.this, tempAbst);
        });

        binding.includeAvatar.getRoot().setOnClickListener((v) -> {
            cropUri = ImageUtils.getCropPhotoUri();
            if (avatarBottomSheet == null) {
                avatarBottomSheet = DialogUtils.getAvatarBottomSheet(UserInfoActivity.this, avatarConsumer);
            }
            avatarBottomSheet.show();
        });

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

//        binding.includeVer.ivVerIdentity.setOnClickListener((v) -> {
//
//            if (user.getId().intValue() != UserService.getInstance().getUser().getId().intValue()) {
//                return;
//            }
//
//            if (user.getUser_auth_status() == null || user.getUser_auth_status() == MValue.AUTH_STATUS_ING) {
//                return;
//            }
//            if (user.getUser_auth_status() != null && user.getUser_auth_status() != MValue.AUTH_STATUS_SUC) {//&& user.getService_auth_status() != null && user.getService_auth_status() != MValue.AUTH_STATUS_SUC
//                IntentUtils.toAuthIDActivity(this, MValue.AUTH_ID_FROM.USER_AUTH);
//            }
//        });

        binding.includeVer.ivVerService.setOnClickListener((v) -> {

            if (user.getId().intValue() != UserService.getInstance().getUser().getId().intValue()) {
                return;
            }

            /**
             * 认证中 不可点
             */
            if (user.getService_auth_status() == null || user.getService_auth_status() == MValue.AUTH_STATUS_ING) {
                return;
            }
            AuthService.toAuthPageByIndex(v.getContext());
        });


        binding.vMore.setOnClickListener((v) -> {
            bottomSheetDialog = DialogUtils.getHomepageBottomSheet(this, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        bottomSheetDialog.dismiss();
                        params = SignUtils.getNormalParams();
                        params.put(MKey.BLACK_UID, user.getId());
                        String sign = SignUtils.doSign(params);
                        params.put(MKey.SIGN, sign);
                        if (user.getIs_black() == 1) {
                            HttpClient.Builder.getGuodongServer().un_black(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, baseBean.getMessage(), true);
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
                                    tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        } else {
                            HttpClient.Builder.getGuodongServer().black_user(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, baseBean.getMessage(), true);
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
                                    tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        }


                    } else if (integer == 0) {
                        bottomSheetDialog.dismiss();
                        IntentUtils.toReportActivity(UserInfoActivity.this, String.valueOf(user.getId()), ReportFromType.REPROT);
                    }
                }
            }, user.getIs_black() == 1);

            bottomSheetDialog.show();

        });


        binding.ivChat.setOnClickListener((v) -> {
            IntentUtils.intent2ChatActivity(this, String.valueOf(user.getId()));
        });

        binding.includeMeetTag.getRoot().setOnClickListener((v) -> {
            List<String> selectedIds = new ArrayList<>();
            selectedIds = UserInfoService.getSelectedIds(currentSelectedMeetTags);
            chooseTagsDialog = new ChooseTagsDialog.Builder(this, user.getMeet_tags().getTip(), uiService, user.getMeet_tags().getItems(), consumer, user.getMeet_tags().getTitle(), TagsDialogType.CHOOSE_MEET, selectedIds).create();

            chooseTagsDialog.show();
        });

        binding.includeChattip.getRoot().setOnClickListener((v) -> {
            binding.includeChattip.getRoot().setVisibility(View.GONE);
        });
    }

    private void initUserInfo() {
        userInfoService.initUserInfo(user, new HttpObserver<UserInfoBean>() {
            @Override
            public void onSuccess(BaseBean<UserInfoBean> baseBean) {
                UserInfoBean sendSMSBeanBaseBean = baseBean.getData();
                binding.plytContainer.toMain();
                user = sendSMSBeanBaseBean.getUser_profile();
                currentSelectedMeetTags = user.getMeet_tags().getSelect_items();
                tempAbst = user.getAbst();
                if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                    UserService.getInstance().setUser(sendSMSBeanBaseBean.getUser_profile());
                }

                setUserUI();
            }

            @Override
            public void onError(BaseBean<UserInfoBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                tipDialog.show();
            }
        });
    }

    private void editUserInfo() {
        params = SignUtils.getNormalParams();

        if (type.equals(MValue.USER_INFO_TYPE_AUTH) || type.equals(MValue.USER_INFO_TYPE_AUTH_COMMIT) || UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
            if (StringUtils.isEmpty(user.getUpdate_avatar())) {
                tipDialog = DialogUtils.getFailDialog(this, "请先上传头像", true);
                tipDialog.show();
                return;
            }
            if (StringUtils.isEmpty(birthdayDate) && StringUtils.isEmpty(user.getBirthday())) {
                tipDialog = DialogUtils.getFailDialog(this, "请选择生日", true);
                tipDialog.show();
                return;
            }
            if (StringUtils.isEmpty(height) || !binding.includeHeight.tvRight.isSelected()) {
                tipDialog = DialogUtils.getFailDialog(this, "请选择身高", true);
                tipDialog.show();
                return;
            }
            if (StringUtils.isEmpty(weight) || !binding.includeKg.tvRight.isSelected()) {
                tipDialog = DialogUtils.getFailDialog(this, "请选择体重", true);
                tipDialog.show();
                return;
            }
            if (StringUtils.isEmpty(binding.includeSignatureEdit.tvRight.getText()) || !binding.includeSignatureEdit.tvRight.isSelected()) {
                tipDialog = DialogUtils.getFailDialog(this, "请输入个性签名", true);
                tipDialog.show();
                return;
            }
            if (!checkUtils.checkAbst(binding.includeSignatureEdit.tvRight.getText().toString())) {
                String result = MAPP.mapp.getString(R.string.input_tip_abst);
                String result2 = String.format(result, ConfigService.getInstance().getAbstMinLength() + "", ConfigService.getInstance().getAbstMaxLength() + "");
                tipDialog = DialogUtils.getFailDialog(this, result2, true);
                tipDialog.show();
                return;
            }

        }


        params.put(MKey.NICK_NAME, binding.includeNick.tvRight.getText().toString());

        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
        loadingDialog.show();
        HttpClient.Builder.getGuodongServer().user_check_nick(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(BaseBeanEntity o) {
                params = SignUtils.getNormalParams();
                params.put(MKey.NICK_NAME, binding.includeNick.tvRight.getText().toString());
                if (!StringUtils.isEmpty(birthdayDate)) {
                    params.put(MKey.BIRTHDAY, birthdayDate);
                }
                if (!StringUtils.isEmpty(height) && binding.includeHeight.tvRight.isSelected()) {
                    params.put(MKey.HEIGHT, height);
                }
                if (!StringUtils.isEmpty(weight) && binding.includeKg.tvRight.isSelected()) {
                    params.put(MKey.WEIGHT, weight);
                }
                if (!StringUtils.isEmpty(binding.includeSignatureEdit.tvRight.getText()) && binding.includeSignatureEdit.tvRight.isSelected()) {
                    params.put(MKey.ABST, binding.includeSignatureEdit.tvRight.getText().toString());
                }


                if (currentSelectedMeetTags != null && currentSelectedMeetTags.size() > 0) {
                    String ids = "";

                    for (String s : UserInfoService.getSelectedIds(currentSelectedMeetTags)) {
                        ids = ids + s + ",";
                    }
                    params.put(MKey.TAG_IDS, ids);
                }


                String sign = SignUtils.doSign(params);
                params.put(MKey.SIGN, sign);

                HttpClient.Builder.getGuodongServer().edit_profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserInfoEditBean>() {

                    @Override
                    public void onSuccess(BaseBean<UserInfoEditBean> baseBean) {
                        UserInfoEditBean sendSMSBeanBaseBean = baseBean.getData();
                        UserService.getInstance().setUser(sendSMSBeanBaseBean.getUser());
                        if (from.equals(MValue.FROM_MY)) {
                            RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
                        }
                        if (from.equals(MValue.USER_INFO_TYPE_AUTH_COMMIT)) {
                            finish();
                        } else if (from.equals(MValue.USER_INFO_TYPE_AUTH)) {
//                            IntentUtils.toAuthIDActivity(UserInfoActivity.this, MValue.AUTH_ID_FROM.SERVICE_AUTH);
//                            finish();
//                            tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, "提交成功", true);
//                            tipDialog.show();
//                            finish();
                            doCommit();
                        } else {
                            user = sendSMSBeanBaseBean.getUser();
                            loadingDialog.dismiss();
                            tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, "修改成功", true);
                            tipDialog.show();
                            doEdit(false);
                            setUserUI();
                        }
                    }

                    @Override
                    public void onError(BaseBean<UserInfoEditBean> baseBean) {
                        if (tipDialog != null) {
                            tipDialog.dismiss();
                        }
                        tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                        tipDialog.show();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                loadingDialog.dismiss();
                loadingDialog = DialogUtils.getFailDialog(UserInfoActivity.this, throwable.getMessage(), true);
                loadingDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }
        }));

    }

    private void doCommit() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().submit_auth(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<SubmitAuthBean>() {
            @Override
            public void onSuccess(BaseBean<SubmitAuthBean> baseBean) {
                SubmitAuthBean submitAuthBean = baseBean.getData();
                tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, getString(R.string.commit_auth_suc), true);
                UserService.getInstance().getUser().setUser_auth_status(submitAuthBean.getUser_auth_status());
                UserService.getInstance().getUser().setService_auth_status(submitAuthBean.getService_auth_status());
                tipDialog.show();
                IntentUtils.toAuthIndexActivity(UserInfoActivity.this, RecordVideoFrom.AUTH);
                RxBus.getInstance().post(NormalEvent.FINISH_AUTH_WELCOME_PAGE);
                finish();
            }

            @Override
            public void onError(BaseBean<SubmitAuthBean> baseBean) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }

    private void initVisitLayout() {
        if (!type.equals(MValue.USER_INFO_TYPE_EDIT) && !type.equals(MValue.USER_INFO_TYPE_AUTH) && !type.equals(MValue.USER_INFO_TYPE_AUTH_COMMIT) && user.getVisit_list() != null && user.getVisit_list().getList() != null && user.getVisit_list().getList().size() > 0) {
            userInfoService.initUserVisitLayout(binding.includeIndexVisit, user);
            binding.vVisit.setVisibility(View.VISIBLE);
        } else {
            binding.vVisit.setVisibility(View.GONE);
            binding.includeIndexVisit.getRoot().setVisibility(View.GONE);
        }

    }


    private void doEdit(boolean dof) {
        //编辑状态下 把最近来访隐藏
        binding.includeIndexVisit.getRoot().setVisibility(dof ? View.GONE : View.VISIBLE);
        //服务者编辑状态下 或者去认证流程下 把约会态度显示
        if (dof) {
            binding.includeMeetTag.tvTitle.setText(user.getMeet_tags().getTitle());
            binding.includeMeetTag.getRoot().setVisibility(View.VISIBLE);
            if (currentSelectedMeetTags != null && currentSelectedMeetTags.size() > 0) {
                initMeetTags();
            }
        } else {
            binding.includeMeetTag.getRoot().setVisibility(View.GONE);
        }


        binding.vEdit.setVisibility(dof ? View.GONE : View.VISIBLE);
        binding.ivEdit.setVisibility(dof ? View.GONE : View.VISIBLE);
        binding.v3.setVisibility(dof ? View.GONE : View.VISIBLE);
        binding.includeVer.getRoot().setVisibility(dof ? View.GONE : View.VISIBLE);
        binding.includeUserinfo.getRoot().setVisibility(dof ? View.GONE : View.VISIBLE);
        binding.includeSignature.getRoot().setVisibility(dof ? View.GONE : View.VISIBLE);
        binding.v2.setVisibility(dof ? View.GONE : View.VISIBLE);

        binding.includeSignatureEdit.getRoot().setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.v2AbstEdit.setVisibility(dof ? View.VISIBLE : View.GONE);

//        binding.lineAvatar.setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.includeAvatar.getRoot().setVisibility(dof ? View.VISIBLE : View.GONE);

        binding.lineNick.setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.includeNick.getRoot().setVisibility(dof ? View.VISIBLE : View.GONE);

        binding.includeAge.getRoot().setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.lineAge.setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.includeHeight.getRoot().setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.lineHeight.setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.includeKg.getRoot().setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.lineKg.setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.v4.setVisibility(dof ? View.VISIBLE : View.GONE);
        binding.btnBottom.setVisibility(dof ? View.VISIBLE : View.GONE);
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
        if (localMedia == null) {
            return;
        }
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
        loadingDialog.show();

        ImageUtils.showImage(binding.includeAvatar.tvRight, localMedia.getCutPath());

        UploadService.doUploadAvatar(localMedia.getCutPath(), new Observer<UploadAvatarReq>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(UploadAvatarReq uploadAvatarReq) {
                if (uploadAvatarReq != null) {
                    updateAvatar(uploadAvatarReq);
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.dismiss();
            }

            @Override
            public void onComplete() {

            }
        });

    }


    private void updateAvatar(UploadAvatarReq req) {
        params = SignUtils.getNormalParams();

        params.put(MKey.AVATAR, JSON.toJSONString(req));
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().upload_avatar(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserInfoEditBean>() {
            @Override
            public void onSuccess(BaseBean<UserInfoEditBean> baseBean) {
                user.setUpdate_avatar("test");
                loadingDialog.dismiss();
                tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<UserInfoEditBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });

//        HttpClient.Builder.getGuodongServer().upload_avatar(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserInfoEditBean>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                addDisposable(disposable);
//            }
//
//            @Override
//            public void onSuccess(UserInfoEditBean sendSMSBeanBaseBean) {
//
//                UserService.getInstance().setUser(sendSMSBeanBaseBean.getUser());
//                RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
//
//                loadingDialog.dismiss();
//                tipDialog = DialogUtils.getSuclDialog(UserInfoActivity.this, "修改成功", true);
//                tipDialog.show();
//            }
//
//
//            @Override
//            public void onError(Throwable throwable) {
//                loadingDialog.dismiss();
//                loadingDialog = DialogUtils.getFailDialog(UserInfoActivity.this, throwable.getMessage(), true);
//                loadingDialog.show();
//            }
//
//            @Override
//            public void onError(BaseBean<UserInfoEditBean> basebean) {
//                if (tipDialog != null && tipDialog.isShowing()) {
//                    tipDialog.dismiss();
//                }
//
//                tipDialog = DialogUtils.getFailDialog(UserInfoActivity.this, basebean.getMessage(), true);
//                tipDialog.show();
//            }
//
//
//        }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }
}
