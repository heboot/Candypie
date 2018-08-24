package com.gdlife.candypie.activitys.order;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.order.UserOrderFragmentAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ActivityUserOrderBinding;
import com.gdlife.candypie.fragments.order.OrderListFragment;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by heboot on 2018/3/7.
 */

public class UserOrderActivity extends BaseActivity<ActivityUserOrderBinding> {

    private OrderListFragment orderListFragment, orderListFragment2, orderListFragment3, orderListFragment4, orderListFragment5;

    private ArrayList<String> titles;

    private List<OrderListFragment> fragments = new ArrayList<>();

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_order));
        setSwipeBackEnable(false);
    }

    @Override
    public void initData() {

        initTitles();

        orderListFragment = new OrderListFragment(MValue.ORDER_LIST_STATUS_ALL);
        orderListFragment2 = new OrderListFragment(MValue.ORDER_LIST_STATUS_DAIWANCHENG);
        orderListFragment3 = new OrderListFragment(MValue.ORDER_LIST_STATUS_FINISH);
        orderListFragment4 = new OrderListFragment(MValue.ORDER_LIST_STATUS_CANCEL);
        orderListFragment5 = new OrderListFragment(MValue.ORDER_LIST_STATUS_DAICHULI);

        fragments.add(orderListFragment);
        fragments.add(orderListFragment2);
        fragments.add(orderListFragment3);
        fragments.add(orderListFragment4);
        fragments.add(orderListFragment5);

        String[] ttt = null;

        ttt = titles.toArray(new String[titles.size()]);

        binding.rvList.setAdapter(new UserOrderFragmentAdapter(getSupportFragmentManager(), fragments));

        binding.rvList.setOffscreenPageLimit(fragments.size());

        binding.stTitle.setViewPager(binding.rvList, ttt);

    }

    private void initTitles() {
        titles = new ArrayList<String>();
        titles.add(getString(R.string.order_title_all));
        titles.add(getString(R.string.order_title_daiwancheng));
        titles.add(getString(R.string.order_title_yiwancheng));
        titles.add(getString(R.string.order_title_cancel));
        titles.add(getString(R.string.order_title_daichuli));
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
                if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
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
    protected int getLayoutId() {
        return R.layout.activity_user_order;
    }
}
