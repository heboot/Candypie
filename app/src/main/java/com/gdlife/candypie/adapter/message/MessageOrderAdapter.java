package com.gdlife.candypie.adapter.message;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemOrderRobBinding;
import com.gdlife.candypie.fragments.message.MessageOrderFragment;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.utils.DateUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class MessageOrderAdapter extends BaseQuickAdapter<OrderListBean.ListBean, BaseViewHolder> {


    private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

    private WeakReference<MessageOrderFragment> weakReference;

    public MessageOrderAdapter(int layoutResId, List<OrderListBean.ListBean> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListBean.ListBean s) {
        ItemOrderRobBinding binding = DataBindingUtil.bind(helper.itemView);

        listBean = ThemeService.getServiceBeanById(s.getService_id());

        ImageUtils.showAvatar(binding.ivAvatar, s.getUser().getAvatar());

        if (listBean == null) {
            listBean = new ConfigBean.ServiceItemsConfigBean.ListBean();
            listBean.setType("out");
            listBean.setTitle(MAPP.mapp.getString(R.string.service_out));
        }

        if (listBean.getType().equals(MValue.ORDER_TYPE_VIDEO)) {
            binding.tvServeName.setText(listBean.getTitle());

            binding.tvServePrice.setText(s.getPrice() + MAPP.mapp.getString(R.string.price_unit) + " / " + MAPP.mapp.getString(R.string.unit_minute));

            binding.tvServeTime.setText(DateUtil.getRecommendUserInterval(s.getCreate_time() * 1000l));
        } else {
            // TODO: 2018/8/15 地址新版UI不显示了  所以没有跳转
//            binding.includeOrderContent.includeAddress.getRoot().setOnClickListener((v) -> {
//                if (listBean.getType().equals("out")) {
//                    return;
//                }
//                IntentUtils.toChooseAddressDetailActivity(binding.getRoot().getContext(), s.getPoi(), "look");
//            });

//            if (s.getPoi() != null) {
//                binding.includeOrderContent.includeAddress.setContent(s.getPoi().getName() + "。");
//                binding.includeOrderContent.includeAddress.tvTitle.setText(ThemeService.getOrderAddressSpannable(binding.getRoot().getContext(), binding.includeOrderContent.includeAddress.getContent()));
//            }

//
            binding.tvServeName.setText(listBean.getTitle());

            String time = s.getService_time() % 60.0 == 0 ? String.valueOf(s.getService_time() / 60) : String.valueOf(s.getService_time() / 60.0);

            if (!listBean.getType().equals("out")) {
                binding.tvServePrice.setText(OrderService.getPriceText(s.getPrice()) + MAPP.mapp.getString(R.string.price_unit));
            }


            binding.tvServeTime.setText(DateUtil.getRecommendUserInterval(s.getCreate_time() * 1000l));
//                binding.tvServeTime.setText(ThemeService.getServiceOrderTime(s.getStart_time(), s.getEnd_time(), s.getService_time()));

        }


        if (s.getAction_config() != null) {
            binding.btnRegister.setText(s.getAction_config().get(0).getValue());
            if (StringUtils.isEmpty(s.getAction_config().get(0).getColor())) {
                binding.btnRegister.setEnabled(false);
                binding.btnRegister.setSelected(false);
                binding.btnRegister.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
            } else {
                binding.btnRegister.setEnabled(true);
                binding.btnRegister.setSelected(true);
                binding.btnRegister.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_58586C));
            }
        }


        binding.btnRegister.setOnClickListener((v) -> {

            if (listBean.getType().equals("out") || StringUtils.isEmpty(s.getAction_config().get(0).getAction())) {
                return;
            }
//
//            if (s.getService_type().equals(MValue.ORDER_TYPE_VIDEO)) {
////                if (!permissionUtils.hasCameraPermission(MAPP.mapp)) {
////                    permissionUtils.getCameraPermission(weakReference.get().getActivity());
////                    return;
////                }
//            }
            weakReference.get().apply(binding.btnRegister, s.getId(), s);
        });

        binding.ivAvatar.setOnClickListener((v) -> {
            if (listBean.getType().equals("out")) {
                return;
            }
//            if (s.getUser().getId().intValue() != s.getUid()) { //if (s.getUser().getRole() == MValue.USER_ROLE_SERVICER) {
//                IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s.getUser(), null, null);
//            } else {
//                IntentUtils.toUserInfoActivity(v.getContext(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s.getUser(), null, null);
//            }
            IntentUtils.toUserPageActivity(MAPP.mapp.getCurrentActivity(), String.valueOf(s.getUser().getId()));
        });

        binding.getRoot().setOnClickListener((v) -> {
            IntentUtils.toOrderDetailActivity(v.getContext(), s.getId(), true);
        });


    }


}
