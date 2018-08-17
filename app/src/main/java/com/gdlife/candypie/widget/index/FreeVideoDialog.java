package com.gdlife.candypie.widget.index;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.DialogFreeVideoBinding;
import com.gdlife.candypie.databinding.DialogTipTitleBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.index.IndexPopTipBean;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class FreeVideoDialog extends Dialog {


    public FreeVideoDialog(Context context) {
        super(context);
    }

    public FreeVideoDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private Consumer<Integer> observer;

        private FreeVideoDialog dialog;

        private IndexPopTipBean indexPopTipBean;

        public Builder(Context context, IndexPopTipBean indexPopTipBean, Consumer consumer) {
            this.context = context;
            this.indexPopTipBean = indexPopTipBean;
            this.observer = consumer;
        }

        public FreeVideoDialog create() {


            dialog = new FreeVideoDialog(context, R.style.QMUI_TipDialog);

            DialogFreeVideoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_free_video, null, false);


            ImageUtils.showImage(binding.ivAd, indexPopTipBean.getImg());
            binding.ivClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

//            binding.ivAd.setOnClickListener((v)->{
//                dialog.dismiss();
//                IntentUtils.toDiscoverActivity(v.getContext());
//            });

            RxView.clicks(binding.ivAd).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    dialog.dismiss();

                    observer.accept(1);

//                    if (!StringUtils.isEmpty(indexPopTipBean.getUrl())) {
//                        IntentUtils.toHTMLActivity(context, null, indexPopTipBean.getUrl() + "?token=" + UserService.getInstance().getToken());
//                    } else {
//                        IntentUtils.toDiscoverActivity(context);
//                    }
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
