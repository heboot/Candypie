package com.gdlife.candypie.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdlife.candypie.R;
import com.gdlife.candypie.listener.AnimatorListener;
import com.gdlife.candypie.listener.OnDelayedClickListener;
import com.gdlife.candypie.utils.JoyAnimationUtils;


public class ProgressFramelayout extends FrameLayout {
    public final static int STATUS_PROGRESS = 0;
    public final static int STATUS_MAIN = 1;
    public final static int STATUS_NODATA = 2;
    public final static int STATUS_ERROR = 3;
    private final static int MIN_ANIMATION_TIME = 600;
    private final static int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private Context mContext;
    private View progressView;
    //private GifMovieView gifMovieView;
    private View noDataLayout;
    private View mainView;

    private int progressLayoutId;
    private int noDataLayoutId;
    private String noDataIntro;
    private int noDataImageId;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    public int currentStatus = STATUS_PROGRESS;
    private OnClickListener errorClickListener;
    private OnClickListener noDataClickListener;
    private View errorView;
    private View noDataView;
    private MainAnimationListener mainAnimationListener;
    private ErrorAnimationListener errorAnimationListener;
    private NoDataAnimationListener noDataAnimationListener;
    private ProgressAnimationListener progressAnimationListener;
    private ImageView errorTv;
    private ImageView noDataImageView;
    private TextView tvNodataIntro;
    private long currentProgreesStartTime = 0;
    private boolean needLoading;

    public OnClickListener getErrorClickListener() {
        return errorClickListener;
    }

    public void setErrorClickListener(OnClickListener errorClickListener) {
        this.errorClickListener = errorClickListener;
        this.noDataClickListener = errorClickListener;
    }

    public void setNoDataClickListener(OnClickListener noDataClickListener) {
        this.noDataClickListener = noDataClickListener;
        this.errorClickListener = noDataClickListener;
    }

    public boolean isNeedLoading() {
        return needLoading;
    }

    public void setNeedLoading(boolean needLoading) {
        this.needLoading = needLoading;
        currentStatus = STATUS_PROGRESS;
        progressView.setVisibility(VISIBLE);
    }

    public OnClickListener getNoDataClickListener() {
        return noDataClickListener;
    }

    public ProgressFramelayout(Context context) {
        this(context, null, 0);
    }

