package com.gdlife.candypie.serivce;

import android.content.Context;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.user.UserSkillBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/5.
 */

public class AuthService {


    public static void newOrderToMessageRobPage() {

    }


    /**
     * 1如果收到更新用户信息的通知
     * 2如果更新了服务者认证状态字段，并且与本地不一致
     * 3并且更新的值为SUC
     */
    public void checkAuthStatusIsSuc(int serviceAuthStatus) {
        if (serviceAuthStatus == MValue.AUTH_STATUS_SUC) {
//        if (updateUser.getService_auth_status() != null && updateUser.getService_auth_status() == MValue.AUTH_STATUS_SUC && (UserService.getInstance().getUser().getService_auth_status() == null || updateUser.getService_auth_status().intValue() != UserService.getInstance().getUser().getService_auth_status().intValue())) {
            showSetPriceDialogByAuthSuc(MAPP.mapp.getCurrentActivity());
        }
    }

    /**
     * 当用户在程序里，收到认证成功通知的时候 弹出窗口让用户去设置价格
     */
    public void showSetPriceDialogByAuthSuc(Context context) {
        TipCustomOneDialog tipCustomOneDialog = new TipCustomOneDialog.Builder(context, "恭喜你已通过服务者认证", "去设置视频价格", new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                IntentUtils.toSetPriceActivity(context, true);
            }
        }).create();
        tipCustomOneDialog.setCancelable(false);
        tipCustomOneDialog.show();
    }


    /**
     * 获取用户技能认证可选的服务列表
     *
     * @param user
     * @return
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getUserAuthUserSkills(User user) {

        List<UserSkillBean> userSkillBeans = user.getSkill_list();

        List<ConfigBean.ServiceItemsConfigBean.ListBean> listBeanList = new ArrayList<>();
        listBeanList.addAll(ThemeService.getAllAuthThemeList());

        List<ConfigBean.ServiceItemsConfigBean.ListBean> result = new ArrayList<>();

//        result.addAll(ThemeService.getAllThemeList());

        Map<String, ConfigBean.ServiceItemsConfigBean.ListBean> beanMap = new HashMap<>();


        for (ConfigBean.ServiceItemsConfigBean.ListBean listBean : listBeanList) {
            beanMap.put(listBean.getId(), listBean);
        }

        if (userSkillBeans != null && userSkillBeans.size() > 0) {
            for (UserSkillBean userSkillBean : userSkillBeans) {
                beanMap.remove(userSkillBean.getId());
            }
        }


        for (ConfigBean.ServiceItemsConfigBean.ListBean listBean : beanMap.values()) {
            result.add(listBean);
        }

        return result;

    }

    public static void toAuthPageByIndex(Context context) {
        if (UserService.getInstance().getUser() == null) {
            return;
        }
        if (UserService.getInstance().getUser().getService_auth_status() == null) {
            IntentUtils.toAuthBootActivity(context);
            return;
        }
        if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_NO) {
            IntentUtils.toAuthBootActivity(context);
        } else if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() != MValue.AUTH_STATUS_SUC && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_ING) {
            IntentUtils.toAuthIndexActivity(context, RecordVideoFrom.AUTH);
        } else if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {

        } else {
            IntentUtils.toAuthBootActivity(context);
        }
    }

    public static void toAuthPageByAuthBoot(Context context) {
        if (UserService.getInstance().getUser() == null) {
            return;
        }
        if (UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_FAIL) {
            IntentUtils.toAuthCommitActivity(context);
        } else if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_NO) {
            IntentUtils.toAuthCommitActivity(context);
        } else {
            IntentUtils.toAuthIndexActivity(context, RecordVideoFrom.AUTH);
        }
    }


}
