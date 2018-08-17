package com.gdlife.candypie.fragments;

import android.annotation.SuppressLint;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.databinding.FragmentImgBinding;

/**
 * Created by heboot on 2018/3/20.
 */

public class ImageFragment extends BaseFragment<FragmentImgBinding> {

    private Integer path;

    public ImageFragment() {
    }


    @SuppressLint("ValidFragment")
    public ImageFragment(int position, int pa) {
        this.path = pa;
    }


    @Override
    public void initUI() {

    }

    @Override
    public void initData() {
        if (path != null && binding != null && binding.ivImg != null) {
            binding.ivImg.setBackgroundResource(path);
        }
    }

    @Override
    public void initListener() {


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_img;
    }
}
