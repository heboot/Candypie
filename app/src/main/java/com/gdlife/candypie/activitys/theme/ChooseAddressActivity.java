package com.gdlife.candypie.activitys.theme;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.theme.ChooseAddressItemAdapter;
import com.gdlife.candypie.adapter.theme.ChooseAddressSortAdapter;
import com.gdlife.candypie.adapter.theme.ChooseAddressZoneAdapter;
import com.gdlife.candypie.adapter.theme.ChooseAddressZoneChildAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityNewThemeChooseAddressBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.theme.NewThemeSelectAddressDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.bean.theme.MeetPoiListData;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.TipCustomDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/26.
 */

public class ChooseAddressActivity extends BaseActivity<ActivityNewThemeChooseAddressBinding> {

    private NewThemeSelectAddressDialog dialog;
    private NewThemeSelectAddressDialog.Builder builder;
    private Consumer<ConfigBean.CityConfigBean> configBeanConsumer;

    private ChooseAddressZoneAdapter zoneAdapter;

    private ChooseAddressZoneChildAdapter zoneChildAdapter;

    private ChooseAddressItemAdapter addressItemAdapter;

    private ChooseAddressSortAdapter sortAdapter;

    private String service_id, tags, radius, sort, zone, keyword;

    private MeetPoiDataBean meetPoiBean;

    private String cityId;

