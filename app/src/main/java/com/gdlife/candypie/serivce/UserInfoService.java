package com.gdlife.candypie.serivce;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserInfoActivity;
import com.gdlife.candypie.adapter.index.IndexVisitAdapter;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.LayoutIndexVisitBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.VisitListBean;
import com.heboot.bean.user.PreEditMeetTagsBean;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.entity.User;
import com.heboot.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/1.
 */

public class UserInfoService {

    private Map<String, Object> params;

    /**
     * 初始化个人信息
     *
     * @param user
     * @param httpObserver
     */
    public void initUserInfo(User user, HttpObserver<UserInfoBean> httpObserver) {
        params = SignUtils.getNormalParams();
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(httpObserver);
    }

    /**
     * 初始化用户的最近访客布局
     *
     * @param includeIndexVisit
     * @param user
     */
    public void initUserVisitLayout(LayoutIndexVisitBinding includeIndexVisit, User user) {
        VisitListBean visitListBean = user.getVisit_list();
        if (visitListBean != null && visitListBean.getList() != null && visitListBean.getList().size() > 0) {
            includeIndexVisit.getRoot().setVisibility(View.VISIBLE);
            includeIndexVisit.rvList.setLayoutManager(new LinearLayoutManager(includeIndexVisit.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });
            includeIndexVisit.rvList.setAdapter(new IndexVisitAdapter(visitListBean.getList(), user));

            includeIndexVisit.tvTitle.setText(visitListBean.getTitle());

            includeIndexVisit.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserVisitActivity(includeIndexVisit.getRoot().getContext(), String.valueOf(UserService.getInstance().getUser().getId()));
            });
        } else {
//            if (user.getId() != null && UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && user.getId() == UserService.getInstance().getUser().getId()) {
//                includeIndexVisit.getRoot().setVisibility(View.VISIBLE);
//            } else {
            includeIndexVisit.getRoot().setVisibility(View.GONE);
//            }

        }
    }


    /**
     * 根据选择的IDS 获取所有选择的标签对象数据
     *
     * @param selectedIds
     * @param allTags
     * @return
     */
    public static List<TagsChildBean> getSelectedMeetTags(List<String> selectedIds, List<TagsChildBean> allTags) {

        List<TagsChildBean> result = new ArrayList<>();

        if (selectedIds == null || selectedIds.size() <= 0) {
            return null;
        }
        for (String cid : selectedIds) {
            for (TagsChildBean tagsChildBean : allTags) {
                if (tagsChildBean.getId().equals(cid)) {
                    result.add(tagsChildBean);
                }
            }
        }
        return result;

    }


    /**
     * 根据选择过的服务 获取选择出来的ids
     *
     * @param selectedTags
     * @return
     */
    public static List<String> getSelectedIds(List<TagsChildBean> selectedTags) {
        List<String> result = new ArrayList<>();

        if (selectedTags == null || selectedTags.size() <= 0) {
            return null;
        }

        for (TagsChildBean tagsChildBean : selectedTags) {
            result.add(tagsChildBean.getId());
        }

        return result;

    }

    public static String getTagsString(List<TagsChildBean> selectedTags) {
        String result = "";

        int current = 0;
        for (TagsChildBean tagsChildBean : selectedTags) {
            if (current < selectedTags.size() - 1) {
                result = result + tagsChildBean.getContent() + "，";
            } else {
                result = result + tagsChildBean.getContent();
            }
            current = current + 1;
        }

        return result;

    }


    /**
     * 初始化我的态度
     *
     * @param observer
     */
    public void preUserMeetTags(HttpObserver<PreEditMeetTagsBean> observer) {
        Map params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().pre_edit_meet_tags(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    @BindingAdapter("android:showLastUpdateTime")
    public static void showUserLastUpdateTime(TextView tv, User user) {

        tv.setText(DateUtil.getUserLastUpdateTime(user.getUpdate_time()));

    }


    /**
     * 输入签名的字数提示
     *
     * @return
     */
    public static String getInputSignTip() {
        String minPrice = MAPP.mapp.getConfigBean().getInput_limit_config().getAbst().get(0);
        String minTime = MAPP.mapp.getConfigBean().getInput_limit_config().getAbst().get(1);
        String result = MAPP.mapp.getString(R.string.input_sign_tip);
        String result2 = String.format(result, minPrice, minTime);
        return result2;
    }


}
