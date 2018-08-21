package com.gdlife.candypie.adapter.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.video.UserVideosActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.databinding.ItemUserVideoBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.bean.video.UserVideosBean;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/6.
 */

public class UserVideosAdapter extends BaseRecyclerViewAdapter {

    private boolean isMe = false;

    private WeakReference<UserVideosActivity> weakReference;

    private boolean isReplace;

//    public UserVideosAdapter(WeakReference<UserVideosActivity> weakReference, boolean isMe, List<HomepageVideoBean> datas, boolean isReplace) {
//        this.isMe = isMe;
//        this.weakReference = weakReference;
//        this.isReplace = isReplace;
//        data.addAll(datas);
//
//        if (isMe) {
//            data.add(0, new HomepageVideoBean());
//        }
//
//    }

    private User user;

    public UserVideosAdapter(WeakReference<UserVideosActivity> weakReference, boolean isMe, List<HomepageVideoBean> datas, boolean isReplace, User user) {
        this.isMe = isMe;
        this.weakReference = weakReference;
        this.isReplace = isReplace;
        this.user = user;
        data.addAll(datas);
        if (isMe) {
            data.add(0, new HomepageVideoBean());
        }

    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserVideosAdapter.ViewHolder(parent, R.layout.item_user_video);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<HomepageVideoBean, ItemUserVideoBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final HomepageVideoBean s, int position) {
            if (isMe) {
                if (position == 0) {
                    binding.ivPlay.setVisibility(View.GONE);
                    ImageUtils.showImage(binding.ivImg, R.drawable.icon_add_video);
                    binding.tvStatus.setVisibility(View.GONE);

                    binding.getRoot().setOnClickListener((v) -> {
                        weakReference.get().chooseVideo(isReplace);
                    });
                } else {
                    ImageUtils.showImage(binding.ivImg, s.getCover_img());
                    binding.getRoot().setOnClickListener((v) -> {
                        if (s.getStatus() == MValue.VIDEO_AUTH_STATUS_ING) {
                            return;
                        } else {
                            if (isReplace) {
                                MValue.REPLACE_VIDEO = true;
                            }
                            IntentUtils.toPlayerActivity2(binding.getRoot().getContext(), s.getPath(), isReplace ? VideoPreviewFrom.CHOOSE : VideoPreviewFrom.USER, s.getId(), s.getCover_img());
                        }
                    });
                    if (s.getStatus() == MValue.VIDEO_AUTH_STATUS_ING) {
                        binding.tvStatus.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvStatus.setVisibility(View.GONE);
                    }
                }
            } else {
                if (s.getStatus() == MValue.VIDEO_AUTH_STATUS_ING) {
                    binding.tvStatus.setVisibility(View.VISIBLE);
                } else {
                    binding.tvStatus.setVisibility(View.GONE);
                }
                ImageUtils.showImage(binding.ivImg, s.getCover_img());

                RxView.clicks(binding.getRoot()).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        IntentUtils.toPlayerActivity3(binding.getRoot().getContext(), data, position, user.getNickname());
                    }
                });

            }


//            binding.getRoot().setOnClickListener((v) -> {
//                IntentUtils.toPlayerActivity(binding.getRoot().getContext(), s.getPath());
//            });

        }

    }


}
