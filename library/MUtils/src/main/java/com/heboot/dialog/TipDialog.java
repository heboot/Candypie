package com.heboot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.heboot.utils.R;
import com.heboot.utils.databinding.DialogTipBindingByIM;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class TipDialog extends Dialog {


    public TipDialog(Context context) {
        super(context);
    }

    public TipDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String content;

        private Consumer<Integer> observer;


        private TipDialog dialog;

        private String left, right = null;


        public Builder(Context context, Consumer<Integer> observer, String content) {
            this.context = context;
            this.content = content;
            this.observer = observer;

        }

        public Builder(Context context, Consumer<Integer> observer, String content, String left, String right) {
            this.context = context;
            this.content = content;
            this.observer = observer;
            this.left = left;
            this.right = right;
        }

        public TipDialog create() {


            dialog = new TipDialog(context, R.style.QMUI_TipDialog);

            DialogTipBindingByIM binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_tip_im, null, false);

            if (left != null) {
                binding.tvCancel.setText(left);
            }

            if (right != null) {
                binding.tvConfirm.setText(right);
            }

            binding.tvContent.setText(content);

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
