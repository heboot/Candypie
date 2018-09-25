package com.gdlife.candypie.activitys.common;

import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.ActiveAdapter;
import com.gdlife.candypie.adapter.search.SearchRecommendUserAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivitySearchBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.common.SearchService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.search.SearchInitBean;
import com.heboot.bean.search.SearchUserBean;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchUserActivity extends BaseActivity<ActivitySearchBinding> {

    private SearchService searchService = new SearchService();

    private String keyWord;

    private HttpObserver<SearchUserBean> httpObserver;

    private ActiveAdapter activeAdapter;

    private UIService uiService;

    private Consumer<TagsChildBean> tagsChildBeanConsumer;

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
        tagsChildBeanConsumer = new Consumer<TagsChildBean>() {
            @Override
            public void accept(TagsChildBean tagsChildBean) throws Exception {
                IntentUtils.toTagUserlistActivity(SearchUserActivity.this, tagsChildBean.getId(), "");
            }
        };
        init();
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
                    binding.clytInit.setVisibility(View.GONE);
                    if (activeAdapter == null) {
                        binding.rvList.setVisibility(View.VISIBLE);
                        activeAdapter = new ActiveAdapter(baseBean.getData().getList(), true);
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
                    binding.clytInit.setVisibility(View.GONE);
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

    private void init() {
        binding.qmuiLoading.setVisibility(View.VISIBLE);
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().search_init(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<SearchInitBean>() {
            @Override
            public void onSuccess(BaseBean<SearchInitBean> baseBean) {
                binding.qmuiLoading.setVisibility(View.GONE);
                if ((baseBean.getData().getHot_recommend() == null
                        || baseBean.getData().getHot_recommend().getItems() == null
                        || baseBean.getData().getHot_recommend().getItems().size() == 0)
                        && (baseBean.getData().getHot_tags() == null
                        || baseBean.getData().getHot_tags().getItems() == null
                        || baseBean.getData().getHot_tags().getItems().size() == 0)
                        ) {
                    binding.clytInit.setVisibility(View.GONE);
                } else {
                    binding.clytInit.setVisibility(View.VISIBLE);
                    initTags(baseBean.getData());
                    initRecommendUsers(baseBean.getData());
                }


            }

            @Override
            public void onError(BaseBean<SearchInitBean> baseBean) {
                tipDialog.dismiss();
            }
        });
    }


    private void initTags(SearchInitBean searchInitBean) {
        if (searchInitBean.getHot_tags() != null && searchInitBean.getHot_tags().getItems() != null && searchInitBean.getHot_tags().getItems().size() > 0) {
            binding.tvTagTitle.setText(searchInitBean.getHot_tags().getTitle());
            binding.qflContainer.setVisibility(View.VISIBLE);
            binding.tvTagTitle.setVisibility(View.VISIBLE);
            if (uiService == null) {
                uiService = new UIService();
            }
            uiService.initMeetSelectedTagsLayout(searchInitBean.getHot_tags().getItems(), binding.qflContainer, getResources().getDimensionPixelOffset(R.dimen.y12), getResources().getDimensionPixelOffset(R.dimen.x10), tagsChildBeanConsumer);
        } else {
            binding.tvTagTitle.setVisibility(View.GONE);
            binding.qflContainer.setVisibility(View.GONE);
        }
    }

    private void initRecommendUsers(SearchInitBean searchInitBean) {

        if (searchInitBean.getHot_recommend() != null && searchInitBean.getHot_recommend().getItems() != null && searchInitBean.getHot_recommend().getItems().size() > 0) {
            binding.tvRecommendTitle.setVisibility(View.VISIBLE);
            binding.rvRecommendList.setVisibility(View.VISIBLE);
            binding.vRecommendLine.setVisibility(View.VISIBLE);
            binding.tvRecommendTitle.setText(searchInitBean.getHot_recommend().getTitle());
            binding.rvRecommendList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvRecommendList.setAdapter(new SearchRecommendUserAdapter(R.layout.item_search_recommend_user, searchInitBean.getHot_recommend().getItems()));
        } else {
            binding.tvRecommendTitle.setVisibility(View.GONE);
            binding.rvRecommendList.setVisibility(View.GONE);
            binding.vRecommendLine.setVisibility(View.GONE);
        }
    }


    private void initNodata(int visi) {
        binding.includeNodata.getRoot().setVisibility(visi);
    }

}
