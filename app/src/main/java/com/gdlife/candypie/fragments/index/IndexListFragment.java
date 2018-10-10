package com.gdlife.candypie.fragments.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.TestActivity;
import com.gdlife.candypie.adapter.index.IndexUsersAdapter3;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.CustomEvent;
import com.gdlife.candypie.common.IndexTopADType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.databinding.FragmentIndexListBinding;
import com.gdlife.candypie.databinding.IncludeNodataInfoBinding;
import com.gdlife.candypie.databinding.LayoutIndexUserTopBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.index.ADService;
import com.gdlife.candypie.utils.CustomEventUtil;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.GlideImageLoaderByIndexTop;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.dialog.index.DongtaiServicerTipDialog;
import com.gdlife.candypie.widget.index.FreeVideoDialog;
import com.gdlife.candypie.widget.index.FreeVideoRecommendDialog;
import com.gdlife.candypie.widget.rv.IndexUserWhite3ItemDecoration;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.bean.index.IndexV5Bean;
import com.heboot.entity.User;
import com.heboot.event.DiscoverEvent;
import com.heboot.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.yalantis.dialog.TipCustomDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class IndexListFragment extends BaseFragment<FragmentIndexListBinding> {

    private IndexUsersAdapter3 testAdapter;

    private GridLayoutManager staggeredGridLayoutManager;

    private View headView;

    private boolean headFlag = false;

    private FreeVideoDialog freeVideoDialog;

    private Consumer freeDialogConsumer;

    private IndexV5Bean indexBean;

    private TipCustomDialog womenTipDialog;

    private String pageName;

    public static IndexListFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString(MKey.NAME, name);
        IndexListFragment fragment = new IndexListFragment();
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

        staggeredGridLayoutManager = new GridLayoutManager(_mActivity, 2);

        binding.rvList.setLayoutManager(staggeredGridLayoutManager);

        if (pageName.equals("a")) {
            binding.rvList.addItemDecoration(new IndexUserWhite3ItemDecoration(2, getResources().getDimensionPixelOffset(R.dimen.x3), true));
        } else {
            binding.rvList.addItemDecoration(new IndexUserWhite3ItemDecoration(2, getResources().getDimensionPixelOffset(R.dimen.x3), false));
        }


        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);

    }

    @Override
    public void initData() {
        if (pageName.equals("r")) {
            testAdapter = new IndexUsersAdapter3(R.layout.layout_index_user, new ArrayList<>(), pageName);
        } else {
            testAdapter = new IndexUsersAdapter3(R.layout.layout_index_user_huoyue, new ArrayList<>(), pageName);
        }

        binding.rvList.setAdapter(testAdapter);
        freeDialogConsumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (indexBean.getPopup_tip().getAction().equals(IndexTopADType.user_video_recommend_index.toString())) {
                    IntentUtils.toDiscoverActivity(getContext(), 0, testAdapter.getData(), sp, total);
                } else {
                    ADService.doADAction(getContext(), indexBean.getPopup_tip());
                }
            }
        };

    }

    private boolean move = false;

    @Override
    public void initListener() {
        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    move = false;
                }
