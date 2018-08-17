package com.gdlife.candypie.widget.theme;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutNewThemeRewardBinding;
import com.gdlife.candypie.databinding.LayoutNewThemeTimeBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.config.MyConfigSelectBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class NewThemeSelectRewardDialog extends Dialog {

    public NewThemeSelectRewardDialog(Context context) {
        super(context);
    }

    public NewThemeSelectRewardDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private Consumer<MyConfigSelectBean> observer;

        private Integer duIndex;

        private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

        private MyConfigSelectBean myConfigSelectBean;

        private String unit;

        public Builder(Context context, Integer duIndex, ConfigBean.ServiceItemsConfigBean.ListBean listBean, Consumer<MyConfigSelectBean> observer, String unit) {
            this.context = context;
            this.observer = observer;
            this.duIndex = duIndex;
            this.listBean = listBean;
            this.unit = unit;

        }

        public NewThemeSelectRewardDialog create() {

            List<String> reawrdDatas;

            if (duIndex == null) {
                reawrdDatas = ThemeService.getVideoRewardConfig(listBean);
            } else {
                reawrdDatas = ThemeService.getRewardConfig(duIndex, listBean);
            }


            final NewThemeSelectRewardDialog dialog = new NewThemeSelectRewardDialog(context, R.style.zhezhao_dialog);

            LayoutNewThemeRewardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_theme_reward, null, false);
//            binding.ivBg.setOnClickListener((v) -> {
////                dialog.dismiss();
//            });

//            binding.ivBg.setBlurredView(binding.ivBg2);

//            binding.ivBg.invalidate();

            binding.tvUnit.setText(unit);

            binding.includeTop.tvTitme.setText(MAPP.mapp.getString(R.string.choose_reward));

            binding.rv1.setOffset(1);
            binding.rv1.setItems(reawrdDatas);

            binding.rv1.setSeletion(1);

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeTop.ivOk.setOnClickListener((v) -> {
                Observable.create(new ObservableOnSubscribe<MyConfigSelectBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<MyConfigSelectBean> emitter) throws Exception {
                        dialog.dismiss();


                        myConfigSelectBean = new MyConfigSelectBean();

                        myConfigSelectBean.setRv1SelectedIndex(binding.rv1.getSeletedIndex());
                        myConfigSelectBean.setRv1SelectedText(binding.rv1.getSeletedItem());


                        if (duIndex == null) {
                            myConfigSelectBean.setSelectedText(MAPP.mapp.getString(R.string.price_title) + binding.rv1.getSeletedItem() + MAPP.mapp.getString(R.string.unit_coin));
                        } else {
                            myConfigSelectBean.setSelectedText(MAPP.mapp.getString(R.string.price_title) + binding.rv1.getSeletedItem() + MAPP.mapp.getString(R.string.price_unit));
                        }


                        emitter.onNext(myConfigSelectBean);
                    }
                }).subscribe(observer);
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
