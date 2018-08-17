package com.gdlife.candypie.activitys.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.CropKey;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.homepage.HomepageSkillAdapter;
import com.gdlife.candypie.adapter.homepage.HomepageVideoAdapter;
import com.gdlife.candypie.adapter.homepage.HomepageVideosAdapter;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityHomepageBinding;
import com.gdlife.candypie.fragments.homepage.HomepageVideoFragment;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.DownloadService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VideoService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.FrameUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.VideoUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.common.CropAction;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.ThemeEvent;
import com.heboot.event.UserEvent;
import com.heboot.event.VideoEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.heboot.utils.ValueUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.PublishAlbumDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.Serializable;
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

import static com.heboot.event.UserEvent.UPDATE_USER_INFO_BY_MEET_TAG;

/**
 * Created by heboot on 2018/2/28.
 */

public class HomepageActivity extends BaseActivity<ActivityHomepageBinding> {

    private String from;

    private MValue.HOMEPAG_FROM homepag_from;

    private User user;

    private HomepageSkillAdapter skillAdapter;//技能适配器

    private List<HomepageVideoFragment> fragments;//顶部视频适配器

    private HomepageVideosAdapter videosAdapter;//视频集适配器

    private int height = 400;

    private ConfigBean.ServiceItemsConfigBean.ListBean serviceItemBean;

    private BottomSheetDialog videoBottomSheet;

    private Consumer<Integer> videoConsumer;

    private PublishAlbumDialog publishAlbumDialog;

    private VideoUtils videoUtils;

    @Inject
    VideoService videoService;

    FrameUtils frameUtils;

    private String videoPath;

    private TipDialog coinDialog;

    DownloadService downloadService;

    private List<String> frames = new ArrayList();

    private BottomSheetDialog bottomSheetDialog;

    private ShareDialog shareDialog;

    private PermissionUtils permissionUtils;


    private UIService uiService;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.setHideTitle(true);
        binding.includeToolbar.setWhiteBack(true);
        binding.vTop.setBackgroundColor(Color.WHITE);
        binding.vTop.setAlpha(0);

