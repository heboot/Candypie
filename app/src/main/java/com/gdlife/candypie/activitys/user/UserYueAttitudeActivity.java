package com.gdlife.candypie.activitys.user;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.user.UserYueAttitudeAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityYueAttitudeBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.user.PreEditMeetTagsBean;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.UserEvent.UPDATE_USER_INFO_BY_MEET_TAG;

public class UserYueAttitudeActivity extends BaseActivity<ActivityYueAttitudeBinding> {

    private UserYueAttitudeAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yue_attitude;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText("约会态度");

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.btnBottom.setSelected(true);

    }

    @Override
    public void initData() {
        preUserMeetTags();
    }

    @Override
    public void initListener() {
        binding.btnBottom.setOnClickListener((v) -> {
            editUserMeetTags();
        });
    }

    public void updateui(){
        binding.rvList.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void tipMinSelect() {
        tipDialog = DialogUtils.getFailDialog(this, "至少选择一项", true);
        tipDialog.show();
    }

    public void tipMaxSelect(int max) {
        tipDialog = DialogUtils.getFailDialog(this, "最多选择" + max + "项", true);
        tipDialog.show();
    }

    public void preUserMeetTags() {
//        params = SignUtils.getNormalParams();
//        String sign = SignUtils.doSign(params);
//        params.put(MKey.SIGN, sign);
//        HttpClient.Builder.getGuodongServer().pre_edit_meet_tags(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PreEditMeetTagsBean>() {
//            @Override
//            public void onSuccess(BaseBean<PreEditMeetTagsBean> baseBean) {
//                binding.tvTitle.setText(baseBean.getData().getTip());
//                adapter = new UserYueAttitudeAdapter(new WeakReference(UserYueAttitudeActivity.this), baseBean.getData().getMeet_tags(), baseBean.getData().getTags_items(), baseBean.getData().getMax_select());
//                binding.rvList.setAdapter(adapter);
//            }
//
//            @Override
//            public void onError(BaseBean<PreEditMeetTagsBean> baseBean) {
//                if(tipDialog != null && tipDialog.isShowing()){
//                    tipDialog.dismiss();
//                }
//                tipDialog = DialogUtils.getFailDialog(UserYueAttitudeActivity.this,baseBean.getMessage(),true);
//                tipDialog.show();
//            }
//        });
    }

    public void editUserMeetTags() {

        if (adapter == null || adapter.getCurrentSelectedIds() == null || adapter.getCurrentSelectedIds().size() < 1) {
            tipMinSelect();
            return;
        }

        String result = "";

        for(String s : adapter.getCurrentSelectedIds()){
            result = result + s + ",";
        }

        params = SignUtils.getNormalParams();
        params.put(MKey.TAG_IDS, result);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().edit_meet_tags(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PreEditMeetTagsBean>() {
            @Override
            public void onSuccess(BaseBean<PreEditMeetTagsBean> baseBean) {
                if(tipDialog != null && tipDialog.isShowing()){
                    tipDialog.dismiss();
                }

                RxBus.getInstance().post(UPDATE_USER_INFO_BY_MEET_TAG);

                tipDialog = DialogUtils.getSuclDialog(UserYueAttitudeActivity.this,baseBean.getMessage(),true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<PreEditMeetTagsBean> baseBean) {
                if(tipDialog != null && tipDialog.isShowing()){
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(UserYueAttitudeActivity.this,baseBean.getMessage(),true);
                tipDialog.show();
            }
        });
    }


}
