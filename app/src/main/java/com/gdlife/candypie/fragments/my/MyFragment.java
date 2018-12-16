package com.gdlife.candypie.fragments.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.faceunity.FURenderer;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.auth.AuthCommitActivity;
import com.gdlife.candypie.activitys.user.UserInfoActivity;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.adapter.my.MyBottomMenuAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.databinding.FragmentMyBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.UploadService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VerService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.GlideImageLoaderByIndexTop;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.bean.me.MeDataBean;
import com.heboot.bean.user.UserInfoEditBean;
import com.heboot.entity.User;
import com.heboot.entity.my.MyBottomMenuModel;
import com.heboot.event.MeEvent;
import com.heboot.event.UserEvent;
import com.heboot.event.VideoEvent;
import com.heboot.req.UploadAvatarReq;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.suke.widget.SwitchButton;
import com.yalantis.dialog.TipCustomDialog;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyFragment extends BaseFragment<FragmentMyBinding> {

    private User meUser;

    private VideoChatService videoChatService;

    private List<MyBottomMenuModel> myBottomMenuModels = new ArrayList<>();

    private Consumer<Integer> avatarConsumer;

    private BottomSheetDialog avatarBottomSheet;

    private QMUITipDialog loadingDialog;

    private Uri cropUri;

    private TipCustomDialog womenTipDialog;

    private TipCustomDialog userCloseTipDialog;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (meUser == null) {
            initMeData();
            initQuPai();
        }
    }

    private void initQuPai() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                System.loadLibrary("QuCore-ThirdParty");
                System.loadLibrary("QuCore");
                QupaiHttpFinal.getInstance().initOkHttpFinal();
//                Logger.setDebug(true);
            }
        }).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    @Override
    public void initUI() {
        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(1200);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initMeData();
            }
        });

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
//              LogUtil.e("发送刷新用户视频集事件3", o.toString());
                if (o.equals(MeEvent.REFRESH_ME_BY_AUTH_SUC) || o.equals(UserEvent.UPDATE_PROFILE) || o.equals(MeEvent.REFRESH_ME_BY_SET_VIDEO_PRICE)) {
                    initMeData();
                }


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        avatarConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                avatarBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        PictureSelector.create(_mActivity)
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
                        PictureSelector.create(_mActivity).openGallery(PictureMimeType.ofImage()).enableCrop(true)
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

        binding.ivAvatar.setOnClickListener((v) -> {
            cropUri = ImageUtils.getCropPhotoUri();
            if (avatarBottomSheet == null) {
                avatarBottomSheet = DialogUtils.getAvatarBottomSheet(_mActivity, avatarConsumer);
            }
            avatarBottomSheet.show();
        });


        /**
         * 个人主页按钮
         */
        binding.vHomepage.setOnClickListener((v) -> {
//            if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
//                IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_MY, UserService.getInstance().getUser(), null, null);
//            } else {
//                IntentUtils.toUserInfoActivity(binding.getRoot().getContext(), MValue.FROM_MY, MValue.USER_INFO_TYPE_NORMAL, UserService.getInstance().getUser(), null, null);
//            }
            IntentUtils.toUserPageActivity(MAPP.mapp.getCurrentActivity(), String.valueOf(UserService.getInstance().getUser().getId()));
        });
        /**
         * 编辑按钮
         */
        binding.vEdit.setOnClickListener((v) -> {
            IntentUtils.toUserInfoActivity(_mActivity, MValue.FROM_MY, MValue.USER_INFO_TYPE_EDIT, meUser, null, null);
        });
        /**
         * 我的账户
         */
        binding.includeMyMenuTop.llytAccount.setOnClickListener((v) -> {
            IntentUtils.toAccountActivity(_mActivity);
        });
        /**
         * 认证
         */
        binding.includeMyMenuTop.llytVer.setOnClickListener((v) -> {
            if (!UserService.getInstance().isServicer()) {
                AuthService.toAuthPageByIndex(_mActivity);
            }
        });
        /**
         * 订单
         */
        binding.includeMyMenuTop.llytOrder.setOnClickListener((v) -> {
            IntentUtils.toUserOrderActivity(_mActivity);
        });
        /**
         * 充值
         */
        binding.includeMyMenuCenter.qbRecharge.setOnClickListener((v) -> {
            IntentUtils.toAccountActivity(_mActivity);
        });
        /**
         * 视频接听状态
         */