        binding.layoutHomepageContent.includeSkill.rvList.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        setSwipeBackEnable(false);


    }

    @Override
    public void initData() {

        uiService = new UIService();
        permissionUtils = new PermissionUtils();

        downloadService = new DownloadService();

        frameUtils = new FrameUtils();

        DaggerServiceComponent.builder().build().inject(this);
//        DaggerServiceComponent.builder().videoServiceModule(new VideoServiceModule(videoUtils)).build().inject(this);
        videoUtils = videoService.getVideoUtils();
//        videoService = MAPP.mapp.getVideoService();

        height = (int) (QMUIDisplayHelper.getScreenHeight(this) / 4);

        from = (String) getIntent().getExtras().get(MKey.FROM);

        serviceItemBean = (ConfigBean.ServiceItemsConfigBean.ListBean) getIntent().getExtras().get(MKey.SERVICE_ITEM);

        homepag_from = (MValue.HOMEPAG_FROM) getIntent().getExtras().get(MKey.PAGE_FROM);

        binding.setFrom(from);
        binding.layoutHomepageContent.setFrom(MValue.FROM_MY);

        binding.layoutHomepageContent.includeUserinfo.setFrom(MValue.USER_INFO_FROM_HOMEPAGE);
//
//
        //自己看自己
        if (from.equals(MValue.FROM_MY)) {
            user = UserService.getInstance().getUser();
            binding.layoutHomepageContent.includeVer.includeTitle.setTitle(getString(R.string.ver_info_my));
            binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.my_skill));
            binding.layoutHomepageContent.includeUserinfo.ivEdit.setVisibility(View.VISIBLE);
            binding.layoutHomepageContent.includeUserinfo.setUser(user);
            binding.layoutHomepageContent.includeUserinfo.includeSexage.setUser(user);
        }
        //看别人
        else {
            user = (User) getIntent().getExtras().get(MKey.USER);
            if (user.getIs_favs() == 1) {
                binding.tvLeft.setText(getString(R.string.fav_status_on));
                binding.ivFav.setBackgroundResource(R.drawable.icon_fav_fouse);
            } else {
                binding.tvLeft.setText(getString(R.string.fav_status_un));
                binding.ivFav.setBackgroundResource(R.drawable.icon_fav_normal);
            }

            if (UserService.getInstance().getUser() == null || user.getId().intValue() != UserService.getInstance().getUser().getId().intValue()) {
                binding.layoutHomepageContent.includeUserinfo.ivEdit.setVisibility(View.GONE);
            }

            binding.layoutHomepageContent.includeVer.includeTitle.setTitle(getString(R.string.ver_info_other));
            binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.other_skill));

        }

        if (UserService.getInstance().getUser() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
            binding.ivMore.setVisibility(View.INVISIBLE);
            binding.vMore.setVisibility(View.GONE);

            binding.ivShare.setVisibility(View.INVISIBLE);
        } else {
            binding.ivMore.setVisibility(View.VISIBLE);
            binding.vMore.setVisibility(View.VISIBLE);
            binding.ivShare.setVisibility(View.VISIBLE);
        }


        initUserInfo();

    }

    private void setUserUI() {

        binding.setUser(user);
        binding.layoutHomepageContent.setUser(user);

        binding.layoutHomepageContent.includeVer.includeServiceVer.tvTitle.setText(getString(R.string.ver_info_service));
        binding.layoutHomepageContent.includeVer.includeServiceVer.tvStatus.setText(getString(R.string.ver_ok));
//        binding.layoutHomepageContent.includeVer.includeIdVer.tvTitle.setText(getString(R.string.ver_info_id));
//        binding.layoutHomepageContent.includeVer.includeIdVer.tvStatus.setText(getString(R.string.ver_ok));

        ImageUtils.showImage(binding.layoutHomepageContent.includeUserinfo.avatar, user.getAvatar());

        binding.layoutHomepageContent.includeSignature.includeTitle.setTitle(getString(R.string.infop_signature));

        binding.layoutHomepageContent.includeSignature.includeContent.setTitle(user.getAbst());

        ImageUtils.showImage(binding.layoutHomepageContent.includeVer.includeServiceVer.ivServiceVer, user.getAvatar());

        if (user.getIs_favs() == 1) {
            binding.tvLeft.setText(getString(R.string.fav_status_on));
            binding.ivFav.setBackgroundResource(R.drawable.icon_fav_fouse);
        } else {
            binding.tvLeft.setText(getString(R.string.fav_status_un));
            binding.ivFav.setBackgroundResource(R.drawable.icon_fav_normal);
        }
        //技能部分
        if (user.getSkill_list() != null && user.getSkill_list().size() > 0) {
            binding.layoutHomepageContent.includeSkill.getRoot().setVisibility(View.VISIBLE);
            if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() == user.getId()) {
                binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.my_skill));
            } else {
                binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.other_skill));
            }
            skillAdapter = new HomepageSkillAdapter(ThemeService.filterUserSkill(user.getSkill_list()), UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId().intValue() == user.getId().intValue() ? true : false);
            binding.layoutHomepageContent.includeSkill.rvList.setAdapter(skillAdapter);
        } else {
            binding.layoutHomepageContent.includeSkill.getRoot().setVisibility(View.VISIBLE);
            if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId().intValue() == user.getId().intValue()) {
                binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.my_skill));
                skillAdapter = new HomepageSkillAdapter(ThemeService.filterUserSkill(user.getSkill_list()), UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId().intValue() == user.getId().intValue() ? true : false);
                binding.layoutHomepageContent.includeSkill.rvList.setAdapter(skillAdapter);
            } else {
                binding.layoutHomepageContent.includeSkill.getRoot().setVisibility(View.GONE);
            }

        }

        //下面视频部分
        binding.layoutHomepageContent.includeVideos.includeTitle.setTitle(getString(R.string.user_videos));
        if (user.getVideo_list() != null && user.getVideo_list().size() > 0) {
            binding.layoutHomepageContent.includeVideos.getRoot().setVisibility(View.VISIBLE);
            binding.layoutHomepageContent.includeVideos.getRoot().setFocusable(false);
            binding.layoutHomepageContent.includeVideos.rvList.setFocusable(false);
            videosAdapter = new HomepageVideosAdapter(new WeakReference<HomepageActivity>(this), UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId().intValue() == user.getId().intValue() ? true : false, user.getVideo_list());
            binding.layoutHomepageContent.includeVideos.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            binding.layoutHomepageContent.includeVideos.rvList.setAdapter(videosAdapter);
        } else if (from.equals(MValue.FROM_MY)) {
            binding.layoutHomepageContent.includeVideos.getRoot().setFocusable(false);
            binding.layoutHomepageContent.includeVideos.rvList.setFocusable(false);
            videosAdapter = new HomepageVideosAdapter(new WeakReference<HomepageActivity>(this), UserService.getInstance().getUser().getId().intValue() == user.getId().intValue() ? true : false, user.getVideo_list());
            binding.layoutHomepageContent.includeVideos.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            binding.layoutHomepageContent.includeVideos.rvList.setAdapter(videosAdapter);
        }

        //底部按钮
        initBottomUI();


        initMeetTags();

        initComments();

        initVisitUsers();

    }


    private void initVisitUsers() {
        if (user.getVisit_list() != null && user.getVisit_list().getList() != null && user.getVisit_list().getList().size() > 0) {

            binding.layoutHomepageContent.includeIndexVisit.rvList.setLayoutManager(new LinearLayoutManager(HomepageActivity.this, LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });

            binding.layoutHomepageContent.includeIndexVisit.rvList.setAdapter(new IndexVisitAdapter(user.getVisit_list().getList(), user));

            binding.layoutHomepageContent.includeIndexVisit.tvTitle.setText(user.getVisit_list().getTitle());

            binding.layoutHomepageContent.includeIndexVisit.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserVisitActivity(HomepageActivity.this, String.valueOf(UserService.getInstance().getUser().getId()));
            });
        } else {
            binding.layoutHomepageContent.includeIndexVisit.getRoot().setVisibility(View.GONE);
        }
    }

    private void initComments() {
        if (user != null && user.getUser_eval() != null && user.getUser_eval().getTag_list() != null && user.getUser_eval().getTag_list().size() > 0) {
            binding.layoutHomepageContent.includeCommentTags.getRoot().setVisibility(View.VISIBLE);

            binding.layoutHomepageContent.includeCommentTags.tvTitle.setText(user.getUser_eval().getTitle());
            binding.layoutHomepageContent.includeCommentTags.tvNums.setText("( " + user.getUser_eval().getNums() + " )");
            uiService.initTagsLayout(user.getUser_eval().getTag_list(), binding.layoutHomepageContent.includeCommentTags.qflContainer, getResources().getDimensionPixelOffset(R.dimen.y12), getResources().getDimensionPixelOffset(R.dimen.x14), false, getResources().getDimensionPixelOffset(R.dimen.y30), null);
        } else {
            binding.layoutHomepageContent.includeCommentTags.getRoot().setVisibility(View.GONE);
        }
    }

    private void initMeetTags() {


        /**
         * meet tags
         */

        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {

            initTagsView();

            binding.layoutHomepageContent.qfytContainerMeetTag.setOnClickListener((v) -> {
                IntentUtils.toUserInfoActivity(binding.getRoot().getContext(), MValue.FROM_MY, MValue.USER_INFO_TYPE_EDIT, UserService.getInstance().getUser(), null, null);
            });

        }

    }

    private void initTagsView() {
        if (user.getMeet_tags().getSelect_items() != null && user.getMeet_tags().getSelect_items().size() > 0) {
            uiService.initMeetSelectedTagsLayout(user.getMeet_tags().getSelect_items(), binding.layoutHomepageContent.qfytContainerMeetTag, getResources().getDimensionPixelOffset(R.dimen.y10), getResources().getDimensionPixelOffset(R.dimen.x10));
        }
    }


    private void initBottomUI() {
        if (homepag_from != null) {
            if (homepag_from == MValue.HOMEPAG_FROM.RECOMMEND_USER) {

                if (serviceItemBean != null && serviceItemBean.getType().equals(MValue.ORDER_TYPE_VIDEO)) {
                    binding.tvRight.setText(ThemeService.getVideoServiceMinPrice() + getString(R.string.unit_coin) + "/" + getString(R.string.unit_minute));
                    binding.tvRight.setOnClickListener((v) -> {
                        if (UserService.getInstance().checkTourist()) {
                            return;
                        }
//                        postVideoService();
                    });
//                    binding.tvRight.setOnClickListener((v) -> {
//                        IntentUtils.toThemeListActivity(this, true, user);
//                    });

                } else if (serviceItemBean != null) {
                    binding.tvRight.setText(getString(R.string.select_ta));
                    binding.tvRight.setOnClickListener((v) -> {
                        if (UserService.getInstance().checkTourist()) {
                            return;
                        }
                        RxBus.getInstance().post(ThemeEvent.CLOSE_NEW_SERVICE_PAGE_EVENT);
                        IntentUtils.toNewThemeServiceActivity(this, serviceItemBean, MValue.NEW_SERVICE_TYPE_ONE, user);
                    });
                }

            } else if (homepag_from == MValue.HOMEPAG_FROM.ONE_ONE) {
                binding.tvRight.setText(getString(R.string.select_ta));
                binding.tvRight.setOnClickListener((v) -> {
                    if (UserService.getInstance().checkTourist()) {
                        return;
                    }
                    IntentUtils.toThemeListActivity(this, true, user);
                });
            } else if (homepag_from == MValue.HOMEPAG_FROM.CHOOSE_USER) {
                binding.tvRight.setText(getString(R.string.select_ta));
                binding.tvRight.setOnClickListener((v) -> {
                    if (UserService.getInstance().checkTourist()) {
                        return;
                    }
                    RxBus.getInstance().post(new ThemeEvent.ChooseUserByHomepageEvent(user.getId()));
                    finish();
                });
            }
        } else {
            binding.tvRight.setText(getString(R.string.select_ta));
            binding.tvRight.setOnClickListener((v) -> {
                if (UserService.getInstance().checkTourist()) {
                    return;
                }
                IntentUtils.toThemeListActivity(this, true, user);
            });
        }
    }


    @Override
    public void initListener() {

        rxObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_USERVIDEOS)) {
                    if (publishAlbumDialog != null) {
                        publishAlbumDialog.showAniTo100();
                        publishAlbumDialog.dismiss();
                    }

                    tipDialog = DialogUtils.getSuclDialog(HomepageActivity.this, getString(R.string.upload_suc_auth_tip), true);
                    tipDialog.show();

                    sp = 1;
                    initUserInfo();
                } else if (o instanceof VideoEvent.VideoUploadProgressEvent) {
                    int progress = ((VideoEvent.VideoUploadProgressEvent) o).getProgress();
                    if (publishAlbumDialog == null) {
                        return;
                    }
                    publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_ing));
                    publishAlbumDialog.getBinding().pb.setProgress(progress);
                    if (progress >= 100) {
                        publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_suc));
                        publishAlbumDialog.dismiss();
                    }
                } else if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
                    finish();
                } else if (o.equals(UserEvent.UPDATE_PROFILE) || o.equals(UPDATE_USER_INFO_BY_MEET_TAG)) {
                    initUserInfo();
                } else if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH)) {
                    if (publishAlbumDialog != null) {
                        publishAlbumDialog.showAniTo100();
                        publishAlbumDialog.dismiss();
                    }

                    tipDialog = DialogUtils.getSuclDialog(HomepageActivity.this, getString(R.string.upload_suc_auth_tip), true);
                    tipDialog.show();
                    sp = 1;
                    initUserInfo();
                }
