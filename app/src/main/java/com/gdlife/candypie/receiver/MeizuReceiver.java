package com.gdlife.candypie.receiver;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.heboot.utils.LogUtil;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;

public class MeizuReceiver extends MzPushMessageReceiver {

    @Override
    public void onRegister(Context context, String pushId) {
        LogUtil.e("mapp meizu test", "onRegister" + pushId);
    }

    @Override
    public void onUnRegister(Context context, boolean success) {

    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {

    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        LogUtil.e("mapp meizu test", "onRegister" + JSON.toJSONString(registerStatus));
    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }
}
