package com.gdlife.candypie.widget.common;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.DialogTipBinding;
import com.gdlife.candypie.databinding.DialogTipTitleBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class TipTitleDialog extends Dialog {


    public TipTitleDialog(Context context) {
        super(context);
    }

    public TipTitleDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String content;

        private Consumer<Integer> observer;

        private String title;

        private TipTitleDialog dialog;


        public Builder(Context context, Consumer<Integer> observer, String title, String content) {
            this.context = context;
            this.content = content;
            this.observer = observer;
            this.title = title;
        }

        public TipTitleDialog create() {


            dialog = new TipTitleDialog(context, R.style.QMUI_TipDialog);

            DialogTipTitleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_tip_title, null, false);

            binding.tvContent.setText(content);

            binding.tvTitle.setText(title);

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
