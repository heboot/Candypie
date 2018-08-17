package com.gdlife.candypie.adapter.user;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserFavsListActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemFavUserListBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.LogUtil;
import com.yalantis.dialog.TipCustomDialog;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class UserFavAdapter extends BaseRecyclerViewAdapter {

    protected Map<String, Object> params;

    private TipCustomDialog tipCustomDialog;

    private WeakReference<UserFavsListActivity> weakReference;

    public UserFavAdapter(WeakReference<UserFavsListActivity> weakReference, List<User> mdata) {
        data = mdata;
        this.weakReference = weakReference;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserFavAdapter.ViewHolder(parent, R.layout.item_fav_user_list);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<User, ItemFavUserListBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {

            binding.setUser(s);

            binding.includeSexage.setUser(s);

            UserService.getUserInfo(binding.tvInfo, s);


            binding.clytContainer.setOnClickListener((v) -> {
                IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, null, null);
            });

            binding.bottomWrapper.setOnClickListener((v) -> {


                tipCustomDialog = new TipCustomDialog.Builder(binding.getRoot().getContext(), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                        if (integer == 1) {
                            tipCustomDialog.dismiss();
                            weakReference.get().unFav(s.getId());
//                            params = SignUtils.getNormalParams();
//                            params.put(MKey.TO_UID, s.getId());
//                            HttpClient.Builder.getGuodongServer().unfavs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
//                                @Override
//                                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
////                                    remove(position);
////                                    notifyItemRemoved(position);
//                                    weakReference.get().initCoupons();
//                                }
//
//                                @Override
//                                public void onError(BaseBean<BaseBeanEntity> baseBean) {
//
//                                }
//                            });

                        }

                    }
                }, "确定取消收藏\n" + s.getNickname() + "?", "取消", "确定").create();

                tipCustomDialog.show();

            });

        }
    }

}
