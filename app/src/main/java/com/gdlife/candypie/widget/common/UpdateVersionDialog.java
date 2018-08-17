package com.gdlife.candypie.widget.common;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.DialogUpdateBinding;
import com.gdlife.candypie.serivce.UpdateVersionService;

/**
 * Created by heboot on 2018/2/2.
 */

public class UpdateVersionDialog extends Dialog {


    public UpdateVersionDialog(Context context) {
        super(context);
    }

    public UpdateVersionDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String content, title;

        private int is_force;


        UpdateVersionDialog dialog;


        public Builder(Context context, String title, String content, int is_force) {
            this.context = context;
            this.content = content;
            this.title = title;
            this.is_force = is_force;
        }


        public UpdateVersionDialog create() {


            dialog = new UpdateVersionDialog(context, R.style.QMUI_TipDialog);

            DialogUpdateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_update, null, false);

            binding.tvContent.setText(content);

            binding.tvTitle.setText(title);

            binding.tvTitle.setVisibility(View.GONE);
            binding.tvConfirm.setOnClickListener((v) -> {

                if (is_force == 0) {
                    dialog.dismiss();
                    UpdateVersionService.downloadNewVersion(context, binding.pb);
                } else {
                    binding.pb.setVisibility(View.VISIBLE);
                    UpdateVersionService.downloadNewVersion(context, binding.pb);
                }


            });

            binding.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            if (is_force == 0) {
                binding.ivClose.setVisibility(View.VISIBLE);
                binding.vClose.setVisibility(View.VISIBLE);
                dialog.setCancelable(true);
            } else {
                binding.ivClose.setVisibility(View.GONE);
                binding.vClose.setVisibility(View.GONE);
                dialog.setCancelable(false);
            }


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
        }


    }


}
