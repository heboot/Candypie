package com.gdlife.candypie.fragments.homepage;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.HomepageActivity;
import com.gdlife.candypie.adapter.discover.HomepageBottomVideosAdapter;
import com.gdlife.candypie.adapter.homepage.HomepageSkillAdapter;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.databinding.FragmentHomepageBottomBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.NimChatService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.view.verticalview.DragLayout;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.ViewUtils;
import com.qmuiteam.qmui.util.QMUIViewHelper;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomepageBottomFragment extends BaseFragment<FragmentHomepageBottomBinding> {

    private HomepageSkillAdapter skillAdapter;//技能适配器

    private User user;

    private HomepageBottomVideosAdapter videosAdapter;//视频集适配器

    private String from;

    private BottomSheetDialog bottomSheetDialog;

    private ShareDialog shareDialog;

    private PermissionUtils permissionUtils;

    private TipDialog coinDialog;

    private DragLayout dragLayout;

    private VideoChatService videoChatService = new VideoChatService();

    private UIService uiService;

    private int currentscrollY = 0;

    public HomepageBottomFragment() {
    }


    @SuppressLint("ValidFragment")
    public HomepageBottomFragment(User other, DragLayout dragLayout) {
        this.user = other;
        this.dragLayout = dragLayout;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage_bottom;
    }

    @Override
    public void initUI() {
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.tvTitle.setText("");
        binding.includeToolbar.setHideTitle(false);
    }

    @Override
    public void initData() {
        uiService = new UIService();
        permissionUtils = new PermissionUtils();
        if (user != null) {
            initUserUI();
            binding.ivShare.setVisibility(View.GONE);
            binding.ivMore.setVisibility(View.GONE);
            binding.includeToolbar.getRoot().setVisibility(View.GONE);
            binding.vTop.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                    initUserUI();
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
            RxBus.getInstance().post(DiscoverEvent.DISCOVER_TO_VIDEOPAGE_EVENT);
        });

        //编辑个人信息按钮点击
        binding.layoutHomepageContent.includeUserinfo.ivEdit.setOnClickListener((v) -> {
            IntentUtils.toUserInfoActivity(getContext(), MValue.FROM_MY, MValue.USER_INFO_TYPE_EDIT, user, null, null);
        });


        binding.tvLeft.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            postVideoService();
        });

        binding.ivToyue.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            IntentUtils.toThemeListActivity(getContext(), true, user);
        });

        binding.ivTochat.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            NimChatService.getInstance().toChat(getContext(), user);
        });


        binding.vMore.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            bottomSheetDialog = DialogUtils.getHomepageBottomSheet(getContext(), new Consumer<Integer>() {
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
                                    tipDialog = DialogUtils.getSuclDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();
                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        } else {
                            HttpClient.Builder.getGuodongServer().black_user(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                                @Override
                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getSuclDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();

                                }

                                @Override
                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                                    tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                                    tipDialog.show();
                                }
                            });
                        }


                    } else if (integer == 0) {
                        bottomSheetDialog.dismiss();
                        IntentUtils.toReportActivity(getContext(), String.valueOf(user.getId()), ReportFromType.REPROT);
                    }
                }
            }, user.getIs_black() == 1);

            bottomSheetDialog.show();


        });

        binding.ivShare.setOnClickListener((v) -> {
            if (shareDialog == null) {
                shareDialog = new ShareDialog.Builder(getContext(), String.valueOf(user.getId()), user.getAvatar(), user.getNickname(), MAPP.mapp.getConfigBean().getShare_config().getProfile_share_config()).create();
            }
            shareDialog.show();
        });

        binding.layoutHomepageContent.includeVideos.includeTitle.getRoot().setOnClickListener((v) -> {
            IntentUtils.toUserVideosActivity(getContext(), user, UserVideoActivityFrom.NORMAL);
        });
        binding.layoutHomepageContent.includeVideos.tvRight.setOnClickListener((v) -> {
            IntentUtils.toUserVideosActivity(getContext(), user, UserVideoActivityFrom.NORMAL);
        });

        binding.svContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentscrollY == 0 && event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (dragLayout != null) {
                        dragLayout.setEnable(true);
                    }
                }
                return false;
            }
        });

        binding.svContainer.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                currentscrollY = scrollY;
                if (currentscrollY == 0) {
                    if (dragLayout != null) {
                        dragLayout.setEnable(true);
                    }
                } else {
                    if (dragLayout != null) {
                        dragLayout.setEnable(false);
                    }

                }
            }
        });
    }

    private void postVideoService() {
        videoChatService.postVideoService(permissionUtils, (BaseActivity) getActivity(), user, coinDialog);
    }

    private void initUserUI() {

        if (user.getIs_favs() == 1) {
//            binding.tvLeft.setText(getString(R.string.fav_status_on));
//            binding.ivFav.setBackgroundResource(R.drawable.icon_fav_fouse);
        } else {
//            binding.tvLeft.setText(getString(R.string.fav_status_un));
//            binding.ivFav.setBackgroundResource(R.drawable.icon_fav_normal);
        }

        if (UserService.getInstance().getUser() == null || user.getId().intValue() != UserService.getInstance().getUser().getId().intValue()) {
            binding.layoutHomepageContent.includeUserinfo.ivEdit.setVisibility(View.GONE);
        } else if (UserService.getInstance().getUser() == null || user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
            binding.layoutHomepageContent.includeUserinfo.ivEdit.setVisibility(View.GONE);
        }

        if (UserService.getInstance().getUser() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
            binding.ivMore.setVisibility(View.INVISIBLE);
            binding.vMore.setVisibility(View.GONE);
            binding.ivShare.setVisibility(View.INVISIBLE);
            from = MValue.FROM_MY;
        } else {
            binding.ivMore.setVisibility(View.VISIBLE);
            binding.vMore.setVisibility(View.VISIBLE);
            binding.ivShare.setVisibility(View.VISIBLE);
            from = MValue.FROM_OTHER;
        }

//        if(!StringUtils.isEmpty(isOther)){
//            binding.setFrom( MValue.FROM_MY);
//        }else{
//            binding.setFrom(from);
//        }


        binding.layoutHomepageContent.includeVer.includeTitle.setTitle(getString(R.string.ver_info_other));
        binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.other_skill));

        binding.setUser(user);
        binding.layoutHomepageContent.setUser(user);

        binding.layoutHomepageContent.includeUserinfo.setFrom(from);
        binding.layoutHomepageContent.includeUserinfo.setUser(user);
        binding.layoutHomepageContent.includeUserinfo.includeSexage.setUser(user);

        binding.layoutHomepageContent.includeVer.includeServiceVer.tvTitle.setText(getString(R.string.ver_info_service));
        binding.layoutHomepageContent.includeVer.includeServiceVer.tvStatus.setText(getString(R.string.ver_ok));
