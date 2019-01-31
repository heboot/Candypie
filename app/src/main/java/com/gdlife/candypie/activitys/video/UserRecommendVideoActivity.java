package com.gdlife.candypie.activitys.video;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.IndexUsersAdapter3;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.CustomEvent;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityRecommendVideosBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.CustomEventUtil;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.rv.IndexUserWhite3ItemDecoration;
import com.heboot.base.BaseBean;
import com.heboot.bean.user.RecommendServicerListBean;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserRecommendVideoActivity extends BaseActivity<ActivityRecommendVideosBinding> {

    private IndexUsersAdapter3 testAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend_videos;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText("视频动态");

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvList.addItemDecoration(new IndexUserWhite3ItemDecoration(2, getResources().getDimensionPixelOffset(R.dimen.x3), false));


        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);

    }

    @Override
    public void initData() {
        testAdapter = new IndexUsersAdapter3(R.layout.layout_index_user, new ArrayList<>(), "r");
        binding.rvList.setAdapter(testAdapter);
        initRecommendVideos();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initRecommendVideos();
            }
        });
        testAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CustomEventUtil.onEvent(CustomEvent.INDEX_CLICK_USER_R);
                IntentUtils.toDiscoverActivity(binding.getRoot().getContext(), position, adapter.getData(), sp, total);
            }
        });

        testAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (sp >= total) {
                    testAdapter.loadMoreEnd();
//                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    return;
                }
                sp = sp + 1;
                initRecommendVideos();
            }
        }, binding.rvList);

        testAdapter.setPreLoadNumber(2);

    }

    private void initRecommendVideos() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().user_video_recommend_index(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RecommendServicerListBean>() {
            @Override
            public void onSuccess(BaseBean<RecommendServicerListBean> baseBean) {
                total = baseBean.getData().getTotalPages();
                binding.srytIndex.setRefreshing(false);
                testAdapter.loadMoreComplete();
                testAdapter.disableLoadMoreIfNotFullPage();

                binding.plytContainer.toMain();
                if (sp == 1 && testAdapter == null) {
                    testAdapter = new IndexUsersAdapter3(R.layout.layout_index_user, baseBean.getData().getList(), "r");
                    binding.rvList.setAdapter(testAdapter);
                } else {
                    if (sp == 1) {


                        testAdapter.getData().clear();
                        testAdapter.addData(baseBean.getData().getList());
                    } else {
                        testAdapter.addData(baseBean.getData().getList());
                    }

                }
            }

            @Override
            public void onError(BaseBean<RecommendServicerListBean> baseBean) {

            }
        });
    }
}
