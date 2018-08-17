package com.netease.nim.uikit.repository;

import com.heboot.bean.gift.GiftBean;

import java.util.ArrayList;
import java.util.List;

public class GiftRepository {

    public List<GiftBean> getVideoGiftsByPage1(List<GiftBean> gift_config) {
        if (gift_config.size() >= 8) {
            List<GiftBean> result = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                result.add(gift_config.get(i));
            }
            return result;
        }
        return gift_config;
    }

    public List<GiftBean> getVideoGiftsByPage2(List<GiftBean> gift_config) {
        if (gift_config.size() > 8) {
            List<GiftBean> result = new ArrayList<>();
            for (int i = 8; i < gift_config.size(); i++) {
                result.add(gift_config.get(i));
            }
            return result;
        }
        return gift_config;
    }


}
