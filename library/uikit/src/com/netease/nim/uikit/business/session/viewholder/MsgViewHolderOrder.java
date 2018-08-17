package com.netease.nim.uikit.business.session.viewholder;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.heboot.bean.message.CustomMessageModel;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.event.MessageEvent;
import com.heboot.message.MessageType;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DateUtil;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.databinding.LayoutMessageOrderBinding;


public class MsgViewHolderOrder extends MsgViewHolderBase {

    protected TextView tvContent;

    private TextView tvtitle;

    private LayoutMessageOrderBinding binding;

    private ImageView ivAddress, ivMoney, ivName, ivTime;

    private TextView tvAddress, tvMoney, tvName, tvTime;

    private View vAddress, vMoney, vName, vTime;

    private ConstraintLayout container;


    public MsgViewHolderOrder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(NimUIKit.getContext()), R.layout.layout_message_order, null, false);
        return R.layout.layout_message_order;
    }

    @Override
    protected void inflateContentView() {

        vAddress = view.findViewById(R.id.include_address);
        vMoney = view.findViewById(R.id.include_price);
        vName = view.findViewById(R.id.include_title);
        vTime = view.findViewById(R.id.include_time);

        ivAddress = vAddress.findViewById(R.id.iv_point);
        tvAddress = vAddress.findViewById(R.id.tv_title);

        ivMoney = vMoney.findViewById(R.id.iv_point);
        tvMoney = vMoney.findViewById(R.id.tv_title);

        ivName = vName.findViewById(R.id.iv_point);
        tvName = vName.findViewById(R.id.tv_title);

        ivTime = vTime.findViewById(R.id.iv_point);
        tvTime = vTime.findViewById(R.id.tv_title);

        tvContent = view.findViewById(R.id.tv_content);
        tvtitle = view.findViewById(R.id.tv_title);

        container = (ConstraintLayout) view.findViewById(R.id.clyt_container);
    }

    @Override
    protected void bindContentView() {
        CustomMessageModel model = null;
        try {
            model = JSON.parseObject(message.getAttachment().toJson(false), CustomMessageModel.class);
        } catch (Exception e) {

        }

        if (model != null) {
            if (model.getType().equals(MessageType.MESSAGE_TYPE_ORDER)) {
                OrderListBean.ListBean messageUserSameModel = JSON.parseObject(model.getData(), OrderListBean.ListBean.class);
                if (messageUserSameModel != null) {
                    handleTextNotification(messageUserSameModel);

                }


            }
        }

    }

    private void handleTextNotification(OrderListBean.ListBean s) {

        tvtitle.setText(tvtitle.getContext().getString(R.string.order_info));


        ivAddress.setBackgroundResource(R.drawable.icon_order_address);
        tvAddress.setText(s.getPoi().getName());

        ivMoney.setBackgroundResource(R.drawable.icon_order_money);
        tvMoney.setText(s.getPrice() + " ");
//
        ivName.setBackgroundResource(R.drawable.icon_order_service);
        tvName.setText(s.getService_name());

        ivTime.setBackgroundResource(R.drawable.icon_order_time);
        tvTime.setText(DateUtil.getServiceOrderTime(s.getStart_time(), s.getEnd_time(), s.getService_time()));

        tvContent.setText(s.getTip());

        view.setOnClickListener((v) -> {
            RxBus.getInstance().post(new MessageEvent.ToOrderDetailByMessageEvent(s.getId()));
        });

        container.setOnClickListener((v) -> {
            RxBus.getInstance().post(new MessageEvent.ToOrderDetailByMessageEvent(s.getId()));
        });


    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
