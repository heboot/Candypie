package com.gdlife.candypie.serivce;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.heboot.entity.User;

/**
 * Created by heboot on 2018/3/2.
 */

public class VerService {

    @BindingAdapter("android:showServiceVerStatus")
    public static void showServiceVerStatus(TextView textView, User user) {
        if (user.getService_auth_status() == null || user.getService_auth_status() == MValue.AUTH_STATUS_NO) {
            textView.setText(textView.getContext().getString(R.string.ver_info_un_servicer));
        } else if (user.getService_auth_status() == MValue.AUTH_STATUS_ING) {
            textView.setText(textView.getContext().getString(R.string.ver_service_status_ing));
        } else if (user.getService_auth_status() == MValue.AUTH_STATUS_SUC) {
            textView.setText(textView.getContext().getString(R.string.ver_service_status_suc));
        }
    }

    @BindingAdapter("android:showIDVerStatus")
    public static void showIDVerStatus(TextView textView, User user) {
        if (user == null || user.getUser_auth_status() == null || user.getUser_auth_status() == MValue.AUTH_STATUS_NO) {
            textView.setText(textView.getContext().getString(R.string.ver_id_status_no));
        } else if (user.getUser_auth_status() != null && user.getUser_auth_status() == MValue.AUTH_STATUS_ING) {
            textView.setText(textView.getContext().getString(R.string.ver_id_status_ing));
        } else if (user.getUser_auth_status() != null && user.getUser_auth_status() == MValue.AUTH_STATUS_SUC) {
            textView.setText(textView.getContext().getString(R.string.ver_id_status_suc));
        }
    }
}
