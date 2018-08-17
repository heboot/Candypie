package com.gdlife.candypie.widget.setprice;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutSetPriceVideoBinding;
import com.gdlife.candypie.serivce.user.SetPriceService;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by heboot on 2018/2/2.
 * 视频聊天定价弹窗
 */

public class SetPriceCoinDialog extends Dialog {

    public SetPriceCoinDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;

        private Consumer<Integer> consumer;

        private List<String> priceList;

        private String subTitle;

        public Builder(Context context, String subTitle, List<String> priceList, Consumer<Integer> consumer) {
            this.context = context;
            this.priceList = priceList;
            this.consumer = consumer;
            this.subTitle = subTitle;
        }

        public SetPriceCoinDialog create() {

            final SetPriceCoinDialog dialog = new SetPriceCoinDialog(context, R.style.QMUI_Dialog);

            LayoutSetPriceVideoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_set_price_video, null, false);


            binding.rv1.setItems(priceList);

            binding.includeTop.tvTitme.setText(R.string.set_price_title_video);

            binding.tvSubTitle.setText(subTitle);

            binding.includeTop.vClose.setOnClickListener((v) -> {
                try {
                    dialog.dismiss();
                    consumer.accept(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            binding.includeTop.ivOk.setOnClickListener((v) -> {
                try {
                    dialog.dismiss();
                    consumer.accept(binding.rv1.getSeletedIndex());
                } catch (Exception e) {
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
