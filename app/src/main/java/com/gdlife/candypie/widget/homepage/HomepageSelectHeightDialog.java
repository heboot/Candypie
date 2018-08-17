package com.gdlife.candypie.widget.homepage;

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
import com.gdlife.candypie.databinding.LayoutChooseHeightKgBinding;
import com.gdlife.candypie.serivce.HomepageService;
import com.gdlife.candypie.serivce.ThemeService;
import com.heboot.bean.config.ConfigBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class HomepageSelectHeightDialog extends Dialog {

    public HomepageSelectHeightDialog(Context context) {
        super(context);
    }

    public HomepageSelectHeightDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private Consumer<String> observer;


        public Builder(Context context, Consumer<String> observer) {
            this.context = context;
            this.observer = observer;

        }

        public HomepageSelectHeightDialog create() {

            List<String> reawrdDatas = HomepageService.getChooseHeights();

            int selectIndex = 0;

            for (int i = 0; i < reawrdDatas.size(); i++) {
                if (reawrdDatas.get(i).equals("165")) {
                    selectIndex = i;
                    continue;
                }
            }


            final HomepageSelectHeightDialog dialog = new HomepageSelectHeightDialog(context, R.style.zhezhao_dialog);

            LayoutChooseHeightKgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_choose_height_kg, null, false);

            binding.includeTop.tvTitme.setText(MAPP.mapp.getString(R.string.choose_height));

            binding.tvUnit.setText(MAPP.mapp.getString(R.string.unit_height));


            binding.rv1.setOffset(1);
            binding.rv1.setItems(reawrdDatas);
            binding.rv1.setSeletion(selectIndex);

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeTop.ivOk.setOnClickListener((v) -> {
                Observable.create(new ObservableOnSubscribe<String>() {

                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                        dialog.dismiss();


                        emitter.onNext(reawrdDatas.get(binding.rv1.getSeletedIndex()));
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
