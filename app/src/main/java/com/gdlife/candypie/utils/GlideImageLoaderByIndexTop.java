package com.gdlife.candypie.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.IndexTopADType;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.serivce.UserService;
import com.heboot.bean.index.IndexPopTipBean;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by heboot on 2018/2/8.
 */

public class GlideImageLoaderByIndexTop extends ImageLoader {

    private IndexPopTipBean path;


    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        this.path = (IndexPopTipBean) path;
        if (path instanceof IndexPopTipBean) {
            ImageUtils.showImage(imageView, ((IndexPopTipBean) path).getImg());

        }

        imageView.setOnClickListener((v) -> {
            MobclickAgent.onEvent(context, NumEventKeys.main_banner_click.toString());
            if (((IndexPopTipBean) path).getType().equals(IndexTopADType.video.toString())) {
                if (MValue.IndexBannerEnable) {
                    IntentUtils.toPlayerActivity2(imageView.getContext(), ((IndexPopTipBean) path).getType(), VideoPreviewFrom.USER, null, ((IndexPopTipBean) path).getVideo_cover_img(), true);
                }
            } else if (((IndexPopTipBean) path).getType().equals(IndexTopADType.h5.toString())) {
                if (MValue.IndexBannerEnable) {
                    IntentUtils.toHTMLActivity(imageView.getContext(), ((IndexPopTipBean) path).getTitle(), ((IndexPopTipBean) path).getUrl() + "?token=" + UserService.getInstance().getToken());
                }
            }

        });
    }

    @Override
    public ImageView createImageView(Context context) {
        QMUIRadiusImageView qmuiRadiusImageView = new QMUIRadiusImageView(context);
        qmuiRadiusImageView.setElevation(QMUIDisplayHelper.dp2px(context, 4));
        qmuiRadiusImageView.setCornerRadius(context.getResources().getDimensionPixelOffset(R.dimen.x3));
//        qmuiRadiusImageView.setScaleType(ImageView.ScaleType.CENTER);
        qmuiRadiusImageView.setCircle(false);
        qmuiRadiusImageView.setLayoutParams(new ViewGroup.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.x369), context.getResources().getDimensionPixelOffset(R.dimen.y100)));
        qmuiRadiusImageView.setBorderWidth(0);
        return qmuiRadiusImageView;
    }
}
