package com.gdlife.candypie.utils;

import android.widget.EditText;
import android.widget.TextView;

import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.serivce.ConfigService;
import com.heboot.utils.LogUtil;

/**
 * Created by heboot on 2018/2/3.
 */

public class CheckUtils {


    public void setEditLastSelection(EditText editText) {
        if (editText.getText().length() <= 0) {
            return;
        }
        editText.setSelection(editText.getText().toString().length());
    }

    /**
     * 检查年龄
     *
     * @param ageStr
     * @return
     */
    public boolean checkAge(String ageStr) {
        if (StringUtils.isEmpty(ageStr)) {
            return false;
        }
        int age = Integer.parseInt(ageStr);
        if (age > ConfigService.getInstance().getMinAg() && age < ConfigService.getInstance().getMaxAge()) {
            return true;
        }
        return false;
    }


    /**
     * 检查个性签名
     *
     * @param abst
     * @return
     */
    public boolean checkAbst(String abst) {
        if (StringUtils.isEmpty(abst)) {
            return false;
        }
        if (StringUtils.countLength(abst) >= ConfigService.getInstance().getAbstMinLength() && StringUtils.countLength(abst) <= ConfigService.getInstance().getAbstMaxLength()) {
            return true;
        }
        return false;
    }


    /**
     * 检查昵称
     *
     * @param nick
     * @return
     */
    public boolean checkNick(String nick) {
        if (!StringUtils.isEmpty(nick)
                && StringUtils.countLength(nick) >= ConfigService.getInstance().getNickNameMinLength()
                && StringUtils.countLength(nick) <= ConfigService.getInstance().getNickNameMaxLength()
                ) {
            return true;
        }
        return false;
    }

    /**
     * 检查验证码
     *
     * @param code
     * @param code2
     * @param code3
     * @param code4
     * @return
     */
    public boolean checkCode(String code, String code2, String code3, String code4) {
        if (StringUtils.isEmpty(code)
                || StringUtils.isEmpty(code2)
                || StringUtils.isEmpty(code3)
                || StringUtils.isEmpty(code4)
                ) {
            return false;
        }
        if (StringUtils.countLength(code) == 1
                && StringUtils.countLength(code2) == 1
                && StringUtils.countLength(code3) == 1
                && StringUtils.countLength(code4) == 1

//                && code.equals("1")
//                && code2.equals("1")
//                && code3.equals("1")
//                && code4.equals("1")

                ) {
            return true;
        }
        return false;
    }

    /**
     * 检查密码
     *
     * @param pwd
     * @return
     */
    public boolean checkPwd(String pwd) {
        if(StringUtils.isEmpty(pwd)){
            return false;
        }
        if (StringUtils.countLength(pwd) <= ConfigService.getInstance().getPasswordMaxLength()
                && StringUtils.countLength(pwd) >= ConfigService.getInstance().getPasswordMinLength()) {
            return true;
        }
        return false;
    }

    /**
     * 检查手机号
     *
     * @param code
     * @param mobile
     * @return
     */
    public boolean checkMobile(int code, String mobile) {
        String sss = mobile.replaceAll(" ", "");
        if (StringUtils.countLength(sss) == 11) {
            return true;
        }
        return false;
    }

    /**
     * 设置底部按钮颜色以及是否可以点击
     *
     * @param button
     * @param enable
     */
    public void setBottomEnable(TextView button, boolean enable) {
        button.setSelected(enable);
//        button.setEnabled(enable);
    }
}
