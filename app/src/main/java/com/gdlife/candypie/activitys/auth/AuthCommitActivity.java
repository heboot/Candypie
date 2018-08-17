package com.gdlife.candypie.activitys.auth;

import android.support.v4.content.ContextCompat;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityAuthCommitBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.auth.ServiceAuthQBean;
import com.heboot.bean.auth.SubmitAuthBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.VideoEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/5.
 */

public class AuthCommitActivity extends BaseActivity<ActivityAuthCommitBinding> {


    @Inject
    UIService uiService;

    private ServiceAuthQBean authBean;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.tvRight.setText(R.string.commit_auth);
        DaggerServiceComponent.builder().build().inject(this);
        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.theme_color));
        binding.includeToolbar.tvTitle.setText(getString(R.string.ver_info_servicer));
    }

    @Override
    public void initData() {
        binding.setUser(UserService.getInstance().getUser());
        binding.includeUserinfo.setUser(UserService.getInstance().getUser());
        binding.includeUserinfo.includeSexage.setUser(UserService.getInstance().getUser());
        servicer_auth_profile();
    }

    @Override
    public void initListener() {

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
//              LogUtil.e("发送刷新用户视频集事件3", o.toString());
                if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH)) {
                    tipDialog = DialogUtils.getSuclDialog(AuthCommitActivity.this, getString(R.string.upload_suc), true);
                    tipDialog.show();
                    servicer_auth_profile();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            submit_auth();
        });

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
//
//        binding.ivVerIdentity.setOnClickListener((v) -> {
//            IntentUtils.toAuthIDActivity(this, MValue.AUTH_ID_FROM.EDIT);
//        });

        binding.includeUserinfo.ivEdit.setOnClickListener((v) -> {
            IntentUtils.toUserInfoActivity(this, MValue.USER_INFO_TYPE_AUTH_COMMIT, MValue.USER_INFO_TYPE_AUTH, UserService.getInstance().getUser(), null, null);
        });

        binding.btnReplace.setOnClickListener((v) -> {
            IntentUtils.toAuthIndexActivity(this, RecordVideoFrom.COMMIT);
        });

        binding.vPlayer.setOnClickListener((v) -> {
            IntentUtils.toPlayerActivity2(this, authBean.getUser_profile().getCover_video().getPath(), VideoPreviewFrom.USER, null, authBean.getUser_profile().getCover_video().getCover_img());
        });


    }

    private void submit_auth() {

        if (StringUtils.isEmpty(UserService.getInstance().getUser().getWeight())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_auth_userinfo), true);
            tipDialog.show();
            return;
        }


        if (StringUtils.isEmpty(UserService.getInstance().getUser().getHeight())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.please_select_height), true);
            tipDialog.show();
            return;
        }
        if (StringUtils.isEmpty(UserService.getInstance().getUser().getWeight())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.please_select_kg), true);
            tipDialog.show();
            return;
        }
        if (StringUtils.isEmpty(UserService.getInstance().getUser().getAbst())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.please_select_abst), true);
            tipDialog.show();
            return;
        }


//        if (authBean == null || authBean.getUser_profile() == null || StringUtils.isEmpty(authBean.getUser_profile().getId_face())) {
//            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_auth_userid_auth), true);
//            tipDialog.show();
//            return;
//        }

        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().submit_auth(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<SubmitAuthBean>() {
            @Override
            public void onSuccess(BaseBean<SubmitAuthBean> baseBean) {
                SubmitAuthBean submitAuthBean = baseBean.getData();
                tipDialog = DialogUtils.getSuclDialog(AuthCommitActivity.this, getString(R.string.commit_auth_suc), true);
                UserService.getInstance().getUser().setUser_auth_status(submitAuthBean.getUser_auth_status());
                UserService.getInstance().getUser().setService_auth_status(submitAuthBean.getService_auth_status());
                tipDialog.show();
                IntentUtils.toAuthIndexActivity(AuthCommitActivity.this, RecordVideoFrom.AUTH);
                RxBus.getInstance().post(NormalEvent.FINISH_AUTH_WELCOME_PAGE);
                finish();
            }

            @Override
            public void onError(BaseBean<SubmitAuthBean> baseBean) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(AuthCommitActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });


    }

    private void servicer_auth_profile() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().servicer_auth_profile(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<ServiceAuthQBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(ServiceAuthQBean serviceAuthQBean) {
                if (serviceAuthQBean != null && serviceAuthQBean.getUser_profile() != null) {
                    authBean = serviceAuthQBean;
                    if (serviceAuthQBean.getUser_profile().getCover_video() != null && !StringUtils.isEmpty(serviceAuthQBean.getUser_profile().getCover_video().getPath())) {
                        ImageUtils.showImage(binding.vPlayer, serviceAuthQBean.getUser_profile().getCover_video().getCover_img());
                    }
//                    ImageUtils.showImage(binding.ivPlayer, serviceAuthQBean.getUser_profile().getCover_video().getCover_img());
                }
            }


            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onError(BaseBean<ServiceAuthQBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(AuthCommitActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }

        }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_commit;
    }
}
