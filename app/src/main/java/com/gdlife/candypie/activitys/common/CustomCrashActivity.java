package com.gdlife.candypie.activitys.common;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ActivityCustomCrashBinding;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class CustomCrashActivity extends Activity {

    private ActivityCustomCrashBinding binding;

    private QMUIDialog detailDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);

//
        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());


        if (BuildConfig.DEBUG) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_crash);
            //        binding.tvDetail.setText(CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));
            binding.tvDetail.setVisibility(View.VISIBLE);
            binding.tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailDialog = new QMUIDialog.MessageDialogBuilder(CustomCrashActivity.this).setTitle("问题详情")
                            .setMessage(CustomActivityOnCrash.getStackTraceFromIntent(getIntent()))
                            .addAction("复制到剪切板", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    ClipData mClipData = ClipData.newPlainText("Label", CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));
                                    // 将ClipData内容放到系统剪贴板里。
                                    cm.setPrimaryClip(mClipData);
                                    ToastUtils.showToast("复制成功");
                                    detailDialog.dismiss();
                                }
                            }).addAction("关闭", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    detailDialog.dismiss();
                                }
                            })
                            .create();
                    detailDialog.show();

                }
            });

            binding.tvRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(CustomCrashActivity.this, config);
                }
            });
//            CustomActivityOnCrash.restartApplication(CustomCrashActivity.this, config);
        } else {
            CustomActivityOnCrash.restartApplication(CustomCrashActivity.this, config);
        }


    }


}
