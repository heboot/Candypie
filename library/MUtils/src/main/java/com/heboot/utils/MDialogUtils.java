package com.heboot.utils;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MDialogUtils {


    private static QMUITipDialog tipDialog;


    private static Observable timeObservable = Observable.timer(1500, TimeUnit.MILLISECONDS);

    private static Consumer consumer = (v) -> {
        tipDialog.dismiss();
    };

    public static QMUITipDialog getFailDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(text)
                .create();
        tipDialog.setCancelable(true);
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }

}
