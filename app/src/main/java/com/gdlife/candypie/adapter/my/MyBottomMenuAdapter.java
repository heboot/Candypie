package com.gdlife.candypie.adapter.my;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.databinding.LayoutMyBottomMenusItemBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.my.MyBottomMenuModel;

import java.util.List;

public class MyBottomMenuAdapter extends BaseQuickAdapter<MyBottomMenuModel, BaseViewHolder> {

    public MyBottomMenuAdapter(int layoutResId, @Nullable List<MyBottomMenuModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBottomMenuModel item) {
        LayoutMyBottomMenusItemBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.tvName.setText(item.getName());
        ImageUtils.showImage(binding.iv, item.getIcon());
        binding.getRoot().setOnClickListener((v) -> {
            if (item.getName().equals("美颜设置")) {
                IntentUtils.toFaceSettingActivity(helper.itemView.getContext());
            } else if (item.getName().equals("上传视频")) {
                IntentUtils.toAuthIndexActivity(helper.itemView.getContext(), RecordVideoFrom.USER);
            } else if (item.getName().equals("我的收藏")) {
                IntentUtils.toUserFavsListActivity(helper.itemView.getContext());
            } else if (item.getName().equals("设置")) {
                IntentUtils.toSettingActivity(helper.itemView.getContext());
            } else if (item.getName().equals("新手指南")) {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getUser_help());
            } else if (item.getName().equals("常见问题")) {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getUser_help());
            } else if (item.getName().equals("意见反馈")) {
//                IntentUtils.toSettingActivity(helper.itemView.getContext());
                IntentUtils.toReportActivity(helper.itemView.getContext(), null, ReportFromType.FEEBACK);
            }
        });
    }
}
