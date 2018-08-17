package com.gdlife.candypie.serivce;

import android.support.v4.app.FragmentActivity;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.utils.AudioUtil;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.gift.GiftPlayView;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.message.SystemMessage;
import com.heboot.bean.message.SystemNotification;
import com.heboot.bean.theme.PushUserTipBean;
import com.heboot.common.VideoCatState;
import com.heboot.entity.User;
import com.heboot.event.GiftEvent;
import com.heboot.event.MessageEvent;
import com.heboot.event.ThemeEvent;
import com.heboot.event.TurntableEvent;
import com.heboot.event.UserEvent;
import com.heboot.event.VideoChatEvent;
import com.heboot.message.MessageToAction;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MessageService {


    public void doSystemNotificationAction(SystemNotification systemNotification) {
        //更新用户信息
        if (systemNotification.getType().equals(MessageToAction.update_user_profile.toString())) {

            if (systemNotification.getValue().getUser().getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                User user = systemNotification.getValue().getUser();
                user.setVideo_chat_status(1);
                UserService.getInstance().setUser(user);

                RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
                RxBus.getInstance().post(new UserEvent.UPDATE_USER_BY_IM(UserService.getInstance().getUser()));

                if (systemNotification.getValue().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                    RxBus.getInstance().post(UserEvent.AUTH_SERVICE_SUC_EVENT);
                    Observable.just("Amit")
                            //延时两秒，第一个参数是数值，第二个参数是事件单位
                            .delay(3000, TimeUnit.MILLISECONDS)
                            // Run on a background thread
                            .subscribeOn(Schedulers.io())
                            // Be notified on the main thread
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    AuthService authService = new AuthService();
                                    authService.checkAuthStatusIsSuc(systemNotification.getValue().getService_auth_status());
                                }
                            });//这里的观察者依然不重要
                }
            }

        } else if (systemNotification.getType().equals(MessageToAction.update_video_chat_status.toString())) {
            if (systemNotification.getValue().getStatus().equals(VideoCatState.cancel.toString())) {
                Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        RxBus.getInstance().post(new VideoChatEvent.UPDATE_VIDEO_STATE_EVENT(VideoCatState.valueOf(systemNotification.getValue().getStatus()), systemNotification.getValue().getUser_service_id()));
                    }
                });
            } else {
                RxBus.getInstance().post(new VideoChatEvent.UPDATE_VIDEO_STATE_EVENT(VideoCatState.valueOf(systemNotification.getValue().getStatus()), systemNotification.getValue().getUser_service_id()));
            }

        } else if (systemNotification.getType().equals(MessageToAction.send_gift.toString())) {
            GiftRepository giftRepository = new GiftRepository();
            GiftBean giftBean = giftRepository.getGiftById(systemNotification.getValue().getGift().getId());
            giftBean.setIncome_msg(systemNotification.getValue().getGift().getIncome_msg());
            giftBean.setTip_msg(systemNotification.getValue().getGift().getTip_msg());
            try {
                new GiftPlayView(giftBean).show(((FragmentActivity) MAPP.mapp.getCurrentActivity()).getFragmentManager(), "gift");
            } catch (Exception e) {

            }
//            RxBus.getInstance().post(new GiftEvent.SendGiftEvent(giftBean));
        } else if (systemNotification.getType().equals(MessageToAction.update_video_service.toString())) {
            RxBus.getInstance().post(new VideoChatEvent.UPDATE_VIDEO_SERVICE_ENENT(systemNotification));
        } else if (systemNotification.getType().equals(MessageToAction.push_user_tip.toString())) {
            PushUserTipBean pushUserTipBean = new PushUserTipBean();
            pushUserTipBean.setUser_service_id(systemNotification.getValue().getUser_service_id());
            pushUserTipBean.setPush_users(systemNotification.getValue().getPush_users());
            RxBus.getInstance().post(new ThemeEvent.PushUsersEvent(pushUserTipBean));
        } else if (systemNotification.getType().equals(MessageToAction.invite_play_turntable.toString())) {
            RxBus.getInstance().post(new TurntableEvent.TurntableInviteEvent(systemNotification.getValue().getUser_service_id(), systemNotification.getValue().getTurntable_amount(), systemNotification.getValue().getRole(), systemNotification.getValue().getTime(), systemNotification.getValue().getTurntable_apply_time(), systemNotification.getValue().getUser_turntable_id()));
//            EventBus.getDefault().post(new TurntableEvent.TurntableInviteEvent(systemNotification.getValue().getUser_service_id(),systemNotification.getValue().getTurntable_amount(),systemNotification.getValue().getRole(),systemNotification.getValue().getTime(),systemNotification.getValue().getTurntable_apply_time(),systemNotification.getValue().getUser_turntable_id()));
        } else if (systemNotification.getType().equals(MessageToAction.play_turntable_result.toString())) {
            RxBus.getInstance().post(new TurntableEvent.PlayTurntableResultEvent(systemNotification.getValue().getResult()));
        } else if (systemNotification.getType().equals(MessageToAction.refuse_turntable_invite.toString())) {
            RxBus.getInstance().post(new TurntableEvent.TurntableRefuseEvent(systemNotification.getValue().getUser_service_id()));
        } else if (systemNotification.getType().equals(MessageToAction.update_camera_status.toString())) {
            RxBus.getInstance().post(new VideoChatEvent.UPDATE_CAMERA_STATUS_EVENT(systemNotification));
        }


        Observable delay = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                emitter.onNext("");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).delay(3, TimeUnit.SECONDS);

        delay.subscribe(new Observer() {

            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Object o) {
                if (systemNotification.getType().equals(MessageToAction.select_push_user.toString())) {

                    if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getSound() == 1) {
                        AudioUtil.playSound(R.raw.money, 1, 0);
                    }
                    RxBus.getInstance().post(new MessageEvent.NewApplyUserEvent(systemNotification.getValue().getUser_service_id(), systemNotification.getValue().getApply_user()));
                } else if (systemNotification.getType().equals(MessageToAction.apply.toString())) {
                    if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getSound() == 1) {
                        AudioUtil.playSound(R.raw.money, 1, 0);
                    }
                    RxBus.getInstance().post(new MessageEvent.NewOrderEvent(systemNotification.getValue().getUser_service_id(), systemNotification.getValue().getPush_service()));
                } else if (systemNotification.getValue() != null && !StringUtils.isEmpty(systemNotification.getValue().getTo_action()) && systemNotification.getValue().getTo_action().equals(MessageToAction.start_video_chat.toString())) {
//                    if (!APPUtils.isBackground(MAPP.mapp)) {
                    // TODO: 2018/4/13  3秒延迟
                    IntentUtils.toVideoChatActivity(MAPP.mapp, systemNotification.getValue().getUser_service_id(), systemNotification.getValue().getChat_room_config(), VideoChatFrom.SERVICER);
//                    }
                }
                if (systemNotification.getValue() != null && systemNotification.getValue().getIs_tip() == 1) {
                    // TODO: 2018/4/13  3秒延迟
                    EventBus.getDefault().post(new MessageEvent.ShowMessageNotiEvent(systemNotification));
                }
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("messageservice", "onError" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


    }

    public void doSystemMessageAction(String toAction, SystemMessage systemMessage) {

        if (!StringUtils.isEmpty(toAction)) {
            if (toAction.equals(MessageToAction.income_list.toString())) {
                IntentUtils.toBalanceLogActivity(MAPP.mapp.getCurrentActivity());
            } else if (toAction.equals(MessageToAction.coupons_list.toString())) {
                IntentUtils.toCouponsActivity(MAPP.mapp.getCurrentActivity(), false, null, null);
            } else if (toAction.equals(MessageToAction.order_detail.toString())) {
                IntentUtils.toOrderDetailActivity(MAPP.mapp.getCurrentActivity(), systemMessage.getUser_service_id(), false);
            } else if (toAction.equals(MessageToAction.push_list.toString())) {
                AuthService.newOrderToMessageRobPage();
//                IntentUtils.toIndexServicerContainerActivity(MAPP.mapp.getCurrentActivity(), null);
            } else if (toAction.equals(MessageToAction.link.toString())) {
                IntentUtils.toHTMLActivity(MAPP.mapp.getCurrentActivity(), null, systemMessage.getLink());
            } else if (toAction.equals(MessageToAction.service_profile.toString())) {
                User user = new User();
                user.setId(systemMessage.getUid());
                IntentUtils.toHomepageActivity(MAPP.mapp.getCurrentActivity(), MValue.FROM_OTHER, user, null, null);
            } else if (toAction.equals(MessageToAction.servicer_profile.toString())) {
                User user = new User();
                user.setId(systemMessage.getUid());
                IntentUtils.toHomepageActivity(MAPP.mapp.getCurrentActivity(), MValue.FROM_OTHER, user, null, null);
            } else if (toAction.equals(MessageToAction.user_profile.toString())) {
                User user = new User();
                user.setId(systemMessage.getUid());
                IntentUtils.toUserInfoActivity(MAPP.mapp.getCurrentActivity(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, user, null, null);
            } else if (toAction.equals(MessageToAction.link.toString())) {
                IntentUtils.toHTMLActivity(MAPP.mapp.getCurrentActivity(), null, systemMessage.getLink());
            } else if (toAction.equals(MessageToAction.post_service.toString())) {

                ConfigBean.ServiceItemsConfigBean.ListBean listBean = ThemeService.getServiceBeanById(systemMessage.getService_id());
                if (listBean != null) {
                    IntentUtils.toNewThemeServiceActivity(MAPP.mapp.getCurrentActivity(), listBean, listBean.getType().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                }

            }
        }


    }
}