//                staggeredGridLayoutManager.invalidateSpanAssignments();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int test = staggeredGridLayoutManager.findFirstVisibleItemPosition();


                if (test == 1) {
                    if (dy > 0) {
                        int re = getResources().getDimensionPixelOffset(R.dimen.y179);

                        if (Math.abs(recyclerView.getChildAt(0).getY()) > re) {
                            if (binding.ivZz.getVisibility() == View.GONE) {
                                binding.ivZz.setVisibility(View.VISIBLE);

                            }
                        }
                    } else {

                        int re = getResources().getDimensionPixelOffset(R.dimen.y149);

                        if (recyclerView.getChildAt(0).getY() < re) {
                            if (binding.ivZz.getVisibility() == View.VISIBLE) {
                                binding.ivZz.setVisibility(View.GONE);
                            }
                        }
//
                    }
                } else if (test > 1) {
                    if (binding.ivZz.getVisibility() == View.GONE) {
                        binding.ivZz.setVisibility(View.VISIBLE);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o instanceof DiscoverEvent.DiscoverUpdatePositionEvent) {
                    if (pageName.equals("r")) {
                        move = true;
                        binding.rvList.smoothScrollToPosition(((DiscoverEvent.DiscoverUpdatePositionEvent) o).getPosition());
                        move(((DiscoverEvent.DiscoverUpdatePositionEvent) o).getPosition());
                    }
                } else if (o instanceof DiscoverEvent.DiscoverUpdateDatasEvent) {
                    if (pageName.equals("r")) {
                        move = true;
                        sp = ((DiscoverEvent.DiscoverUpdateDatasEvent) o).getCurrentSp();
//                        testAdapter.getData().addAll(((DiscoverEvent.DiscoverUpdateDatasEvent) o).getUsres());
//                        testAdapter.notifyDataSetChanged();
                        binding.rvList.smoothScrollToPosition(((DiscoverEvent.DiscoverUpdateDatasEvent) o).getPosition());
                        move(((DiscoverEvent.DiscoverUpdateDatasEvent) o).getPosition());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initIndexData();
            }
        });
        testAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getData() == null || adapter.getData().size() <= 0) {
                    return;
                }

                if (pageName.equals("r")) {
                    CustomEventUtil.onEvent(CustomEvent.INDEX_CLICK_USER_R);
                    IntentUtils.toDiscoverActivity(binding.getRoot().getContext(), position, adapter.getData(), sp, total);
                } else {
                    if (pageName.equals("a")) {
                        CustomEventUtil.onEvent(CustomEvent.INDEX_CLICK_USER_A);
                    } else if (pageName.equals("n")) {
                        CustomEventUtil.onEvent(CustomEvent.INDEX_CLICK_USER_N);
                    }
                    IntentUtils.toHomepageActivity(_mActivity, MValue.FROM_OTHER, (User) adapter.getData().get(position), null, null);
                }
