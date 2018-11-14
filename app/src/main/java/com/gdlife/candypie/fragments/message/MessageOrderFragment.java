package com.gdlife.candypie.fragments.message;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.message.MessageOrderAdapter;
import com.gdlife.candypie.adapter.message.MessageVideoOrderListAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.databinding.FragmentMessageOrderBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.theme.ApplyOrderBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.bean.videochat.VideoChatOrderBean;
import com.heboot.utils.PreferencesUtils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageOrderFragment extends BaseFragment<FragmentMessageOrderBinding> {

    private MessageVideoOrderListAdapter messageOrderAdapter;

    private PermissionUtils permissionUtils;

    private ServerService serverService = new ServerService();

    private UIService uiService = new UIService();


    public static MessageOrderFragment newInstance() {
        Bundle args = new Bundle();
        MessageOrderFragment fragment = new MessageOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_order;
    }

    @Override
    public void initUI() {
        permissionUtils = new PermissionUtils();
        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);
        binding.rList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (messageOrderAdapter == null || messageOrderAdapter.getData() == null || messageOrderAdapter.getData().size() == 0) {
//            if (binding.nodataLayout.getVisibility() != View.VISIBLE) {
            binding.srytIndex.setRefreshing(true);
            initOrders();
        }
//
//        }

    }

    @Override
    public void initData() {
        messageOrderAdapter = new MessageVideoOrderListAdapter(R.layout.item_video_order, new ArrayList<>());
        binding.rList.setAdapter(messageOrderAdapter);
    }

    @Override
    public void initListener() {

        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initOrders();

            }
        });

        messageOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (sp >= total) {
                    messageOrderAdapter.loadMoreEnd();
                    return;
                }
                sp = sp + 1;
                initOrders();
            }
        }, binding.rList);

    }

    private void initOrders() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().video_chat_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<VideoChatOrderBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(VideoChatOrderBean orderListBean) {


                total = orderListBean.getTotalPages();
                pageSize = orderListBean.getPageSize();
                if (orderListBean.getSp() == 1) {
                    binding.srytIndex.setRefreshing(false);
                } else {
                    messageOrderAdapter.loadMoreComplete();
                }

                if (orderListBean != null && orderListBean.getList() != null && orderListBean.getList().size() > 0) {
                    if (messageOrderAdapter == null) {
                        messageOrderAdapter = new MessageVideoOrderListAdapter(R.layout.item_video_order, orderListBean.getList());
                        binding.rList.setAdapter(messageOrderAdapter);
                    } else {
                        if (sp == 1) {
                            messageOrderAdapter.getData().clear();
                            messageOrderAdapter.notifyDataSetChanged();
                        }
//                        for (OrderListBean.ListBean listBean : orderListBean.getList()) {
//                            indexOrderAdapter.doAdd(listBean);
//                        }
                        messageOrderAdapter.addData(orderListBean.getList());
                    }

                } else {
                    if (messageOrderAdapter != null) {
                        if (messageOrderAdapter.getData() != null) {
                            messageOrderAdapter.getData().clear();
                            messageOrderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        messageOrderAdapter = new MessageVideoOrderListAdapter(R.layout.item_order_rob, new ArrayList<>());
                        binding.rList.setAdapter(messageOrderAdapter);
                    }

                    messageOrderAdapter.setEmptyView(uiService.getEmptyViewByOrder("目前没有新的订单"));

//                    binding.nodataLayout.setVisibility(View.VISIBLE);
//                    binding.tvNodateIntro.setText("目前没有新的订单");
//                    binding.rvList.setVisibility(View.GONE);
//                    binding.plytContainer.toNoData();
                }


            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<VideoChatOrderBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(getContext(), basebean.getMessage(), true);
                tipDialog.show();
            }


        }));
    }


    public void apply(CheckBox textView, String userServiceId, OrderListBean.ListBean s) {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().apply(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ApplyOrderBean>() {
            @Override
            public void onSuccess(BaseBean<ApplyOrderBean> baseBean) {
                ApplyOrderBean applyOrderBean = baseBean.getData();
                initOrders();

                textView.setText(getString(R.string.rob_order_ed));
                textView.setSelected(false);
                textView.setChecked(false);
                textView.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_58586C));


//                money.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                if (applyOrderBean.getChat_room_config() != null && !StringUtils.isEmpty(applyOrderBean.getChat_room_config().getChannel_key())) {
                    IntentUtils.toVideoChatActivity(getContext(), userServiceId, applyOrderBean.getChat_room_config(), UserService.getInstance().isServicer() ? VideoChatFrom.SERVICER : VideoChatFrom.USER, false);
                } else if (s.getStatus() == MValue.ORDER_STATUS_DAICHULI) {
                    IntentUtils.intent2ChatActivity(getContext(), MValue.CHAT_PRIEX + s.getUser().getId());
                }

            }

            @Override
            public void onError(BaseBean<ApplyOrderBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(getContext(), baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


}
