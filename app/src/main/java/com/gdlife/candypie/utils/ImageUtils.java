package com.gdlife.candypie.utils;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdlife.candypie.R;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.VipService;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.bean.pay.VipLevelBean;
import com.heboot.entity.User;
import com.heboot.utils.DateUtil;
import com.heboot.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by heboot on 2018/2/8.
 */

public class ImageUtils {

    public static String IMG_PRIFIX_AVATAR_100 = "?x-oss-process=style/avatar_100";

    public static final String IMG_PRIFIX_ALBUM_200 = "?x-oss-process=style/album_200";

    public static void showImage(ImageView imageView, String url) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        try {
            Glide.with(imageView.getContext()).load(url).dontAnimate().into(imageView);
        } catch (Exception e) {

        }

    }

    public static void showImage(ImageView imageView, int redId) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        try {
            Glide.with(imageView.getContext()).load(redId).dontAnimate().into(imageView);
        } catch (Exception e) {

        }

    }

    public static Uri getCropPhotoUri() {
        String path = SDCardUtils.getRootPathPrivatePic() + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    @BindingAdapter("android:showVipImg")
    public static void showVipImg(ImageView imageView, User user) {
        if (user == null) {
            return;
        }
        VipLevelBean serviceLevelBean = VipService.getCurrentVipLevel(user.getVip_level());
        if (serviceLevelBean != null) {
            // TODO: 2018/4/3  为了模拟器适配  注释
            Glide.with(imageView.getContext()).load(serviceLevelBean.getIcon()).dontAnimate().into(imageView);
        }

    }

    @BindingAdapter("android:showImg")
    public static void showImg(ImageView imageView, String url) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(url).dontAnimate().into(imageView);
    }

    @BindingAdapter("android:showAvatar")
    public static void showAvatar(ImageView imageView, String url) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(url + IMG_PRIFIX_AVATAR_100).dontAnimate().placeholder(R.drawable.icon_avatar_normal).error(R.drawable.icon_avatar_normal).into(imageView);//.placeholder(R.drawable.icon_avatar_normal).error(R.drawable.icon_avatar_normal)
    }

    public static void showIndexUserImage(ImageView imageView, String url) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(url + IMG_PRIFIX_ALBUM_200).dontAnimate().placeholder(R.drawable.img_holder_index_zfx).error(R.drawable.icon_avatar_normal).into(imageView);//.placeholder(R.drawable.icon_avatar_normal).error(R.drawable.icon_avatar_normal)
    }

    public static void showIndexRecommendUserImage(ImageView imageView, String url) {
        // TODO: 2018/4/3  为了模拟器适配  注释
        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(R.drawable.img_holder_index_cfx).error(R.drawable.icon_avatar_normal).into(imageView);//.placeholder(R.drawable.icon_avatar_normal).error(R.drawable.icon_avatar_normal)
    }

    public static String getImageUploadName(String filePath) {
        return "public/uploads/images/" + DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD) + "/" + FileUtils.getFileName(filePath);
    }

    public static String getAuthUploadName(String filePath) {
        return "public/uploads/auth/" + DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD) + "/" + FileUtils.getFileName(filePath);
    }

    public static int[] getImageWH(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    //保存图片
    public static String saveBitmap(Bitmap mBitmap) {
        if (mBitmap == null) {
            ToastUtils.showToast("处理图片失败，请稍后重试");
            return null;
        }
        FileOutputStream fos = null;
        String filePath = SDCardUtils.getRootPathPrivatePic() + System.currentTimeMillis() + ".jpg";
        try {

            File imagePath = new File(filePath);
            if (!imagePath.exists()) {
            }
            fos = new FileOutputStream(imagePath);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {

                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

}
