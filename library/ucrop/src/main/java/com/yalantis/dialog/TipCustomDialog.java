package com.yalantis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.yalantis.ucrop.R;
import com.yalantis.ucrop.databinding.DialogTipCustomBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class TipCustomDialog extends Dialog {


    public TipCustomDialog(Context context) {
        super(context);
    }

    public TipCustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String content;

        private Consumer<Integer> observer;

        private String left, right;


        TipCustomDialog dialog;


        public Builder(Context context, Consumer<Integer> observer, String content, String left, String right) {
            this.context = context;
            this.content = content;
            this.observer = observer;
            this.left = left;
            this.right = right;

        }

        public TipCustomDialog create() {


            dialog = new TipCustomDialog(context, R.style.QMUI_TipDialog);

            DialogTipCustomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_tip_custom, null, false);

            binding.tvContent.setText(content);

            binding.setLeftText(left);

            binding.setRightText(right);

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
