package com.gdlife.candypie.serivce;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.UpdateVersionDialog;
import com.heboot.utils.LogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Downloading;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Mission;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;

public class UpdateVersionService {

    public static void checkVersionUpdate(Context context, Consumer<String> next) {
        if (MValue.updateVersionBean != null) {
            UpdateVersionDialog updateVersionDialog = new UpdateVersionDialog.Builder(context, MValue.updateVersionBean.getTitle(), MValue.updateVersionBean.getContent(), MValue.updateVersionBean.getIs_force()).create();
            if (next != null) {
                updateVersionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            next.accept("update");
                        } catch (Exception e) {
                        }
                    }
                });
            }
            updateVersionDialog.show();
        } else {
            try {
                next.accept("update");
            } catch (Exception e) {
            }
        }
    }

    public static void downloadNewVersion(Context context, ProgressBar progressBar) {
        if (MValue.updateVersionBean.getIs_force() == 0) {
            ToastUtils.showToast("正在下载...");
        }
        Mission mission = new Mission(MValue.updateVersionBean.getUrl(), "mitangpai.apk", SDCardUtils.getRootPathPrivateVideo());
        RxDownload rxDownload = RxDownload.INSTANCE;
        rxDownload.create(mission).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Status>() {
            @Override
            public void accept(Status status) throws Exception {
                try {
                    LogUtil.e("下载状态", JSON.toJSONString(status));
                    if (status instanceof Succeed) {
                        ToastUtils.showToast("下载成功...");
                        IntentUtils.installAPP(context, SDCardUtils.getRootPathPrivateVideo() + "/mitangpai.apk");
                    } else if (status instanceof Downloading) {
                        if (MValue.updateVersionBean.getIs_force() == 1) {
                            progressBar.setProgress((int) status.getDownloadSize());
                            progressBar.setMax((int) status.getTotalSize());
                        }
                    } else if (status instanceof Failed) {
                        ToastUtils.showToast("下载失败...");
                    }
                } catch (Exception e) {
                    LogUtil.e("下载状态err", JSON.toJSONString(e));
                }

            }
        });
        RxDownload.INSTANCE.start(MValue.updateVersionBean.getUrl()).
                subscribe();
    }

}
