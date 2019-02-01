package com.gdlife.candypie.adapter.theme;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.theme.ThemeListActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemThemeListBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/1.
 */

public class ThemeListAdapter extends BaseRecyclerViewAdapter {

    private User user;

    private TipCustomOneDialog tipDialog;

    private WeakReference<ThemeListActivity> weakReference;

    public ThemeListAdapter(List<ConfigBean.ServiceItemsConfigBean.ListBean> mdata, User one2one) {
        data = mdata;
        this.user = one2one;
    }

    public ThemeListAdapter(WeakReference<ThemeListActivity> weakReference, List<ConfigBean.ServiceItemsConfigBean.ListBean> mdata, User one2one) {
        data = mdata;
        this.user = one2one;
        this.weakReference = weakReference;
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeListAdapter.ViewHolder(parent, R.layout.item_theme_list);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<ConfigBean.ServiceItemsConfigBean.ListBean, ItemThemeListBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final ConfigBean.ServiceItemsConfigBean.ListBean s, int position) {
            ImageUtils.showImage(binding.ivImg, s.getImg());
            binding.setListBean(s);
            binding.getRoot().setOnClickListener((v) -> {
                if (!s.getType().equals(MValue.ORDER_TYPE_VIDEO) && user != null) {
                    if (!ThemeService.supportCity(user.getCity_id())) {
                        tipDialog = new TipCustomOneDialog.Builder(binding.getRoot().getContext(), "服务者所在城市尚未开通线下服务，敬请期待", "知道了", new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                tipDialog.dismiss();
                            }
                        }).create();
                        tipDialog.show();
                    } else {
                        IntentUtils.toNewThemeServiceActivity(binding.getRoot().getContext(), s, s.getType().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_ONE, user);
                    }
                    IntentUtils.toNewThemeServiceActivity(binding.getRoot().getContext(), s, s.getId().equals("1") ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_ONE, user);
                } else if (user != null) {
                    if (s.getType().equals(MValue.ORDER_TYPE_VIDEO)) {
                        if (weakReference != null && weakReference.get() != null) {
                            weakReference.get().startChatVideoService();
                        }
                    } else {
                        IntentUtils.toNewThemeServiceActivity(binding.getRoot().getContext(), s, s.getType().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_ONE, user);
                    }

                    IntentUtils.toNewThemeServiceActivity(binding.getRoot().getContext(), s, s.getId().equals("1") ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                }
                else {
                    IntentUtils.toNewThemeServiceActivity(binding.getRoot().getContext(), s, s.getType().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);

                }

            });
        }
    }


}
