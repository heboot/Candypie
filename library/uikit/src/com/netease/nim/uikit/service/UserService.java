package com.netease.nim.uikit.service;

import com.aliyun.common.utils.StringUtils;
import com.heboot.entity.User;
import com.heboot.utils.DateUtil;

public class UserService {
    public static String getUserAgeByBirthdayNoSui(User user) {
        if (user == null) {
            return "";
        }
        if (StringUtils.isEmpty(user.getBirthday())) {
            return "";
        }
        return DateUtil.getCurrentAgeByBirthdate(DateUtil.str2Date(user.getBirthday(), DateUtil.FORMAT_YMD)) +
                "";
    }


}
