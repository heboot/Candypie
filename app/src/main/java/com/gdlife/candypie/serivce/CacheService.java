package com.gdlife.candypie.serivce;

import com.gdlife.candypie.utils.FileUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.heboot.utils.LogUtil;
import com.netease.nim.uikit.common.util.file.FileUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CacheService {

    public static String getAppCache() {

        String videoPath = SDCardUtils.getRootPathPrivateVideo();

        String framePath = SDCardUtils.getRootPathPrivateFrame();

        String picPath = SDCardUtils.getRootPathPrivatePic();

        long videoResult = SDCardUtils.getFolderSize(new File(videoPath));

        long frameResult = SDCardUtils.getFolderSize(new File(framePath));

        long picResult = SDCardUtils.getFolderSize(new File(picPath));

        float result = (videoResult + frameResult + picResult) / 1024 / 1024;

        LogUtil.e("CacheService videoResult", videoResult + "");
        LogUtil.e("CacheService frameResult", frameResult + "");
        LogUtil.e("CacheService picResult", picResult + "");

        return result + "mb";
    }


    public static void clearAppCache() {

        String videoPath = SDCardUtils.getRootPathPrivateVideo();

        String framePath = SDCardUtils.getRootPathPrivateFrame();

        String picPath = SDCardUtils.getRootPathPrivatePic();

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                FileUtils.deleteFile(new File(videoPath));
                FileUtils.deleteFile(new File(framePath));
                FileUtils.deleteFile(new File(picPath));
                emitter.onNext(emitter);
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();

    }


}
