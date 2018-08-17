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
import com.gdlife.candypie.databinding.DialogPermissionTipBinding;
import com.gdlife.candypie.databinding.DialogSetPriceTipBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class SetPriceTipDialog extends Dialog {


    public SetPriceTipDialog(Context context) {
        super(context);
    }

    public SetPriceTipDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;

        private SetPriceTipDialog dialog;

        private boolean isFromIndex;


        public Builder(Context context, boolean isFromIndex) {
            this.context = context;
            this.isFromIndex = isFromIndex;
        }

        public SetPriceTipDialog create() {


            dialog = new SetPriceTipDialog(context, R.style.QMUI_TipDialog);

            dialog.setCancelable(false);

            DialogSetPriceTipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_set_price_tip, null, false);

            RxView.clicks(binding.tvConfirm).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    dialog.dismiss();
                    IntentUtils.toSetPriceActivity(context, isFromIndex);
                }
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
