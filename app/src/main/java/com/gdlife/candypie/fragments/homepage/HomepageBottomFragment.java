package com.gdlife.candypie.fragments.homepage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.discover.HomepageBottomVideosAdapter;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.adapter.user.UserFollowAdapter;
import com.gdlife.candypie.adapter.user.UserGiftAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.databinding.ActivityUserpageBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.serivce.user.FollowService;
import com.gdlife.candypie.serivce.user.UserPageService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.gdlife.candypie.widget.dialog.follow.FollowTipDialog;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialogHehe;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.dialog.TipDialog;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.event.VideoChatEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class HomepageBottomFragment extends BaseFragment<ActivityUserpageBinding> {


//    private String userId;

//    private UserPageViewModel userPageViewModel;

    private User user;

    private UIService uiService = new UIService();

    private UserGiftAdapter userGiftAdapter;

    private VideoChatService videoChatService;

    private PermissionUtils permissionUtils;

    private BottomSheetDialog bottomSheetDialog;

    private ShareDialog shareDialog;

    private int height = 400;

    private HomepageBottomVideosAdapter videosAdapter;//视频集适配器

    private UserPageService userPageService = new UserPageService();

    private boolean flag = false;

    //守护弹窗
    private FollowTipDialog followTipDialog;

    private HttpObserver<BaseBeanEntity> followObs;

    private com.gdlife.candypie.widget.common.TipDialog coinDialog;

    private FollowService followService;


    @SuppressLint("ValidFragment")
    public HomepageBottomFragment(User other) {
        this.user = other;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_userpage;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(_mActivity);
        binding.includeToolbar.setWhiteBack(true);
        binding.vTop.setAlpha(0);
//        userId = getIntent().getExtras().getString(MKey.UID);
    }

    @Override
    public void initData() {
//        if (userPageService.isFirst()) {
//            binding.vTipMsg.setVisibility(View.VISIBLE);
//            YoYo.with(Techniques.Bounce).repeat(1000).playOn(binding.vTipMsg);
//        } else {
//            binding.vTipMsg.setVisibility(View.GONE);
//        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.e(TAG, "setUserVisibleHint ==" + isVisibleToUser);
        if (isVisibleToUser && !flag) {
            initUserInfo();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {


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
                if (o instanceof DiscoverEvent.DiscoverUpdateUserEvent) {
                    user = ((DiscoverEvent.DiscoverUpdateUserEvent) o).getUser();
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

        binding.includeBottom.ivMsg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(_mActivity)) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(_mActivity, "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            userPageService.setFirst();
            IntentUtils.intent2ChatActivity(_mActivity, MValue.CHAT_PRIEX + user.getId());
        });
        binding.includeBottom.ivSendGift.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(_mActivity)) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(_mActivity, "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            BottomVideoGiftSheetDialogHehe bottomVideoGiftSheetDialogHehe = new BottomVideoGiftSheetDialogHehe(user.getId() + "", null);
            bottomVideoGiftSheetDialogHehe.show(getFragmentManager(), "");
        });
        binding.includeBottom.vVideoBg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(_mActivity)) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(_mActivity, "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            if (videoChatService == null) {
                videoChatService = new VideoChatService();
            }
            if (permissionUtils == null) {
                permissionUtils = new PermissionUtils();
            }
            if (user == null) {
                return;
            }
            videoChatService.postVideoService(permissionUtils, _mActivity, user, null);
        });
        binding.includeAvatar.includeFav.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(_mActivity)) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(_mActivity, "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            doFav(user.getIs_favs());
        });
        binding.svContainer.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                float aaa = (float) scrollY / (float) height;


                if (scrollY <= height) {


                    if (aaa > 0.5f) {
                        MStatusBarUtils.setActivityLightMode(_mActivity);
                        binding.ivShare.setBackgroundResource(R.drawable.icon_homepage_share_black);
                        binding.ivMore.setBackgroundResource(R.drawable.icon_homepage_more_black);
                        binding.includeToolbar.setWhiteBack(false);
                        binding.includeToolbar.ivBack.setBackgroundResource(R.drawable.icon_back_black);
                    } else {
                        MStatusBarUtils.setActivityNOLightMode(_mActivity);
                        binding.ivShare.setBackgroundResource(R.drawable.icon_homepage_share_white);
                        binding.ivMore.setBackgroundResource(R.drawable.icon_homepage_more_white);
                        binding.includeToolbar.setWhiteBack(true);
                        binding.includeToolbar.ivBack.setBackgroundResource(R.drawable.icon_back_white);
                    }

                    binding.vTop.setAlpha(aaa);
                }


            }
        });
        binding.includeAvatar.tvFollow.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getActivity())) {
                return;
            }
            if (followTipDialog == null) {
                if (followObs == null) {
                    followObs = new HttpObserver<BaseBeanEntity>() {
                        @Override
                        public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                            if (!StringUtils.isEmpty(baseBean.getService_time())) {
                                RxBus.getInstance().post(new VideoChatEvent.UPDATE_VIDEO_SERVICE_TIME_ENENT(baseBean.getService_time()));
                            }
                            if (followTipDialog != null && followTipDialog.isVisible()) {
                                followTipDialog.dismiss();
                            }
                            if (tipDialog != null) {
                                tipDialog.dismiss();
                            }
                            tipDialog = DialogUtils.getSuclDialog(_mActivity, baseBean.getMessage(), true);
                            tipDialog.show();
                        }

                        @Override
                        public void onError(BaseBean<BaseBeanEntity> baseBean) {
                            if (followTipDialog != null && followTipDialog.isVisible()) {
                                followTipDialog.dismiss();
                            }
                            if (tipDialog != null) {
                                tipDialog.dismiss();
                            }
                            tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                            tipDialog.show();
                        }
                    };
                }
                followTipDialog = new FollowTipDialog(String.valueOf(user.getId()), user.getFollow_love_config().getPrice(), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (StringUtils.isEmpty(UserService.getInstance().getUser().getCoin()) || Integer.parseInt(UserService.getInstance().getUser().getCoin()) < Integer.parseInt(user.getFollow_love_config().getPrice())) {
                            // TODO: 2018/3/22 提示去充值钻石
                            if (coinDialog == null) {
                                coinDialog = new com.gdlife.candypie.widget.common.TipDialog.Builder(getContext(), new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        if (integer == 1) {
                                            IntentUtils.toAccountActivity(MAPP.mapp.getCurrentActivity());
                                        }

                                    }
                                }, getString(R.string.new_video_service_coin_tip_title), "充值").create();
                                coinDialog.show();
                            } else {
                                coinDialog.show();
                            }
                        } else {
                            if (followService == null) {
                                followService = new FollowService();
                            }
                            followService.doFollowLove(String.valueOf(user.getId()), followObs);
                        }
                    }
                });
            }
            followTipDialog.show(getFragmentManager(), "follow");

        });

        binding.vMore.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(_mActivity, "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            bottomSheetDialog = DialogUtils.getHomepageBottomSheet(_mActivity, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        if (UserService.getInstance().checkTourist()) {
                            return;
                        }
                        bottomSheetDialog.dismiss();
                        params = SignUtils.getNormalParams();
                        params.put(MKey.BLACK_UID, user.getId());
                        String sign = SignUtils.doSign(params);
                        params.put(MKey.SIGN, sign);
                        if (user.getIs_black() == 1) {
                            HttpClient.Builder.getGuodongServer().un_black(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(_mActivity, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        } else {
                            HttpClient.Builder.getGuodongServer().black_user(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(_mActivity, baseBean.getMessage(), true);
                                    tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                        }
                                    });
                                    tipDialog.show();

                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        }


                    } else if (integer == 0) {
                        bottomSheetDialog.dismiss();
                        IntentUtils.toReportActivity(_mActivity, String.valueOf(user.getId()), ReportFromType.REPROT);
                    }
                }
            }, user.getIs_black() == 1);

            bottomSheetDialog.show();


        });
        binding.ivShare.setOnClickListener((v) -> {
            if (shareDialog == null) {
                shareDialog = new ShareDialog.Builder(_mActivity, String.valueOf(user.getId()), user.getAvatar(), user.getNickname(), MAPP.mapp.getConfigBean().getShare_config().getProfile_share_config()).create();
            }
            shareDialog.show();
        });
        binding.includeVideos.includeTitle.getRoot().setOnClickListener((v) -> {
//            videoUsers.clear();
//            for (HomepageVideoBean homepageVideoBean : user.getUser_video().getList()) {
//                videoUsers.add(user);
//            }
//            IntentUtils.toUserVideoAudioPlayActivity(_mActivity, 0, user.getUser_video().getTotal(), videoUsers);
            IntentUtils.toUserVideosActivity(_mActivity, user, UserVideoActivityFrom.NORMAL);
        });
        binding.includeVideos.tvRight.setOnClickListener((v) -> {
//            videoUsers.clear();
//            for (HomepageVideoBean homepageVideoBean : user.getUser_video().getList()) {
//                videoUsers.add(user);
//            }
//            IntentUtils.toUserVideoAudioPlayActivity(_mActivity, 0, user.getUser_video().getTotal(), videoUsers);
            IntentUtils.toUserVideosActivity(_mActivity, user, UserVideoActivityFrom.NORMAL);
        });
    }

    private List<User> videoUsers = new ArrayList();

    private void doFav(int fav) {
        params = SignUtils.getNormalParams();
        params.put(MKey.TO_UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        if (fav == 0) {
            HttpClient.Builder.getGuodongServer().favs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    user.setIs_favs(1);
                    setFavStatus(1);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    if (tipDialog != null) {
                        tipDialog.dismiss();
                    }
                    tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });

        } else {
            HttpClient.Builder.getGuodongServer().unfavs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    user.setIs_favs(0);
                    setFavStatus(0);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    if (tipDialog != null) {
                        tipDialog.dismiss();
                    }
                    tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });
        }
    }

    private void initUserInfo() {
        params = SignUtils.getNormalParams();
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().user_profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserInfoBean>() {
            @Override
            public void onSuccess(BaseBean<UserInfoBean> baseBean) {
                binding.plytContainer.toMain();
                user = baseBean.getData().getUser_profile();
                initUserUI();
                if (UserService.getInstance().getUser() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                    binding.includeBottom.getRoot().setVisibility(View.GONE);
                    binding.vBottom.setVisibility(View.GONE);
                    binding.vTipMsg.setVisibility(View.GONE);
                } else {
                    binding.includeBottom.getRoot().setVisibility(View.VISIBLE);
                    binding.vBottom.setVisibility(View.VISIBLE);
                    if (userPageService.isFirst()) {
                        binding.vTipMsg.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.Bounce).repeat(1000).playOn(binding.vTipMsg);
                    } else {
                        binding.vTipMsg.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(BaseBean<UserInfoBean> baseBean) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                if (!_mActivity.isDestroyed()) {
                    tipDialog.show();
                }
            }
        });


    }

    private void initUserUI() {
        initTop();
        initUserVideo();
        initInfo();
        initFollowList();
        initUserGifts();
        initUserAbst();
        initVisitUsers();
        initComments();
        initBottom();
    }

    private void initUserVideo() {
        //守护
        //如果守护不可用就不显示了
        if (user.getFollow_love_config() == null || user.getFollow_love_config().getStatus() == 0) {
            binding.includeAvatar.tvFollow.setVisibility(View.GONE);
        }

        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
            binding.includeVideos.getRoot().setVisibility(View.GONE);
        } else {
            if (user.getUser_video() != null && user.getUser_video().getList() != null && user.getUser_video().getList().size() > 0) {
                binding.includeVideos.includeTitle.setTitle(user.getUser_video().getTitle());
                binding.includeVideos.tvVideoNum.setText("( " + user.getUser_video().getNums() + " )");
                binding.includeVideos.getRoot().setVisibility(View.VISIBLE);
                binding.includeVideos.getRoot().setFocusable(false);
                binding.includeVideos.rvList.setFocusable(false);
                videosAdapter = new HomepageBottomVideosAdapter(new WeakReference(this), false, user.getUser_video().getList(), user);
                binding.includeVideos.rvList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }

                    @Override
                    public boolean canScrollHorizontally() {
                        if (user.getUser_video() != null && user.getUser_video().getList() != null && user.getUser_video().getList().size() >= 4) {
                            return true;
                        }
                        return false;
                    }
                });
                binding.includeVideos.rvList.setAdapter(videosAdapter);
            } else {
                binding.includeVideos.getRoot().setVisibility(View.GONE);
            }
        }


    }

    private void initBottom() {
        binding.includeBottom.setFrom(1);
        if (user.getVideo_chat_status() == 1) {
            binding.includeBottom.vVideoBg.setBackgroundResource(R.drawable.bg_gradient_videochat);
        } else {
            binding.includeBottom.vVideoBg.setBackgroundResource(R.drawable.bg_rect_d6d6df_22);
        }

        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
            binding.includeBottom.tvPrice.setText("视频聊天");
        } else {
            binding.includeBottom.tvPrice.setText("聊天" + user.getVideo_chat_price() + "钻/分钟");
        }

    }


    /**
     * 最近访客
     */
    private void initVisitUsers() {
        if (user.getVisit_list() != null && user.getVisit_list().getList() != null && user.getVisit_list().getList().size() > 0) {

            binding.includeVisit.includeVisit.rvList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });

            binding.includeVisit.includeVisit.rvList.setAdapter(new IndexVisitAdapter(user.getVisit_list().getList(), user));

            binding.includeVisit.includeVisit.tvTitle.setText(user.getVisit_list().getTitle());

            binding.includeVisit.includeVisit.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserVisitActivity(_mActivity, user.getId() + "");
            });
        } else {
            binding.includeVisit.includeVisit.getRoot().setVisibility(View.GONE);
        }
    }

    /**
     * 评论
     */
    private void initComments() {
        if (user != null && user.getUser_eval() != null && user.getUser_eval().getTag_list() != null && user.getUser_eval().getTag_list().size() > 0) {
            binding.includeComment.getRoot().setVisibility(View.VISIBLE);

            binding.includeComment.includeCommentTags.tvTitle.setText(user.getUser_eval().getTitle());
            binding.includeComment.includeCommentTags.tvNums.setText("( " + user.getUser_eval().getNums() + " )");
            uiService.initTagsLayout(user.getUser_eval().getTag_list(), binding.includeComment.includeCommentTags.qflContainer, getResources().getDimensionPixelOffset(R.dimen.y12), getResources().getDimensionPixelOffset(R.dimen.x14), false, getResources().getDimensionPixelOffset(R.dimen.y30), null);
        } else {
            binding.includeComment.includeCommentTags.getRoot().setVisibility(View.GONE);
        }
    }

    /**
     * 个性签名
     */
    private void initUserAbst() {
        if (user.getSignature() != null) {
            binding.includeAbst.tvTitle.setText(user.getSignature().getTitle());
            binding.includeAbst.tvAbst.setText(user.getSignature().getAbst());
        }
        if (user.getMeet_tags() != null && user.getMeet_tags().getSelect_items() != null && user.getMeet_tags().getSelect_items().size() > 0) {
            binding.includeAbst.qfytContainerMeetTag.setVisibility(View.VISIBLE);
            uiService.initMeetSelectedTagsLayout(user.getMeet_tags().getSelect_items(), binding.includeAbst.qfytContainerMeetTag, getResources().getDimensionPixelOffset(R.dimen.y12), getResources().getDimensionPixelOffset(R.dimen.x10));
//            binding.includeAbst.qfytContainerMeetTag.setOnClickListener((v) -> {
//                IntentUtils.toUserInfoActivity(binding.getRoot().getContext(), MValue.FROM_MY, MValue.USER_INFO_TYPE_EDIT, UserService.getInstance().getUser(), null, null);
//            });
        } else {
            binding.includeAbst.qfytContainerMeetTag.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化用户礼物布局
     */
    private void initUserGifts() {
        if (user.getGift() != null && user.getGift().getList() != null && user.getGift().getList().size() > 0) {
            binding.includeGift.getRoot().setVisibility(View.VISIBLE);
            binding.includeGift.tvTitle.setText(user.getGift().getTitle());
            binding.includeGift.tvNum.setText("( " + user.getGift().getNums() + " )");
            userGiftAdapter = new UserGiftAdapter(R.layout.item_userpage_gift, user.getGift().getList());
            userGiftAdapter.setEnableLoadMore(false);
            userGiftAdapter.setUpFetchEnable(false);
            binding.includeGift.rvList.setLayoutManager(new GridLayoutManager(_mActivity, 6) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            binding.includeGift.rvList.setAdapter(userGiftAdapter);
        } else {
            binding.includeGift.getRoot().setVisibility(View.GONE);
        }
    }
//

    /**
     * 初始化顶部头像和收藏等区域
     */
    private void initTop() {

        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
            binding.includeAvatar.tvFollow.setVisibility(View.GONE);
        } else {
            binding.includeAvatar.tvFollow.setVisibility(View.VISIBLE);
        }


        binding.includeAvatar.tvName.setText(user.getNickname());
        //展示头像
        ImageUtils.showIndexUserImage(binding.includeAvatar.ivAvatar, user.getAvatar());
        //设置年龄 性别
        binding.includeAvatar.includeSexage.setUser(user);
        //设置在线状态
        if (user.getOnline_status() != null) {
            binding.includeAvatar.tvOnline.getRoot().setVisibility(View.VISIBLE);
            binding.includeAvatar.tvOnline.setOnlineStatus(user.getOnline_status());
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(user.getOnline_status().getIcon_color()));
            binding.includeAvatar.tvOnline.ivPoint.setImageDrawable(colorDrawable);
        }
        //设置蜜糖号
        binding.includeAvatar.tvMtId.setText("蜜糖号:" + user.getId());
        //设置收藏区域
        setFavStatus(user.getIs_favs());
    }
//

    /**
     * 初始化个人资料区域
     */
    private void initInfo() {
        if (user.getProfile() != null && user.getProfile().getList() != null && user.getProfile().getList().size() > 0) {
            uiService.initMeetSelectedTagsLayout(user.getProfile().getList(), binding.includeInfo.qfytContainerMeetTag, getResources().getDimensionPixelOffset(R.dimen.y12), getResources().getDimensionPixelOffset(R.dimen.x10));
        }

    }

    /**
     * 初始化最近守护
     */
    private void initFollowList() {
        if (user.getFollow_love_list() != null && user.getFollow_love_list().getList() != null && user.getFollow_love_list().getList().size() > 0) {
            binding.includeFollow.getRoot().setVisibility(View.VISIBLE);

            binding.includeFollow.includeVisit.rvList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });

            binding.includeFollow.includeVisit.rvList.setAdapter(new UserFollowAdapter(user.getFollow_love_list().getList(), user));

            binding.includeFollow.includeVisit.tvTitle.setText(user.getFollow_love_list().getTitle());
            binding.includeFollow.includeVisit.tvSubTitle.setVisibility(View.VISIBLE);
            binding.includeFollow.includeVisit.tvSubTitle.setText("(" + user.getFollow_love_list().getNums() + ")");
            binding.includeFollow.includeVisit.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserFollowActivity(_mActivity, user, user.getFollow_love_list().getNums());
            });
        } else {
            binding.includeFollow.getRoot().setVisibility(View.GONE);
        }
    }
//

    /**
     * 初始化收藏数据
     */
    private void setFavStatus(int fav) {
        if (fav == 1) {
            binding.includeAvatar.includeFav.ivUnfav.setVisibility(View.GONE);
            binding.includeAvatar.includeFav.tvUnfav.setVisibility(View.GONE);
            binding.includeAvatar.includeFav.vUnfavBg.setVisibility(View.GONE);
            binding.includeAvatar.includeFav.tvFaved.setVisibility(View.VISIBLE);
        } else {
            binding.includeAvatar.includeFav.ivUnfav.setVisibility(View.VISIBLE);
            binding.includeAvatar.includeFav.tvUnfav.setVisibility(View.VISIBLE);
            binding.includeAvatar.includeFav.vUnfavBg.setVisibility(View.VISIBLE);
            binding.includeAvatar.includeFav.tvFaved.setVisibility(View.GONE);
        }
    }

}
