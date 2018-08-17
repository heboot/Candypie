package com.gdlife.candypie.adapter.homepage;

import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemHomepageSkillBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.user.UserSkillBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.netease.nim.uikit.common.util.media.ImageUtil;

import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class HomepageSkillAdapter extends BaseRecyclerViewAdapter {

    private boolean isMy;

    public HomepageSkillAdapter(List<UserSkillBean> mdata, boolean isMy) {
        data.addAll(mdata);
        this.isMy = isMy;
        if (isMy) {
            UserSkillBean userSkillBean = new UserSkillBean();
            userSkillBean.setId(null);//MAPP.mapp.getString(R.string.add_skill)
            data.add(userSkillBean);
        }
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomepageSkillAdapter.ViewHolder(parent, R.layout.item_homepage_skill);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<UserSkillBean, ItemHomepageSkillBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final UserSkillBean s, int position) {
            if (isMy) {
                if (position == data.size() - 1) {
                    if (isMy) {
                        ImageUtils.showImage(binding.ivImg, R.drawable.icon_add_skill);
                        binding.tvName.setText(MAPP.mapp.getString(R.string.add_skill));
                        binding.getRoot().setOnClickListener((v) -> {
                            IntentUtils.toUserSkillListActivity(v.getContext());
                        });
                    }
                } else {
                    ConfigBean.ServiceItemsConfigBean.ListBean listBean = ThemeService.getServiceBeanById(s.getId());

                    if (s.getStatus() == MValue.AUTH_STATUS_SUC) {
                        ImageUtils.showImage(binding.ivImg, listBean.getIcon());
                        binding.tvName.setText(listBean.getTitle());
                    } else {
                        ImageUtils.showImage(binding.ivImg, listBean.getDisable_icon());
                        binding.tvName.setText(MAPP.mapp.getString(R.string.skill_auth_status_ing));
                    }
                }
            } else {

                ConfigBean.ServiceItemsConfigBean.ListBean listBean = ThemeService.getServiceBeanById(s.getId());

                if(listBean != null){
                    if (s.getStatus() == MValue.AUTH_STATUS_SUC) {
                        ImageUtils.showImage(binding.ivImg, listBean.getIcon());
                        binding.tvName.setText(listBean.getTitle());
                    } else {
                        ImageUtils.showImage(binding.ivImg, listBean.getDisable_icon());
                        binding.tvName.setText(listBean.getTitle());
                    }
                }


            }


        }

    }
}
