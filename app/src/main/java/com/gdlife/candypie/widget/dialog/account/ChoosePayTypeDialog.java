package com.gdlife.candypie.widget.dialog.account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.pay.RechargeActivity;
import com.gdlife.candypie.adapter.gift.GiftFragmentAdapter;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayType;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.DialogChoosePaytypeBinding;
import com.gdlife.candypie.databinding.LayoutVideoGiftPagerBinding;
import com.gdlife.candypie.fragments.gift.GiftRVFragment;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.GiftService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.bean.pay.ServicePaymentBean;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/2.
 */

@SuppressLint("ValidFragment")
public class ChoosePayTypeDialog extends DialogFragment {

    private DialogChoosePaytypeBinding binding;

    private String payType;

    private Consumer<String> consumer;

    public ChoosePayTypeDialog(Consumer<String> consumer) {
        this.consumer = consumer;
    }


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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_choose_paytype, null, false);


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

        binding.vAli.setOnClickListener((v) -> {
            payType = PayType.ALIPAY.toString().toLowerCase();
            try {
                dismiss();
                consumer.accept(payType);
            } catch (Exception e) {
            }
        });
        binding.vWx.setOnClickListener((v) -> {
            payType = PayType.ALIPAY.toString().toLowerCase();
            try {
                dismiss();
                consumer.accept(payType);
            } catch (Exception e) {
            }
        });
    }


}
