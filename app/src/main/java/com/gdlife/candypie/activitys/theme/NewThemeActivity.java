package com.gdlife.candypie.activitys.theme;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.theme.NewThemeRecommendUserAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayFrom;
import com.gdlife.candypie.common.ServiceSelectUserFrom;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityNewTheme2Binding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.theme.NewThemeSelectAddressDialog;
import com.gdlife.candypie.widget.theme.NewThemeSelectRequireDialog;
import com.gdlife.candypie.widget.theme.NewThemeSelectRewardDialog;
import com.gdlife.candypie.widget.theme.NewThemeSelectTimeDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.config.MyConfigSelectBean;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.bean.theme.ServiceReqParamsBean;
import com.heboot.bean.user.RecommendServicerListBean;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.event.ThemeEvent;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.TipCustomDialog;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by heboot on 2018/2/2.
 */

public class NewThemeActivity extends BaseActivity<ActivityNewTheme2Binding> implements EasyPermissions.PermissionCallbacks {

    private NewThemeSelectTimeDialog newThemeSelectTimeDialog;
    private NewThemeSelectRequireDialog newThemeSelectRequireDialog;
    private NewThemeSelectRewardDialog newThemeSelectRewardDialog;
    private NewThemeSelectAddressDialog newThemeSelectCityDialog;

    private ObjectAnimator objectAnimator;

    private Consumer<MyConfigSelectBean> timeObserver, reqObserver, rewardObserver;

    private Consumer<ConfigBean.CityConfigBean> cityObserver;

    private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

    private NewThemeSelectTimeDialog.Builder timeBuilder;

    private NewThemeSelectRequireDialog.Builder requireBuilder;

    private NewThemeSelectRewardDialog.Builder reawrdBuilder;

    private NewThemeSelectAddressDialog.Builder cityBuilder;

    private Integer selectedDuIndex = null;//选择的市场，要去匹配价格

    private MeetPoiDataBean.ListBean meetPoiBean;

    private Long startTime;

    private String type;

    private NewThemeRecommendUserAdapter recommendUserAdapter;

    private ConfigBean.RequireIdsBean requireIdsBean;

    private ConfigBean.CityConfigBean cityConfigBean;

    private ServiceReqParamsBean reqDataBean;

    private User selectUser;

    private String rewardPrice;

    private TipDialog coinDialog;

    private TipCustomDialog tipCustomDialog;

    @Inject
    ServerService serverService;

    VideoChatService videoChatService;

