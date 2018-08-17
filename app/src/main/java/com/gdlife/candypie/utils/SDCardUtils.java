package com.gdlife.candypie.utils;

import android.os.Environment;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MValue;
import com.heboot.utils.LogUtil;

import java.io.File;

/**
 * Created by Heboot on 16/7/24.
 */
public class SDCardUtils {
    /**
     * 获取SD卡的状态
     */
    public static String getState() {
        return Environment.getExternalStorageState();
    }


    /**
     * SD卡是否可用
     *
     * @return 只有当SD卡已经安装并且准备好了才返回true
     */
    public static boolean isAvailable() {
        return getState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取SD卡的根目录
     *
     * @return null：不存在SD卡
     */
    public static File getRootDirectory() {
        return isAvailable() ? Environment.getExternalStorageDirectory() : null;
    }

    public static String getRootPathPrivateVideo() {
        File file = new File(MAPP.mapp.getExternalFilesDir(MValue.SD_PATH_ROOT), MValue.SD_PATH_VIDEO);
        if (!file.mkdirs()) {
            file.mkdir();
        }
        return file.getPath();
    }

    public static String getRootPathPrivatePic() {
        File file = new File(MAPP.mapp.getExternalFilesDir(MValue.SD_PATH_ROOT), MValue.SD_PATH_PIC);
        if (!file.mkdirs()) {
            file.mkdir();
        }
        return file.getPath();
    }

    public static String getRootPathPrivateFrame() {
        File file = new File(MAPP.mapp.getExternalFilesDir(MValue.SD_PATH_ROOT), MValue.SD_PATH_FRAME + System.currentTimeMillis() + "/");
        if (!file.mkdirs()) {
            file.mkdir();
        }
        return file.getPath();
    }

    /**
     * 获取SD卡的根路径
     *
     * @return null：不存在SD卡
     */
    public static String getRootPath() {
        File rootDirectory = getRootDirectory();
        return rootDirectory != null ? rootDirectory.getAbsolutePath() : null;
    }

    /**
     * 获取sd卡路径
     *
     * @return Stringpath
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

}
