package com.heboot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.heboot.utils.R;
import com.heboot.utils.databinding.DialogTipCustomOneBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class TipCustomOnePermissionDialog extends Dialog {


    public TipCustomOnePermissionDialog(Context context) {
        super(context);
    }

    public TipCustomOnePermissionDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String content;


        private String center;
        private Consumer<Integer> observer;

        TipCustomOnePermissionDialog dialog;


        public Builder(Context context, String content, String center) {
            this.context = context;
            this.content = content;
            this.center = center;

        }

        public Builder(Context context, String content, String center, Consumer<Integer> observer) {
            this.context = context;
            this.content = content;
            this.center = center;
            this.observer = observer;
        }

        public TipCustomOnePermissionDialog create() {


            dialog = new TipCustomOnePermissionDialog(context, R.style.QMUI_TipDialog);

            DialogTipCustomOneBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_tip_custom_one, null, false);

            binding.tvContent.setText(content);

            binding.setCenterText(center);


            binding.tvConfirm.setOnClickListener((v) -> {
                if (observer != null) {
                    Observable.create(new ObservableOnSubscribe<Integer>() {

                        @Override
                        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                            observer.accept(0);
                        }
                    }).subscribe(observer);
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
