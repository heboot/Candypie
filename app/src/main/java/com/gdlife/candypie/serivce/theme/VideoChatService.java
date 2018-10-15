package com.gdlife.candypie.serivce.theme;

import android.app.Activity;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.dialog.ServiceTipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.bean.video.VideoChatStratEndBean;
import com.heboot.bean.videochat.VideoChatTipBean;
import com.heboot.entity.User;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.suke.widget.SwitchButton;
import com.yalantis.dialog.TipCustomDialog;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VideoChatService {

    private Map<String, Object> params;


    public void checkVideoService() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer()._check_run_service(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                if (baseBean.getData() != null && baseBean.getData().getRun_service_tip() != null) {
                    IntentUtils.toVideoChatActivity(MAPP.mapp, baseBean.getData().getRun_service_tip().getUser_service_id(), baseBean.getData().getRun_service_tip().getChat_room_config(), VideoChatFrom.SERVICER);
                }

            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }


    /**
     * 切换视频接听状态
     *
     * @param switchButton
     */
    public void switch_video_chat_status(SwitchButton switchButton) {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().switch_video_chat_status(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                UserService.getInstance().getUser().setVideo_chat_status(1 - UserService.getInstance().getUser().getVideo_chat_status());
                if (UserService.getInstance().getUser().getVideo_chat_status() == 1) {
                    switchButton.setChecked(true);
                } else {
                    switchButton.setChecked(false);
                }
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }

    /**
     * 完成视频订单
     *
     * @param userServiceId
     * @param httpObserver
     */
    public void completeVideoChatOrder(String userServiceId, HttpObserver<VideoChatStratEndBean> httpObserver) {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, userServiceId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().video_chart_over(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(httpObserver);
    }


    /**
     * 检查视频聊天所需要的权限
     *
     * @param permissionUtils
     * @param activity
     */
    private boolean checkVideoChatPermission(PermissionUtils permissionUtils, Activity activity) {
        if (!permissionUtils.hasCameraPermission(MAPP.mapp)) {
            permissionUtils.getCameraPermission(activity);
            return false;
        }
        return true;
    }


    /**
     * 检查视频聊天需要的钻石够不够
     *
     * @param activity
     * @param user
     * @param coinDialog
     */
    private boolean checkVideoChatCoin(Activity activity, User user, TipDialog coinDialog) {
        int minCoin = ThemeService.getVideoMinReqCoin(ThemeService.getVideoServiceMinPrice());

        if (user != null && !StringUtils.isEmpty(user.getVideo_chat_price())) {
            int myCoin = Integer.parseInt(UserService.getInstance().getUser().getCoin());
            int otherCoin = Integer.parseInt(user.getVideo_chat_price());


            if (myCoin < otherCoin) {
                // TODO: 2018/3/22 提示去充值钻石
                if (coinDialog == null) {
                    coinDialog = new TipDialog.Builder(activity, new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if (integer == 1) {
                                IntentUtils.toAccountActivity(MAPP.mapp.getCurrentActivity());
                            }

                        }
                    }, activity.getString(R.string.new_video_service_coin_tip_title), "充值"
                    ).create();
                    coinDialog.show();
                    return false;
                } else {
                    coinDialog.show();
                }
                return false;
            } else {
                return true;
            }
        } else if (Integer.parseInt(UserService.getInstance().getUser().getCoin()) < minCoin) {
            // TODO: 2018/3/22 提示去充值钻石
            if (coinDialog == null) {
                coinDialog = new TipDialog.Builder(activity, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            IntentUtils.toAccountActivity(MAPP.mapp.getCurrentActivity());
                        }

                    }
                }, activity.getString(R.string.new_video_service_coin_tip_title), "充值"
                ).create();
                coinDialog.show();
                return false;
            } else {
                coinDialog.show();
            }
            return false;
        }
        return true;
    }

    private QMUITipDialog tipDialog;

    /**
     * 调用接口发起视频单子
     *
     * @param activity
     * @param user
     */
    private void sendVideoChat(Activity activity, User user, HttpObserver<PostThemeBean> observable) {

        tipDialog = DialogUtils.getLoadingDialog(activity, "", false);

        Map<String, Object> params;
        params = SignUtils.getNormalParams();
        if (user != null && user.getId() != null) {
            params.put(MKey.SELECT_UID, user.getId());
        }
        params.put(MKey.SERVICE_ID, "1");
        params.put(MKey.PRICE, ThemeService.getVideoServiceMinPrice());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().video_chat(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PostThemeBean>() {
            @Override
            public void onSuccess(BaseBean<PostThemeBean> baseBean) {
                if (observable != null) {
                    observable.onSuccess(baseBean);
                } else {
                    tipDialog.dismiss();
                    IntentUtils.toVideoChatActivity(activity, baseBean.getData().getUser_service_id(), baseBean.getData().getChat_room_config(), VideoChatFrom.USER);
                }
            }

            @Override
            public void onError(BaseBean<PostThemeBean> baseBean) {
                if (observable != null) {
                    observable.onError(baseBean);
                } else {
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(activity, baseBean.getMessage(), true);
                    tipDialog.show();
                }

            }
        });
    }


    /**
     * 发起视频单子
     *
     * @param permissionUtils
     * @param activity
     * @param user
     * @param coinDialog
     */
    public void postVideoService(PermissionUtils permissionUtils, BaseActivity activity, User user, TipDialog coinDialog, HttpObserver<PostThemeBean> observable) {

        if (UserService.getInstance().checkTourist()) {
            return;
        }

        if (checkVideoChatPermission(permissionUtils, activity)) {
            if (checkVideoChatCoin(activity, user, coinDialog)) {
                sendVideoChat(activity, user, observable);
            }
        }

    }

    public void postVideoService(PermissionUtils permissionUtils, Activity activity, User user, TipDialog coinDialog) {

        if (UserService.getInstance().checkTourist()) {
            return;
        }

        if (checkVideoChatPermission(permissionUtils, activity)) {
            if (checkVideoChatCoin(activity, user, coinDialog)) {
                sendVideoChat(activity, user, null);
            }
        }

    }

    private TipCustomDialog videoChatTipDialog;

    private PermissionUtils permissionUtils;

    public void video_chat_tip(User user) {

        Map<String, Object> params;
        params = SignUtils.getNormalParams();
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().video_chat_tip(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<VideoChatTipBean>() {
            @Override
            public void onSuccess(BaseBean<VideoChatTipBean> baseBean) {
                if (baseBean.getData().getStatus() == 1) {
                    videoChatTipDialog = new TipCustomDialog.Builder(MAPP.mapp.getCurrentActivity(), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if (integer == 1) {
                                if (permissionUtils == null) {
                                    permissionUtils = new PermissionUtils();
                                }
                                postVideoService(permissionUtils, MAPP.mapp.getCurrentActivity(), user, null);
                            }

                        }
                    }, baseBean.getData().getTip_message(), "再看看", "视频聊天").create();
                    videoChatTipDialog.show();
                }
            }

            @Override
            public void onError(BaseBean<VideoChatTipBean> baseBean) {


            }
        });
    }

}
