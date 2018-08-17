package com.gdlife.candypie.widget.theme;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.LayoutOrderCommentBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.event.OrderEvent;
import com.heboot.rxbus.RxBus;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/2.
 */

public class ServiceCommentDialog extends Dialog {


    public ServiceCommentDialog(Context context) {
        super(context);
    }

    public ServiceCommentDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private int isDelay = 0;

        private int videoLike = -2;

        private int service_status = 0;

        ServiceCommentDialog dialog;

        private String userServiceId;

        protected Map<String, Object> params;

        private LayoutOrderCommentBinding binding;

        private boolean isVideo;


        public Builder(Context context, String userServiceId, boolean isVideo) {
            this.context = context;
            this.userServiceId = userServiceId;
            this.isVideo = isVideo;
        }

        public ServiceCommentDialog create() {


            dialog = new ServiceCommentDialog(context, R.style.zhezhao_dialog);

            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_order_comment, null, false);

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeTop.tvTitme.setText(R.string.order_comment_title);

            if (isVideo) {
                binding.inlcudeCommetDelay.getRoot().setVisibility(View.GONE);
                binding.inlcudeCommetVideolike.getRoot().setVisibility(View.VISIBLE);
            }

            binding.inlcudeCommetDelay.tvDelay.setSelected(false);
            binding.inlcudeCommetDelay.tvUndelay.setSelected(false);

            binding.inlcudeCommetDelay.tvDelay.setOnClickListener((v) -> {
                binding.inlcudeCommetDelay.tvDelay.setSelected(true);
                binding.inlcudeCommetDelay.tvUndelay.setEnabled(false);
                binding.inlcudeCommetDelay.tvTocomment.setEnabled(false);
                isDelay = 1;
                commentDelay();
            });

            binding.inlcudeCommetDelay.tvUndelay.setOnClickListener((v) -> {
                binding.inlcudeCommetDelay.tvUndelay.setSelected(true);
                binding.inlcudeCommetDelay.tvDelay.setEnabled(false);
                binding.inlcudeCommetDelay.tvTocomment.setEnabled(false);
                isDelay = 0;
                commentDelay();
            });

            binding.inlcudeCommetVideolike.ivLike.setOnClickListener((v) -> {
                if (binding.inlcudeCommetVideolike.ivLike.isSelected()) {
                    binding.inlcudeCommetVideolike.ivLike.setSelected(false);
                    binding.inlcudeCommetVideolike.ivNolike.setSelected(false);
                    videoLike = -2;
                } else {
                    binding.inlcudeCommetVideolike.ivNolike.setSelected(false);
                    binding.inlcudeCommetVideolike.ivLike.setSelected(true);
                    videoLike = -1;
                }

                checkBottom();
            });

            binding.inlcudeCommetVideolike.ivNolike.setOnClickListener((v) -> {
                if (binding.inlcudeCommetVideolike.ivNolike.isSelected()) {
                    binding.inlcudeCommetVideolike.ivNolike.setSelected(false);
                    binding.inlcudeCommetVideolike.ivLike.setSelected(false);
                    videoLike = -2;
                } else {
                    binding.inlcudeCommetVideolike.ivLike.setSelected(false);
                    binding.inlcudeCommetVideolike.ivNolike.setSelected(true);
                    videoLike = 1;
                }

                checkBottom();
            });

            binding.inlcudeCommetVideolike.btnBottom.setOnClickListener((v -> {
                commentVideo();
            }));

            binding.inlcudeCommetDelay.tvTocomment.setOnClickListener((v) -> {
                binding.inlcudeCommetDelay.getRoot().setVisibility(View.GONE);
                binding.inlcudeCommetVideolike.getRoot().setVisibility(View.VISIBLE);
                binding.includeTop.ivOk.setVisibility(View.GONE);
            });

//            binding.inlcudeCommetVideolike.rbComment.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
//                @Override
//                public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
//                    service_status = (int) rating;
//                }
//            });

            binding.inlcudeCommetVideolike.rbComment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    service_status = (int) rating;
                    checkBottom();
                }
            });

            binding.includeTop.ivOk.setVisibility(View.GONE);


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
        }


        private void commentDelay() {
            params = SignUtils.getNormalParams();
            params.put(MKey.USER_SERVICE_ID, userServiceId);
            params.put(MKey.IS_DELAY, isDelay);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);

            HttpClient.Builder.getGuodongServer().is_delay(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    RxBus.getInstance().post(OrderEvent.REFRESH_ORDER_ENENT);
                    binding.includeTop.ivOk.setVisibility(View.GONE);
                    binding.inlcudeCommetDelay.getRoot().setVisibility(View.GONE);
                    binding.inlcudeCommetVideolike.getRoot().setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {

                }
            });
//            binding.includeTop.ivOk.setVisibility(View.GONE);
//            binding.inlcudeCommetDelay.getRoot().setVisibility(View.GONE);
//            binding.inlcudeCommetVideolike.getRoot().setVisibility(View.VISIBLE);
        }

        private void commentVideo() {
            dialog.dismiss();
            params = SignUtils.getNormalParams();
            params.put(MKey.USER_SERVICE_ID, userServiceId);
            params.put(MKey.VIDEO_STATUS, videoLike);
            params.put(MKey.SERVICE_STATUS, service_status);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);

            HttpClient.Builder.getGuodongServer().service_eval(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    RxBus.getInstance().post(OrderEvent.REFRESH_ORDER_ENENT);
                    dialog.dismiss();
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {

                }
            });
        }

        private void checkBottom() {
            if (videoLike > -2 && service_status > 0) {
                binding.inlcudeCommetVideolike.btnBottom.setSelected(true);
                binding.inlcudeCommetVideolike.btnBottom.setEnabled(true);
            } else {
                binding.inlcudeCommetVideolike.btnBottom.setSelected(false);
                binding.inlcudeCommetVideolike.btnBottom.setEnabled(false);
            }
        }


    }


}