//                Intent intent = new Intent(_mActivity, TestActivity.class);
//                _mActivity.startActivity(intent);

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
                initIndexData();
            }
        }, binding.rvList);

        testAdapter.setPreLoadNumber(2);

    }

    private DongtaiServicerTipDialog dongtaiServicerTipDialog;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (testAdapter == null || testAdapter.getData() == null || testAdapter.getData().size() == 0) {
            if (binding.nodataLayout.getVisibility() != View.VISIBLE) {
                binding.srytIndex.setRefreshing(true);
                initIndexData();
            }
        }

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    private void move(int pi) {
        binding.rvList.scrollBy(0, getResources().getDimensionPixelOffset(R.dimen.y200));
    }

    private void initIndexData() {
        if (move) {
            binding.srytIndex.setRefreshing(false);
            testAdapter.loadMoreComplete();
            return;
        }
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.TYPE, pageName);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().index_v8(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<IndexV5Bean>() {
            @Override
            public void onSuccess(BaseBean<IndexV5Bean> baseBean) {
                indexBean = baseBean.getData();
                total = indexBean.getTotalPages();
                binding.srytIndex.setRefreshing(false);
                testAdapter.loadMoreComplete();
//                testAdapter.loadMoreEnd();
                testAdapter.disableLoadMoreIfNotFullPage();
                if (sp == 1 && testAdapter == null) {
                    testAdapter = new IndexUsersAdapter3(R.layout.layout_index_user, indexBean.getList(), pageName);
                    binding.rvList.setAdapter(testAdapter);
                } else {
                    if (sp == 1) {
                        if (!headFlag) {
                            if (indexBean.getTop_ad_list() != null && indexBean.getTop_ad_list().size() > 0) {
                                initHeadView(indexBean.getTop_ad_list());
                            }
                            headFlag = true;
                        }

                        if (indexBean.getList() == null || indexBean.getList().size() == 0) {
//                            testAdapter.setEmptyView(getEmptyView());
                            binding.nodataLayout.setVisibility(View.VISIBLE);
                            if (pageName.equals("f")) {
                                binding.tvNodateIntro.setText("你还没有收藏其他人哦");
                            } else {
                                binding.tvNodateIntro.setText("暂时没有数据哦");
                            }
                        } else {
                            binding.nodataLayout.setVisibility(View.GONE);
                        }

                        testAdapter.getData().clear();
                        testAdapter.addData(indexBean.getList());
                    } else {
                        testAdapter.addData(indexBean.getList());
                    }

                }

                //免费聊
                if (indexBean.getRecommend_config() != null && indexBean.getRecommend_config().getList() != null) {
                    FreeVideoRecommendDialog freeVideoRecommendDialog = new FreeVideoRecommendDialog.Builder(_mActivity, indexBean.getRecommend_config(), new WeakReference(_mActivity)).create();
                    freeVideoRecommendDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (UserService.getInstance().getFirstIndex()) {
                            }
                        }
                    });
                    freeVideoRecommendDialog.show();
                }

                //首页弹窗
                if (indexBean.getPopup_tip() != null) {
                    freeVideoDialog = new FreeVideoDialog.Builder(_mActivity, indexBean.getPopup_tip(), freeDialogConsumer).create();
                    freeVideoDialog.show();
                }

                if (indexBean.getService_auth() == 1) {
                    freeVideoDialog = new FreeVideoDialog.Builder(_mActivity, indexBean.getPopup_tip(), freeDialogConsumer).create();
                    freeVideoDialog.show();
                }

                if (indexBean.getService_auth_tip() == 1) {
                    if (womenTipDialog == null) {
                        womenTipDialog = new TipCustomDialog.Builder(_mActivity, new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                if (integer == 0) {
                                    womenTipDialog.dismiss();
                                } else {
                                    IntentUtils.toAuthIndexActivity(_mActivity, RecordVideoFrom.AUTH);
                                }
                            }
                        }, "终于等到你。完成视频认证，躺着也能赚钱啦", "知道了", "去认证").create();
                    }
                    womenTipDialog.show();
                }


                //上传视频弹出
                if (pageName.equals("r")) {
                    if (UserService.getInstance().isServicer() && indexBean.getVideo_tip() == 1) {
                        if (dongtaiServicerTipDialog == null) {
                            dongtaiServicerTipDialog = new DongtaiServicerTipDialog();
                        }
                        if (!dongtaiServicerTipDialog.isAdded()) {
                            dongtaiServicerTipDialog.show(getFragmentManager(), "");
                        }

                    }
                }
            }


            @Override
            public void onError(BaseBean<IndexV5Bean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(_mActivity, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }

    private void initNormalHeadView() {
//        LayoutIndexlistTopBinding headBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_indexlist_top, null, false);
//        testAdapter.addHeaderView(headBinding.getRoot());
    }

    private View getEmptyView() {
        IncludeNodataInfoBinding includeNodataInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(_mActivity), R.layout.include_nodata_info, null, false);
        if (pageName.equals("f")) {
            includeNodataInfoBinding.tvNodateIntro.setText("您还没有收藏哦");
        }
        return includeNodataInfoBinding.getRoot();
    }


    private void initHeadView(List<IndexPopTipBean> topAdListBean) {

        LayoutIndexUserTopBinding headBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_index_user_top, null, false);

        List<IndexPopTipBean> images = new ArrayList<>();

        for (IndexPopTipBean bean : topAdListBean) {
            images.add(bean);
        }

        //设置图片加载器
        headBinding.ivBanner.setImageLoader(new GlideImageLoaderByIndexTop());
        //设置图片集合
        headBinding.ivBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        headBinding.ivBanner.start();

        headView = headBinding.getRoot();

//        binding.rvList.addHeaderView(headView);
        testAdapter.addHeaderView(headView);


    }
}
