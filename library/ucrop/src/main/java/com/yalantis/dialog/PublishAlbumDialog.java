package com.yalantis.dialog;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.heboot.event.VideoEvent;
import com.heboot.rxbus.RxBus;
import com.yalantis.ucrop.R;
import com.yalantis.ucrop.databinding.ViewProgressDialogBinding;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by Heboot on 2017/8/4.
 */

public class PublishAlbumDialog extends Dialog {

    private ViewProgressDialogBinding binding;


    private int time;

    private ValueAnimator animator;

    private int currentprogress = 80;

    public PublishAlbumDialog(Context context) {
        super(context);
    }

    public PublishAlbumDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public PublishAlbumDialog create() {
            final PublishAlbumDialog dialog = new PublishAlbumDialog(context, R.style.QMUI_TipDialog);
            final ViewProgressDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_progress_dialog, null, false);
            dialog.setBinding(binding);
            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);


            RxBus.getInstance().toObserverable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Object o) {
                    if (o instanceof VideoEvent.VideoUploadProgressEvent) {
                        int progress = ((VideoEvent.VideoUploadProgressEvent) o).getProgress();
                        binding.tvLoadingText.setText(context.getString(R.string.upload_ing));
                        binding.pb.setProgress(progress);
                        if (progress >= 100) {
                            binding.tvLoadingText.setText(context.getString(R.string.upload_suc));
                            dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });


            return dialog;
        }
    }

    public void showAni() {
//        animator = ValueAnimator.ofInt(0, 80).setDuration(time);
//        animator.setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int p = (int) valueAnimator.getAnimatedValue();
//                currentprogress = p;
////                LogUtils.e("progress test", p + ">");
//                getBinding().tvLoadingText.setText("正在上传..");
//                getBinding().pb.setProgress(p);
//            }
//        });
//        animator.start();

    }

    public void showAniTo100() {
//        animator = ValueAnimator.ofInt(currentprogress, 100).setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int p = (int) valueAnimator.getAnimatedValue();
//                getBinding().pb.setProgress(p);
//                if (p == 100) {
//                    getBinding().tvLoadingText.setText("上传成功");
//                    dismiss();
////                    RxBus.getInstance().post(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_TO_USERINFO);
////                    EventBus.getDefault().post(new ClosePageEvent());
//                } else {
//                    getBinding().tvLoadingText.setText("正在上传..");
//
//                }
//
//            }
//        });
//        animator.start();

    }

    public void stopAni() {
        if (animator != null) {
            animator.cancel();
        }


    }

    public ViewProgressDialogBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewProgressDialogBinding binding) {
        this.binding = binding;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
