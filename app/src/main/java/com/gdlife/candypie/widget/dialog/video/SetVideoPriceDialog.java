package com.gdlife.candypie.widget.dialog.video;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.DialogSetVideoPriceBinding;
import com.gdlife.candypie.databinding.LayoutNewThemeRewardBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.config.MyConfigSelectBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class SetVideoPriceDialog extends Dialog {

    public SetVideoPriceDialog(Context context) {
        super(context);
    }

    public SetVideoPriceDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private Consumer<Integer> observer;


        public Builder(Context context, Consumer<Integer> observer) {
            this.context = context;
            this.observer = observer;

        }

        public SetVideoPriceDialog create() {

            List<String> reawrdDatas;

            reawrdDatas = MAPP.mapp.getConfigBean().getVideo_config().getPrice();


            final SetVideoPriceDialog dialog = new SetVideoPriceDialog(context, R.style.zhezhao_dialog);

            DialogSetVideoPriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_set_video_price, null, false);
//            binding.ivBg.setOnClickListener((v) -> {
////                dialog.dismiss();
//            });

//            binding.ivBg.setBlurredView(binding.ivBg2);

//            binding.ivBg.invalidate();

            binding.tvUnit.setText("钻");

            binding.includeTop.tvTitme.setText("设定收费价格");

            binding.rv1.setOffset(1);
            binding.rv1.setItems(reawrdDatas);

            binding.rv1.setSeletion(1);

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeTop.ivOk.setOnClickListener((v) -> {
                try {
                    observer.accept(binding.rv1.getSeletedIndex());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);

            return dialog;
        }
    }


}
