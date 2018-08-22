package com.gdlife.candypie.serivce;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.auth.AuthIDActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.ServiceAction;
import com.gdlife.candypie.common.ServiceSelectUserFrom;
import com.gdlife.candypie.common.TagsDialogType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.ServiceCancelTipDialog;
import com.gdlife.candypie.widget.dialog.ChooseTagsDialog;
import com.gdlife.candypie.widget.theme.ServiceCommentDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.order.PreServiceEvalBean;
import com.heboot.bean.order.ServiceEvalBean;
import com.heboot.bean.theme.CancelServiceBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.entity.User;
import com.heboot.event.OrderEvent;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/16.
 */

public class OrderService {

    @Inject
    UIService uiService;


    private ServiceCancelTipDialog tipDialog;

    private QMUITipDialog qmuiTipDialog;

    public OrderService() {
        DaggerServiceComponent.builder().build().inject(this);
        params = SignUtils.getNormalParams();
    }

    protected Map<String, Object> params;


    public static String getPriceText(String text) {
        return String.valueOf((int) Double.parseDouble(text));
    }

    /**
     * 完成订单
     *
     * @param context
     * @param user_service_id
     */
    public void completeOrder(Context context, String user_service_id) {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, user_service_id);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().complete_order(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onSuccess(BaseBeanEntity cancelServiceBean) {
                doCommentOrder(context, user_service_id, null);
                RxBus.getInstance().post(OrderEvent.REFRESH_ORDER_ENENT);
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> basebean) {
                QMUITipDialog tipChildDialog = null;
                if (tipChildDialog != null && tipChildDialog.isShowing()) {
                    tipChildDialog.dismiss();
                }
                tipChildDialog = DialogUtils.getFailDialog(MAPP.mapp.getCurrentActivity(), basebean.getMessage(), true);
                tipChildDialog.show();
            }


        }));
    }


    /**
     * 取消订单用户服务提示
     *
     * @param context
     * @param user_service_id
     */
    public void initCancelData(Context context, String user_service_id, boolean isVideo) {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, user_service_id);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        if (isVideo) {
            cancelOrder(user_service_id);
        } else {
            HttpClient.Builder.getGuodongServer().cancel_tip(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<CancelServiceBean>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                }

                @Override
                public void onSuccess(CancelServiceBean cancelServiceBean) {
                    doCancelAction(context, user_service_id, cancelServiceBean);
                }


                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onError(BaseBean<CancelServiceBean> basebean) {
                    QMUITipDialog tipChildDialog = null;
                    if (tipChildDialog != null && tipChildDialog.isShowing()) {
                        tipChildDialog.dismiss();
                    }
                    tipChildDialog = DialogUtils.getFailDialog(MAPP.mapp.getCurrentActivity(), basebean.getMessage(), true);
                    tipChildDialog.show();
                }


            }));
        }
    }

    /**
     * 取消订单
     *
     * @param user_service_id
     */
    public void cancelOrder(String user_service_id) {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, user_service_id);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().cancel_order(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                RxBus.getInstance().post(OrderEvent.CANCEL_ORDER_ENENT);
                if (baseBean.getData().getService_cancel_result() == 1) {
                    IntentUtils.toServerCancelCauseActivity(MAPP.mapp, user_service_id);
                }
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }


    /**
     * 根据类型去选择取消的弹窗还是去页面
     *
     * @param context
     * @param user_service_id
     * @param cancelServiceBean
     */
    public void doCancelAction(Context context, String user_service_id, CancelServiceBean cancelServiceBean) {
        if (cancelServiceBean == null || (StringUtils.isEmpty(cancelServiceBean.getCancel_val_time()) && StringUtils.isEmpty(cancelServiceBean.getCancel_rate()))) {
            cancelOrder(user_service_id);
            return;
        }
        if (!StringUtils.isEmpty(cancelServiceBean.getCancel_rate())) {
            // TODO: 2018/3/19 弹窗
            tipDialog = new ServiceCancelTipDialog.Builder(context, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        cancelOrder(user_service_id);
                    }
                }
            }, cancelServiceBean.getCancel_rate()).create();
            tipDialog.show();
        } else if (!StringUtils.isEmpty(cancelServiceBean.getCancel_val_time())) {
            //去取消页面
            IntentUtils.toServiceCancelActivity(context, user_service_id, cancelServiceBean);
        }

    }

    public void toOrderDetail(Context context, User user, int orderStatus, String userServiceId) {
        if (user.getId() == UserService.getInstance().getUser().getId()) {
            IntentUtils.toOrderDetailActivity(context, userServiceId, false);
        } else {
            if (orderStatus == MValue.ORDER_LIST_STATUS_DENGDAIQUEREN) {
                return;
            }
            IntentUtils.toOrderDetailActivity(context, userServiceId, false);
        }
    }


    public void setOrderBottomOption(Context context, TextView left, TextView center, TextView right, OrderListBean.ListBean listBean) {

        if (listBean.getAction_config() == null || listBean.getAction_config().size() == 0) {
            right.setVisibility(View.INVISIBLE);
            return;
        }

        if (listBean.getAction_config().size() == 1) {
            right.setVisibility(View.VISIBLE);
            center.setVisibility(View.INVISIBLE);
            left.setVisibility(View.INVISIBLE);
//            qmuiRoundButtonDrawable.setBgData(ColorStateList.valueOf(Color.parseColor(listBean.getAction_config().get(0).getColor())));
            if (!StringUtils.isEmpty(listBean.getAction_config().get(0).getColor())) {
                right.setBackground(uiService.getOrderOptionBG(listBean.getAction_config().get(0).getColor()));
                right.setTextColor(Color.WHITE);
            } else {
                right.setBackgroundResource(R.drawable.bg_rect_border_user_list_tag);
                right.setTextColor(ContextCompat.getColor(context, R.color.color_343445));
            }


//            right.setBackgroundColor(Color.parseColor(listBean.getAction_config().get(0).getColor()));
            right.setText(listBean.getAction_config().get(0).getValue());
            right.setOnClickListener((v) -> {
                doOrderAction(context, listBean.getAction_config().get(0).getAction(), listBean);
            });
        } else if (listBean.getAction_config().size() == 2) {
            right.setVisibility(View.VISIBLE);
            center.setVisibility(View.VISIBLE);
            left.setVisibility(View.INVISIBLE);

            if (!StringUtils.isEmpty(listBean.getAction_config().get(1).getColor())) {
                right.setBackground(uiService.getOrderOptionBG(listBean.getAction_config().get(1).getColor()));
                right.setTextColor(Color.WHITE);
            } else {
                right.setBackgroundResource(R.drawable.bg_rect_border_user_list_tag);
                right.setTextColor(ContextCompat.getColor(right.getContext(), R.color.color_343445));
            }


            right.setText(listBean.getAction_config().get(1).getValue());

            if (!StringUtils.isEmpty(listBean.getAction_config().get(0).getColor())) {
                center.setBackground(uiService.getOrderOptionBG(listBean.getAction_config().get(0).getColor()));
                center.setTextColor(Color.WHITE);
//                center.setBackgroundColor(Color.parseColor(listBean.getAction_config().get(1).getColor()));
            } else {
                center.setBackgroundResource(R.drawable.bg_rect_border_user_list_tag);
                center.setTextColor(ContextCompat.getColor(right.getContext(), R.color.color_343445));
            }


            center.setText(listBean.getAction_config().get(0).getValue());
            right.setOnClickListener((v) -> {
                doOrderAction(context, listBean.getAction_config().get(1).getAction(), listBean);
            });
            center.setOnClickListener((v) -> {
                doOrderAction(context, listBean.getAction_config().get(0).getAction(), listBean);
            });
        } else if (listBean.getAction_config().size() == 3) {
            right.setVisibility(View.VISIBLE);
            center.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(listBean.getAction_config().get(2).getColor())) {
                right.setBackground(uiService.getOrderOptionBG(listBean.getAction_config().get(2).getColor()));
                right.setTextColor(Color.WHITE);
//                right.setBackgroundColor(Color.parseColor(listBean.getAction_config().get(0).getColor()));
            } else {
                right.setBackgroundResource(R.drawable.bg_rect_border_user_list_tag);
                right.setTextColor(ContextCompat.getColor(right.getContext(), R.color.color_343445));
            }


            right.setText(listBean.getAction_config().get(2).getValue());

            if (!StringUtils.isEmpty(listBean.getAction_config().get(1).getColor())) {
//                center.setBackgroundColor(Color.parseColor(listBean.getAction_config().get(1).getColor()));
                center.setBackground(uiService.getOrderOptionBG(listBean.getAction_config().get(1).getColor()));
                center.setTextColor(Color.WHITE);
            } else {
                center.setBackgroundResource(R.drawable.bg_rect_border_user_list_tag);
                center.setTextColor(ContextCompat.getColor(right.getContext(), R.color.color_343445));
            }

            center.setText(listBean.getAction_config().get(1).getValue());

            if (!StringUtils.isEmpty(listBean.getAction_config().get(0).getColor())) {
//                left.setBackgroundColor(Color.parseColor(listBean.getAction_config().get(2).getColor()));
                left.setBackground(uiService.getOrderOptionBG(listBean.getAction_config().get(0).getColor()));
                left.setTextColor(Color.WHITE);
            } else {
                left.setBackgroundResource(R.drawable.bg_rect_border_user_list_tag);
                left.setTextColor(ContextCompat.getColor(right.getContext(), R.color.color_343445));
            }


            left.setText(listBean.getAction_config().get(0).getValue());
            left.setOnClickListener((v) -> {
                doOrderAction(context, listBean.getAction_config().get(0).getAction(), listBean);
            });
            right.setOnClickListener((v) -> {
                doOrderAction(context, listBean.getAction_config().get(2).getAction(), listBean);
            });
            center.setOnClickListener((v) -> {
                doOrderAction(context, listBean.getAction_config().get(1).getAction(), listBean);
            });
        }
    }

    private VideoChatService videoChatService;

    private PermissionUtils permissionUtils;

    /**
     * 订单操作
     *
     * @param context
     * @param action
     * @param listBean
     */
    public void doOrderAction(Context context, String action, OrderListBean.ListBean listBean) {
        if (StringUtils.isEmpty(action)) {
            return;
        }
        if (action.equals(ServiceAction.IM.toString().toLowerCase())) {
            IntentUtils.intent2ChatActivity(context, MValue.CHAT_PRIEX + listBean.getUsers().get(0).getId());
        } else if (action.equals(ServiceAction.TEL.toString().toLowerCase())) {
            if (listBean != null && listBean.getUsers() != null && listBean.getUsers().size() > 0 && !StringUtils.isEmpty(listBean.getUsers().get(0).getTel())) {
                DialogUtils.getCallMobileDialog(context, listBean.getUsers().get(0).getTel()).show();
            }
        } else if (action.equals(ServiceAction.CANCEL.toString().toLowerCase())) {
            initCancelData(context, listBean.getId(), listBean.getService_type().equals(MValue.ORDER_TYPE_VIDEO));
        } else if (action.equals(ServiceAction.COMPLETE.toString().toLowerCase())) {
//            //先弹出评论框再说
//            ServiceCommentDialog dialog = new ServiceCommentDialog.Builder(context, listBean.getId(), listBean.getService_type().equals(MValue.ORDER_TYPE_VIDEO)).create();
//            dialog.show();
            completeOrder(context, listBean.getId());
//            initCommentTagsData(context, listBean.getId(), new Consumer<List<TagsChildBean>>() {
//                @Override
//                public void accept(List<TagsChildBean> tagsChildBeans) throws Exception {
//                    if (tagsChildBeans != null && tagsChildBeans.size() > 0) {
//                        service_tags_eval(context, listBean.getId(), tagsChildBeans);
//                    } else {
////                        DialogUtils.getInfolDialog(context, "至少选择一个标签", true).show();
//                    }
//                }
//            });

        } else if (action.equals(ServiceAction.AGAIN.toString().toLowerCase())) {
            IntentUtils.toThemeListActivity(context, true, listBean.getUsers().get(0));
        } else if (action.equals(ServiceAction.COMMENT.toString().toLowerCase())) {
            doCommentOrder(context, listBean.getId(), null);
        } else if (action.equals(ServiceAction.VIDEO_CHAT.toString().toLowerCase())) {
            if (videoChatService == null) {
                videoChatService = new VideoChatService();
            }
            if (permissionUtils == null) {
                permissionUtils = new PermissionUtils();
            }
            videoChatService.postVideoService(permissionUtils, MAPP.mapp.getCurrentActivity(), listBean.getUsers().get(0), null);
        }
    }

    private ChooseTagsDialog chooseTagsDialog;


    /**
     * 进行订单评价
     *
     * @param context
     * @param userServiceId
     */
    public void doCommentOrder(Context context, String userServiceId, DialogInterface.OnDismissListener dismissListener) {
        initCommentTagsData(context, userServiceId, new Consumer<List<TagsChildBean>>() {
            @Override
            public void accept(List<TagsChildBean> tagsChildBeans) throws Exception {
                if (tagsChildBeans != null && tagsChildBeans.size() > 0) {
                    service_tags_eval(context, userServiceId, tagsChildBeans, dismissListener);
                } else {
                    chooseTagsDialog = null;

//                        DialogUtils.getInfolDialog(context, "至少选择一个标签", true).show();
                }
            }
        }, dismissListener);
    }

    /**
     * 加载评论标签数据
     */
    public void initCommentTagsData(Context context, String userServiceID, Consumer<List<TagsChildBean>> observer, DialogInterface.OnDismissListener dismissListener) {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, userServiceID);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().pre_service_eval(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PreServiceEvalBean>() {
            @Override
            public void onSuccess(BaseBean<PreServiceEvalBean> baseBean) {
                if (baseBean.getData() != null && baseBean.getData().getEval_items() != null && baseBean.getData().getEval_items().size() > 0) {

                    chooseTagsDialog = new ChooseTagsDialog.Builder(context, null, uiService, baseBean.getData().getEval_items(), observer, "订单评价", TagsDialogType.CHOOSE_COMMENT_TAG).create();
                    chooseTagsDialog.setObserver(observer);
                    if (dismissListener != null) {
                        chooseTagsDialog.setOnDismissListener(dismissListener);
                    }
                    chooseTagsDialog.show();
                } else {
                    return;
                }
            }

            @Override
            public void onError(BaseBean<PreServiceEvalBean> baseBean) {
                qmuiTipDialog = DialogUtils.getInfolDialog(context, baseBean.getMessage(), true);
                if (dismissListener != null) {
                    qmuiTipDialog.setOnDismissListener(dismissListener);
                }
                qmuiTipDialog.show();

            }
        });
    }


    /**
     * 提交服务评价
     *
     * @param context
     * @param userServiceID
     * @param tagsChildBeans
     */
    public void service_tags_eval(Context context, String userServiceID, List<TagsChildBean> tagsChildBeans, DialogInterface.OnDismissListener dismissListener) {

        List<String> tagIds = UserInfoService.getSelectedIds(tagsChildBeans);

        String ids = "";

        for (String s : tagIds) {
            ids = ids + s + ",";
        }

        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, userServiceID);
        params.put(MKey.TAG_IDS, ids);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().service_tags_eval(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ServiceEvalBean>() {
            @Override
            public void onSuccess(BaseBean<ServiceEvalBean> baseBean) {
//                if (chooseTagsDialog != null && chooseTagsDialog.isShowing()) {
//                    chooseTagsDialog.dismiss();
//                }
                if (chooseTagsDialog != null && chooseTagsDialog.isShowing()) {
                    RxBus.getInstance().post(OrderEvent.REFRESH_ORDER_ENENT);
                    chooseTagsDialog.showSucView(baseBean.getData().getEval_reward_tip(), baseBean.getData().getEval_title());
                }
                DialogUtils.getSuclDialog(context, baseBean.getMessage(), true).show();
            }

            @Override
            public void onError(BaseBean<ServiceEvalBean> baseBean) {
                if (chooseTagsDialog != null && chooseTagsDialog.isShowing()) {
                    chooseTagsDialog.dismiss();
                }
                qmuiTipDialog = DialogUtils.getInfolDialog(context, baseBean.getMessage(), true);
                if (dismissListener != null) {
                    qmuiTipDialog.setOnDismissListener(dismissListener);
                }
                qmuiTipDialog.show();


            }
        });
    }


}
