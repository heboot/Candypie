package com.gdlife.candypie.activitys.order;

import android.graphics.Color;
import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.OrderCurrency;
import com.gdlife.candypie.common.ServiceAction;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityOrderDetailBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.dialog.ServiceTipDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.order.OrderDetailBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.entity.User;
import com.heboot.event.OrderEvent;
import com.heboot.utils.DateUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/7.
 */

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {

    private String orderId;

    @Inject
    UIService uiService;

    @Inject
    OrderService orderService;

    private OrderListBean.ListBean orderBean;

    private boolean v1;

    private ServiceTipDialog serviceTipDialog;

    private TipDialog callMobileDialog;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_order_detail));
    }

    @Override
    public void initData() {
        DaggerServiceComponent.builder().build().inject(this);
        v1 = getIntent().getBooleanExtra(MKey.TYPE, false);
        orderId = getIntent().getStringExtra(MKey.USER_SERVICE_ID);
        initOrderDetailData();


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
                if (o.equals(OrderEvent.CANCEL_ORDER_ENENT)) {
                    initOrderDetailData();
                } else if (o.equals(OrderEvent.COMPLETE_ORDER_ENENT)) {
                    initOrderDetailData();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        binding.ivImg.setOnClickListener((v) -> {
            if (orderBean.getUsers() != null && orderBean.getUsers().size() > 0) {
                if (orderBean.getUsers().get(0).getId().intValue() != orderBean.getUid()) {
                    ConfigBean.ServiceItemsConfigBean.ListBean serviceBean = ThemeService.getServiceBeanById(orderBean.getService_id());
                    IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, orderBean.getUsers().get(0), MValue.HOMEPAG_FROM.ONE_ONE, serviceBean);
                } else {
                    IntentUtils.toUserInfoActivity(this, MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, orderBean.getUsers().get(0), null, null);
                }
            }
        });
        binding.tvAll.setOnClickListener((v) -> {
            IntentUtils.toServiceUserListActivity(this, orderBean.getUsers(), orderBean.getAction_config());
        });
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.btnRight.setOnClickListener((v) -> {
            orderService.doOrderAction(this, binding.btnRight.getTag().toString(), orderBean);
        });

        binding.ivMobile.setOnClickListener((v) -> {
            if (orderBean.getUsers() == null || orderBean.getUsers() == null || orderBean.getUsers().size() <= 0 || StringUtils.isEmpty(orderBean.getUsers().get(0).getTel())) {
                ToastUtils.showToast(getString(R.string.tip_call_mobile_fail));
                return;
            }
            callMobileDialog = DialogUtils.getCallMobileDialog(this, orderBean.getUsers().get(0).getTel());
            callMobileDialog.show();
        });

        binding.ivChat.setOnClickListener((v) -> {
            IntentUtils.intent2ChatActivity(this, MValue.CHAT_PRIEX + orderBean.getUsers().get(0).getId());
        });

        binding.includeAddress.getRoot().setOnClickListener((v) -> {
            IntentUtils.toChooseAddressDetailActivity(this, orderBean.getPoi(), "look");
//            IntentUtils.toHTMLActivity(this, "", ThemeService.getAddressMapURL(orderBean.getPoi().getLat(), orderBean.getPoi().getLng()), true, orderBean.getPoi().getLat(), orderBean.getPoi().getLng());
        });


    }

    private void initOrderDetailData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, orderId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().details(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<OrderDetailBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(OrderDetailBean orderDetailBean) {
                orderBean = orderDetailBean.getInfo();
                setUI(orderDetailBean);
                if (orderBean.getStatus() == MValue.ORDER_STATUS_DAICHULI && orderDetailBean.getInfo().getUid() == UserService.getInstance().getUser().getId().intValue()) {
                    serviceTipDialog = new ServiceTipDialog.Builder(OrderDetailActivity.this, new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {

                        }
                    }, MValue.TIP_DIALOG_TYPE.ONE, null, orderBean.getUsers().get(0).getAvatar()).create();
                    serviceTipDialog.show();
                }
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<OrderDetailBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(OrderDetailActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }


        }));

    }

    private void setUI(OrderDetailBean orderDetailBean) {

        ConfigBean.ServiceItemsConfigBean.ListBean listBean = ThemeService.getServiceBeanById(orderDetailBean.getInfo().getService_id());

        binding.setServiceBean(listBean);

        binding.setOrderBean(orderDetailBean.getInfo());
//
        if (orderDetailBean.getInfo() != null && orderDetailBean.getInfo().getStatus_config() != null && !StringUtils.isEmpty(orderDetailBean.getInfo().getStatus_config().getFont_color())) {
            binding.tvStatus.setTextColor(Color.parseColor(orderDetailBean.getInfo().getStatus_config().getFont_color()));
        }

        //如果是视频单子
        if (orderDetailBean.getInfo().getService_type().equals(MValue.ORDER_TYPE_VIDEO)) {
            binding.includeAddress.getRoot().setVisibility(View.GONE);

            binding.includePrice.ivPoint.setBackgroundResource(R.drawable.icon_order_money);
//            binding.includePrice.setContent(orderDetailBean.getInfo().getPrice() + " " + MAPP.mapp.getString(R.string.price_symbol));

            binding.includeTime.ivPoint.setBackgroundResource(R.drawable.icon_order_time);
//            binding.includeTime.setContent(ThemeService.getServiceOrderTimeByVideo(orderDetailBean.getInfo().getStart_time(), orderDetailBean.getInfo().getService_time()));


            if (orderDetailBean.getInfo().getCurrency().equals(OrderCurrency.coin.toString())) {
                if (orderDetailBean.getInfo().getStatus() == MValue.ORDER_LIST_STATUS_YIWANCHENG) {
                    binding.includePrice.setContent(orderDetailBean.getInfo().getPrice() + " " + MAPP.mapp.getString(R.string.unit_coin));
                } else {
                    binding.includePrice.setContent(orderDetailBean.getInfo().getPrice() + MAPP.mapp.getString(R.string.unit_coin) + "  /  " + MAPP.mapp.getString(R.string.unit_minute));
                }

            } else if (orderDetailBean.getInfo().getCurrency().equals(OrderCurrency.money.toString())) {
                binding.includePrice.setContent(orderDetailBean.getInfo().getPrice() + MAPP.mapp.getString(R.string.price_unit));
            }

            binding.includeTime.setContent(ThemeService.getVideoOrderTimeText(orderDetailBean.getInfo().getStatus(), orderDetailBean.getInfo().getStart_time(), orderDetailBean.getInfo().getService_time()));


        } else {
            binding.includeAddress.ivPoint.setBackgroundResource(R.drawable.icon_order_address);
//            binding.includeAddress.setContent(orderDetailBean.getInfo().getPoi() == null ? "" : orderDetailBean.getInfo().getPoi().getAddress());

            binding.includeAddress.setContent(orderDetailBean.getInfo().getPoi().getName() + "。");
            binding.includeAddress.tvTitle.setText(ThemeService.getOrderAddressSpannable(binding.getRoot().getContext(), binding.includeAddress.getContent()));

            binding.includePrice.ivPoint.setBackgroundResource(R.drawable.icon_order_money);
            binding.includePrice.setContent(orderDetailBean.getInfo().getPrice() + " " + MAPP.mapp.getString(R.string.price_symbol));

            binding.includeTime.ivPoint.setBackgroundResource(R.drawable.icon_order_time);
            binding.includeTime.setContent(ThemeService.getServiceOrderTime(orderDetailBean.getInfo().getStart_time(), orderDetailBean.getInfo().getEnd_time(), orderDetailBean.getInfo().getService_time()));
        }


        if (orderDetailBean.getInfo().getUsers() != null && orderDetailBean.getInfo().getUsers().size() > 0) {
            if (orderDetailBean.getInfo().getUsers().size() == 1) {
                binding.tvName.setText(orderDetailBean.getInfo().getUsers().get(0).getNickname());
            } else {
                int index = 1;
                for (User user : orderDetailBean.getInfo().getUsers()) {
                    if (index < orderDetailBean.getInfo().getUsers().size()) {
                        binding.tvName.append(user.getNickname() + "、");
                    } else {
                        binding.tvName.append(user.getNickname());
                    }
                    index = index + 1;
                }
            }

        }
        if (orderDetailBean.getInfo().getUsers() != null && orderDetailBean.getInfo().getUsers().size() == 1) {
//            binding.ivSex.setBackgroundResource(orderDetailBean.getInfo().getUsers().get(0).getSex() == 0 ? R.drawable.icon_sex_woman : R.drawable.icon_sex_man);
            binding.includeSexage.setUser(orderDetailBean.getInfo().getUsers().get(0));
        }

        if (orderDetailBean.getInfo().getExtend() != null && !StringUtils.isEmpty(orderDetailBean.getInfo().getExtend().getRemark())) {
            binding.includeRemark.getRoot().setVisibility(View.VISIBLE);
            binding.includeRemark.ivPoint.setBackgroundResource(R.drawable.icon_order_remark);
            binding.includeRemark.tvTitle.setText(orderDetailBean.getInfo().getExtend().getRemark());
        } else {
            binding.includeRemark.getRoot().setVisibility(View.GONE);
        }

//


        if (orderDetailBean.getInfo().getAction_config() != null && orderDetailBean.getInfo().getAction_config().size() > 0) {
            for (OrderListBean.ListBean.ActionConfigBean actionConfigBean : orderDetailBean.getInfo().getAction_config()) {
                if (actionConfigBean.getAction().equals(ServiceAction.TEL.toString().toLowerCase())) {
                    binding.ivMobile.setVisibility(View.VISIBLE);
                } else if (actionConfigBean.getAction().equals(ServiceAction.IM.toString().toLowerCase())) {
                    binding.ivChat.setVisibility(View.VISIBLE);
                } else {

                    binding.vBottomContainer.setVisibility(View.VISIBLE);
                    binding.btnRight.setVisibility(View.VISIBLE);
//                binding.btnLeft.setVisibility(View.GONE);

                    binding.vBottomContainer.setVisibility(View.VISIBLE);
                    binding.btnRight.setVisibility(View.VISIBLE);
                    binding.btnRight.setText(actionConfigBean.getValue());
                    binding.btnRight.setTag(actionConfigBean.getAction());
                    if (!StringUtils.isEmpty(actionConfigBean.getColor())) {
                        binding.btnRight.setBackground(uiService.getOrderOptionBG(actionConfigBean.getColor()));
                        binding.btnRight.setTextColor(Color.WHITE);
                    }

                }
            }
        } else {
            binding.btnRight.setVisibility(View.GONE);
            binding.vBottomContainer.setVisibility(View.GONE);
        }

        //评论模块
        if (orderDetailBean.getInfo().getService_eval_tags() != null && orderDetailBean.getInfo().getService_eval_tags().getTags() != null && orderDetailBean.getInfo().getService_eval_tags().getTags().size() > 0) {
            binding.includeCommentTagsView.getRoot().setVisibility(View.VISIBLE);
            binding.includeCommentTagsView.tvTitle.setText(orderDetailBean.getInfo().getService_eval_tags().getTitle());
            binding.includeCommentTagsView.tvTime.setText(DateUtil.getRecommendUserInterval(Long.parseLong(orderDetailBean.getInfo().getService_eval_tags().getEval_time()) * 1000L));
            uiService.initTagsLayout(orderDetailBean.getInfo().getService_eval_tags().getTags(), binding.includeCommentTagsView.qflContainer, getResources().getDimensionPixelOffset(R.dimen.y12), getResources().getDimensionPixelOffset(R.dimen.x14), false, getResources().getDimensionPixelOffset(R.dimen.y30), null);
        } else {
            binding.includeCommentTagsView.getRoot().setVisibility(View.GONE);
        }

    }


    @Override
    protected void onDestroy() {
//        DialogUtils.dimissDialog(callMobileDialog);
//        DialogUtils.dimissDialog(serviceTipDialog);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }
}
