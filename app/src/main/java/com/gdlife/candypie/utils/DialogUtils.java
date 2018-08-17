package com.gdlife.candypie.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.serivce.UpdateVersionService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.user.SetPriceService;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.dialog.ServiceTipDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by heboot on 2018/2/7.
 */

public class DialogUtils {

    private static QMUITipDialog tipDialog;

    private static TipDialog dialog;

    private static Observable timeObservable = Observable.timer(1500, TimeUnit.MILLISECONDS);

    private static Consumer consumer = (v) -> {
        if (tipDialog != null) {
            tipDialog.dismiss();
            tipDialog = null;
        }
    };


    private static List<Dialog> dialogs = new ArrayList<>();


    public static void addDialog2Stack(Dialog dialog) {
        if (dialog != null) {//&& dialogs.indexOf(dialog) == -1
            dialogs.add(dialog);
        }
    }

    public static void showDialogStack() {
        if (dialogs != null && dialogs.size() > 0) {
            dialogs.get(0).show();
            dialogs.get(0).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogs.remove(0);
                    showDialogStack();
                }
            });
        }
    }


    /**
     * 弹窗
     *
     * @param context
     * @param permissionUtils
     * @param showFristRob
     * @param serverService
     */
    public static void showIndexDialog(Activity context, PermissionUtils permissionUtils, boolean showFristRob, ServerService serverService) {
        UpdateVersionService.checkVersionUpdate(context, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                permissionUtils.showPermissionDialog(context, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (showFristRob) {
                            if (serverService != null) {
                                serverService.showFirstRobServerDialog(new WeakReference(context), null);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * @param context
     * @param permissionUtils
     * @param showFristRob
     * @param serverService
     * @param showSetPrice
     */
    public static void showIndexDialog(Activity context, PermissionUtils permissionUtils, boolean showFristRob, ServerService serverService, boolean showSetPrice, boolean isFromIndex) {
        UpdateVersionService.checkVersionUpdate(context, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                permissionUtils.showPermissionDialog(context, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (showFristRob) {
                            if (serverService != null) {
                                if (!showSetPrice) {
                                    serverService.showFirstRobServerDialog(new WeakReference(context), null);
                                } else {
                                    SetPriceService setPriceService = new SetPriceService();
                                    serverService.showFirstRobServerDialog(new WeakReference(context), new Consumer<String>() {
                                        @Override
                                        public void accept(String s) throws Exception {
                                            setPriceService.showSetPriceTipDialog(context, isFromIndex);
                                        }
                                    });
                                }
                            }
                        } else if (showSetPrice) {
                            SetPriceService setPriceService = new SetPriceService();
                            setPriceService.showSetPriceTipDialog(context, isFromIndex);
                        }
                    }
                });
            }
        });
    }


    /**
     * 拨打电话的弹窗
     *
     * @param context
     * @param moble
     * @return
     */
    public static TipDialog getCallMobileDialog(Context context, String moble) {
        dialog = new TipDialog.Builder(context, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 1) {
                    IntentUtils.callMobile(context, moble);
                }
            }
        }, context.getString(R.string.call_mobile_tip)).create();
        return dialog;
    }


    /**
     * 关闭dialog
     *
     * @param dialog
     */
    public static void dimissDialog(Dialog ddialog) {
        if (ddialog != null) {
            if (ddialog.isShowing()) {
                ddialog.dismiss();
            }
            ddialog = null;
        }
    }


    /**
     * 构建一个带加载圈的弹出框
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static QMUITipDialog getLoadingDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(text)
                .create();
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }

    /**
     * 构建一个提示失败的弹窗
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
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

    /**
     * 构建一个提示成功的弹窗
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static QMUITipDialog getSuclDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(text)
                .create();
        tipDialog.setCancelable(true);
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }

    public static QMUITipDialog getInfolDialog(Context context, String text, boolean autoDismiss) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(text)
                .create();
        tipDialog.setCancelable(true);
        if (autoDismiss) {
            timeObservable.subscribe(consumer);
        }
        return tipDialog;
    }


    public static QMUIBottomSheet getChoosePicQMUIBottomSheet(Context context) {
        QMUIBottomSheet qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(context)
                .addItem(MAPP.mapp.getString(R.string.choose_pic_camera))
                .addItem(MAPP.mapp.getString(R.string.choose_pic_album))
                .build();
        return qmuiBottomSheet;
    }

    public static QMUIBottomSheet getNAVBottomSheet(Context context, List<String> text, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet.BottomListSheetBuilder qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(context);
        for (String s : text) {
            qmuiBottomSheet.addItem(s);
        }
        qmuiBottomSheet.setOnSheetItemClickListener(listener);
        return qmuiBottomSheet.build();
    }


    /**
     * 构建一个选择头像的选择器
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static BottomSheetDialog getAvatarBottomSheet(Context context, Consumer<Integer> consumer) {
        List<String> list = new ArrayList<>();
        list.add(context.getString(R.string.choose_pic_camera));
        list.add(context.getString(R.string.choose_pic_album));
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
        return bottomSheetDialog;
    }


    public static BottomSheetDialog getHomepageBottomSheet(Context context, Consumer<Integer> consumer, boolean isBlack) {
        List<String> list = new ArrayList<>();
        list.add("举报");
        list.add(isBlack ? "取消拉黑" : "拉黑");
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
        return bottomSheetDialog;
    }


    /**
     * 构建一个选择视频的选择器
     *
     * @param context
     * @param text
     * @param autoDismiss
     * @return
     */
    public static BottomSheetDialog getAuthSkillBottomSheet(Context context, Consumer<Integer> consumer) {
        List<String> list = new ArrayList<>();
        list.add(context.getString(R.string.choose_video_camera));
        list.add(context.getString(R.string.choose_video_album));
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog.Builder(context, consumer, list).create();
        return bottomSheetDialog;
    }

    public static void showUpGoldVipDialog(Context context) {
        ServiceTipDialog serviceTipDialog = new ServiceTipDialog.Builder(context, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        }, MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP, null, UserService.getInstance().getUser().getAvatar(), null, null).create();
//        serviceTipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//            }
//        });
        serviceTipDialog.show();
    }

}
