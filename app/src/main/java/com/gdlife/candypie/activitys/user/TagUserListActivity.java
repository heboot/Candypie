package com.gdlife.candypie.activitys.user;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.IndexUsersAdapter3;
import com.gdlife.candypie.adapter.search.SearchTagUsersAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentTagUserListBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.rv.IndexUserWhite3ItemDecoration;
import com.gdlife.candypie.widget.rv.SearchUserWhite3ItemDecoration;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.IndexV5Bean;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TagUserListActivity extends BaseActivity<FragmentTagUserListBinding> {

    private String title;

    private String tag_id;

    private IndexV5Bean indexBean;
    private SearchTagUsersAdapter testAdapter;

    private GridLayoutManager staggeredGridLayoutManager;

    private UIService uiService = new UIService();

    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tag_user_list;
    }

    @Override
    public void initUI() {
        title = (String) getIntent().getExtras().get(MKey.TITLE);
        tag_id = (String) getIntent().getExtras().get(MKey.TAG_ID);
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(title);
    }

    @Override
    public void initData() {
        staggeredGridLayoutManager = new GridLayoutManager(this, 2);

        binding.rvList.setLayoutManager(staggeredGridLayoutManager);

        binding.rvList.addItemDecoration(new SearchUserWhite3ItemDecoration(2, getResources().getDimensionPixelOffset(R.dimen.x3), true));


        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);

        initIndexData();
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
        requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (sp >= total) {
                    testAdapter.loadMoreEnd();
//                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    return;
                }
                sp = sp + 1;
                initIndexData();
            }
        };
    }

    private void initIndexData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.TAG_ID, tag_id);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().tag_user_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<IndexV5Bean>() {
            @Override
            public void onSuccess(BaseBean<IndexV5Bean> baseBean) {
                indexBean = baseBean.getData();
                total = indexBean.getTotalPages();
                binding.srytIndex.setRefreshing(false);
                if (testAdapter != null) {
                    testAdapter.loadMoreComplete();
                    testAdapter.disableLoadMoreIfNotFullPage();
                }
                if (sp == 1 && testAdapter == null) {
                    testAdapter = new SearchTagUsersAdapter(R.layout.item_search_user_tags, indexBean.getList(), "a");
                    binding.rvList.setAdapter(testAdapter);
                    testAdapter.setOnLoadMoreListener(requestLoadMoreListener, binding.rvList);
                } else {
                    if (sp == 1) {
                        if (indexBean.getList() == null || indexBean.getList().size() == 0) {
                            testAdapter.setEmptyView(uiService.getEmptyViewByOrder("暂时没有数据哦"));
                        }
                        testAdapter.getData().clear();
                        testAdapter.addData(indexBean.getList());
                    } else {
                        testAdapter.addData(indexBean.getList());
                    }

                }


            }

            @Override
            public void onError(BaseBean<IndexV5Bean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(TagUserListActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


}