    private String serverCityId;
    //设置当前城市
    ConfigBean.CityConfigBean cityConfigBean;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_chooseaddress));

        binding.rvDistance.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvNear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvSort.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvSearchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.rvList.setPullRefreshEnabled(false);
    }

    @Override
    public void initData() {

        service_id = (String) getIntent().getExtras().get(MKey.SERVICE_ID);
        tags = (String) getIntent().getExtras().get(MKey.DP_TAGS);
        serverCityId = (String) getIntent().getExtras().get(MKey.CITY_ID);


        //设置当前城市
        cityConfigBean = ThemeService.getCurrentCity();

        configBeanConsumer = new Consumer<ConfigBean.CityConfigBean>() {
            @Override
            public void accept(ConfigBean.CityConfigBean cityConfigBean) throws Exception {
                if (cityConfigBean != null) {
                    cityId = cityConfigBean.getId();
                    binding.tvCity.setText(cityConfigBean.getName());
                    if (addressItemAdapter != null) {
                        addressItemAdapter.setCityId(cityId);
                    }
                    binding.rvDistance.setVisibility(View.GONE);
                    binding.rvNear.setVisibility(View.GONE);
                    binding.rvSort.setVisibility(View.GONE);
                    binding.vBgcolor.setVisibility(View.GONE);
                    sp = 1;
                    binding.tvNear.setText(cityConfigBean.getName());
                    initAddressConfigData();
                    initAddressListData();
                }

            }
        };


        if (cityConfigBean != null) {
            cityId = cityConfigBean.getId();
        }

//        cityConfigBean = null;

        if (StringUtils.isEmpty(serverCityId)) {
            if (cityConfigBean == null) {
                if (builder == null) {
                    builder = new NewThemeSelectAddressDialog.Builder(this, configBeanConsumer, false);
                }
                if (dialog == null) {
                    dialog = builder.create();
                }
                dialog.setCancelable(false);
                dialog.show();
                return;
            }
            initAddressConfigData();
            if (cityConfigBean != null) {
                binding.tvCity.setText(cityConfigBean.getName());
            }
        } else {
            if(StringUtils.isEmpty(cityId)){
                cityId = UserService.getInstance().getUser().getCity_id();
            }
            if (!StringUtils.isEmpty(cityId) && !cityId.equals(serverCityId)) {
                TipCustomDialog tipDialog = new TipCustomDialog.Builder(this, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 0) {
                            //点了取消 继续让他选位置
                            if (cityConfigBean == null) {
                                if (builder == null) {
                                    builder = new NewThemeSelectAddressDialog.Builder(ChooseAddressActivity.this, configBeanConsumer, false);
                                }
                                if (dialog == null) {
                                    dialog = builder.create();
                                }
                                dialog.setCancelable(false);
                                dialog.show();
                                return;
                            }else{
                                initAddressConfigData();
                            }
                        } else if (integer == 1) {
                            //确定后切换到服务者所在城市
                            cityConfigBean = ThemeService.getCityByID(serverCityId);
                            cityId = cityConfigBean.getId();
                            if (cityConfigBean != null) {
                                binding.tvCity.setText(cityConfigBean.getName());
                            }
                            initAddressConfigData();
                        }
                    }
                }, getString(R.string.switch_to_servicer_city), getString(R.string.cancel), getString(R.string.confirm)).create();
                tipDialog.show();
            }else{
                if (cityConfigBean == null) {
                    if (builder == null) {
                        builder = new NewThemeSelectAddressDialog.Builder(this, configBeanConsumer, false);
                    }
                    if (dialog == null) {
                        dialog = builder.create();
                    }
                    dialog.setCancelable(false);
                    dialog.show();
                    return;
                }
                initAddressConfigData();
                if (cityConfigBean != null) {
                    binding.tvCity.setText(cityConfigBean.getName());
                }
            }
        }






    }


    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_PAGE)) {
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

        RxTextView.textChanges(binding.etKeywordSearch).take(1000).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                keyword = charSequence.toString();
                if (!StringUtils.isEmpty(keyword)) {
                    initAddressListData();
                }

            }
        });

        binding.etKeyword.setOnClickListener((v) -> {
            binding.clytSearch.setVisibility(View.VISIBLE);
            QMUIKeyboardHelper.showKeyboard(binding.etKeywordSearch, false);
        });

        binding.tvCancel.setOnClickListener((v) -> {
            binding.clytSearch.setVisibility(View.GONE);
        });


        binding.vNear.setOnClickListener((v) -> {
            if (binding.llytNear.getVisibility() == View.GONE) {
                binding.llytNear.setVisibility(View.VISIBLE);
                binding.rvNear.setVisibility(View.VISIBLE);
                binding.rvDistance.setVisibility(View.VISIBLE);
                binding.rvSort.setVisibility(View.GONE);
                binding.vBgcolor.setVisibility(View.VISIBLE);
            } else {
                binding.vBgcolor.setVisibility(View.GONE);
                binding.llytNear.setVisibility(View.GONE);
            }
        });

        binding.vSort.setOnClickListener((v) -> {
            if (binding.rvSort.getVisibility() == View.GONE) {
                binding.llytNear.setVisibility(View.GONE);
                binding.rvNear.setVisibility(View.GONE);
                binding.rvDistance.setVisibility(View.GONE);
                binding.rvSort.setVisibility(View.VISIBLE);
                binding.vBgcolor.setVisibility(View.VISIBLE);
            } else {
                binding.vBgcolor.setVisibility(View.GONE);
                binding.rvSort.setVisibility(View.GONE);
            }
        });

        binding.vBgcolor.setOnClickListener((v) -> {
            if (binding.rvSort.getVisibility() == View.VISIBLE) {
                binding.rvSort.setVisibility(View.GONE);
            }
            if (binding.rvNear.getVisibility() == View.VISIBLE) {
                binding.rvNear.setVisibility(View.GONE);
            }
            if (binding.rvDistance.getVisibility() == View.VISIBLE) {
                binding.rvDistance.setVisibility(View.GONE);
            }
            binding.vBgcolor.setVisibility(View.GONE);
        });


        binding.tvCity.setOnClickListener((v) -> {
            if (builder == null) {
                builder = new NewThemeSelectAddressDialog.Builder(this, configBeanConsumer, false);
            }
            if (dialog == null) {
                dialog = builder.create();
            }
            dialog.show();
        });

        binding.rvList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (sp + 1 > total) {
                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    binding.rvList.loadMoreComplete();
                    return;
                }
                sp = sp + 1;
                initAddressListData();
            }
        });

    }

    private void initAddressConfigData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SERVICE_ID, service_id);
        params.put(MKey.DP_TAGS, tags);
        params.put(MKey.CITY_ID, cityId);
        params.put(MKey.RADIUS, radius == null ? "" : radius);
        params.put(MKey.SORT, sort == null ? "" : sort);
        params.put(MKey.ZONE, zone == null ? "" : zone);
        params.put(MKey.KEYWORD, keyword == null ? "" : keyword);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().poi_init(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<MeetPoiDataBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(MeetPoiDataBean registerBean) {
                meetPoiBean = registerBean;
                initRVData();
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<MeetPoiDataBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(ChooseAddressActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }

        }));

    }

    private void initAddressListData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SERVICE_ID, service_id);
        params.put(MKey.DP_TAGS, tags);

        params.put(MKey.RADIUS, radius == null ? "" : radius);
        params.put(MKey.SORT, sort == null ? "" : sort);
        params.put(MKey.ZONE, zone == null ? "" : zone);
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        params.put(MKey.KEYWORD, keyword == null ? "" : keyword);
        params.put(MKey.CITY_ID, cityId == null ? "" : cityId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().query_poi(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<MeetPoiListData>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(MeetPoiListData meetPoiListData) {
                sp = meetPoiListData.getSp();
                total = meetPoiListData.getTotalPages();
                pageSize = meetPoiListData.getPageSize();

                if (binding.clytSearch.getVisibility() == View.VISIBLE) {
                    if (meetPoiListData != null && meetPoiListData.getList() != null && meetPoiListData.getList().size() > 0) {
                        if (addressItemAdapter == null) {
                            addressItemAdapter = new ChooseAddressItemAdapter(meetPoiListData.getList());
                            if (StringUtils.isEmpty(addressItemAdapter.getCityId()) && !StringUtils.isEmpty(cityId)) {
                                addressItemAdapter.setCityId(cityId);
                            }
                            binding.rvSearchResult.setAdapter(addressItemAdapter);
                        } else {
                            binding.rvSearchResult.setAdapter(addressItemAdapter);
                            if (sp == 1) {
                                addressItemAdapter.getData().clear();
                            }
                            addressItemAdapter.getData().addAll(meetPoiListData.getList());
                            addressItemAdapter.notifyDataSetChanged();
                        }

                    }
                } else {
                    if (meetPoiListData != null && meetPoiListData.getList() != null && meetPoiListData.getList().size() > 0) {
                        binding.plytContainer.toMain();
                        if (addressItemAdapter == null) {
                            addressItemAdapter = new ChooseAddressItemAdapter(meetPoiListData.getList());
                            binding.rvList.setAdapter(addressItemAdapter);
                        } else {

                            if (sp == 1) {
                                addressItemAdapter.getData().clear();
                            }
                            addressItemAdapter.getData().addAll(meetPoiListData.getList());
                            addressItemAdapter.notifyDataSetChanged();
                        }

                    } else {
                        if (sp == 1) {
                            binding.plytContainer.toNoData();
                        }
                    }
                    binding.rvList.loadMoreComplete();
                }


//                if (addressItemAdapter != null) {
//                    addressItemAdapter.setData(meetPoiListData.getList());
//                    addressItemAdapter.notifyDataSetChanged();
//                }
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<MeetPoiListData> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(ChooseAddressActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }

        }));

    }


    private void initRVData() {

        if (meetPoiBean != null) {

            if (addressItemAdapter == null) {

                if (meetPoiBean.getList() != null) {
                    addressItemAdapter = new ChooseAddressItemAdapter(meetPoiBean.getList());
                    binding.rvList.setAdapter(addressItemAdapter);
                } else {
                    meetPoiBean.setList(new ArrayList<>());
                    addressItemAdapter = new ChooseAddressItemAdapter(meetPoiBean.getList());
                    binding.rvList.setAdapter(addressItemAdapter);
                    binding.plytContainer.toNoData();
                }

                if (meetPoiBean.getList() != null && meetPoiBean.getList().size() > 0 && sp == 1) {
                    binding.plytContainer.toMain();
                } else {
                    if (sp == 1) {
                        binding.plytContainer.toNoData();
                    }

                }
            } else {
                addressItemAdapter.getData().clear();
                addressItemAdapter.getData().addAll(meetPoiBean.getList());
                addressItemAdapter.notifyDataSetChanged();
                binding.plytContainer.toMain();
            }
            if (addressItemAdapter != null) {
                addressItemAdapter.setCityId(cityId);
            }


            zoneAdapter = new ChooseAddressZoneAdapter(new WeakReference<ChooseAddressActivity>(this), ThemeService.getZoneDatas(meetPoiBean));


            binding.rvNear.setAdapter(zoneAdapter);

            initZoneChildData(0, "");

            sortAdapter = new ChooseAddressSortAdapter(new WeakReference<ChooseAddressActivity>(this), meetPoiBean.getSort_config());

            binding.rvSort.setAdapter(sortAdapter);
        } else {
            binding.plytContainer.toNoData();
            binding.rvList.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化商圈子列表的数据
     *
     * @param position
     */
    public void initZoneChildData(int position, String pid) {

        if (zoneChildAdapter == null) {
            zoneChildAdapter = new ChooseAddressZoneChildAdapter(new WeakReference<ChooseAddressActivity>(this), ThemeService.getZoneChildDatas(meetPoiBean, position), pid);
            binding.rvDistance.setAdapter(zoneChildAdapter);
        } else {
            zoneChildAdapter.setPid(pid);
            zoneChildAdapter.setData(ThemeService.getZoneChildDatas(meetPoiBean, position));
            zoneChildAdapter.notifyDataSetChanged();
        }

    }

    public void selectZone(boolean distance, MeetPoiDataBean.ZoneConfigBean.ItemsBeanX.ItemsBean bean) {


        if (distance) {
            radius = String.valueOf(bean.getId());
        } else {
            binding.tvNear.setText(bean.getName());
            zone = bean.getName();
        }

        binding.llytNear.setVisibility(View.GONE);
        binding.vBgcolor.setVisibility(View.GONE);

        sp = 1;

        initAddressListData();
    }

    public void selectSort(MeetPoiDataBean.SortConfigBean sortConfigBean) {
        sort = String.valueOf(sortConfigBean.getId());
        binding.rvSort.setVisibility(View.GONE);
        binding.vBgcolor.setVisibility(View.GONE);
        initAddressListData();
    }


    @Override
    protected void onDestroy() {
        DialogUtils.dimissDialog(dialog);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_theme_choose_address;
    }
}