//                else if (o.equals(VideoEvent.REPLACE_SUC_EVENT)) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tipDialog = DialogUtils.getSuclDialog(HomepageActivity.this, "替换成功", true);
//                            tipDialog.show();
//                        }
//                    });
//
//                    sp = 1;
//                    initUserInfo();
//                }
                else if (o.equals(VideoEvent.UPDATE_AVATAR_SUC_EVENT)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tipDialog = DialogUtils.getSuclDialog(HomepageActivity.this, "替换成功", true);
                            tipDialog.show();
                        }
                    });
                    sp = 1;
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


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

//        //编辑个人信息按钮点击
        binding.layoutHomepageContent.includeUserinfo.ivEdit.setOnClickListener((v) -> {
            IntentUtils.toUserInfoActivity(this, MValue.FROM_MY, MValue.USER_INFO_TYPE_EDIT, user, null, null);
        });

        binding.svContainer.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                float aaa = (float) scrollY / (float) height;


                if (scrollY <= height) {


                    if (aaa > 0.5f) {
                        MStatusBarUtils.setActivityLightMode(HomepageActivity.this);
                        binding.ivShare.setBackgroundResource(R.drawable.icon_homepage_share_black);
                        binding.ivMore.setBackgroundResource(R.drawable.icon_homepage_more_black);
                        binding.includeToolbar.setWhiteBack(false);
                        binding.includeToolbar.ivBack.setBackgroundResource(R.drawable.icon_back_black);
                    } else {
                        MStatusBarUtils.setActivityNOLightMode(HomepageActivity.this);
                        binding.ivShare.setBackgroundResource(R.drawable.icon_homepage_share_white);
                        binding.ivMore.setBackgroundResource(R.drawable.icon_homepage_more_white);
                        binding.includeToolbar.setWhiteBack(true);
                        binding.includeToolbar.ivBack.setBackgroundResource(R.drawable.icon_back_white);
                    }

                    binding.vTop.setAlpha(aaa);
                }


            }
        });

        binding.layoutHomepageContent.includeVideos.tvRight.setOnClickListener((v -> {
            IntentUtils.toUserVideosActivity(this, user, UserVideoActivityFrom.NORMAL);
        }));
        binding.layoutHomepageContent.includeVideos.includeTitle.getRoot().setOnClickListener((v -> {
            IntentUtils.toUserVideosActivity(this, user, UserVideoActivityFrom.NORMAL);
        }));

        binding.btnLogin.setOnClickListener((v) -> {
            IntentUtils.toUserVideosActivity(this, user, UserVideoActivityFrom.REPLACE_MAIN_VIDEO);
        });

        binding.tvLeft.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            doFav(user.getIs_favs());
        });

        videoConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                videoBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        IntentUtils.toAuthIndexActivity(HomepageActivity.this, RecordVideoFrom.USER);
