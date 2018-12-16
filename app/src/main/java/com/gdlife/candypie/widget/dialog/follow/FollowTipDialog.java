package com.gdlife.candypie.widget.dialog.follow;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserPageActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.databinding.DialogChooseSexTipBinding;
import com.gdlife.candypie.databinding.DialogFollowTipBinding;
import com.gdlife.candypie.serivce.user.FollowService;
import com.gdlife.candypie.utils.DialogUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

@SuppressLint("ValidFragment")
public class FollowTipDialog extends DialogFragment {

    private DialogFollowTipBinding binding;


    private String uid;

//    private HttpObserver<BaseBeanEntity> observer;

    private Consumer<Integer> consumer;

    private String price;


    public FollowTipDialog(String uid, String price, Consumer<Integer> consumer) {
        this.uid = uid;
//        this.observer = observer;
        this.price = price;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_follow_tip, null, false);


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

        binding.tvTip.append("是否愿意消费 ");

        SpannableString priceSpan = new SpannableString(price);
        priceSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, priceSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvTip.append(priceSpan);


        binding.tvTip.append(" 钻石，购买");

        String num = " 1 ";

        SpannableString numSpan = new SpannableString(num);
        priceSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvTip.append(numSpan);

        binding.tvTip.append("个守护符守护Ta");


        binding.tvCancel.setOnClickListener((v) -> {
            dismiss();
        });
        binding.tvConfirm.setOnClickListener((v) -> {
            try {
                consumer.accept(1);
            } catch (Exception e) {
            }
//            if (followService == null) {
//                followService = new FollowService();
//            }
//            followService.doFollowLove(uid, observer);
        });
    }


}
