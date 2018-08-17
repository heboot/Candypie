package com.gdlife.candypie.serivce;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.activitys.theme.NewThemeActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayFrom;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.widget.dialog.ServiceTipDialog;
import com.heboot.bean.order.RunServiceTipBean;
import com.heboot.bean.pay.VipLevelBean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.common.RunServiceAction;
import com.heboot.utils.PreferencesUtils;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/21.
 */

public class ServerService {

    /**
     * 显示取消次数太多不能发布弹窗
     *
     * @param context
     */
    public void showCancelMoreDialog(Context context, String expire_time) {
        ServiceTipDialog serviceTipDialog = new ServiceTipDialog.Builder(context, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 1) {
                    VipLevelBean vipLevelBean = VipService.getNextVipLevel(UserService.getInstance().getUser().getVip_level());
                    if (vipLevelBean != null) {
                        IntentUtils.toHTMLActivity(context, null, vipLevelBean.getDetail_url());
                    }
                }
            }
        }, MValue.TIP_DIALOG_TYPE.CONFINE, Long.parseLong(expire_time), null).create();

        serviceTipDialog.show();
    }


    public void doRuningService(Context context, RunServiceTipBean runServiceTipBean) {
        if (runServiceTipBean.getAction().equals(RunServiceAction.start_video_chat.toString())) {
            IntentUtils.toVideoChatActivity(context, runServiceTipBean.getUser_service_id(), runServiceTipBean.getChat_room_config(), VideoChatFrom.SERVICER);
        }
    }


    /**
     * 首单弹窗
     *
     * @param context
     * @param resultBean
     */
    public void showFirstServerDialog(WeakReference<NewThemeActivity> context, PostThemeBean resultBean, String ratio, String prePayPrice) {
        OrderService orderService = new OrderService();
        ServiceTipDialog serviceTipDialog = new ServiceTipDialog.Builder(context.get(), new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 1) {
                    IntentUtils.toPayActivity(context.get(), resultBean, PayFrom.NEW_SERVICE, false);
                    /**
                     * 发单后不关闭发单页面
                     * 2018.05.11 10:38
                     */
//                    context.get().finish();
                } else if (integer == 0) {
                    orderService.cancelOrder(resultBean.getUser_service_id());
                }
            }
        }, MValue.TIP_DIALOG_TYPE.FIRST, null, null, ratio, prePayPrice).create();
        serviceTipDialog.show();
    }


    /**
     * 首次进入抢单页面弹窗
     *
     * @param context
     * @param resultBean
     */
    public void showFirstRobServerDialog(WeakReference<Activity> context, Consumer<String> consumer) {

        boolean isFirst = PreferencesUtils.getBoolean(MAPP.mapp, MKey.FIRST_ROB, true);
        if (isFirst) {
            ServiceTipDialog serviceTipDialog = new ServiceTipDialog.Builder(context.get(), new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {

                }
            }, MValue.TIP_DIALOG_TYPE.FIRST_ROB, null, null).create();
            serviceTipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    PreferencesUtils.putBoolean(MAPP.mapp, MKey.FIRST_ROB, false);
                    if (consumer != null) {
                        try {
                            consumer.accept("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            serviceTipDialog.show();
        }
    }

}
