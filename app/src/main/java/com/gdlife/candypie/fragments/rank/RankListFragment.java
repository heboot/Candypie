package com.gdlife.candypie.fragments.rank;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.rank.RankUserAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentIndexListBinding;
import com.gdlife.candypie.databinding.LayoutIndexUserTopBinding;
import com.gdlife.candypie.databinding.LayoutRankFilterBinding;
import com.gdlife.candypie.databinding.LayoutRankUsertopBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.GlideImageLoaderByIndexTop;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.bean.rank.RankBean;
import com.heboot.common.RANK_FILTER;
import com.heboot.entity.User;
import com.heboot.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RankListFragment extends BaseFragment<FragmentIndexListBinding> {


    private String pageName;

    private RankUserAdapter rankUserAdapter;

    private RANK_FILTER currentSelect = RANK_FILTER.WEEK;

    private boolean headFlag = false;

    private boolean filterHeadFlag = false;

    private RankBean rankBean;

    public static RankListFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString(MKey.NAME, name);
        RankListFragment fragment = new RankListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index_list;
    }

    @Override
    public void initUI() {

        pageName = getArguments().getString(MKey.NAME);


        binding.rvList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
//        binding.rvListRight.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));

        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
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
                sp = 1;
                initIndexData();
            }
        });
//        testAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (adapter.getData() == null || adapter.getData().size() <= 0) {
//                    return;
//                }
//
//                if (pageName.equals("r")) {
//                    IntentUtils.toDiscoverActivity(binding.getRoot().getContext(), position, adapter.getData(), sp, total);
//                } else {
//                    IntentUtils.toHomepageActivity(_mActivity, MValue.FROM_OTHER, (User) adapter.getData().get(position), null, null);
//                }
//
//
//            }
//        });


    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (rankUserAdapter == null || rankUserAdapter.getData() == null || rankUserAdapter.getData().size() == 0) {
            if (binding.nodataLayout.getVisibility() != View.VISIBLE) {
                binding.srytIndex.setRefreshing(true);
                initIndexData();
            }
//
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }


    private void initIndexData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.TYPE, pageName);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().rank(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RankBean>() {
            @Override
            public void onSuccess(BaseBean<RankBean> baseBean) {
                rankBean = baseBean.getData();
                total = baseBean.getData().getTotalPages();
                binding.srytIndex.setRefreshing(false);
                if (currentSelect == RANK_FILTER.WEEK) {
                    List<User> temp = new ArrayList<>();
                    temp.addAll(rankBean.getWeek());
                    initUserListData(temp);
                } else if (currentSelect == RANK_FILTER.MONTH) {
                    List<User> temp = new ArrayList<>();
                    temp.addAll(rankBean.getMonth());
                    initUserListData(temp);
                    initUserListData(baseBean.getData().getMonth());
                }
            }

            @Override
            public void onError(BaseBean<RankBean> baseBean) {

            }
        });
    }


    private void initUserListData(List<User> monthData) {
        if (rankUserAdapter == null) {
            rankUserAdapter = new RankUserAdapter(R.layout.layout_rank_item, monthData, rankBean.getRank_title());
            binding.rvList.setAdapter(rankUserAdapter);
            initFilterHeadView();
            if (monthData != null && monthData.size() > 0) {
                initHeadView(monthData);
            }
            if (monthData == null || monthData.size() == 0) {
                toNodata();
            }

//            }
        } else if (monthData == null || monthData.size() == 0) {
            toNodata();
        } else {
            initHeadView(monthData);
            binding.nodataLayout.setVisibility(View.GONE);
            rankUserAdapter.getData().clear();
            rankUserAdapter.addData(monthData);
//            rankUserAdapter.notifyDataSetChanged();
        }
    }

    private void toNodata() {
        rankUserAdapter.getData().clear();

        if (headFlag) {
            rankUserAdapter.removeHeaderView(headBinding.getRoot());

            headFlag = false;
        }

        binding.nodataLayout.setVisibility(View.VISIBLE);
        if (pageName.equals("f")) {
            binding.tvNodateIntro.setText("你还没有收藏其他人哦");
        } else {
            binding.tvNodateIntro.setText("暂时没有数据哦");
        }
    }

    private LayoutRankUsertopBinding headBinding;

    private void initFilterHeadView() {

        if (!filterHeadFlag) {
            LayoutRankFilterBinding layoutRankFilterBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_rank_filter, null, false);

            layoutRankFilterBinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.y60)));

            layoutRankFilterBinding.tvMonth.setOnClickListener((v) -> {
                layoutRankFilterBinding.tvMonth.setBackgroundResource(R.drawable.bg_rect_rank_filter);
                layoutRankFilterBinding.tvMonth.setTextColor(Color.WHITE);
                layoutRankFilterBinding.tvWeek.setBackground(null);
                layoutRankFilterBinding.tvWeek.setTextColor(ContextCompat.getColor(_mActivity, R.color.color_FF6363));
                currentSelect = RANK_FILTER.MONTH;
                List<User> temp = new ArrayList<>();
                temp.addAll(rankBean.getMonth());
                initUserListData(temp);
            });

            layoutRankFilterBinding.tvWeek.setOnClickListener((v) -> {
                layoutRankFilterBinding.tvWeek.setBackgroundResource(R.drawable.bg_rect_rank_filter);
                layoutRankFilterBinding.tvWeek.setTextColor(Color.WHITE);
                layoutRankFilterBinding.tvMonth.setBackground(null);
                layoutRankFilterBinding.tvMonth.setTextColor(ContextCompat.getColor(_mActivity, R.color.color_FF6363));
                currentSelect = RANK_FILTER.WEEK;
                List<User> temp = new ArrayList<>();
                temp.addAll(rankBean.getWeek());
                initUserListData(temp);
            });


            rankUserAdapter.addHeaderView(layoutRankFilterBinding.getRoot());
        }


    }

    private void initHeadView(List<User> topUsers) {
        if (!headFlag) {

            headBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_rank_usertop, null, false);

            headBinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.y200)));

            if (topUsers != null && topUsers.size() > 0) {
                ImageUtils.showAvatar(headBinding.avatar1, topUsers.get(0).getAvatar());
                headBinding.tv1.setText(topUsers.get(0).getNickname());
                if (topUsers.size() >= 2) {
                    ImageUtils.showAvatar(headBinding.avatar2, topUsers.get(1).getAvatar());
                    headBinding.tv2.setText(topUsers.get(1).getNickname());
                } else {
                    headBinding.v2.setVisibility(View.INVISIBLE);
                    headBinding.avatar2.setVisibility(View.INVISIBLE);
                    headBinding.numv2.setVisibility(View.INVISIBLE);
                    headBinding.tv2.setVisibility(View.INVISIBLE);
                }
                if (topUsers.size() >= 3) {
                    ImageUtils.showAvatar(headBinding.avatar3, topUsers.get(2).getAvatar());
                    headBinding.tv3.setText(topUsers.get(2).getNickname());
                } else {
                    headBinding.v3.setVisibility(View.INVISIBLE);
                    headBinding.avatar3.setVisibility(View.INVISIBLE);
                    headBinding.numv3.setVisibility(View.INVISIBLE);
                    headBinding.tv3.setVisibility(View.INVISIBLE);
                }
            }
            headFlag = true;

            rankUserAdapter.addHeaderView(headBinding.getRoot());

        } else {
            if (topUsers != null && topUsers.size() > 0) {
                ImageUtils.showAvatar(headBinding.avatar1, topUsers.get(0).getAvatar());
                headBinding.tv1.setText(topUsers.get(0).getNickname());
                if (topUsers.size() >= 2) {
                    ImageUtils.showAvatar(headBinding.avatar2, topUsers.get(1).getAvatar());
                    headBinding.tv2.setText(topUsers.get(1).getNickname());
                } else {
                    headBinding.v2.setVisibility(View.INVISIBLE);
                    headBinding.avatar2.setVisibility(View.INVISIBLE);
                    headBinding.numv2.setVisibility(View.INVISIBLE);
                    headBinding.tv2.setVisibility(View.INVISIBLE);
                }
                if (topUsers.size() >= 3) {
                    ImageUtils.showAvatar(headBinding.avatar3, topUsers.get(2).getAvatar());
                    headBinding.tv3.setText(topUsers.get(2).getNickname());
                } else {
                    headBinding.v3.setVisibility(View.INVISIBLE);
                    headBinding.avatar3.setVisibility(View.INVISIBLE);
                    headBinding.numv3.setVisibility(View.INVISIBLE);
                    headBinding.tv3.setVisibility(View.INVISIBLE);
                }
            }
        }


    }
}
