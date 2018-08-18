package com.gdlife.candypie.fragments.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.MainActivity;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.FragmentIndexBinding;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialog;
import com.heboot.bean.config.TopMenuBean;
import com.heboot.event.MessageEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class IndexFragment extends BaseFragment<FragmentIndexBinding> {

    private PermissionUtils permissionUtils;

    public static IndexFragment newInstance() {
        Bundle args = new Bundle();
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void initUI() {
        permissionUtils = new PermissionUtils();

    }

    @Override
    public void initData() {
        LogUtil.e(TAG, "initData");
        List<String> titles = new ArrayList();

        ArrayList<Fragment> fragments = new ArrayList<>();

        if (MAPP.mapp.getConfigBean().getIndex_top_menu() == null || MAPP.mapp.getConfigBean().getIndex_top_menu().size() == 0) {
            return;
        }

        for (TopMenuBean topMenuBean : MAPP.mapp.getConfigBean().getIndex_top_menu()) {
            titles.add(topMenuBean.getTitle());
            fragments.add(IndexListFragment.newInstance(topMenuBean.getName()));
        }


        String[] strings = new String[titles.size()];

        titles.toArray(strings);

        binding.stTitle.setViewPager(binding.rvList, strings, _mActivity, fragments);

    }

    @Override
    public void initListener() {


        binding.clytFirst.setOnClickListener((v) -> {
            binding.clytFirst.setVisibility(View.GONE);
            UserService.getInstance().setFirstIndex();
        });

//
//        binding.vSearchbg.setOnClickListener((v) -> {
//            IntentUtils.toSearchActivity(getContext());
//        });
        binding.vSearchbg2.setOnClickListener((v) -> {
            IntentUtils.toSearchActivity(getContext());
        });


    }


}
