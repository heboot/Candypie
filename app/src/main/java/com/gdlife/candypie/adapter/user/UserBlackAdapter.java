package com.gdlife.candypie.adapter.user;

import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.HomepageActivity;
import com.gdlife.candypie.activitys.user.UserBlackListActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemFavUserListBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.yalantis.dialog.TipCustomDialog;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserBlackAdapter extends BaseRecyclerViewAdapter {

    private TipCustomDialog tipCustomDialog;

    protected Map<String, Object> params;

    private WeakReference<UserBlackListActivity> weakReference;

    public UserBlackAdapter(WeakReference<UserBlackListActivity> weakReference, List<User> mdata) {
        data = mdata;
        this.weakReference = weakReference;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserBlackAdapter.ViewHolder(parent, R.layout.item_fav_user_list);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<User, ItemFavUserListBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {

            binding.tvAction.setText(MAPP.mapp.getString(R.string.cancel_black));

            binding.includeSexage.setUser(s);

            binding.setUser(s);

            UserService.getUserInfo(binding.tvInfo, s);

//            binding.getRoot().setOnClickListener((v) -> {
//                if (s.getRole() == MValue.USER_ROLE_SERVICER) {//&& s.getService_auth_status() == MValue.AUTH_STATUS_SUC
//                    IntentUtils.toHomepageActivity(MAPP.mapp, MValue.FROM_OTHER, s, null, null);
//                } else {
//                    IntentUtils.toUserInfoActivity(MAPP.mapp, MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s, null, null);
//                }
//            });

            binding.bottomWrapper.setOnClickListener((v) -> {


                tipCustomDialog = new TipCustomDialog.Builder(binding.getRoot().getContext(), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                        if (integer == 1) {
                            tipCustomDialog.dismiss();
                            weakReference.get().unBlack(String.valueOf(s.getId()));

                        }

                    }
                }, "确定取消对" + s.getNickname() + "的拉黑吗?", "点错了", "确定").create();

                tipCustomDialog.show();

            });

        }
    }

}
