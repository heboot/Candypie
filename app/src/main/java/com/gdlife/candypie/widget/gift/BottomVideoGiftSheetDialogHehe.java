package com.gdlife.candypie.widget.gift;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
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

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.gift.GiftFragmentAdapter;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.LayoutVideoGiftPagerBinding;
import com.gdlife.candypie.fragments.gift.GiftRVFragment;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.GiftService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.gift.GiftBean;
import com.heboot.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/2/2.
 */

@SuppressLint("ValidFragment")
public class BottomVideoGiftSheetDialogHehe extends DialogFragment {

    private LayoutVideoGiftPagerBinding binding;
    private GiftRepository giftRepository = new GiftRepository();
    private List<GiftRVFragment> giftRVFragments = new ArrayList<>();
    private GiftService giftService;
    private String toUid;
    private List<CheckBox> checkBoxes = new ArrayList<>();


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        Dialog dialog = new Dialog(getActivity(), R.style.QMUI_BottomSheet);
//
//
//        // 设置宽度为屏宽、靠近屏幕底部。
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        window.setAttributes(wlp);
//
//
//        return dialog;
//    }

    public BottomVideoGiftSheetDialogHehe(String toUid) {
        this.toUid = toUid;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.giftDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置 dialog 的背景为 null
        getDialog().getWindow().setBackgroundDrawable(null);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_video_gift_pager, null, false);

        giftService = new GiftService();

        initView();


        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        return binding.getRoot();
    }


    private void initView() {

        //设置用户当前的余额
        binding.tvCoinBalance.setText(UserService.getInstance().getUser().getCoin());

        //去充值跳转
        binding.qrbRecharge.setOnClickListener((v) -> {
            IntentUtils.toRechargeActivity(getContext(), RechargeType.COIN);
        });

        List<GiftBean> allGifts = giftRepository.getAllGifts();
//        allGifts.addAll(giftRepository.getAllGifts());
//        allGifts.add(giftRepository.getAllGifts().get(0));
//        allGifts.add(giftRepository.getAllGifts().get(0));
//        allGifts.add(giftRepository.getAllGifts().get(0));
//        allGifts.add(giftRepository.getAllGifts().get(0));
//        allGifts.add(giftRepository.getAllGifts().get(0));

        List<GiftBean> giftBeans = new ArrayList<>();
        for (int i = 0; i < allGifts.size(); i++) {

            giftBeans.add(allGifts.get(i));
            if (i > 0 && (giftBeans.size() == 8 || i == allGifts.size() - 1)) {
//                    GiftRVFragment giftRVFragment = GiftRVFragment.newInstance(giftBeans);
                GiftRVFragment giftRVFragment = new GiftRVFragment(giftBeans);
                giftRVFragments.add(giftRVFragment);
                giftBeans.clear();
            }
        }

        if (giftRVFragments != null && giftRVFragments.size() > 0) {
            checkBoxes.clear();
            binding.llytPoint.removeAllViews();
            binding.llytPoint.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getContext().getResources().getDimensionPixelOffset(R.dimen.x7), getContext().getResources().getDimensionPixelOffset(R.dimen.y7));
            layoutParams.setMargins(getContext().getResources().getDimensionPixelOffset(R.dimen.x12), 0, 0, 0);
            for (GiftRVFragment giftRVFragment : giftRVFragments) {
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setLayoutParams(layoutParams);
                checkBox.setButtonDrawable(null);
                checkBox.setBackgroundResource(R.drawable.selector_boot_point);
//                ViewUtils.setMarginLeft(checkBox, getContext().getResources().getDimensionPixelOffset(R.dimen.x12));
                checkBoxes.add(checkBox);
                binding.llytPoint.addView(checkBox);
            }

            if (checkBoxes != null) {
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (i == 0) {
                        checkBoxes.get(i).setChecked(true);
                    } else {
                        checkBoxes.get(i).setChecked(false);
                    }
                }
            }

        } else {
            binding.llytPoint.setVisibility(View.INVISIBLE);
        }


        binding.viewPager.setAdapter(new GiftFragmentAdapter(getChildFragmentManager(), giftRVFragments));

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (checkBoxes != null) {
                    for (int i = 0; i < checkBoxes.size(); i++) {
                        if (i == position) {
                            checkBoxes.get(i).setChecked(true);
                        } else {
                            checkBoxes.get(i).setChecked(false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.qrbSend.setOnClickListener((v) -> {
            if (MValue.currentSelectedGiftBean != null) {
                giftService.sendGift(MValue.currentSelectedGiftBean, toUid, null);
                dismiss();
            }
        });
    }
}
