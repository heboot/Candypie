package com.gdlife.candypie.activitys.theme;

import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ActivityNewThemeChooseAddressDetailBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.ThemeEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DistanceUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

/**
 * Created by heboot on 2018/2/27.
 */

public class ChooseAddressDetailActivity extends BaseActivity<ActivityNewThemeChooseAddressDetailBinding> {

    private MeetPoiDataBean.ListBean meetPoiBean;

    private TipDialog callMobileDialog;

    private String type;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_chooseaddress_detail));
    }

    @Override
    public void initData() {
        meetPoiBean = (MeetPoiDataBean.ListBean) getIntent().getExtras().get(MKey.MEET_POI_LISTBEAN);
        type = getIntent().getStringExtra(MKey.TYPE);
        binding.setListBean(meetPoiBean);
        ImageUtils.showImage(binding.ivImg, meetPoiBean.getPhoto_urls());
        binding.tvDistance.setText(DistanceUtil.getDistanceStr(meetPoiBean.getLat(), meetPoiBean.getLng(), MValue.currentLocation.getLatitude(), MValue.currentLocation.getLongitude()));


        if (!StringUtils.isEmpty(type)) {
            binding.v2.setVisibility(View.INVISIBLE);
            binding.tvInfo.setVisibility(View.INVISIBLE);
            binding.btnBottom.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.btnBottom.setOnClickListener((v) -> {
            RxBus.getInstance().post(new ThemeEvent.ThemeChooseAddressEvent(meetPoiBean));
            RxBus.getInstance().post(NormalEvent.FINISH_PAGE);
            finish();
        });

        binding.tvMobile.setOnClickListener((v) -> {
            callMobileDialog = DialogUtils.getCallMobileDialog(ChooseAddressDetailActivity.this, meetPoiBean.getTelephone());
            callMobileDialog.show();
        });

        binding.tvComment.setOnClickListener((v -> {
            IntentUtils.toHTMLActivity(this, "", meetPoiBean.getReview_all_url());
        }));

        binding.tvAddress.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, "", ThemeService.getAddressMapURL(meetPoiBean.getLat(), meetPoiBean.getLng()), true, meetPoiBean.getLat(), meetPoiBean.getLng());
        });

    }

    @Override
    protected void onDestroy() {
        DialogUtils.dimissDialog(callMobileDialog);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_theme_choose_address_detail;
    }
}