//        binding.layoutHomepageContent.includeVer.includeIdVer.tvTitle.setText(getString(R.string.ver_info_id));
//        binding.layoutHomepageContent.includeVer.includeIdVer.tvStatus.setText(getString(R.string.ver_ok));

        ImageUtils.showImage(binding.layoutHomepageContent.includeUserinfo.avatar, user.getAvatar());

        binding.layoutHomepageContent.includeSignature.includeTitle.setTitle(getString(R.string.infop_signature));

        binding.layoutHomepageContent.includeSignature.includeContent.setTitle(user.getAbst());

        ImageUtils.showImage(binding.layoutHomepageContent.includeVer.includeServiceVer.ivServiceVer, user.getAvatar());


        //技能部分
        if (user.getSkill_list() != null && user.getSkill_list().size() > 0) {
            binding.layoutHomepageContent.includeSkill.rvList.setLayoutManager(new GridLayoutManager(getContext(), 3) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            binding.layoutHomepageContent.includeSkill.getRoot().setVisibility(View.VISIBLE);
            if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() == user.getId()) {
                binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.my_skill));
            } else {
                binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.other_skill));
            }
            skillAdapter = new HomepageSkillAdapter(ThemeService.filterUserSkill(user.getSkill_list()), false);
            binding.layoutHomepageContent.includeSkill.rvList.setAdapter(skillAdapter);
        } else {
            binding.layoutHomepageContent.includeSkill.getRoot().setVisibility(View.GONE);
            if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId().intValue() == user.getId().intValue()) {
                binding.layoutHomepageContent.includeSkill.includeTitle.setTitle(getString(R.string.my_skill));
                skillAdapter = new HomepageSkillAdapter(ThemeService.filterUserSkill(user.getSkill_list()), UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId().intValue() == user.getId().intValue() ? true : false);
                binding.layoutHomepageContent.includeSkill.rvList.setAdapter(skillAdapter);
                binding.layoutHomepageContent.includeSkill.getRoot().setVisibility(View.GONE);
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
            videosAdapter = new HomepageBottomVideosAdapter(new WeakReference(this), false, user.getVideo_list(),user);
            binding.layoutHomepageContent.includeVideos.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

                @Override
                public boolean canScrollHorizontally() {
                    if (user.getVideo_list().size() >= 4) {
                        return true;
                    }
                    return false;
                }
            });
            binding.layoutHomepageContent.includeVideos.rvList.setAdapter(videosAdapter);
        }
