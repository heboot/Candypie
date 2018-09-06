package com.gdlife.candypie.adapter.user;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.databinding.ItemUserpageGiftBinding;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.utils.ImageUtils;
import com.heboot.bean.user.UserGiftChildBean;

import java.util.List;

public class UserGiftAdapter extends BaseQuickAdapter<UserGiftChildBean, BaseViewHolder> {

    private GiftRepository giftRepository;

    public UserGiftAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        giftRepository = new GiftRepository();
    }


    @Override
    protected void convert(BaseViewHolder helper, UserGiftChildBean s) {

        ItemUserpageGiftBinding binding = DataBindingUtil.bind(helper.itemView);

        ImageUtils.showImage(binding.ivGift, giftRepository.getGiftById(s.getId()) == null ? "" : giftRepository.getGiftById(s.getId()).getImg());

        binding.tvNum.setText(s.getNums() + "");

    }
}
