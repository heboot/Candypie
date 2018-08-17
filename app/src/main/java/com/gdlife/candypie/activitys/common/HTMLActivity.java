package com.gdlife.candypie.activitys.common;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.PayFrom;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityHtmlBinding;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.NavUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.ShareDialog;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.heboot.bean.config.ConfigBean;
import com.heboot.event.PayEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Heboot on 2017/7/5.
 */

public class HTMLActivity extends BaseActivity<ActivityHtmlBinding> {


    private boolean nav;

    @Inject
    NavUtils navUtils;

    private double lat, lng;

    private QMUIBottomSheet qmuiBottomSheet;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(false);


        String title = getIntent().getStringExtra(MKey.TITLE);
        if (!StringUtils.isEmpty(title)) {
            binding.includeToolbar.tvTitle.setText(title);
        }
    }

    @Override
    public void initData() {

        DaggerUtilsComponent.builder().build().inject(this);

        nav = getIntent().getBooleanExtra(MKey.TYPE, false);

        lat = getIntent().getExtras().getDouble(MKey.LAT, 0);

        lng = getIntent().getExtras().getDouble(MKey.LNG, 0);

        if (nav) {
            binding.includeToolbar.tvRight.setText(getString(R.string.do_nav));
            binding.includeToolbar.setShowRight(true);
        }

        String url = getIntent().getStringExtra(MKey.URL);
        int index = url.indexOf("http://");
        if (index < 0) {
            index = url.indexOf("https://");
            if (index < 0) {
                url = "http://" + url;
            }

        }

        binding.wvContent.loadUrl(url);

        WebSettings webSettings = binding.wvContent.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

//        binding.wvContent.setWebViewClient(new WebViewClient() {
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                view.loadUrl(request.getUrl().toString());
////                return true;
////            }
//
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                LogUtil.e(TAG + "ed", ">>" + url);
//            }
//        });

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                binding.includeToolbar.tvTitle.setText(title);
            }


        };

        binding.wvContent.registerHandler("recharge_gold_vip_action", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                IntentUtils.toPayActivity(HTMLActivity.this, null, PayFrom.RECHARGE_GOLD_VIP_ACTION, false);
                function.onCallBack(data);
            }
        });

        binding.wvContent.registerHandler("share_action", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                ConfigBean.ShareConfigBeanWebModel configBeanModel = JSON.parseObject(data, ConfigBean.ShareConfigBeanWebModel.class);
                ConfigBean.ShareConfigBeanModel shareConfigBeanModel = new ConfigBean.ShareConfigBeanModel();
                shareConfigBeanModel.setContent(configBeanModel.getContent());
                shareConfigBeanModel.setImg(configBeanModel.getImg());
                shareConfigBeanModel.setIs_share(configBeanModel.getIs_share());
                shareConfigBeanModel.setContent(configBeanModel.getContent());
                shareConfigBeanModel.setTitle(configBeanModel.getTitle());
                shareConfigBeanModel.setType(Arrays.asList(configBeanModel.getType().split(",")));
                shareConfigBeanModel.setLink(configBeanModel.getLink());
                shareConfigBeanModel.setAction_id(configBeanModel.getAction_id());
                ShareDialog shareDialog = new ShareDialog.Builder(HTMLActivity.this, null, null, null, shareConfigBeanModel).create();
                shareDialog.show();
                function.onCallBack(data);
            }
        });
//
        binding.wvContent.registerHandler("post_video_action", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                IntentUtils.toAuthIndexActivity(HTMLActivity.this, RecordVideoFrom.USER);
                function.onCallBack(data);
            }
        });

        binding.wvContent.setDefaultHandler(new DefaultHandler());

        binding.wvContent.setWebChromeClient(wvcc);


    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            if (binding.wvContent.canGoBack()) {
                binding.wvContent.goBack();
            } else {
                finish();
            }
        });
        if (nav) {
            binding.includeToolbar.tvRight.setOnClickListener((v) -> {
                qmuiBottomSheet = navUtils.showNavDialog(this, lat, lng);
                qmuiBottomSheet.show();
            });
        }

        rxObservable.subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(PayEvent.RechargeGoldVipSUCEvent)) {
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


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (binding.wvContent.canGoBack()) {
                binding.wvContent.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.wvContent.canGoBack()) {
            if (binding.wvContent.canGoBack()) {
                binding.wvContent.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_html;
    }
}
