package com.gdlife.candypie.adapter.my;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.databinding.LayoutMyBottomMenusItemBinding;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.heboot.entity.my.MyBottomMenuModel;
import com.heboot.utils.ViewUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.util.QMUIViewOffsetHelper;

import java.util.List;

public class MyBottomMenuAdapter extends BaseQuickAdapter<MyBottomMenuModel, BaseViewHolder> {

    private PermissionUtils permissionUtils;

    private ViewGroup.MarginLayoutParams layoutParams;

    public MyBottomMenuAdapter(int layoutResId, @Nullable List<MyBottomMenuModel> data) {
        super(layoutResId, data);
//        int count = (QMUIDisplayHelper.getScreenWidth(MAPP.mapp) - MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x10)) / 3;
//        layoutParams = new ViewGroup.MarginLayoutParams(count, count);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBottomMenuModel item) {
        LayoutMyBottomMenusItemBinding binding = DataBindingUtil.bind(helper.itemView);
//        binding.getRoot().setLayoutParams(layoutParams);
//        ViewUtils.setMargins(binding.getRoot(), MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x3), MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x3), 0, 0);
        binding.tvName.setText(item.getName());
        ImageUtils.showImage(binding.iv, item.getIcon());
        binding.getRoot().setOnClickListener((v) -> {
            if (item.getName().equals("美颜设置")) {
                if (UserService.getInstance().checkTourist()) {
                    return;
                }
                if (permissionUtils == null) {
                    permissionUtils = new PermissionUtils();
                }
                if (permissionUtils.hasCameraPermission(MAPP.mapp)) {
                    IntentUtils.toFaceSettingActivity(binding.getRoot().getContext());
                } else {
                    permissionUtils.getCameraPermission(MAPP.mapp.getCurrentActivity());
                }
            } else if (item.getName().equals("上传视频")) {
//                IntentUtils.toAuthIndexActivity(helper.itemView.getContext(), RecordVideoFrom.USER);
                IntentUtils.toUserVideosActivity(helper.itemView.getContext(), UserService.getInstance().getUser(), UserVideoActivityFrom.NORMAL);
            } else if (item.getName().equals("我的收藏")) {
                IntentUtils.toUserFavsListActivity(helper.itemView.getContext());
            } else if (item.getName().equals("设置")) {
                IntentUtils.toSettingActivity(helper.itemView.getContext());
            } else if (item.getName().equals("新手指南")) {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getUser_help());
            } else if (item.getName().equals("常见问题")) {
                if (UserService.getInstance().isServicer()) {
                    IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getServicer_qa());
                } else {
                    IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getUser_qa());
                }
            } else if (item.getName().equals("意见反馈")) {
//                IntentUtils.toSettingActivity(helper.itemView.getContext());
                IntentUtils.toReportActivity(helper.itemView.getContext(), null, ReportFromType.FEEBACK);
            } else if (item.getName().equals("解锁视频")) {
                IntentUtils.toUserUnlockVideosActivity(helper.itemView.getContext());
            } else if (item.getName().equals("我的守护")) {
                IntentUtils.toUserFollowActivity(helper.itemView.getContext(), UserService.getInstance().getUser(), UserService.getInstance().getUser().getNums());
            }
        });
    }
}
