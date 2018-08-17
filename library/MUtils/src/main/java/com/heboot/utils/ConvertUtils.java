package com.heboot.utils;

import com.heboot.entity.User;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Heboot on 2017/9/14.
 */

public class ConvertUtils {

    public static User converUserModel(User oldModel, User newModel) {

        //先确定是否是一个人
        Integer id = null;
        if ((oldModel.getId() != null && oldModel.getId() > 0) && (newModel.getId() == null || newModel.getId() <= 0)) {
            id = oldModel.getId();
        } else if ((oldModel.getId() == null || oldModel.getId() >= 0) && (newModel.getId() != null && newModel.getId() >= 0)) {
            id = newModel.getId();
        }
        try {
            BeanPropertiesUtil.copyProperties(newModel, oldModel);
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
            CrashReport.postCatchedException(new Throwable("converUserModel  Exception  "));
            return oldModel;
        }

        if ((oldModel.getId() == null || oldModel.getId() <= 0) && id != null) {
            oldModel.setId(id);
        } else if ((oldModel.getId() == null || oldModel.getId() <= 0) && id == null) {
            CrashReport.postCatchedException(new Throwable("converUserModel ID Exception  "));
        }


//        //先确定是否是一个人
//        Integer id = null;
//        if ((oldModel.getId() != null && oldModel.getId() > 0) && (newModel.getId() == null || newModel.getId() <= 0)) {
//            id = oldModel.getId();
//        } else if ((oldModel.getId() == null || oldModel.getId() >= 0) && (newModel.getId() != null && newModel.getId() >= 0)) {
//            id = newModel.getId();
//        }
//        try {
//            BeanPropertiesUtil.copyProperties(newModel, oldModel);
//        } catch (Exception e) {
//            CrashReport.postCatchedException(e);
//            CrashReport.postCatchedException(new Throwable("converUserModel  Exception  "));
//            return oldModel;
//        }
//
//        if ((oldModel.getId() == null || oldModel.getId() <= 0) && id != null) {
//            oldModel.setId(id);
//        } else if ((oldModel.getId() == null || oldModel.getId() <= 0) && id == null) {
//            CrashReport.postCatchedException(new Throwable("converUserModel ID Exception  "));
//        }
//
        return oldModel;
    }

}
