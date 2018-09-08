package com.gdlife.candypie.serivce;

import com.gdlife.candypie.utils.SDCardUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.DownloadConfig;
import zlc.season.rxdownload3.core.Mission;
import zlc.season.rxdownload3.core.Status;

/**
 * Created by heboot on 2018/3/24.
 */

public class DownloadService {


    public Disposable downlaodVideo(String videoPath, String localName, Consumer<Status> observer) {
        Mission mission = new Mission(videoPath, localName, SDCardUtils.getRootPathPrivateVideo());
        RxDownload rxDownload = RxDownload.INSTANCE;
        return rxDownload.create(mission).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

    }

    public Disposable downlaodAvatar(String videoPath, String localName, Consumer<Status> observer) {
        Mission mission = new Mission(videoPath, localName, SDCardUtils.getRootPathPrivatePic());
        RxDownload rxDownload = RxDownload.INSTANCE;
        return rxDownload.create(mission).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

    }

}