//                        videoUtils.toRecordVideo(HomepageActivity.this);
                        break;
                    case 1:
                        videoUtils.toSelectVideo(HomepageActivity.this);
                        break;
                }
            }
        };

        binding.ivVoice.setOnClickListener((v) -> {
            if (binding.ivVoice.getTag() != null && binding.ivVoice.getTag().equals("mute")) {
                RxBus.getInstance().post(VideoEvent.MUTE_OFF);
                binding.ivVoice.setTag("off");
                binding.ivVoice.setBackgroundResource(R.drawable.icon_homepage_voice_open);
            } else {
                RxBus.getInstance().post(VideoEvent.MUTE_ON);
                binding.ivVoice.setTag("mute");
                binding.ivVoice.setBackgroundResource(R.drawable.icon_homepage_voice_close);
            }
        });

        binding.btnRegister.setOnClickListener((v) -> {

            tipDialog = DialogUtils.getLoadingDialog(this, "", false);
            tipDialog.setCancelable(true);
            tipDialog.show();

            String downloadedVideoPath = user.getMain_video_list().get(0).getId() + "video";

            File file = new File(SDCardUtils.getRootPathPrivateVideo() + "/" + downloadedVideoPath);

            if (!file.exists()) {

//                doUpload(videoPath);
                // TODO: 2018/3/24 先进行下载
                downloadService.downlaodVideo(user.getMain_video_list().get(0).getPath(), downloadedVideoPath, new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
                        if (status instanceof Succeed) {
                            toFrame(SDCardUtils.getRootPathPrivateVideo() + "/" + downloadedVideoPath);
                        } else if (status instanceof Failed) {
                            tipDialog.dismiss();
                            ToastUtils.showToast("下载失败，请稍后重试");
                        }
                    }
                });
                RxDownload.INSTANCE.start(user.getMain_video_list().get(0).getPath()).
                        subscribe();
            } else {
                toFrame(SDCardUtils.getRootPathPrivateVideo() + "/" + downloadedVideoPath);
            }

        });


        binding.vMore.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            bottomSheetDialog = DialogUtils.getHomepageBottomSheet(this, new Consumer<Integer>() {
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
                                    tipDialog = DialogUtils.getSuclDialog(HomepageActivity.this, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(HomepageActivity.this, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        } else {
                            HttpClient.Builder.getGuodongServer().black_user(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(HomepageActivity.this, baseBean.getMessage(), true);
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
                                    tipDialog = DialogUtils.getFailDialog(HomepageActivity.this, baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        }


                    } else if (integer == 0) {
                        bottomSheetDialog.dismiss();
                        IntentUtils.toReportActivity(HomepageActivity.this, String.valueOf(user.getId()), ReportFromType.REPROT);
                    }
                }
            }, user.getIs_black() == 1);

            bottomSheetDialog.show();


        });


        binding.ivShare.setOnClickListener((v) -> {
            if (shareDialog == null) {
                shareDialog = new ShareDialog.Builder(this, String.valueOf(user.getId()), user.getAvatar(), user.getNickname(), MAPP.mapp.getConfigBean().getShare_config().getProfile_share_config()).create();
            }
            shareDialog.show();
        });

    }

    private void toFrame(String toFramePath) {


        Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
//                File file = new File(toFramePath);
//                LogUtil.e(TAG, file.exists() + "");
//                FrameUtils frameUtils = new FrameUtils();
//                try {
//                    frames = frameUtils.getFrams(toFramePath, SDCardUtils.getRootPathPrivateFrame());
//                } catch (Exception e) {
//                    tipDialog.dismiss();
//                    e.printStackTrace();
//                }
//
//
//                if (frames != null && frames.size() > 0) {
//                UCrop.of(Uri.parse(frames.get(0)), Uri.parse(videoPath2 + "/ccc.jpg")).startByFrame(this);

//                    tipDialog.dismiss();

                FrameUtils frameUtils = new FrameUtils();
                String fristImage = ImageUtils.saveBitmap(frameUtils.getFrameByVideo(toFramePath));

                ValueUtils.currentVideoPath = toFramePath;

                UCrop.Options options = new UCrop.Options();

                options.setShowCropGrid(false);
                options.setFreeStyleCropEnabled(false);
                options.setHideBottomControls(true);
                options.setToolbarTitle("裁剪");
                options.setToolbarColor(Color.TRANSPARENT);
                options.setToolbarWidgetColor(Color.WHITE);


                options.getOptionBundle().putSerializable("frames", (Serializable) frames);
                tipDialog.dismiss();
                CropAction.CROP_ACTION = CropAction.CROP_ACTION_VALUE.UPDATE_AVATAR.toString();
                MValue.CURRENT_UPDATE_AVATAR_VIDEO_ID = user.getMain_video_list().get(0).getId();

                options.getOptionBundle().putSerializable("path", toFramePath);
                options.getOptionBundle().putSerializable("outPath", SDCardUtils.getRootPathPrivateFrame());

                UCrop.of(Uri.fromFile(new File(fristImage)), ImageUtils.getCropPhotoUri())
                        .withOptions(options)
                        .withAspectRatio(1, 1)
                        .startByFrame(HomepageActivity.this);
            }
