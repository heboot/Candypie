package com.gdlife.candypie.widget.common;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.DialogCashTipBinding;
import com.gdlife.candypie.databinding.DialogShareBinding;
import com.gdlife.candypie.serivce.ShareService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class ShareDialog extends Dialog {


    public ShareDialog(Context context) {
        super(context);
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;


        private ShareDialog dialog;

        private String nick, uid, avatar;

        private ConfigBean.ShareConfigBeanModel shareConfigBeanModel;


        public Builder(Context context, String uid, String avatar, String nick, ConfigBean.ShareConfigBeanModel shareConfigBeanModel) {
            this.context = context;
            this.nick = nick;
            this.shareConfigBeanModel = shareConfigBeanModel;
            this.uid = uid;
            this.avatar = avatar;
        }

        public ShareDialog create() {

            ShareService shareService = new ShareService();

            dialog = new ShareDialog(context, R.style.QMUI_TipDialog);

            DialogShareBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_share, null, false);


            for (String shareType : shareConfigBeanModel.getType()) {
                if (shareType.equals("wx_pf")) {
                    ImageUtils.showImage(binding.includeWx.ivIcon, R.drawable.icon_wx);
                    binding.includeWx.getRoot().setVisibility(View.VISIBLE);
                    binding.includeWx.tvName.setText(MAPP.mapp.getString(R.string.wx_friend));
                } else if (shareType.equals("wx_zone")) {
                    ImageUtils.showImage(binding.includeWxCircle.ivIcon, R.drawable.icon_wx_circle);
                    binding.includeWxCircle.getRoot().setVisibility(View.VISIBLE);
                    binding.includeWxCircle.tvName.setText(MAPP.mapp.getString(R.string.wx_quan));
                } else if (shareType.equals("weibo")) {
                    ImageUtils.showImage(binding.includeWeibo.ivIcon, R.drawable.icon_weibo);
                    binding.includeWeibo.getRoot().setVisibility(View.VISIBLE);
                    binding.includeWeibo.tvName.setText(MAPP.mapp.getString(R.string.weibo));
                }
            }


            binding.includeWeibo.getRoot().setOnClickListener((v) -> {
                shareService.doShareWeibo(context, avatar, nick, shareConfigBeanModel);
                dialog.dismiss();
            });

            binding.includeWx.getRoot().setOnClickListener((v) -> {
//                if (StringUtils.isEmpty(uid) && StringUtils.isEmpty(nick) && StringUtils.isEmpty(avatar)) {
//                    shareService.doShareWXByImage(context, shareConfigBeanModel);
//                } else {
                shareService.doShareWXByWebpage(context, uid, avatar, nick, shareConfigBeanModel);

//                }
                dialog.dismiss();
            });

            binding.includeWxCircle.getRoot().setOnClickListener((v) -> {
//                if (StringUtils.isEmpty(uid) && StringUtils.isEmpty(nick) && StringUtils.isEmpty(avatar)) {
//                    shareService.doShareWXCircleByImage(context, shareConfigBeanModel);
//                } else {
                shareService.doShareWXCircleByWebpage(context, uid, avatar, nick, shareConfigBeanModel);
//                }

                dialog.dismiss();
            });

            binding.clytContainer.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.ivClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
        }


    }


}
