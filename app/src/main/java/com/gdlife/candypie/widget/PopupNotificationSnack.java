package com.gdlife.candypie.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.androidadvance.topsnackbar.TSnackbar;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.PopupNotificationBinding;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.message.SystemNotificationValueBean;
import com.heboot.event.NormalEvent;
import com.heboot.message.MessageToAction;
import com.heboot.rxbus.RxBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Heboot on 2017/7/17.
 */

public class PopupNotificationSnack {

    private PopupNotificationBinding binding;

    private TSnackbar snackbar;

    private SystemNotificationValueBean model;

    public PopupNotificationSnack(View view, Context context, SystemNotificationValueBean model2) {
//        activity_loading_page = new WeakReference<RouteListActivity>(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        model = model2;
        binding = DataBindingUtil.inflate(inflater, R.layout.popup_notification, null, false);
        binding.setModel(model);
        binding.getRoot().setOnClickListener((v) -> {
            snackbar.dismiss();
            toaction(v);
        });
        snackbar = TSnackbar.make(view, "", TSnackbar.LENGTH_LONG);
        initSnackbar();
    }

    private void initSnackbar() {
        View snackbarview = snackbar.getView();
        snackbarview.setBackgroundResource(android.R.color.transparent);
        TSnackbar.SnackbarLayout snackbarLayout = (TSnackbar.SnackbarLayout) snackbarview;
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;

        snackbarLayout.addView(binding.getRoot(), 0, p);
    }


    public void toaction(View v) {
        if (StringUtils.isEmpty(model.getTo_action())) {
            IntentUtils.toSystemMessageActivity(binding.getRoot().getContext());
        } else if (model.getTo_action().equals(MessageToAction.push_list.toString())) {
            AuthService.newOrderToMessageRobPage();
//            IntentUtils.toIndexServicerContainerActivity(binding.getRoot().getContext(), null);
        } else if (model.getTo_action().equals(MessageToAction.income_list.toString())) {
            IntentUtils.toBalanceLogActivity(binding.getRoot().getContext());
        } else if (model.getTo_action().equals(MessageToAction.order_detail.toString())) {
            IntentUtils.toOrderDetailActivity(binding.getRoot().getContext(), model.getUser_service_id(), false);
        } else if (model.getTo_action().equals(MessageToAction.push_user_list.toString())) {
            RxBus.getInstance().post(NormalEvent.FINISH_PAGE_BY_SELECT_USER);
        } else if (model.getTo_action().equals(MessageToAction.coupons_list.toString())) {
            IntentUtils.toCouponsActivity(binding.getRoot().getContext(), false, null, null);
        } else if (model.getTo_action().equals(MessageToAction.service_profile.toString())) {
//            IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, , MValue.HOMEPAG_FROM.ONE_ONE, serviceBean);
        }
    }

    private void CustomTimeToast() {
//
        snackbar.show();

    }

    public void show() {
//        toast.show();
        CustomTimeToast();
    }

    public SystemNotificationValueBean getModel() {
        return model;
    }

    public void setModel(SystemNotificationValueBean model) {
        this.model = model;
    }
}
