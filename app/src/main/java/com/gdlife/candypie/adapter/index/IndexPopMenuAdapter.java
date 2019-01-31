package com.gdlife.candypie.adapter.index;

import android.animation.Animator;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemPopMenuBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/1.
 */

public class IndexPopMenuAdapter extends BaseRecyclerViewAdapter {

    private boolean enable;

    private int currentIndex = 0;

    private AlphaAnimation animation2;

    private ViewPropertyAnimator animator;

    private List<String> avatars;

    public IndexPopMenuAdapter(boolean enable, List<ConfigBean.ServiceItemsConfigBean.ListBean> mdata, List<String> avatar) {//List<ConfigBean.ServiceItemsConfigBean.ListBean> mdata,
        animation2 = new AlphaAnimation(1f, 0.1f);
        animation2.setDuration(800);
        this.data.addAll(mdata);
        this.enable = enable;
        this.avatars = avatar;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new IndexPopMenuAdapter.ViewHolder(parent, R.layout.item_pop_menu);
    }

    boolean flag = false;

    public void addMenu(ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
        data.add(listBean);
        notifyItemInserted(data.size());
    }


    private class ViewHolder extends BaseRecyclerViewHolder<ConfigBean.ServiceItemsConfigBean.ListBean, ItemPopMenuBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final ConfigBean.ServiceItemsConfigBean.ListBean s, int position) {
            binding.tvName.setText(s.getTitle());
            if (StringUtils.isEmpty(s.getImg())) {
                ImageUtils.showImage(binding.ivImg, R.drawable.jn6);
            } else {
                ImageUtils.showImage(binding.ivImg, s.getImg());
            }
            //如果是发现
            if (s.getId().equals("-1")) {

                binding.ivImg.animate().setInterpolator(new LinearInterpolator());
                binding.ivImg.animate().alpha(0.1f).setDuration(2000).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if (!flag) {
                            currentIndex = currentIndex + 1;
                            if (currentIndex >= avatars.size()) {
                                currentIndex = 0;
                            }
                            if (binding == null || binding.ivImg == null || avatars == null || avatars.get(currentIndex) == null) {
                                binding.ivImg.animate().cancel();
                                return;
                            }
                            ImageUtils.showImage(binding.ivImg, avatars.get(currentIndex));
                            binding.ivImg.animate().alpha(1f).setDuration(2000).start();
                            flag = true;
                        } else {
                            if (binding == null || binding.ivImg == null || MAPP.mapp == null || avatars == null) {
                                return;
                            }
                            flag = false;
                            binding.ivImg.animate().setStartDelay(1000).alpha(0.1f).setDuration(2000).start();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
                animator = binding.ivImg.animate();

            }

            RxView.clicks(binding.getRoot()).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    if (enable) {
                        if (s.getId().equals("0")) {
                            IntentUtils.toThemeListActivity(binding.getRoot().getContext(), false, null);
                        } else if (s.getId().equals("-1")) {
//                            IntentUtils.toDiscoverActivity(binding.getRoot().getContext());
                            IntentUtils.toUserRecommendVideoActivity(binding.getRoot().getContext());
                        } else {
                            IntentUtils.toNewThemeServiceActivity(binding.getRoot().getContext(), s, s.getType().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                        }
                    }
                }
            });

        }

    }

    public void stopAni() {
        if (animator != null) {
            animator.cancel();
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