//        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getVideo_chat_status() == 1) {
//            binding.includeMenuEnableVideo.sbVideoEnable.setChecked(true);
//        } else {
//            binding.includeMenuEnableVideo.sbVideoEnable.setChecked(false);
//        }
        binding.includeMyMenuCenter.sbVideoEnable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                if (UserService.getInstance().isServicer()) {
                if (isChecked) {
                    if (UserService.getInstance().getUser().getVideo_chat_status() == 1) {
                        return;
                    }
                    if (videoChatService == null) {
                        videoChatService = new VideoChatService();
                    }
                    videoChatService.switch_video_chat_status(binding.includeMyMenuCenter.sbVideoEnable);
                } else {
                    if (UserService.getInstance().getUser().getVideo_chat_status() == 0) {
                        return;
                    }
                    if (videoChatService == null) {
                        videoChatService = new VideoChatService();
                    }
                    videoChatService.switch_video_chat_status(binding.includeMyMenuCenter.sbVideoEnable);
                }

//                }


            }
        });
        /**
         * 设置价格
         */
        binding.includeMyMenuCenter.vSetVideoPrice.setOnClickListener((v) -> {
            if (UserService.getInstance().isServicer()) {
                IntentUtils.toSetPriceActivity(_mActivity, false);
            } else {
                showAuthDialog();
            }

        });
        /**
         * 账单明细
         */
        binding.includeMyMenuCenter.clytBalanceLog.setOnClickListener((v) -> {
            IntentUtils.toBalanceLogActivity(_mActivity);
        });
        /**
         * 最近来访
         */
        binding.includeMyMenuCenter.clytVisit.setOnClickListener((v -> {
            IntentUtils.toUserVisitActivity(_mActivity, String.valueOf(meUser.getId()));
        }));

        binding.includeMyMenuTop.llytRecommend.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getRole() != null && UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), null, MAPP.mapp.getConfigBean().getInvite_url_config().getS_u() + "?token=" + UserService.getInstance().getToken());
            } else {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), null, MAPP.mapp.getConfigBean().getInvite_url_config().getU_u() + "?token=" + UserService.getInstance().getToken());
            }
        });


        binding.includeMyMenuCenter.vEnable.setOnClickListener((v) -> {
            if (UserService.getInstance().isServicer()) {
                if (UserService.getInstance().getUser().getVideo_chat_status() == 1) {
                    binding.includeMyMenuCenter.sbVideoEnable.setChecked(false);
                } else {
                    binding.includeMyMenuCenter.sbVideoEnable.setChecked(true);
                }
            } else {
//                showAuthDialog();
                if (videoChatService == null) {
                    videoChatService = new VideoChatService();
                }
                if (UserService.getInstance().getUser().getVideo_chat_status() == 1) {
                    //                        关闭接听后无法接受主播来电 （再想想、关闭）
                    if (userCloseTipDialog == null) {
                        userCloseTipDialog = new TipCustomDialog.Builder(_mActivity, new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                if (integer == 0) {
                                    userCloseTipDialog.dismiss();
                                } else {
//                                    videoChatService.switch_video_chat_status(binding.includeMyMenuCenter.sbVideoEnable);
                                    binding.includeMyMenuCenter.sbVideoEnable.setChecked(false);
                                }
                            }
                        }, "关闭接听后无法接受主播来电", "再想想", "关闭").create();
                    }
                    userCloseTipDialog.show();
                } else {
//                    videoChatService.switch_video_chat_status(binding.includeMyMenuCenter.sbVideoEnable);
                    binding.includeMyMenuCenter.sbVideoEnable.setChecked(true);
                }

            }

        });

    }

    private void initMeData() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().me(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<MeDataBean>() {
            @Override
            public void onSuccess(BaseBean<MeDataBean> baseBean) {
                binding.srytIndex.setRefreshing(false);
                meUser = baseBean.getData().getUser();
                binding.plytContainer.toMain();
                initCenterDatas();
            }

            @Override
            public void onError(BaseBean<MeDataBean> baseBean) {
                binding.srytIndex.setRefreshing(false);
            }
        });
    }

    private void showAuthDialog() {
        if (womenTipDialog == null) {
            womenTipDialog = new TipCustomDialog.Builder(_mActivity, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 0) {
                        womenTipDialog.dismiss();
                    } else {
                        IntentUtils.toAuthBootActivity(_mActivity);
                    }
                }
            }, "认证主播后，即可设定视频服务价格", "取消", "去认证").create();
        }
        womenTipDialog.show();
    }

    /**
     * 初始化中间的菜单数据
     */
    private void initCenterDatas() {

        binding.setUser(meUser);

        if (meUser.getOrder_tip_nums() > 0) {
            binding.includeMyMenuTop.tvOrderNumTip.setVisibility(View.VISIBLE);
            binding.includeMyMenuTop.tvOrderNumTip.setText(meUser.getOrder_tip_nums() + "");
        } else {
            binding.includeMyMenuTop.tvOrderNumTip.setVisibility(View.INVISIBLE);
        }

        binding.includeMyMenuCenter.setUser(meUser);

        if (UserService.getInstance().isServicer()) {
            binding.includeMyMenuCenter.sbVideoEnable.setEnabled(true);
            binding.includeMyMenuTop.tvVerStatus.setText(getString(R.string.ver_ok));
        } else {
            binding.includeMyMenuCenter.sbVideoEnable.setEnabled(true);
            VerService.showServiceVerStatus(binding.includeMyMenuTop.tvVerStatus, UserService.getInstance().getUser());
        }

        binding.includeSex.setUser(meUser);

        /**
         * 视频接听状态
         */
        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getVideo_chat_status() == 1) {
            binding.includeMyMenuCenter.sbVideoEnable.setChecked(true);
        } else {
            binding.includeMyMenuCenter.sbVideoEnable.setChecked(false);
        }
        /**
         * 最近来访
         */
        if (meUser.getVisit_list() != null && meUser.getVisit_list().getList() != null && meUser.getVisit_list().getList().size() > 0) {
            binding.includeMyMenuCenter.clytVisit.setVisibility(View.VISIBLE);
            initVisit();
        } else {
            binding.includeMyMenuCenter.clytVisit.setVisibility(View.GONE);
        }
        /**
         * 广告区域
         */
        initBanner();
        /**
         * 底部菜单
         */
        initBottomMenus();
    }

    /**
     * 初始化最近访客
     */
    private void initVisit() {

        if (binding.includeMyMenuCenter.rvList.getAdapter() != null) {
            return;
        }

        binding.includeMyMenuCenter.rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        binding.includeMyMenuCenter.rvList.setAdapter(new IndexVisitAdapter(meUser.getVisit_list().getList(), UserService.getInstance().getUser()));

        binding.includeMyMenuCenter.clytVisit.setOnClickListener((v) -> {
            IntentUtils.toUserVisitActivity(getActivity(), String.valueOf(UserService.getInstance().getUser().getId()));
        });
    }


    /**
     * 初始化广告区域
     */
    private void initBanner() {

        if (meUser.getAd_list() == null || meUser.getAd_list().size() <= 0) {
            return;
        }

        List<IndexPopTipBean> images = new ArrayList<>();

        for (IndexPopTipBean bean : meUser.getAd_list()) {
            images.add(bean);
        }

        //设置图片加载器
        binding.includeMyMenuCenter.ivBanner.setImageLoader(new GlideImageLoaderByIndexTop());
        //设置图片集合
        binding.includeMyMenuCenter.ivBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        binding.includeMyMenuCenter.ivBanner.start();
    }


    /**
     * 初始化底部菜单
     */
    private void initBottomMenus() {

        if (myBottomMenuModels.size() > 0) {
            return;
        }

        MyBottomMenuModel unlockVideos = new MyBottomMenuModel("解锁视频", R.drawable.icon_my_menu_unlock);
        myBottomMenuModels.add(unlockVideos);

        MyBottomMenuModel meiyan = new MyBottomMenuModel("美颜设置", R.drawable.icon_my_menu_meiyan);
        myBottomMenuModels.add(meiyan);

        MyBottomMenuModel uploadVideo = new MyBottomMenuModel("上传视频", R.drawable.icon_my_menu_uploadvideo);
        myBottomMenuModels.add(uploadVideo);

        MyBottomMenuModel fav = new MyBottomMenuModel("我的收藏", R.drawable.icon_my_menu_fav);
        myBottomMenuModels.add(fav);

        MyBottomMenuModel foller = new MyBottomMenuModel("我的守护", R.drawable.icon_follow_me);
        myBottomMenuModels.add(foller);


        MyBottomMenuModel zhinan = new MyBottomMenuModel("新手指南", R.drawable.icon_my_menu_zhinan);
        myBottomMenuModels.add(zhinan);

        MyBottomMenuModel qa = new MyBottomMenuModel("常见问题", R.drawable.icon_my_menu_qa);
        myBottomMenuModels.add(qa);

        MyBottomMenuModel feeback = new MyBottomMenuModel("意见反馈", R.drawable.icon_my_menu_feedback);
        myBottomMenuModels.add(feeback);

        MyBottomMenuModel setting = new MyBottomMenuModel("设置", R.drawable.icon_my_menu_setting);
        myBottomMenuModels.add(setting);


        binding.includeMyMenuBottom.rvList.setLayoutManager(new GridLayoutManager(_mActivity, 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        binding.includeMyMenuBottom.rvList.setAdapter(new MyBottomMenuAdapter(R.layout.layout_my_bottom_menus_item, myBottomMenuModels));

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
        loadingDialog = DialogUtils.getLoadingDialog(_mActivity, "", false);
        loadingDialog.show();

//        ImageUtils.showImage(binding.includeAvatar.tvRight, localMedia.getCutPath());

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
                loadingDialog.dismiss();
                tipDialog = DialogUtils.getSuclDialog(_mActivity, baseBean.getMessage(), true);
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<UserInfoEditBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


}
