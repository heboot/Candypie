package com.gdlife.candypie.widget.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.LayoutGiftPlayViewBinding;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.gift.GiftBean;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.functions.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("ValidFragment")
public class GiftPlayView extends DialogFragment {

    private LayoutGiftPlayViewBinding binding;

    private GiftBean giftBean;

    public GiftPlayView(GiftBean giftBean) {
        this.giftBean = giftBean;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置 dialog 的背景为 null
        getDialog().getWindow().setBackgroundDrawable(null);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.layout_gift_play_view, null, false);
        if (!StringUtils.isEmpty(giftBean.getTip_msg())) {
            showTipView(giftBean.getTip_msg(), giftBean.getIncome_msg());
        }
        loadAnimation(giftBean.getPlay_url());
        return binding.getRoot();
    }

    /**
     * 设置下载器，这是一个可选的配置项。
     *
     * @param parser
     */
    private void resetDownloader(SVGAParser parser) {
        parser.setFileDownloader(new SVGAParser.FileDownloader() {
            @Override
            public void resume(final URL url, final Function1<? super InputStream, Unit> complete, final Function1<? super Exception, Unit> failure) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).get().build();
                        try {
                            Response response = client.newCall(request).execute();
                            complete.invoke(response.body().byteStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                            failure.invoke(e);
                        }
                    }
                }).start();
            }
        });
    }


    private void loadAnimation(String s) {
        MValue.currentSelectedGiftBean = null;
        binding.svgaview.setLoops(1);
        SVGAParser parser = new SVGAParser(MAPP.mapp.getCurrentActivity());
        resetDownloader(parser);
        try {
            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            parser.parse(url, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    binding.svgaview.setVisibility(View.VISIBLE);
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    binding.svgaview.setImageDrawable(drawable);
                    binding.svgaview.setCallback(new SVGACallback() {
                        @Override
                        public void onPause() {

                        }

                        @Override
                        public void onFinished() {
                            binding.svgaview.stopAnimation();
                            binding.svgaview.setVisibility(View.GONE);
                            dismiss();
                        }

                        @Override
                        public void onRepeat() {

                        }

                        @Override
                        public void onStep(int i, double v) {

                        }
                    });
                    binding.svgaview.startAnimation();

                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }


    private void showTipView(String info1, String info2) {
        binding.includeHehe.setInfo1(info1);
        binding.includeHehe.setInfo2(info2);
        YoYo.with(Techniques.SlideInLeft).duration(500).withListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObserableUtils.countdown(3).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 0) {
                            hideTipView();
                        }
                    }
                });
            }
        }).playOn(binding.includeHehe.getRoot());
    }

    private void hideTipView() {
        if (binding.includeHehe.getRoot().getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.SlideOutRight).duration(500).playOn(binding.includeHehe.getRoot());
        }
    }

}
