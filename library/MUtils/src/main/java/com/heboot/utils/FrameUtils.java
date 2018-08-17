package com.heboot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.qmuiteam.qmui.util.QMUIDeviceHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/1/30.
 */

public class FrameUtils {

    public static int getFrameItemWidth(Context context, int size) {
        int totalWidth = QMUIDisplayHelper.getScreenWidth(context);

        int currentWidth = totalWidth - QMUIDisplayHelper.dp2px(context, 40);

        return currentWidth / size;

    }

    public List<String> getFrams(String viedioPath, String potoPath) throws Exception {

        List<String> mThumbPaths = new ArrayList<String>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        File file = new File(viedioPath);
        FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
        retriever.setDataSource(inputStream.getFD());

//        retriever.setDataSource(viedioPath, new HashMap<String, String>());
        // 取得视频的长度(单位为毫秒)
        String time = retriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        // 取得视频的长度(单位为秒)
        int seconds = Integer.valueOf(time) / 1000;
        // 获得想要的帧数
        @SuppressWarnings("unused")
        int num = seconds * 3;//24
        // 得到每一秒时刻的bitmap比如第一秒,第二秒
        if (!new File(potoPath).exists()) {
            new File(potoPath).mkdirs();
        }

        for (int i = 1; i <= 10; i++) {//for (int i = 1; i <= seconds; i++) {
            // 获取的是微秒
            Bitmap bitmap = retriever.getFrameAtTime(i * 1000 * 1000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            String path = potoPath + File.separator + i + ".jpg";

            mThumbPaths.add(path);
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mThumbPaths;

    }

    public static int getFrameItemWidth2(int size, int totalWidth) {


        return totalWidth / size;

    }


}
