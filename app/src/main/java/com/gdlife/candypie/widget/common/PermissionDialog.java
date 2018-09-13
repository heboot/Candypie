package com.gdlife.candypie.widget.common;

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
import com.gdlife.candypie.databinding.DialogPermissionTipBinding;
import com.gdlife.candypie.serivce.UserService;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class PermissionDialog extends Dialog {


    public PermissionDialog(Context context) {
        super(context);
    }

    public PermissionDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private Consumer<Integer> observer;

        private PermissionDialog dialog;

        private boolean hasLoc, hasNoti = false;


        public Builder(Context context, Consumer<Integer> observer, boolean hasLoc, boolean hasNoti) {
            this.context = context;
            this.observer = observer;
            this.hasLoc = hasLoc;
            this.hasNoti = hasNoti;
        }

        public PermissionDialog create() {


            dialog = new PermissionDialog(context, R.style.QMUI_TipDialog);

            DialogPermissionTipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_permission_tip, null, false);

            if (hasLoc) {
                binding.llytContent2.setVisibility(View.GONE);
            }

            if (hasNoti) {
                binding.llytContent.setVisibility(View.GONE);
            } else {
                if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status().intValue() == MValue.AUTH_STATUS_SUC) {
                    binding.tvCancel.setVisibility(View.GONE);
                }
            }

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
