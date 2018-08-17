package com.gdlife.candypie.fragments.index;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.ActiveAdapter;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentActiveUserlistBinding;
import com.gdlife.candypie.databinding.LayoutIndexActiveHeadBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.index.ADService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.ActiveUserListBean;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.bean.index.VisitListBean;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IndexActiveFragment extends BaseFragment<FragmentActiveUserlistBinding> {

    private ActiveAdapter activeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_active_userlist;
    }

    @Override
    public void initUI() {
        binding.rList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rList.setPullRefreshEnabled(false);

        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);

        binding.rList.setLoadingMoreProgressStyle(ProgressStyle.BallGridPulse);

    }

    @Override
    public void initData() {
        initRechargeConfig();
    }

    @Override
    public void initListener() {
        binding.rList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {


                if (sp >= total) {
                    binding.rList.loadMoreComplete();
                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    return;
                }
                sp = sp + 1;
                initRechargeConfig();
            }
        });
        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initRechargeConfig();
            }
        });
    }

    private void initRechargeConfig() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, 20);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().active_user_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ActiveUserListBean>() {
            @Override
            public void onSuccess(BaseBean<ActiveUserListBean> baseBean) {
                total = baseBean.getData().getTotalPages();
                binding.srytIndex.setRefreshing(false);
                if (sp == 1 && activeAdapter == null) {

                    activeAdapter = new ActiveAdapter(baseBean.getData().getList());
                    binding.rList.setAdapter(activeAdapter);

                    if (baseBean.getData() != null && baseBean.getData().getTop_ad_list() != null) {
                        initHeadView(baseBean.getData().getTop_ad_list().get(0), baseBean.getData().getVisit_list());
                    } else if (baseBean.getData().getVisit_list() != null && baseBean.getData().getVisit_list().getList() != null) {
                        initHeadView(null, baseBean.getData().getVisit_list());
                    }


                } else {
                    binding.rList.loadMoreComplete();
                    if (sp == 1) {
                        activeAdapter.getData().clear();
                    }
                    activeAdapter.getData().addAll(baseBean.getData().getList());
                    activeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(BaseBean<ActiveUserListBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


    private View headView;

    private void initHeadView(IndexPopTipBean topAdListBean, VisitListBean visitListBean) {
        LayoutIndexActiveHeadBinding cbinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.layout_index_active_head, null, false);
        if (visitListBean != null && visitListBean.getList() != null && visitListBean.getList().size() > 0) {
            cbinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.y160)));
        } else {
            cbinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.y80)));
        }

        binding.getRoot().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_EDEDF3));

        if (topAdListBean != null) {
            ImageUtils.showImage(cbinding.ivBg, topAdListBean.getImg());

            cbinding.ivBg.setOnClickListener((v) -> {
                ADService.doADAction(getActivity(), topAdListBean);
            });

        }


        if (visitListBean != null && visitListBean.getList() != null && visitListBean.getList().size() > 0) {

            cbinding.includeIndexVisit.getRoot().setVisibility(View.VISIBLE);

            cbinding.vb.setVisibility(View.VISIBLE);

            cbinding.includeIndexVisit.rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });
            cbinding.includeIndexVisit.rvList.setAdapter(new IndexVisitAdapter(visitListBean.getList(), UserService.getInstance().getUser()));

            cbinding.includeIndexVisit.tvTitle.setText(visitListBean.getTitle());

            cbinding.includeIndexVisit.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserVisitActivity(getActivity(), String.valueOf(UserService.getInstance().getUser().getId()));
            });
        } else {
            cbinding.includeIndexVisit.getRoot().setVisibility(View.GONE);

            cbinding.vb.setVisibility(View.GONE);
        }


        headView = cbinding.getRoot();


        binding.rList.addHeaderView(headView);
    }
}
