package com.gdlife.candypie.activitys.login2register;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityRegisterInfoBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DateUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/5.
 */

public class RegisterInfoActivity extends BaseActivity<ActivityRegisterInfoBinding> {

    @Inject
    CheckUtils checkUtils;

    private ObjectAnimator animator;

    private int currentSelect = -1;

    private String birthdayDate;

    private TimePickerView pvTime;

    private TipCustomOneDialog chooseSexTipDialog;

    @Override
    public void initUI() {
        MobclickAgent.onEvent(RegisterInfoActivity.this, NumEventKeys.register_view.toString());
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.register_info));
        binding.btnBottom.setText(getString(R.string.complete));
        binding.includeSex.tvWoman.setSelected(false);
        binding.includeSex.tvMan.setSelected(false);

    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);
        checkUtils.setBottomEnable(binding.btnBottom, true);

        chooseSexTipDialog = new TipCustomOneDialog.Builder(this, "性别设定后不可更改", "知道了").create();

    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });


        binding.includeSex.tvMan.setOnClickListener((v) -> {
            currentSelect = 1;
            binding.includeSex.tvMan.setSelected(true);
            binding.includeSex.tvWoman.setSelected(false);
            chooseSexTipDialog.show();

        });

        binding.includeSex.tvWoman.setOnClickListener((v) -> {
            currentSelect = 0;
            binding.includeSex.tvMan.setSelected(false);
            binding.includeSex.tvWoman.setSelected(true);
            chooseSexTipDialog.show();
        });

        binding.etBirthday.setOnClickListener((v) -> {

            QMUIKeyboardHelper.hideKeyboard(v);

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(1986, 1, 1);
            Calendar startDate = Calendar.getInstance();
            //startDate.set(2013,1,1);
            Calendar endDate = Calendar.getInstance();
            //endDate.set(2020,1,1);

            //正确设置方式 原因：注意事项有说明
            startDate.set(ConfigService.getInstance().getMaxAgeYear(), 0, 1);
            endDate.set(ConfigService.getInstance().getMinAgeYear(), 12, 31);

            if (pvTime == null) {

                //时间选择器
                pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        try {
                            birthdayDate = DateUtil.date2Str(date, DateUtil.FORMAT_YMD);
                            binding.etBirthday.setText(DateUtil.getCurrentAgeByBirthdate(date) + MAPP.mapp.getString(R.string.age_unit));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("年", "月", "日", null, null, null)
                        .setContentSize(22)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setRangDate(startDate, endDate).build();
            }
            pvTime.setDate(selectedDate);//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
            pvTime.show();
        });

        binding.btnBottom.setOnClickListener((v) -> {
            checkInfoData();
//            Intent intent = new Intent(this, IndexActivity.class);
//            startActivity(intent);
        });
    }


    @Override
    protected void onPause() {
        if (animator != null) {
            animator.end();
            animator.removeAllListeners();
        }
        super.onPause();
    }

    private void checkInfoData() {

        if (currentSelect < 0) {
            tipDialog = DialogUtils.getFailDialog(this, "请先选择性别", true);
            tipDialog.show();
            return;
        }

        if (!checkUtils.checkNick(binding.etNick.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_nick), true);
            tipDialog.show();
            return;
        }

        if (StringUtils.isEmpty(binding.etBirthday.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_birthday), true);
            tipDialog.show();
            return;
        }

        tipDialog = DialogUtils.getLoadingDialog(this, "", false);
        tipDialog.show();

        params = SignUtils.getNormalParams();
        params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().user_check_nick(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(BaseBeanEntity o) {
                params = SignUtils.getNormalParams();
                params.put(MKey.MOBILE, UserService.getInstance().getUser().getMobile());
                params.put(MKey.CODE, UserService.getInstance().getUser().getSmscode());
                params.put(MKey.PASSWORD, UserService.getInstance().getUser().getPwd());
                params.put(MKey.NICK_NAME, binding.etNick.getText().toString());
                params.put(MKey.BIRTHDAY, birthdayDate);
                params.put(MKey.SEX, currentSelect);
                String sign = SignUtils.doSign(params);
                params.put(MKey.SIGN, sign);
                HttpClient.Builder.getGuodongServer().user_register(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<RegisterBean>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(RegisterBean sendSMSBeanBaseBean) {
                        MobclickAgent.onEvent(RegisterInfoActivity.this, NumEventKeys.register_success.toString());
                        User user = sendSMSBeanBaseBean.getUser();
                        user.setPwd(UserService.getInstance().getUser().getPwd());
                        user.setMobile(UserService.getInstance().getUser().getMobile().replaceAll(" ", ""));
                        UserService.getInstance().setUser(user);
                        UserService.getInstance().putSPUser(user);
                        RxBus.getInstance().post(NormalEvent.FINISH_PAGE);
                        LoginService loginService = new LoginService();
                        loginService.doLoginAgora(sendSMSBeanBaseBean.getVideo_user(), sendSMSBeanBaseBean.getIm_user());
                        tipDialog.dismiss();
                        RxBus.getInstance().post(UserEvent.LOGIN_SUC);
//                        if (MValue.IS_FROM_TOURIST) {
//                            MValue.IS_FROM_TOURIST = false;
//                            finish();
//                        } else {

                        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                            IntentUtils.toIndexActivity(RegisterInfoActivity.this);
                        } else {
                            IntentUtils.toMainActivity(RegisterInfoActivity.this);
                        }

//                        Intent intent = new Intent(RegisterInfoActivity.this, IndexActivity.class);
//                        startActivity(intent);
                        finish();
//                        }

                    }


                    @Override
                    public void onError(Throwable throwable) {
                        tipDialog.dismiss();
                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, throwable.getMessage(), true);
                        tipDialog.show();
                    }

                    @Override
                    public void onError(BaseBean<RegisterBean> basebean) {
                        if (tipDialog != null && tipDialog.isShowing()) {
                            tipDialog.dismiss();
                        }

                        tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, basebean.getMessage(), true);
                        tipDialog.show();
                    }


                }));
            }

            @Override
            public void onError(Throwable throwable) {
                tipDialog.dismiss();
                tipDialog = DialogUtils.getFailDialog(RegisterInfoActivity.this, throwable.getMessage(), true);
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> basebean) {

            }
        }));


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_info;
    }
}
