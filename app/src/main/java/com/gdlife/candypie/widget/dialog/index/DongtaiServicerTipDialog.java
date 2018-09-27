package com.gdlife.candypie.widget.dialog.index;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.databinding.DialogChooseSexTipBinding;
import com.gdlife.candypie.databinding.DialogDongtaiServicerTipBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;

/**
 * Created by heboot on 2018/2/2.
 */

@SuppressLint("ValidFragment")
public class DongtaiServicerTipDialog extends DialogFragment {

    private DialogDongtaiServicerTipBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.payTypeDialogStyle);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        //设置 dialog 的背景为 null
//        getDialog().getWindow().setBackgroundDrawable(null);


        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;

        window.setAttributes(windowParams);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_dongtai_servicer_tip, null, false);


        initView();


        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; //底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        return binding.getRoot();
    }


    private void initView() {
        binding.getRoot().setOnClickListener((v) -> {
            dismiss();
            IntentUtils.toUserVideosActivity(getContext(), UserService.getInstance().getUser(), UserVideoActivityFrom.NORMAL);
        });
        binding.button.setOnClickListener((v) -> {
            dismiss();
            IntentUtils.toUserVideosActivity(getContext(), UserService.getInstance().getUser(), UserVideoActivityFrom.NORMAL);
        });
    }


}
