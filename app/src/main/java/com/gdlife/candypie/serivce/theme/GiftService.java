package com.gdlife.candypie.serivce.theme;

import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.activitys.video.VideoChatActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.gift.GiftPlayView;
import com.heboot.base.BaseBean;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.video.VideoChatStratEndBean;
import com.yalantis.dialog.TipCustomDialog;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GiftService {

    public void sendGift(GiftBean giftBean, String toUid, String userServiceId) {
        Map<String, Object> params = SignUtils.getNormalParams();
        if (Integer.parseInt(UserService.getInstance().getUser().getCoin()) < Integer.parseInt(giftBean.getPrice())) {
            TipCustomDialog coinDialog = new TipCustomDialog.Builder(MAPP.mapp.getCurrentActivity(), new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        IntentUtils.toRechargeActivity(MAPP.mapp.getCurrentActivity(), RechargeType.COIN);
                    }

                }
            }, "你的钻石余额不足\n请充值", "取消", "充值"

            ).create();
            coinDialog.show();
            return;
        }
        params = SignUtils.getNormalParams();
        if (!StringUtils.isEmpty(userServiceId)) {
            params.put(MKey.USER_SERVICE_ID, userServiceId);
        }
        params.put(MKey.GIFT_ID, giftBean.getId());
        params.put(MKey.NUMS, 1);
        params.put(MKey.TO_UID, toUid);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().send_gift(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<VideoChatStratEndBean>() {
            @Override
            public void onSuccess(BaseBean<VideoChatStratEndBean> baseBean) {
//                    ((ViewGroup) MAPP.mapp.getCurrentActivity().findViewById(android.R.id.content)).addView(new GiftPlayView(MAPP.mapp.getCurrentActivity(), giftBean));

                try {
                    new GiftPlayView(giftBean).show(((FragmentActivity) MAPP.mapp.getCurrentActivity()).getFragmentManager(), "gift");
                } catch (Exception e) {

                }

            }

            @Override
            public void onError(BaseBean<VideoChatStratEndBean> baseBean) {
                TipCustomDialog coinDialog = new TipCustomDialog.Builder(MAPP.mapp.getCurrentActivity(), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            IntentUtils.toRechargeActivity(MAPP.mapp.getCurrentActivity(), RechargeType.COIN);
                        }

                    }
                }, "你的钻石余额不足\n请充值", "取消", "充值"

                ).create();
                coinDialog.show();
            }
        });
    }


}
