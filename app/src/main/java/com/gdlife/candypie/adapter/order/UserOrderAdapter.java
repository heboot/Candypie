package com.gdlife.candypie.adapter.order;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.OrderCurrency;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ItemOrderBinding;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by heboot on 2018/3/6.
 */

public class UserOrderAdapter extends BaseRecyclerViewAdapter {

    @Inject
    OrderService orderService;

    private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

    public UserOrderAdapter(List<OrderListBean.ListBean> orderListBeanList) {
        DaggerServiceComponent.builder().build().inject(this);
        data = orderListBeanList;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new UserOrderAdapter.ViewHolder(parent, R.layout.item_order);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<OrderListBean.ListBean, ItemOrderBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final OrderListBean.ListBean s, int position) {

            orderService.setOrderBottomOption(binding.getRoot().getContext(), binding.btnLeft, binding.btnCenter, binding.btnRight, s);

            binding.setOrderBean(s);

            binding.includeOrderContent.setOrderBean(s);


            binding.includeOrderContent.setUser(s.getUsers() != null && s.getUsers().size() > 0 ? s.getUsers().get(0) : null);
            binding.includeOrderContent.includeSexage.setUser(s.getUsers() != null && s.getUsers().size() > 0 ? s.getUsers().get(0) : null);
            if (s.getStatus_config() != null && StringUtils.isEmpty(s.getStatus_config().getFont_color())) {
                binding.includeOrderContent.tvStatus.setTextColor(Color.parseColor(s.getStatus_config().getFont_color()));
            }

            listBean = ThemeService.getServiceBeanById(s.getService_id());

            if (listBean == null) {
                listBean = new ConfigBean.ServiceItemsConfigBean.ListBean();
                listBean.setType("out");
                listBean.setTitle(MAPP.mapp.getString(R.string.service_out));
            }

            if (listBean.getType().equals(MValue.ORDER_TYPE_VIDEO)) {
                binding.includeOrderContent.includeTitle.ivPoint.setBackgroundResource(R.drawable.icon_order_service);
                binding.includeOrderContent.includeTitle.setContent(listBean.getTitle());

                binding.includeOrderContent.includeAddress.ivPoint.setBackgroundResource(R.drawable.icon_order_address);
                binding.includeOrderContent.includeAddress.setContent(MAPP.mapp.getString(R.string.theme_choose_address_unlimited));

                binding.includeOrderContent.includePrice.ivPoint.setBackgroundResource(R.drawable.icon_order_money);

                if (s.getCurrency().equals(OrderCurrency.coin.toString())) {
                    if (s.getStatus() == MValue.ORDER_LIST_STATUS_YIWANCHENG) {
                        binding.includeOrderContent.includePrice.setContent(s.getAmount() + " " + MAPP.mapp.getString(R.string.unit_coin));
                    } else {
                        binding.includeOrderContent.includePrice.setContent(s.getAmount() + MAPP.mapp.getString(R.string.unit_coin) + "  /  " + MAPP.mapp.getString(R.string.unit_minute));
                    }

                } else if (s.getCurrency().equals(OrderCurrency.money.toString())) {
                    binding.includeOrderContent.includePrice.setContent(s.getPrice() + MAPP.mapp.getString(R.string.price_unit));
                }

                binding.includeOrderContent.includePrice.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                ViewUtils.setViewHeight(binding.includeOrderContent.includeTime.getRoot(), 1);

                binding.includeOrderContent.includeTitle.tvTitle.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_343445));


                binding.includeOrderContent.includeTime.ivPoint.setBackgroundResource(R.drawable.icon_order_time);

                binding.includeOrderContent.includeTime.setContent(ThemeService.getVideoOrderTimeText(s.getStatus(), s.getStart_time(), s.getService_time()));


            } else {

                binding.includeOrderContent.includePrice.getRoot().setVisibility(View.VISIBLE);

                binding.includeOrderContent.includeTime.getRoot().setVisibility(View.VISIBLE);


                binding.includeOrderContent.includeAddress.ivPoint.setBackgroundResource(R.drawable.icon_order_address);
                if (s.getPoi() != null) {
                    binding.includeOrderContent.includeAddress.setContent(s.getPoi().getAddress());
                }


                binding.includeOrderContent.includePrice.ivPoint.setBackgroundResource(R.drawable.icon_order_money);
                binding.includeOrderContent.includePrice.setContent(s.getPrice() + " " + MAPP.mapp.getString(R.string.price_symbol));
//
                binding.includeOrderContent.includeTitle.ivPoint.setBackgroundResource(R.drawable.icon_order_service);
                binding.includeOrderContent.includeTitle.setContent(listBean.getTitle());

                binding.includeOrderContent.includeTime.ivPoint.setBackgroundResource(R.drawable.icon_order_time);
                binding.includeOrderContent.includeTime.setContent(ThemeService.getServiceOrderTime(s.getStart_time(), s.getEnd_time(), s.getService_time()));


                binding.includeOrderContent.includeTitle.tvTitle.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_343445));

            }

            binding.includeOrderContent.includePrice.tvTitle.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_FF5252));


            binding.includeOrderContent.ivImg.setOnClickListener((v) -> {
                if (listBean.getType().equals("out")) {
                    return;
                }
                if (s.getUsers() != null && s.getUsers().size() > 0) {
                    //uid是发单人
//                    if (s.getUsers().get(0).getId().intValue() != s.getUid()) {//  if (s.getUsers().get(0).getRole() == MValue.USER_ROLE_SERVICER) {
//                        IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s.getUsers().get(0), MValue.HOMEPAG_FROM.ONE_ONE, null);
//                    } else {
//                        IntentUtils.toUserInfoActivity(v.getContext(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s.getUsers().get(0), null, null);
//                    }

                    IntentUtils.toUserPageActivity(MAPP.mapp.getCurrentActivity(), String.valueOf(s.getUsers().get(0).getId()));
                }


            });

            binding.getRoot().setOnClickListener((v) -> {
                if (listBean.getType().equals("out")) {
                    return;
                }
                orderService.toOrderDetail(binding.getRoot().getContext(), UserService.getInstance().getUser(), s.getStatus(), s.getId());
            });
        }


    }
}