//        else if (from.equals(MValue.FROM_MY)) {
//            binding.includeVideos.getRoot().setFocusable(false);
//            binding.includeVideos.rvList.setFocusable(false);
//            videosAdapter = new HomepageBottomVideosAdapter(new WeakReference(this),  false, user.getVideo_list());
//            binding.includeVideos.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) {
//                @Override
//                public boolean canScrollVertically() {
//                    return false;
//                }
//            });
//            binding.includeVideos.rvList.setAdapter(videosAdapter);
//        }


        //底部按钮
        initBottomUI();

        initMeetTags();

        //评论部分
        initComments();

        initVisitUsers();
    }


    private void initVisitUsers() {
        if (user.getVisit_list() != null && user.getVisit_list().getList() != null && user.getVisit_list().getList().size() > 0) {
            binding.layoutHomepageContent.includeIndexVisit.getRoot().setVisibility(View.VISIBLE);
            binding.layoutHomepageContent.includeIndexVisit.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });

            binding.layoutHomepageContent.includeIndexVisit.rvList.setAdapter(new IndexVisitAdapter(user.getVisit_list().getList(), user));

            binding.layoutHomepageContent.includeIndexVisit.tvTitle.setText(user.getVisit_list().getTitle());

            binding.layoutHomepageContent.includeIndexVisit.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserVisitActivity(getContext(), String.valueOf(UserService.getInstance().getUser().getId()));
            });
        } else {
            binding.layoutHomepageContent.includeIndexVisit.getRoot().setVisibility(View.GONE);
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

        } else {
            if (user.getMeet_tags() != null && user.getMeet_tags().getSelect_items() != null && user.getMeet_tags().getSelect_items().size() > 0) {
                binding.layoutHomepageContent.qfytContainerMeetTag.setVisibility(View.VISIBLE);
                initTagsView();
            } else {
                binding.layoutHomepageContent.qfytContainerMeetTag.setVisibility(View.GONE);
            }

        }

    }

    private void initTagsView() {
        if (user != null && user.getMeet_tags() != null && user.getMeet_tags().getSelect_items() != null && user.getMeet_tags().getSelect_items().size() > 0) {
            uiService.initMeetSelectedTagsLayout(user.getMeet_tags().getSelect_items(), binding.layoutHomepageContent.qfytContainerMeetTag, getResources().getDimensionPixelOffset(R.dimen.y10), getResources().getDimensionPixelOffset(R.dimen.x10));
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

    private void initBottomUI() {

        if (user != null && user.getFree_video_chat() == 1) {
            ViewUtils.setMarginLeft(binding.ivFav, getResources().getDimensionPixelOffset(R.dimen.x30));
            binding.tvLeft.setText(getString(R.string.free_video));
        } else if (user != null && user.getFree_video_chat() == 0) {
            ViewUtils.setMarginLeft(binding.ivFav, getResources().getDimensionPixelOffset(R.dimen.x30));
            if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                binding.tvLeft.setText(getString(R.string.chat_video));
            } else {
                ViewUtils.setMarginLeft(binding.ivFav, getResources().getDimensionPixelOffset(R.dimen.x20));
                binding.llytSelectedPrice.setVisibility(View.VISIBLE);
                binding.tvVideoPrice.setText(user.getVideo_chat_price());
//                binding.tvLeft.setText(user.getVideo_chat_price() + getString(R.string.unit_coin) + "/" + getString(R.string.unit_minute));
            }

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (binding != null) {
                binding.svContainer.smoothScrollTo(0, 0);
            }
        }
    }
}
