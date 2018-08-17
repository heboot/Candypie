package com.gdlife.candypie.serivce.user;

import android.content.Context;

import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.dialog.SetPriceTipDialog;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.SetPriceInitBean;
import com.heboot.bean.user.TagsChildBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetPriceService {


    public void showSetPriceTipDialog(Context context, boolean isFromIndex) {
        SetPriceTipDialog setPriceTipDialog = new SetPriceTipDialog.Builder(context, isFromIndex).create();
        setPriceTipDialog.show();
    }

    /**
     * 只获取 选择 价格的  价格数据
     *
     * @param tagsChildBeanList
     * @return
     */
    public List<String> getSetVideoCoinPriceList(List<TagsChildBean> tagsChildBeanList) {

        List<String> result = new ArrayList<>();

        for (TagsChildBean tagsChildBean : tagsChildBeanList) {
            result.add(tagsChildBean.getContent());
        }

        return result;
    }


    /**
     * 编辑标签
     *
     * @param tagIds
     * @param observer
     */
    public void edit_video_tags(List<String> tagIds, HttpObserver<BaseBeanEntity> observer) {

        String ids = "";

        for (String s : tagIds) {
            ids = ids + s + ",";
        }

        Map params = SignUtils.getNormalParams();
        params.put(MKey.TAG_IDS, ids);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().edit_video_tags(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }


    public void set_service_price(String price, HttpObserver<BaseBeanEntity> observer) {
        Map params = SignUtils.getNormalParams();
        params.put(MKey.PRICE, price);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().set_service_price(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    /**
     * 初始化价格和视频标签设置
     *
     * @param observer
     */
    public void preUserMeetTags(HttpObserver<SetPriceInitBean> observer) {
        Map params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().init_setting(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

}