    public ProgressFramelayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.needLoading = true;
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.JoyProgressFramelayout, defStyleAttr, 0);
        if (a != null) {
            progressLayoutId = a.getResourceId(R.styleable.JoyProgressFramelayout_progress, 0);
            noDataLayoutId = a.getResourceId(R.styleable.JoyProgressFramelayout_no_data, 0);
            noDataImageId = a.getResourceId(R.styleable.JoyProgressFramelayout_no_data_image, 0);
            noDataIntro = a.getString(R.styleable.JoyProgressFramelayout_no_data_intro);
            backgroundColor = a.getResourceId(R.styleable.JoyProgressFramelayout_android_background, DEFAULT_BACKGROUND_COLOR);
            needLoading = a.getBoolean(R.styleable.JoyProgressFramelayout_need_loading, true);
            a.recycle();
        }
        super.setBackgroundColor(backgroundColor);
        init();
    }

    public View getNoDataLayout() {
        return noDataLayout;
    }


    public View getProgressView() {
        return progressView;
    }

    public View getMainView() {
        return mainView;
    }

    private void init() {
        if (progressLayoutId != 0) {
            progressView = LayoutInflater.from(mContext).inflate(progressLayoutId, null, false);
            initLoadingView(progressView);
        }
        if (noDataLayoutId != 0) {
            noDataLayout = LayoutInflater.from(mContext).inflate(noDataLayoutId, null, false);
            initErrorView(noDataLayout);
        }
        mainAnimationListener = new MainAnimationListener();
        errorAnimationListener = new ErrorAnimationListener();
        noDataAnimationListener = new NoDataAnimationListener();
        progressAnimationListener = new ProgressAnimationListener();

    }

    private void initLoadingView(View progressView) {
        if (progressView != null) {
            progressView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    private void initErrorView(View noDataLayout) {

        errorView = noDataLayout.findViewById(R.id.error_layout);
        noDataView = noDataLayout.findViewById(R.id.nodata_layout);
        errorTv = (ImageView) noDataLayout.findViewById(R.id.error_click);
        noDataImageView = (ImageView) noDataLayout.findViewById(R.id.nodata_click);
        tvNodataIntro = (TextView) noDataLayout.findViewById(R.id.tv_nodate_intro);
        if (null != noDataIntro) {
            tvNodataIntro.setText(noDataIntro);
        }
        if (0 != noDataImageId) {
            noDataImageView.setImageResource(noDataImageId);
        }
        if (errorTv != null) {
            errorTv.setOnClickListener(new OnDelayedClickListener() {
                @Override
                public void onDelayClick(View view) {
                    toProgress();
                    if (errorClickListener != null) {
                        errorClickListener.onClick(view);
                    }
                }
            });
        }
//        if (noDataImageView != null) {
//            noDataImageView.setOnClickListener(new OnDelayedClickListener() {
//                @Override
//                public void onDelayClick(View view) {
//                    toProgress();
//                    if (noDataClickListener != null) {
//                        noDataClickListener.onClick(view);
//                    }
//                }
//            });
//        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalStateException("只能拥有一个孩子");
        }
        mainView = getChildAt(0);
        if (noDataLayout != null) {
            this.addView(noDataLayout);
            noDataLayout.setVisibility(GONE);
        }
        if (progressView != null) {
            this.addView(progressView);
            currentProgreesStartTime = System.currentTimeMillis();
            if (needLoading) {
                currentStatus = STATUS_PROGRESS;
                progressView.setVisibility(VISIBLE);
            } else {
                currentStatus = STATUS_MAIN;
                progressView.setVisibility(GONE);
            }
        }
    }


    public void toMain() {
        switch (currentStatus) {
            case STATUS_MAIN:
                break;
            case STATUS_PROGRESS:
                long currentTime = System.currentTimeMillis();
                if (currentTime - currentProgreesStartTime < MIN_ANIMATION_TIME) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressToMain();
                        }
                    }, MIN_ANIMATION_TIME - currentTime);
                } else {
                    progressToMain();
                }
                break;
            case STATUS_ERROR:
                errorToMain();
            case STATUS_NODATA:
                noDataToMain();
                break;

        }
    }


    public void toProgress() {
        currentProgreesStartTime = System.currentTimeMillis();
        switch (currentStatus) {
            case STATUS_MAIN:
                mainToProgress();
                break;
            case STATUS_PROGRESS:
                break;
            case STATUS_ERROR:
                errorToProgress();
            case STATUS_NODATA:
                noDataToProgress();
                break;
        }
        currentStatus = STATUS_PROGRESS;
    }


    public void toError() {

        switch (currentStatus) {
            case STATUS_MAIN:
                mainToError();
                break;
            case STATUS_PROGRESS:
                long currentTime = System.currentTimeMillis();
                if (currentTime - currentProgreesStartTime < MIN_ANIMATION_TIME) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressToError();
                        }
                    }, MIN_ANIMATION_TIME - currentTime);
                } else {
                    progressToError();
                }
                break;
            case STATUS_ERROR:
                break;
            case STATUS_NODATA:
                switchErrorShow();
                break;
        }

    }


    public void toNoData() {
        switch (currentStatus) {
            case STATUS_MAIN:
                mainToNodata();
                break;
            case STATUS_PROGRESS:
                long currentTime = System.currentTimeMillis();
                if (currentTime - currentProgreesStartTime < MIN_ANIMATION_TIME) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressToNoData();
                        }
                    }, MIN_ANIMATION_TIME - currentTime);
                } else {
                    progressToNoData();
                }
                break;
            case STATUS_ERROR:
                switchNoDataShow();
                break;
            case STATUS_NODATA:
                break;
        }

    }


    private void mainToError() {
        progressView.setVisibility(GONE);
        if (noDataLayout == null) {
            throw new IllegalStateException("请设置异常处理布局");
        }
        switchErrorShow();
        JoyAnimationUtils.makeProgressDismissWithListener(mainView, noDataLayout, errorAnimationListener);
    }


    private void progressToMain() {
        if (noDataLayout != null) {
            noDataLayout.setVisibility(GONE);
        }
        JoyAnimationUtils.makeProgressDismissWithListener(progressView, mainView, mainAnimationListener);
    }

    private void mainToProgress() {
        if (noDataLayout != null && noDataLayout.getVisibility() != GONE) {
            noDataLayout.setVisibility(GONE);
        }
        JoyAnimationUtils.makeProgressDismissWithListener(mainView, progressView, progressAnimationListener);
    }


    private void progressToNoData() {
        if (noDataLayout == null) {
            throw new IllegalStateException("请设置异常处理布局");
        }
        mainView.setVisibility(GONE);
        switchNoDataShow();
        JoyAnimationUtils.makeProgressDismissWithListener(progressView, noDataLayout, noDataAnimationListener);
    }

    private void progressToError() {
        if (noDataLayout == null) {
            throw new IllegalStateException("请设置异常处理布局");
        }
        mainView.setVisibility(GONE);
        switchErrorShow();
        JoyAnimationUtils.makeProgressDismissWithListener(progressView, noDataLayout, errorAnimationListener);
    }


    private void errorToMain() {
        if (mainView == null) {
            throw new IllegalStateException("请设置主布局");
        }
        if (progressView != null && progressView.getVisibility() != GONE) {
            progressView.setVisibility(GONE);
        }
        JoyAnimationUtils.makeProgressDismissWithListener(noDataLayout, mainView, mainAnimationListener);
    }

    private void noDataToMain() {
        if (mainView == null) {
            throw new IllegalStateException("请设置主布局");
        }
        if (progressView != null && progressView.getVisibility() != GONE) {
            progressView.setVisibility(GONE);
        }
        JoyAnimationUtils.makeProgressDismissWithListener(noDataLayout, mainView, mainAnimationListener);
    }

    private void noDataToProgress() {
        mainView.setVisibility(GONE);
        if (progressView == null) {
            throw new IllegalStateException("请设置ProgressLoading界面");
        }
        JoyAnimationUtils.makeProgressDismissWithListener(noDataLayout, progressView, progressAnimationListener);
    }

    private void errorToProgress() {
        mainView.setVisibility(GONE);
        if (progressView == null) {
            throw new IllegalStateException("请设置ProgressLoading界面");
        }
        JoyAnimationUtils.makeProgressDismissWithListener(noDataLayout, progressView, progressAnimationListener);
    }


    private void mainToNodata() {
        progressView.setVisibility(GONE);
        if (noDataLayout == null) {
            throw new IllegalStateException("请设置异常处理布局");
        }
        switchNoDataShow();
        JoyAnimationUtils.makeProgressDismissWithListener(mainView, noDataLayout, noDataAnimationListener);

    }


    private void switchErrorShow() {
        if (errorView != null && errorView.getVisibility() != VISIBLE) {
            errorView.setVisibility(VISIBLE);
        }

        if (noDataView != null && noDataView.getVisibility() != GONE) {
            noDataView.setVisibility(GONE);
        }
        currentStatus = STATUS_ERROR;
    }

    private void switchNoDataShow() {
        if (errorView != null && errorView.getVisibility() != GONE) {
            errorView.setVisibility(GONE);
        }

        if (noDataView != null && noDataView.getVisibility() != VISIBLE) {
            noDataView.setVisibility(VISIBLE);
        }
        currentStatus = STATUS_NODATA;
    }

    private class MainAnimationListener extends AnimatorListener {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            currentStatus = STATUS_MAIN;
        }
    }

    private class ProgressAnimationListener extends AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            currentStatus = STATUS_PROGRESS;
        }
    }

    private class NoDataAnimationListener extends AnimatorListener {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            currentStatus = STATUS_NODATA;
        }
    }

    private class ErrorAnimationListener extends AnimatorListener {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            currentStatus = STATUS_ERROR;
        }
    }

    public void setNoDataInfo(String s) {
        if (null != tvNodataIntro) {
            tvNodataIntro.setText(s);
        }
    }


}
