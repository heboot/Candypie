package com.gdlife.candypie.widget.theme;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutChooseCityBinding;
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

public class NewThemeSelectAddressDialog extends Dialog {

    public NewThemeSelectAddressDialog(Context context) {
        super(context);
    }

    public NewThemeSelectAddressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private Consumer<ConfigBean.CityConfigBean> observer;

        private boolean isVideo;

        public Builder(Context context, Consumer<ConfigBean.CityConfigBean> observer, boolean isVideo) {
            this.context = context;
            this.observer = observer;
            this.isVideo = isVideo;

        }

        public NewThemeSelectAddressDialog create() {

            List<String> reawrdDatas = ThemeService.getAllCitysByString(isVideo);

            final NewThemeSelectAddressDialog dialog = new NewThemeSelectAddressDialog(context, R.style.zhezhao_dialog);

            LayoutChooseCityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_choose_city, null, false);

            binding.includeTop.tvTitme.setText(MAPP.mapp.getString(R.string.choose_city));


            binding.rv1.setOffset(1);
            binding.rv1.setItems(reawrdDatas);

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeTop.ivOk.setOnClickListener((v) -> {
                Observable.create(new ObservableOnSubscribe<ConfigBean.CityConfigBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<ConfigBean.CityConfigBean> emitter) throws Exception {

                        dialog.dismiss();
                        ConfigBean.CityConfigBean cityConfigBean = null;
                        if (isVideo) {
                            if (binding.rv1.getSeletedIndex() > 0) {
                                cityConfigBean = ThemeService.getAllCitys().get(binding.rv1.getSeletedIndex() - 1);
                            } else {
                                cityConfigBean = new ConfigBean.CityConfigBean();
                                cityConfigBean.setName(MAPP.mapp.getString(R.string.theme_choose_address_unlimited));
                            }
                        } else {
                            cityConfigBean = ThemeService.getAllCitys().get(binding.rv1.getSeletedIndex());
                        }


                        emitter.onNext(cityConfigBean);
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
