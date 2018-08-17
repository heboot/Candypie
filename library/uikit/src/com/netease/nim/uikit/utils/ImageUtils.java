package com.netease.nim.uikit.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.aliyun.common.utils.StringUtils;
import com.bumptech.glide.Glide;

/**
 * Created by heboot on 2018/2/8.
 */

public class ImageUtils {


    public static void showImage(ImageView imageView, String url) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(url).dontAnimate().into(imageView);
    }

    public static void showImage(ImageView imageView, int redId) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(redId).dontAnimate().into(imageView);
    }


    @BindingAdapter("android:showImg")
    public static void showImg(ImageView imageView, String url) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(url).dontAnimate().into(imageView);
    }


}
