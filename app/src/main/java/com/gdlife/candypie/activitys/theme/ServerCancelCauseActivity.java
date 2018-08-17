package com.gdlife.candypie.activitys.theme;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.theme.ChooseCancelCauseAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityServerCauseBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.theme.CancelCauseBean;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServerCancelCauseActivity extends BaseActivity<ActivityServerCauseBinding> {

    private String user_service_id;

    private ChooseCancelCauseAdapter chooseCancelCauseAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_server_cause;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_setting));

    }

    @Override
    public void initData() {
        user_service_id = getIntent().getExtras().getString(MKey.USER_SERVICE_ID);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        initAddressConfigData();
    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.btnBottom.setOnClickListener((v)->{
            cancelCommit();
        });

    }

    private void initAddressConfigData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, user_service_id);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().pre_cancel_result(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<CancelCauseBean>() {
            @Override
            public void onSuccess(BaseBean<CancelCauseBean> baseBean) {
                if (baseBean.getData().getContent_list() != null) {
                    chooseCancelCauseAdapter = new ChooseCancelCauseAdapter(baseBean.getData().getContent_list());
                    binding.rvList.setAdapter(chooseCancelCauseAdapter);
                }
            }

            @Override
            public void onError(BaseBean<CancelCauseBean> baseBean) {

            }
        });

    }

    private void cancelCommit() {
        params = SignUtils.getNormalParams();
        if(StringUtils.isEmpty(chooseCancelCauseAdapter.getCheckId())){
            tipDialog = DialogUtils.getFailDialog(this,getString(R.string.please_select_cancelcause),true);
            tipDialog.show();
            return;
        }

        params.put(MKey.USER_SERVICE_ID, user_service_id);

        params.put(MKey.CONTENT_ID, chooseCancelCauseAdapter.getCheckId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().cancel_result(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                tipDialog = DialogUtils.getSuclDialog(ServerCancelCauseActivity.this,baseBean.getMessage(),true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });

    }

}
