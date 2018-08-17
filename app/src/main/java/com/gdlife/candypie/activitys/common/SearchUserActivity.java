package com.gdlife.candypie.activitys.common;

import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.ActiveAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.databinding.ActivitySearchBinding;
import com.gdlife.candypie.serivce.common.SearchService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.search.SearchUserBean;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.functions.Consumer;

public class SearchUserActivity extends BaseActivity<ActivitySearchBinding> {

    private SearchService searchService = new SearchService();

    private String keyWord;

    private HttpObserver<SearchUserBean> httpObserver;

    private ActiveAdapter activeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void initData() {
    }


    private void showClear() {
        binding.vClear.setVisibility(View.VISIBLE);
        binding.vClearbg.setVisibility(View.VISIBLE);
    }

    private void hideClear() {
        binding.vClear.setVisibility(View.GONE);
        binding.vClearbg.setVisibility(View.GONE);
    }


    @Override
    public void initListener() {

        addDisposable(RxTextView.textChanges(binding.etContent).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (charSequence.length() > 0) {
                    showClear();
                } else {
                    hideClear();
                }
            }
        }));

        binding.vCancel.setOnClickListener((v) -> {
            finish();
        });

        binding.vClearbg.setOnClickListener((v) -> {
            binding.etContent.setText(null);
        });

        binding.etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;

            }
        });
        httpObserver = new HttpObserver<SearchUserBean>() {
            @Override
            public void onSuccess(BaseBean<SearchUserBean> baseBean) {
                binding.qmuiLoading.setVisibility(View.GONE);
                QMUIKeyboardHelper.hideKeyboard(binding.etContent);
                if (baseBean.getData() != null && baseBean.getData().getList() != null && baseBean.getData().getList().size() > 0) {
                    initNodata(View.GONE);
                    if (activeAdapter == null) {
                        binding.rvList.setVisibility(View.VISIBLE);
                        activeAdapter = new ActiveAdapter(baseBean.getData().getList());
                        binding.rvList.setAdapter(activeAdapter);
                        binding.tvResultTitle.setVisibility(View.VISIBLE);

                    } else {
                        binding.tvResultTitle.setVisibility(View.VISIBLE);
                        binding.rvList.setVisibility(View.VISIBLE);
                        activeAdapter.getData().clear();
                        activeAdapter.setData(baseBean.getData().getList());
                        activeAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.tvResultTitle.setVisibility(View.GONE);
                    binding.rvList.setVisibility(View.GONE);
                    initNodata(View.VISIBLE);
                }
            }

            @Override
            public void onError(BaseBean<SearchUserBean> baseBean) {

            }
        };

        QMUIKeyboardHelper.showKeyboard(binding.etContent, true);

    }

    private void doSearch() {

        keyWord = binding.etContent.getText().toString();

        if (StringUtils.isEmpty(keyWord)) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入蜜糖号", true);
            tipDialog.show();
            return;
        }

        binding.qmuiLoading.setVisibility(View.VISIBLE);

        searchService.query_user(keyWord, httpObserver);
    }

    private void initNodata(int visi) {
        binding.includeNodata.getRoot().setVisibility(visi);
    }

}
