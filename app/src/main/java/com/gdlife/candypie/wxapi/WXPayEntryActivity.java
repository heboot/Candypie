package com.gdlife.candypie.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.heboot.event.PayEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by Heboot on 2017/8/11.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_toolbar);

        api = WXAPIFactory.createWXAPI(this, MValue.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.e("onNewIntent", "onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e(TAG, resp.getType() + "onPayFinish, errCode = " + resp.errCode + "ConstantsAPI.COMMAND_PAY_BY_WX" + ConstantsAPI.COMMAND_PAY_BY_WX);
        if (resp.errCode == -2) {
            finish();
            return;
        }
        LogUtil.e(TAG, ConstantsAPI.COMMAND_PAY_BY_WX + "onPayFinish, 2errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp.errCode != -2) {
//            EventBus.getDefault().post(new PayEvent.PaySucEvent());
//            EventBus.getDefault().post(new UpdateActDetailEvent());
            if (MValue.currentRechargeType == RechargeType.GOLD_VIP) {
                RxBus.getInstance().post(PayEvent.RechargeGoldVipSUCEvent);
                MValue.currentRechargeType = null;
            } else if (MValue.currentRechargeType == RechargeType.RECHARGE) {
                RxBus.getInstance().post(PayEvent.RechargeSUCEvent);
                MValue.currentRechargeType = null;
            } else {
                RxBus.getInstance().post(PayEvent.PaySUCEvent);
                MValue.currentRechargeType = null;
            }

            finish();
//            IntentUtils.intent2CommentActivity(this, );
        }
    }
}