//            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();


    }


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
                    binding.tvLeft.setText(getString(R.string.fav_status_on));
                    binding.ivFav.setBackgroundResource(R.drawable.icon_fav_fouse);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    if (tipDialog != null) {
                        tipDialog.dismiss();
                    }
                    tipDialog = DialogUtils.getFailDialog(HomepageActivity.this, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });

        } else {
            HttpClient.Builder.getGuodongServer().unfavs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    binding.tvLeft.setText(getString(R.string.fav_status_un));
                    binding.ivFav.setBackgroundResource(R.drawable.icon_fav_normal);
                    user.setIs_favs(0);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    if (tipDialog != null) {
                        tipDialog.dismiss();
                    }
                    tipDialog = DialogUtils.getFailDialog(HomepageActivity.this, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });
        }
    }

    private void initSurfaceView() {

        fragments = new ArrayList<>();


        if (UserService.getInstance().getUser() != null && user.getId() == UserService.getInstance().getUser().getId()) {
            if (user.getMain_video_list() != null && user.getMain_video_list().size() > 0) {
                fragments.add(new HomepageVideoFragment(user.getMain_video_list().get(0).getPath(), user.getMain_video_list().get(0).getCover_img()));
            }
        } else {
            if (user.getMain_video_list() != null && user.getMain_video_list().size() > 0) {
                for (HomepageVideoBean homepageVideoBean : user.getMain_video_list()) {
                    fragments.add(new HomepageVideoFragment(homepageVideoBean.getPath(), user.getMain_video_list().get(0).getCover_img()));
                }
            }
        }

        if (user.getMain_video_list() == null || user.getMain_video_list().size() == 0 || user.getMain_video_list().size() == 1) {
            binding.includePoint.ivPoint1.setVisibility(View.GONE);
            binding.includePoint.ivPoint2.setVisibility(View.GONE);
            binding.includePoint.ivPoint3.setVisibility(View.GONE);
        } else if (user.getMain_video_list().size() == 2) {
            binding.includePoint.ivPoint1.setVisibility(View.VISIBLE);
            binding.includePoint.ivPoint2.setVisibility(View.VISIBLE);
            binding.includePoint.ivPoint3.setVisibility(View.GONE);
        } else if (user.getMain_video_list().size() == 3) {
            binding.includePoint.ivPoint1.setVisibility(View.VISIBLE);
            binding.includePoint.ivPoint2.setVisibility(View.VISIBLE);
            binding.includePoint.ivPoint3.setVisibility(View.VISIBLE);
        }

        HomepageVideoAdapter homepageVideoAdapter = new HomepageVideoAdapter(getSupportFragmentManager(), fragments);

        binding.vpVideo.setAdapter(homepageVideoAdapter);

        binding.vpVideo.setOffscreenPageLimit(1);
        binding.includePoint.ivPoint1.setSelected(true);
        binding.vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.includePoint.ivPoint1.setSelected(true);
                        binding.includePoint.ivPoint2.setSelected(false);
                        binding.includePoint.ivPoint3.setSelected(false);
                        break;
                    case 1:
                        binding.includePoint.ivPoint1.setSelected(false);
                        binding.includePoint.ivPoint2.setSelected(true);
                        binding.includePoint.ivPoint3.setSelected(false);
                        break;
                    case 2:
                        binding.includePoint.ivPoint1.setSelected(false);
                        binding.includePoint.ivPoint2.setSelected(false);
                        binding.includePoint.ivPoint3.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initUserInfo() {
        params = SignUtils.getNormalParams();
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().servicer_profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserInfoBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(UserInfoBean sendSMSBeanBaseBean) {


                user = sendSMSBeanBaseBean.getUser_profile();
                binding.layoutHomepageContent.setUser(user);
                binding.layoutHomepageContent.includeUserinfo.setUser(user);
                binding.layoutHomepageContent.includeUserinfo.includeSexage.setUser(user);
                if (from.equals(MValue.FROM_MY) || (UserService.getInstance().getUser() != null && user.getId() == UserService.getInstance().getUser().getId())) {
                    binding.setFrom(MValue.FROM_MY);
                    binding.layoutHomepageContent.setFrom(MValue.FROM_MY);
                    UserService.getInstance().setUser(sendSMSBeanBaseBean.getUser_profile());
                }
                setUserUI();
                initSurfaceView();

            }

            @Override
            public void onError(BaseBean<UserInfoBean> basebean) {

            }

            @Override
            public void onError(Throwable throwable) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(HomepageActivity.this, throwable.getMessage(), true);
                tipDialog.show();
            }

        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MCode.REQUEST_CODE.RECORD_VIDEO_CODE || requestCode == MCode.REQUEST_CODE.CHOOSE_VIDEO_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    doUpload(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
                } else if (type == AliyunVideoRecorder.RESULT_TYPE_CROP) {
                    videoPath = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
                    IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.ADD, null);
//                    doUpload(data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH));
                }
//                    IntentUtils.toVideoFrameActivity(this, data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
//                    videoService.doUploadVideo(this, imagePath, data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    private void doUpload(String path) {

        publishAlbumDialog = new PublishAlbumDialog.Builder(this).create();
        publishAlbumDialog.show();
        publishAlbumDialog.showAni();

        videoPath = path;

        try {
            Bitmap bitmap = frameUtils.getFrameByVideo(videoPath);
            String imagePath = ImageUtils.saveBitmap(bitmap);
            videoService.doUploadVideo(imagePath, null, 0, VideoUploadType.USER, videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        DialogUtils.dimissDialog(tipDialog);
        DialogUtils.dimissDialog(coinDialog);
        super.onDestroy();
    }

    public void chooseVideo() {
        if (videoBottomSheet == null) {
            videoBottomSheet = DialogUtils.getAuthSkillBottomSheet(HomepageActivity.this, videoConsumer);
        }
        videoBottomSheet.show();
//        IntentUtils.toAuthIndexActivity(this, RecordVideoFrom.USER);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_homepage;
    }
}
