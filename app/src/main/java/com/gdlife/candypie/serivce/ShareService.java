package com.gdlife.candypie.serivce;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.SettingNotificationType;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.MD5;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShareService {

    private MyPlatformActionListener myPlatformActionListener;

    private ConfigBean.ShareConfigBeanModel currentShareConfigModel;

    private String uid;

//    private QMUITipDialog loadingDialog;

    public void doShareWeibo(Context context, String avatar, String nick, ConfigBean.ShareConfigBeanModel shareConfigBeanModel) {

//        if (loadingDialog == null) {
//            loadingDialog = DialogUtils.getLoadingDialog(context, null, false);
//        }
//        loadingDialog.show();

        currentShareConfigModel = shareConfigBeanModel;
        myPlatformActionListener = new MyPlatformActionListener();

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(StringUtils.isEmpty(nick) ? shareConfigBeanModel.getTitle() : getShareTitle(nick, shareConfigBeanModel.getTitle()));
        sp.setImageUrl(StringUtils.isEmpty(avatar) ? shareConfigBeanModel.getImg() : getShareAvatar(avatar, shareConfigBeanModel.getImg()));
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }


    public void doShareWXByImage(Context context, ConfigBean.ShareConfigBeanModel shareConfigBeanModel) {

//        if (loadingDialog == null) {
//            loadingDialog = DialogUtils.getLoadingDialog(context, null, false);
//        }
//
//        loadingDialog.show();
        currentShareConfigModel = shareConfigBeanModel;
        myPlatformActionListener = new MyPlatformActionListener();

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setImageUrl(shareConfigBeanModel.getImg());
        sp.setTitle(shareConfigBeanModel.getTitle());
        sp.setText(shareConfigBeanModel.getContent());
        sp.setShareType(Platform.SHARE_IMAGE);
        Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
        weibo.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }


    public void doShareWXCircleByImage(Context context, ConfigBean.ShareConfigBeanModel shareConfigBeanModel) {

//        if (loadingDialog == null) {
//            loadingDialog = DialogUtils.getLoadingDialog(context, null, false);
//        }
//
//        loadingDialog.show();
        currentShareConfigModel = shareConfigBeanModel;
        myPlatformActionListener = new MyPlatformActionListener();

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setImageUrl(shareConfigBeanModel.getImg());
        sp.setTitle(shareConfigBeanModel.getTitle());
        sp.setText(shareConfigBeanModel.getContent());
        sp.setShareType(Platform.SHARE_IMAGE);

        Platform moments = ShareSDK.getPlatform(WechatMoments.NAME);
        moments.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
// 执行图文分享
        moments.share(sp);
    }


    public void doShareWXByWebpage(Context context, String uid, String avatar, String nick, ConfigBean.ShareConfigBeanModel shareConfigBeanModel) {

//        if (loadingDialog == null) {
//            loadingDialog = DialogUtils.getLoadingDialog(context, null, false);
//        }
//
//        loadingDialog.show();
        currentShareConfigModel = shareConfigBeanModel;
        myPlatformActionListener = new MyPlatformActionListener();
        this.uid = uid;

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(StringUtils.isEmpty(nick) ? shareConfigBeanModel.getTitle() : getShareTitle(nick, shareConfigBeanModel.getTitle()));
        sp.setText(StringUtils.isEmpty(nick) ? shareConfigBeanModel.getContent() : getShareTitle(nick, shareConfigBeanModel.getContent()));
        sp.setImageUrl(StringUtils.isEmpty(avatar) ? shareConfigBeanModel.getImg() : getShareAvatar(avatar, shareConfigBeanModel.getImg()));
        sp.setShareType(Platform.SHARE_WEBPAGE);
        if (StringUtils.isEmpty(uid)) {
            if (!StringUtils.isEmpty(shareConfigBeanModel.getLink())) {
                sp.setUrl(getShareLink("", shareConfigBeanModel.getLink()));
                sp.setTitleUrl(getShareLink("", shareConfigBeanModel.getLink()));
            }
        } else {
            if (!StringUtils.isEmpty(shareConfigBeanModel.getLink())) {
                sp.setUrl(getShareLink(uid, shareConfigBeanModel.getLink()));
                sp.setTitleUrl(getShareLink(uid, shareConfigBeanModel.getLink()));
            }
        }


        if (StringUtils.isEmpty(shareConfigBeanModel.getLink())) {
            doShareWXByImage(context, shareConfigBeanModel);
            return;
        }
        Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
        weibo.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }

    public void doShareWXCircleByWebpage(Context context, String uid, String avatar, String nick, ConfigBean.ShareConfigBeanModel shareConfigBeanModel) {

//        if (loadingDialog == null) {
//            loadingDialog = DialogUtils.getLoadingDialog(context, null, false);
//        }
//
//        loadingDialog.show();
        currentShareConfigModel = shareConfigBeanModel;
        myPlatformActionListener = new MyPlatformActionListener();
        this.uid = uid;
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(StringUtils.isEmpty(nick) ? shareConfigBeanModel.getTitle() : getShareTitle(nick, shareConfigBeanModel.getTitle()));
        sp.setText(StringUtils.isEmpty(nick) ? shareConfigBeanModel.getContent() : getShareTitle(nick, shareConfigBeanModel.getContent()));
        sp.setImageUrl(StringUtils.isEmpty(avatar) ? shareConfigBeanModel.getImg() : getShareAvatar(avatar, shareConfigBeanModel.getImg()));
        sp.setShareType(Platform.SHARE_WEBPAGE);

        if (StringUtils.isEmpty(uid)) {
            if (!StringUtils.isEmpty(shareConfigBeanModel.getLink())) {
                sp.setUrl(shareConfigBeanModel.getLink());
                sp.setTitleUrl(shareConfigBeanModel.getLink());
            }
        } else {
            if (!StringUtils.isEmpty(shareConfigBeanModel.getLink())) {
                sp.setUrl(getShareLink(uid, shareConfigBeanModel.getLink()));
                sp.setTitleUrl(getShareLink(uid, shareConfigBeanModel.getLink()));
            }
        }

        if (StringUtils.isEmpty(shareConfigBeanModel.getLink())) {
            doShareWXCircleByImage(context, shareConfigBeanModel);
            return;
        }


        Platform moments = ShareSDK.getPlatform(WechatMoments.NAME);
        moments.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
// 执行图文分享
        moments.share(sp);
    }

    private String getShareTitle(String nick, String title) {
        title = title.replace("{#nickname}", nick);
        return title;
    }


    public String getShareContent(String nick, String title) {
        title = title.replace("{#nickname}", nick);
        return title;
    }

    private String getShareAvatar(String avatar, String img) {
        img = img.replace("{#avatar}", avatar);
        return img;
    }

    private String getShareLink(String uid, String link) {
        if (link.indexOf("video_id") > -1) {
            link = link.replace("{#video_id}", uid);
            link = link.replace("{#video_idmd5}", MD5.getMessageDigest(MValue.CHAT_PRIEX + "_" + uid));
            LogUtil.e("shareservice link=", link);
        }
        if (link.indexOf("uid") > -1) {
            link = link.replace("{#uid}", uid);
            link = link.replace("{#uidmd5}", MD5.getMessageDigest(MValue.CHAT_PRIEX + "_" + uid));
            LogUtil.e("shareservice link=", link);
        }
        if (link.indexOf("share_uid") > -1) {
            link = link.replace("{#share_uid}", UserService.getInstance().getUser() == null ? "0" : UserService.getInstance().getUser().getId() + "");
//            link = link.replace("{#uidmd5}", MD5.getMessageDigest(MValue.CHAT_PRIEX + "_" + uid));
        }
        return link;
    }


    public class MyPlatformActionListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            if (loadingDialog != null) {
//                loadingDialog.dismiss();
//            }


            LogUtil.e("shareservice", ">>" + i + ">>>" + JSON.toJSONString(currentShareConfigModel));
            if (currentShareConfigModel != null) {
                Map<String, Object> params = SignUtils.getNormalParams();
                if (!StringUtils.isEmpty(uid)) {
                    params.put(MKey.OBJECT_ID, uid);
                }
                if (!StringUtils.isEmpty(currentShareConfigModel.getAction_id())) {
                    params.put(MKey.ACTION_ID, currentShareConfigModel.getAction_id());
                }


                if (StringUtils.isEmpty(uid) && StringUtils.isEmpty(currentShareConfigModel.getAction_id())) {
                    return;
                }
                String sign = SignUtils.doSign(params);
                params.put(MKey.SIGN, sign);
                HttpClient.Builder.getGuodongServer().log(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                    @Override
                    public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    }

                    @Override
                    public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    }
                });

            }


        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CrashReport.postCatchedException(new Throwable("分享错误" + i));
            CrashReport.postCatchedException(throwable);
//            if (loadingDialog != null) {
//                loadingDialog.dismiss();
//            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
//            if (loadingDialog != null) {
//                loadingDialog.dismiss();
//            }
        }
    }

}
