package com.gdlife.candypie.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.DialogServiceBinding;
import com.gdlife.candypie.databinding.DialogTipBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VipService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.netease.nim.uikit.business.contact.core.model.IContact;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class ServiceTipDialog extends Dialog {


    public ServiceTipDialog(Context context) {
        super(context);
    }

    public ServiceTipDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;


        private MValue.TIP_DIALOG_TYPE type;


        private Consumer<Integer> observer;

        private Long expire_time;

        private String avatar;

        private String prePayPrice, ratio, rechargePrice;

        ServiceTipDialog dialog;

        public Builder(Context context, Consumer<Integer> observer, MValue.TIP_DIALOG_TYPE type, Long expire_time, String avatar) {
            this.context = context;
            this.observer = observer;
            this.type = type;
            this.expire_time = expire_time;
            this.avatar = avatar;
        }

        public Builder(Context context, Consumer<Integer> observer, MValue.TIP_DIALOG_TYPE type, Long expire_time, String avatar, String rechargePrice) {
            this.context = context;
            this.observer = observer;
            this.type = type;
            this.expire_time = expire_time;
            this.avatar = avatar;
            this.rechargePrice = rechargePrice;
        }

        public Builder(Context context, Consumer<Integer> observer, MValue.TIP_DIALOG_TYPE type, Long expire_time, String avatar, String ratio, String prePayPrice) {
            this.context = context;
            this.observer = observer;
            this.type = type;
            this.expire_time = expire_time;
            this.avatar = avatar;
            this.prePayPrice = prePayPrice;
            this.ratio = ratio;
        }

        public ServiceTipDialog create() {


            dialog = new ServiceTipDialog(context, R.style.QMUI_TipDialog);

            DialogServiceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_service, null, false);

            if (!StringUtils.isEmpty(avatar)) {
                ImageUtils.showImage(binding.ivAvatar, avatar);
            }

            binding.setType(type);

            if (type == MValue.TIP_DIALOG_TYPE.ONE || type == MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER || type == MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP) {
                binding.ivAvatar.setVisibility(View.VISIBLE);
            } else {
                binding.ivAvatar.setVisibility(View.GONE);
            }

            if (type.equals(MValue.TIP_DIALOG_TYPE.CONFINE)) {
                if (StringUtils.isEmpty(VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel()))) {
                    binding.tvConfirm.setVisibility(View.GONE);
                    binding.vCenter.setVisibility(View.GONE);
                }
            }

            binding.tvContent.setText(ThemeService.getServiceTipDialogText(type));

            ThemeService.getServiceTipDialogText2(binding.tvContent2, expire_time, type, ratio, rechargePrice);

            binding.setLeftText(ThemeService.getServiceTipDialogLeftText(type));

            ThemeService.getServiceTipRightText(binding.tvConfirm, type, prePayPrice, rechargePrice);

            ImageUtils.showImage(binding.ivTip, ThemeService.getServiceTipDialogBG(type));

            binding.tvCancel.setOnClickListener((v) -> {
                dialog.dismiss();
                Observable.create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        observer.accept(0);
                    }
                }).subscribe(observer);


            });

            binding.tvConfirm.setOnClickListener((v) -> {
                dialog.dismiss();
                Observable.create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        observer.accept(1);
                    }
                }).subscribe(observer);

            });

            binding.tvCenter.setOnClickListener((v) -> {
                dialog.dismiss();
                Observable.create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        observer.accept(0);
                    }
                }).subscribe(observer);
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
