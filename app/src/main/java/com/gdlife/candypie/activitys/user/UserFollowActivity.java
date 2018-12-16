package com.gdlife.candypie.activitys.user;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.follow.FollowUserAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityUserFollowBinding;
import com.gdlife.candypie.databinding.LayoutFollowUsertopBinding;
import com.gdlife.candypie.databinding.LayoutNodataBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.user.FollowService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.dialog.follow.FollowTipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.UserVisitListBean;
import com.heboot.entity.User;
import com.heboot.event.VideoChatEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class UserFollowActivity extends BaseActivity<ActivityUserFollowBinding> {


    private int followNum;

    private HttpObserver<UserVisitListBean> observer;

    private FollowService followService = new FollowService();

    private boolean isMe = false;

    private FollowUserAdapter followUserAdapter;

    private User user;

    //守护弹窗
    private FollowTipDialog followTipDialog;

    private HttpObserver<BaseBeanEntity> followObs;

    private TipDialog coinDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_follow;
    }

    @Override
    public void initUI() {

        user = (User) getIntent().getExtras().get(MKey.USER);

        followNum = (int) getIntent().getExtras().get(MKey.NUM);

        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        if (UserService.isMe(user)) {
            isMe = true;
            binding.includeToolbar.tvTitle.setText("我的守护:" + followNum + "人");
        } else {
            isMe = false;
            binding.includeToolbar.tvTitle.setText("Ta的守护:" + followNum + "人");
        }


        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        binding.rvListRight.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));

        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);


    }

    @Override
    public void initData() {
        observer = new HttpObserver<UserVisitListBean>() {
            @Override
            public void onSuccess(BaseBean<UserVisitListBean> baseBean) {
                if (UserService.isMe(user)) {
                    binding.includeToolbar.tvTitle.setText("我的守护:" + baseBean.getData().getTotal() + "人");
                }
                binding.srytIndex.setRefreshing(false);
                total = baseBean.getData().getTotalPages();
                followUserAdapter.loadMoreComplete();
                followUserAdapter.disableLoadMoreIfNotFullPage();
                if (sp == 1) {
                    if (!isMe) {
                        initHeadView(baseBean.getData().getList().get(0).getAvatar());
                    }
                    if (baseBean.getData().getList() == null || baseBean.getData().getList().size() == 0) {

                        LayoutNodataBinding layoutNodataBinding = DataBindingUtil.inflate(LayoutInflater.from(UserFollowActivity.this), R.layout.layout_nodata, null, false);
                        layoutNodataBinding.tvNodata.setText("暂时没有数据哦");
                        followUserAdapter.setEmptyView(layoutNodataBinding.getRoot());
                    }

                    followUserAdapter.getData().clear();
                    followUserAdapter.addData(baseBean.getData().getList());
                } else {
                    followUserAdapter.addData(baseBean.getData().getList());
                }

            }

            @Override
            public void onError(BaseBean<UserVisitListBean> baseBean) {

            }
        };

        followUserAdapter = new FollowUserAdapter(R.layout.layout_follow_item, new ArrayList(), isMe ? "花费守护符" : "守护符数量");

        binding.rvList.setAdapter(followUserAdapter);

        initFollowListData();
    }

    @Override
    public void initListener() {
        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initFollowListData();
            }
        });
        followUserAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (sp >= total) {
                    followUserAdapter.loadMoreEnd();
//                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    return;
                }
                sp = sp + 1;
                initFollowListData();
            }
        }, binding.rvList);
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
    }

    private void initFollowListData() {
        followService.followList(String.valueOf(user.getId()), sp, observer);
    }

    private void initHeadView(String avatar) {
        LayoutFollowUsertopBinding headBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_follow_usertop, null, false);

        ImageUtils.showAvatar(headBinding.avatar1, user.getAvatar());

        ImageUtils.showAvatar(headBinding.avatar2, avatar);

        headBinding.tvFollow.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(this)) {
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
                            tipDialog = DialogUtils.getSuclDialog(UserFollowActivity.this, baseBean.getMessage(), true);
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
                            tipDialog = DialogUtils.getFailDialog(UserFollowActivity.this, baseBean.getMessage(), true);
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
                                coinDialog = new TipDialog.Builder(UserFollowActivity.this, new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        if (integer == 1) {
                                            IntentUtils.toAccountActivity(MAPP.mapp.getCurrentActivity());
                                        }

                                    }
                                }, getString(R.string.new_video_service_coin_tip_title), "充值"
                                ).create();
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
            followTipDialog.show(getSupportFragmentManager(), "follow");

        });
        followUserAdapter.addHeaderView(headBinding.getRoot());
    }

}