    PermissionUtils permissionUtils;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);

        binding.btnBottom.setText(getString(R.string.place_an_order));

        binding.includeToolbar.setWhiteBack(true);

        binding.includeNewThemeItem2.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
                    binding.ivPoint.setY(binding.includeNewThemeItem2.getRoot().getY() + binding.includeNewThemeItem2.getRoot().getHeight() / 3);
                }
                binding.includeNewThemeItem2.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }

    @Override
    public void initData() {

        DaggerServiceComponent.builder().build().inject(this);

        videoChatService = new VideoChatService();

        permissionUtils = new PermissionUtils();

        type = getIntent().getExtras().getString(MKey.TYPE);

        selectUser = (User) getIntent().getExtras().get(MKey.USER);

        binding.setType(type);


        binding.includeNewThemeItem1.etText.setHint(getString(R.string.new_theme_time_hint));
        binding.includeNewThemeItem2.etText.setHint(getString(R.string.new_theme_address_hint));
        binding.includeNewThemeItem3.etText.setHint(getString(R.string.new_theme_req_hint));
        binding.includeNewThemeItem4.etText.setHint(getString(R.string.price_title));

        listBean = (ConfigBean.ServiceItemsConfigBean.ListBean) getIntent().getExtras().get(MKey.SERVICE_ITEM);


        if (!type.equals(MValue.NEW_SERVICE_TYPE_ONE)) {
            initRvData();
            if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
//                binding.tvVideoPrice.setText(ThemeService.getVideoServiceHintStr(listBean));
                binding.includeNewThemeItem2.etText.setText(getString(R.string.theme_choose_address_unlimited));
            }
        } else {
            if (StringUtils.isEmpty(listBean.getCover_img())) {
                binding.ivBg.setBackgroundColor(Color.parseColor(listBean.getBg_color()));
            } else {
                ImageUtils.showImage(binding.ivBg, listBean.getCover_img());
            }
        }

        initObservers();

        binding.setListBean(listBean);

        ImageUtils.showImage(binding.ivIcon, listBean.getImg());

        initbuilder();


    }

    private void doPointAni(View target) {
        objectAnimator = ObjectAnimator.ofFloat(binding.ivPoint, "y", binding.ivPoint.getY(), target.getY() + target.getHeight() / 3);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private void initRvData() {


        getRecommendUserList();

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    private void initObservers() {

        timeObserver = new Consumer<MyConfigSelectBean>() {
            @Override
            public void accept(MyConfigSelectBean o) throws Exception {
                binding.includeNewThemeItem1.etText.setText(o.getSelectedText());
                selectedDuIndex = o.getRv4SelectedIndex();

                startTime = o.getStart_time();

                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {

                    if (StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                        doPointAni(binding.includeNewThemeItem3.getRoot());
                    }


                    binding.tvVideoPrice.setText("价格:" + listBean.getTime_price().get(selectedDuIndex).getPrice() + getString(R.string.unit_coin));

                } else {

                    if (StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                        doPointAni(binding.includeNewThemeItem2.getRoot());
                    }

                    /**
                     * 线下单子 选择完时间后显示价格
                     */
                    rewardPrice = listBean.getTime_price().get(selectedDuIndex).getPrice();
                    binding.tvVideoPrice.setText("价格:" + listBean.getTime_price().get(selectedDuIndex).getPrice() + getString(R.string.price_unit));
                    binding.ivPoint.setVisibility(View.VISIBLE);
                    doPointAni(binding.includeNewThemeItem2.getRoot());

                }


            }
        };

        reqObserver = new Consumer<MyConfigSelectBean>() {
            @Override
            public void accept(MyConfigSelectBean o) throws Exception {
                reqDataBean = o.getServiceReqParamsBean();
                binding.includeNewThemeItem3.etText.setText(o.getSelectedText());

//                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
//                    if (StringUtils.isEmpty(binding.includeNewThemeItem4.etText.getText())) {
//                        doPointAni(binding.includeNewThemeItem4.getRoot());
//                    }
//                }
                if (o.getRequireIdsBean() != null) {
                    requireIdsBean = o.getRequireIdsBean();
                }
            }
        };

        rewardObserver = new Consumer<MyConfigSelectBean>() {
            @Override
            public void accept(MyConfigSelectBean o) throws Exception {
                rewardPrice = o.getRv1SelectedText();
                binding.includeNewThemeItem4.etText.setText(o.getSelectedText() + "/" + getString(R.string.unit_minute));
                binding.tvVideoPrice.setVisibility(View.GONE);
            }
        };

        cityObserver = new Consumer<ConfigBean.CityConfigBean>() {
            @Override
            public void accept(ConfigBean.CityConfigBean c) throws Exception {
                cityConfigBean = c;
                binding.includeNewThemeItem2.etText.setText(cityConfigBean.getName());

//                if (StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                doPointAni(binding.includeNewThemeItem3.getRoot());
//                }
            }
        };


    }

    private void initbuilder() {

        if (!type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
            timeBuilder = new NewThemeSelectTimeDialog.Builder(this, timeObserver, listBean);
            newThemeSelectTimeDialog = timeBuilder.create();
        } else {
            cityBuilder = new NewThemeSelectAddressDialog.Builder(this, cityObserver, listBean.getType().equals(MValue.ORDER_TYPE_VIDEO));
            newThemeSelectCityDialog = cityBuilder.create();
        }


        requireBuilder = new NewThemeSelectRequireDialog.Builder(this, reqObserver, ThemeService.getServiceReqIdsBean(listBean), type.equals(MValue.NEW_SERVICE_TYPE_VIDEO), selectUser == null ? false : true);
        newThemeSelectRequireDialog = requireBuilder.create();

        reawrdBuilder = new NewThemeSelectRewardDialog.Builder(this, selectedDuIndex, listBean, rewardObserver, type.equals(MValue.NEW_SERVICE_TYPE_VIDEO) ? getString(R.string.unit_coin) : getString(R.string.price_unit));
        newThemeSelectRewardDialog = reawrdBuilder.create();
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
                if (o instanceof ThemeEvent.ThemeChooseAddressEvent) {
                    meetPoiBean = ((ThemeEvent.ThemeChooseAddressEvent) o).getListBean();
                    binding.includeNewThemeItem2.etText.setText(meetPoiBean.getName());
                    if (StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                        doPointAni(binding.includeNewThemeItem3.getRoot());
                    }
                } else if (o.equals(ThemeEvent.CLOSE_NEW_SERVICE_PAGE_EVENT)) {
                    finish();
                } else if (o.equals(PayEvent.RechargeGoldVipSUCEvent)) {
                    DialogUtils.showUpGoldVipDialog(NewThemeActivity.this);
                } else if (o.equals(NormalEvent.FINISH_NEW_THEME_PAGE)) {
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });


        binding.includeNewThemeItem1.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            newThemeSelectTimeDialog.show();
        });

        binding.includeNewThemeItem2.getRoot().setOnClickListener((v) -> {
            if (checkItemData(type.equals(MValue.NEW_SERVICE_TYPE_VIDEO) ? 0 : 1)) {
                if (StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                    doPointAni(binding.includeNewThemeItem2.getRoot());
                }

                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
                    if (UserService.getInstance().checkTourist()) {
                        return;
                    }
                    newThemeSelectCityDialog.show();
                } else {

                    if (permissionUtils.hasLocPermission()) {
                        IntentUtils.toChooseAddressActivity(this, listBean.getId(), listBean.getDp_tags(), selectUser == null ? null : selectUser.getCity_id());
                    } else {
                        permissionUtils.requestLocationPermission(this);
                    }

                }

            }
            if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
                newThemeSelectCityDialog.show();
            }


        });

        binding.includeNewThemeItem3.getRoot().setOnClickListener((v) -> {
            if (checkItemData(2)) {
                if (StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                    doPointAni(binding.includeNewThemeItem3.getRoot());
                }
                newThemeSelectRequireDialog.show();
            }

        });


        binding.includeNewThemeItem4.getRoot().setOnClickListener((v) -> {
            if (checkItemData(3)) {
                reawrdBuilder = new NewThemeSelectRewardDialog.Builder(this, selectedDuIndex, listBean, rewardObserver, type.equals(MValue.NEW_SERVICE_TYPE_VIDEO) ? getString(R.string.unit_coin) : getString(R.string.price_unit));
                newThemeSelectRewardDialog = reawrdBuilder.create();
                if (StringUtils.isEmpty(binding.includeNewThemeItem4.etText.getText())) {
                    doPointAni(binding.includeNewThemeItem4.getRoot());
                }

                newThemeSelectRewardDialog.show();
            }
        });


        binding.btnBottom.setOnClickListener((v) -> {
            postTheme();
//            Intent intent = new Intent(this, ThemeUserListActivity.class);
//            startActivity(intent);
        });

        binding.vRefreshUser.setOnClickListener((v) -> {
            ObjectAnimator.ofFloat(binding.ivRefreshUser, "rotation", -360, 0).setDuration(500).start();
            getRecommendUserList();
        });

    }

    private void getRecommendUserList() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SERVICE_ID, listBean.getId());
        params.put(MKey.SIGN, SignUtils.doSign(params));
        HttpClient.Builder.getGuodongServer().recommend_servicer_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<RecommendServicerListBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(RecommendServicerListBean registerBean) {
                if (registerBean.getList() != null && registerBean.getList().size() > 0) {
                    recommendUserAdapter = new NewThemeRecommendUserAdapter(registerBean.getList(), listBean);
                    binding.rvList.setAdapter(recommendUserAdapter);
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.ivBg.setBackgroundColor(Color.parseColor(listBean.getBg_color()));
                } else {
                    ImageUtils.showImage(binding.ivBg, listBean.getCover_img());
                    binding.rvList.setVisibility(View.INVISIBLE);
                    binding.vRefreshUser.setVisibility(View.GONE);
                    binding.ivRefreshUser.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(BaseBean<RecommendServicerListBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(NewThemeActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }

            @Override
            public void onError(Throwable throwable) {
            }


        }));
    }

    private boolean checkData() {
        if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
            if (checkItemData(2)) {
                if (checkItemData(3)) {
                    if (checkItemData(4)) {
                        return true;
                    }
                }
            }
        } else {
            if (checkItemData(1)) {
                if (checkItemData(2)) {
                    if (checkItemData(3)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void postTheme() {

        if (!checkData()) {
            return;
        }

        if (UserService.getInstance().checkTourist()) {
            return;
        }


        tipDialog = DialogUtils.getLoadingDialog(this, null, false);

        tipDialog.show();

        params = SignUtils.getNormalParams();

        params.put(MKey.SERVICE_ID, listBean.getId());


        if (!type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {

            params.put(MKey.START_TIME, startTime / 1000);
            if (selectedDuIndex != null) {
                params.put(MKey.SERVICE_TIME, ThemeService.getServiceDurationsByListBean(listBean, selectedDuIndex));
            }
            params.put(MKey.POI, JSON.toJSONString(meetPoiBean));

        }


        if (cityConfigBean != null && !StringUtils.isEmpty(cityConfigBean.getId())) {
            params.put(MKey.CITY_ID, cityConfigBean.getId());
        }

        if (meetPoiBean != null && !StringUtils.isEmpty(meetPoiBean.getCity_id())) {
            params.put(MKey.CITY_ID, meetPoiBean.getCity_id());
        }


        if (reqDataBean != null) {
            params.put(MKey.REQUIRE, JSON.toJSONString(reqDataBean));
        }

        if (selectUser != null) {
            params.put(MKey.SELECT_UID, selectUser.getId());
        }

        params.put(MKey.PRICE, rewardPrice);


//        params.put(MKey.REQUIRE, listBean.getId());


        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        if (!type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {

            HttpClient.Builder.getGuodongServer().post_meet_service(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PostThemeBean>() {
                @Override
                public void onSuccess(BaseBean<PostThemeBean> baseBean) {
                    PostThemeBean resultBean = baseBean.getData();
                    tipDialog.dismiss();
                    if (resultBean.getIs_first_order() == 1) {
                        serverService.showFirstServerDialog(new WeakReference<NewThemeActivity>(NewThemeActivity.this), resultBean, resultBean.getFirst_order_rate(), resultBean.getPayable_amount());
                    } else {
                        IntentUtils.toPayActivity(NewThemeActivity.this, resultBean, selectUser == null ? PayFrom.NEW_SERVICE : PayFrom.NEW_SERVICE_ONE_USER, false);
                        /**
                         * 发单后不关闭发单页面
                         * 2018.05.11 10:38
                         */
//                        finish();
                    }
                }

                @Override
                public void onError(BaseBean<PostThemeBean> baseBean) {
                    if (tipDialog != null) {
                        tipDialog.dismiss();
                    }

                    if (baseBean.getData() != null && baseBean.getData().getRun_service_tip() != null && !StringUtils.isEmpty(baseBean.getData().getRun_service_tip().getUser_service_id())) {
                        ServerService serverService = new ServerService();

                        tipCustomDialog = new TipCustomDialog.Builder(NewThemeActivity.this, new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                if (integer == 1) {
                                    RxBus.getInstance().post(NormalEvent.FINISH_SELECT_USER_PAGE);
                                    serverService.doRuningService(NewThemeActivity.this, baseBean.getData().getRun_service_tip());
                                    finish();
                                }
                            }
                        }, getString(R.string.runing_service_tip), getString(R.string.confirm_ren), getString(R.string.chuli)).create();
                        tipCustomDialog.show();
                    } else if (baseBean.getError_code() == MCode.ERROR_CODE.SERVICE_CANCEL_MORE) {
                        serverService.showCancelMoreDialog(NewThemeActivity.this, baseBean.getData().getExpire_time());
                    } else {
                        tipDialog = DialogUtils.getFailDialog(NewThemeActivity.this, baseBean.getMessage(), true);
                        tipDialog.show();
                    }
                }
            });

        } else {
            /**
             * 视频聊天
             */
            videoChatService.postVideoService(permissionUtils, this, selectUser, coinDialog, new HttpObserver<PostThemeBean>() {
                @Override
                public void onSuccess(BaseBean<PostThemeBean> baseBean) {
                    PostThemeBean postThemeBean = baseBean.getData();
                    tipDialog.dismiss();
                    if (selectUser != null) {
                        postThemeBean.setServiceId(listBean.getId());
                        IntentUtils.toVideoChatActivity(NewThemeActivity.this, postThemeBean.getUser_service_id(), postThemeBean.getChat_room_config(), VideoChatFrom.USER);
                        finish();
                    }
                }

                @Override
                public void onError(BaseBean<PostThemeBean> baseBean) {
                    if (baseBean.getError_code() == MCode.ERROR_CODE.SERVICE_CANCEL_MORE) {
                        serverService.showCancelMoreDialog(NewThemeActivity.this, baseBean.getData().getExpire_time());
                    } else {
                        tipDialog = DialogUtils.getFailDialog(NewThemeActivity.this, baseBean.getMessage(), true);
                        tipDialog.show();
                    }
                }
            });
        }


    }


    private boolean checkItemData(int currentIndex) {
        switch (currentIndex) {
            case 1:
                if (!StringUtils.isEmpty(binding.includeNewThemeItem1.etText.getText())) {
                    return true;
                } else {
                    doAni(binding.includeNewThemeItem1.getRoot(), binding.includeNewThemeItem1.etText);
                }
                break;
            case 2:

                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                        return true;
                    } else {
                        doAni(binding.includeNewThemeItem2.getRoot(), binding.includeNewThemeItem2.etText);
                        return false;
                    }
                }

                if (!StringUtils.isEmpty(binding.includeNewThemeItem1.etText.getText())) {
                } else {
                    doAni(binding.includeNewThemeItem1.getRoot(), binding.includeNewThemeItem1.etText);
                    return false;
                }
                if (!StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                    return true;
                } else {
                    doAni(binding.includeNewThemeItem2.getRoot(), binding.includeNewThemeItem2.etText);
                }
                break;
            case 3:

                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem2.getRoot(), binding.includeNewThemeItem2.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                        return true;
                    } else {
                        doAni(binding.includeNewThemeItem3.getRoot(), binding.includeNewThemeItem3.etText);
                    }
                } else {
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem1.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem1.getRoot(), binding.includeNewThemeItem1.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem2.getRoot(), binding.includeNewThemeItem2.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                        return true;
                    } else {
                        doAni(binding.includeNewThemeItem3.getRoot(), binding.includeNewThemeItem3.etText);
                    }
                }
            case 4:
                if (type.equals(MValue.NEW_SERVICE_TYPE_VIDEO)) {
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem2.getRoot(), binding.includeNewThemeItem2.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem3.getRoot(), binding.includeNewThemeItem3.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem4.etText.getText())) {
                        return true;
                    } else {
                        doAni(binding.includeNewThemeItem4.getRoot(), binding.includeNewThemeItem4.etText);
                    }
                } else {
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem1.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem1.getRoot(), binding.includeNewThemeItem1.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem2.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem2.getRoot(), binding.includeNewThemeItem2.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem3.etText.getText())) {
                    } else {
                        doAni(binding.includeNewThemeItem3.getRoot(), binding.includeNewThemeItem3.etText);
                        return false;
                    }
                    if (!StringUtils.isEmpty(binding.includeNewThemeItem4.etText.getText())) {
                        return true;
                    } else {
                        doAni(binding.includeNewThemeItem4.getRoot(), binding.includeNewThemeItem4.etText);
                    }
                }

                break;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //某些权限已被授予
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    //某些权限已被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            permissionUtils.showPermissionDialog(this, null);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void doAni(View v, TextView textView) {
        textView.setHintTextColor(ContextCompat.getColor(this, R.color.theme_color));
        YoYo.with(Techniques.Shake).duration(300).playOn(v);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_theme2;
    }

    @Override
    protected void onDestroy() {
        DialogUtils.dimissDialog(tipDialog);
        DialogUtils.dimissDialog(newThemeSelectRequireDialog);
        DialogUtils.dimissDialog(newThemeSelectRewardDialog);
        DialogUtils.dimissDialog(newThemeSelectTimeDialog);
        DialogUtils.dimissDialog(newThemeSelectCityDialog);
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            objectAnimator.end();
        }

        super.onDestroy();
    }
}
