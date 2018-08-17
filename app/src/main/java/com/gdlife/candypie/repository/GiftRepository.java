package com.gdlife.candypie.repository;

import com.gdlife.candypie.MAPP;
import com.heboot.bean.gift.GiftBean;

import java.util.ArrayList;
import java.util.List;

public class GiftRepository {

    public List<GiftBean> getVideoGiftsByPage1() {
        if (MAPP.mapp.getConfigBean().getGift_config().size() >= 8) {
            List<GiftBean> result = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                result.add(MAPP.mapp.getConfigBean().getGift_config().get(i));
            }
            return result;
        }
        return MAPP.mapp.getConfigBean().getGift_config();
    }

    public List<GiftBean> getVideoGiftsByPage2() {
        if (MAPP.mapp.getConfigBean().getGift_config().size() > 8) {
            List<GiftBean> result = new ArrayList<>();
            for (int i = 8; i < MAPP.mapp.getConfigBean().getGift_config().size(); i++) {
                result.add(MAPP.mapp.getConfigBean().getGift_config().get(i));
            }
            return result;
        }
        return MAPP.mapp.getConfigBean().getGift_config();
    }

    public List<GiftBean> getAllGifts() {
        return MAPP.mapp.getConfigBean().getGift_config();
    }

    public  GiftBean getGiftById(String id) {
        List<GiftBean> result = getAllGifts();
        for(GiftBean giftBean : result ){
            if(giftBean.getId().equals(id)){
                return giftBean;
            }
        }
         return null;
    }
}
