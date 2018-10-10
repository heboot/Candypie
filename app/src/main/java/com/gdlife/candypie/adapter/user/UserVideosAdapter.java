package com.gdlife.candypie.adapter.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserPageActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/6.
 */

public class UserVideosAdapter extends BaseRecyclerViewAdapter {

    private boolean isMe = false;

    private WeakReference<UserVideosActivity> weakReference;

    private WeakReference<UserPageActivity> userPageActivityWeakReference;

    private List<User> videoUsers = new ArrayList();

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

    public UserVideosAdapter(WeakReference<UserPageActivity> weakReference, boolean isMe, List<HomepageVideoBean> datas, User user) {
        this.isMe = isMe;
        this.userPageActivityWeakReference = weakReference;
        this.isReplace = false;
        this.user = user;
        data.addAll(datas);
//        if (isMe) {
//            data.add(0, new HomepageVideoBean());
//        }

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
            if (isMe && userPageActivityWeakReference == null) {
                if (position == 0) {
                    binding.ivPlay.setVisibility(View.GONE);
                    ImageUtils.showImage(binding.ivImg, R.drawable.icon_add_video);
                    binding.tvStatus.setVisibility(View.GONE);

                    binding.getRoot().setOnClickListener((v) -> {
                        weakReference.get().chooseVideo(isReplace);
                    });

                    binding.vBottom.setVisibility(View.GONE);
                    binding.tvOption.setVisibility(View.GONE);
                } else {
                    ImageUtils.showImage(binding.ivImg, s.getCover_img());
                    binding.getRoot().setOnClickListener((v) -> {
                        if (s.getStatus() == MValue.VIDEO_AUTH_STATUS_ING) {
                            videoUsers.clear();
                            for (HomepageVideoBean homepageVideoBean : user.getUser_video().getList()) {
                                videoUsers.add(user);
                            }
                            IntentUtils.toUserVideoAudioPlayActivity(binding.getRoot().getContext(), position - 1, user.getUser_video().getTotal(), videoUsers);
                            return;
                        } else {
                            if (isReplace) {
                                MValue.REPLACE_VIDEO = true;
                                IntentUtils.toPlayerActivity2(binding.getRoot().getContext(), s.getPath(), isReplace ? VideoPreviewFrom.CHOOSE : VideoPreviewFrom.USER, s.getId(), s.getCover_img());
                            } else {
                                videoUsers.clear();
                                for (HomepageVideoBean homepageVideoBean : user.getUser_video().getList()) {
                                    videoUsers.add(user);
                                }
                                IntentUtils.toUserVideoAudioPlayActivity(binding.getRoot().getContext(), position - 1, user.getUser_video().getTotal(), videoUsers);
                            }

                        }
                    });
                    if (s.getStatus() == MValue.VIDEO_AUTH_STATUS_ING) {
                        binding.tvStatus.setVisibility(View.VISIBLE);
                        binding.vBottom.setVisibility(View.GONE);
                        binding.tvOption.setVisibility(View.GONE);
                        binding.tvOption.setEnabled(false);
                    } else {
                        binding.vBottom.setVisibility(View.VISIBLE);
                        binding.tvOption.setVisibility(View.VISIBLE);
                        if (s.getIs_main_video() == 1) {
                            binding.tvOption.setText("主视频");
                            binding.tvOption.setEnabled(false);
                            binding.vBottom.setOnClickListener(v -> {
                            });
                        } else {
                            binding.tvOption.setText("编辑");
                            binding.tvOption.setEnabled(true);
                            binding.vBottom.setOnClickListener(v -> {
                                weakReference.get().doVideoOption(s);
                            });
                        }
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

                        videoUsers.clear();
                        for (HomepageVideoBean homepageVideoBean : user.getUser_video().getList()) {
                            videoUsers.add(user);
                        }
                        IntentUtils.toUserVideoAudioPlayActivity(binding.getRoot().getContext(), position, user.getUser_video().getTotal(), videoUsers);

//                        IntentUtils.toPlayerActivity3(binding.getRoot().getContext(), data, position, user.getNickname());
                    }
                });

            }

            if (!StringUtils.isEmpty(s.getPrice()) && Integer.parseInt(s.getPrice()) > 0) {
                if (s.getUnlock() == 0 || isMe) {
                    binding.tvPriceTip.setVisibility(View.VISIBLE);
                    binding.tvUnlockTip.setVisibility(View.GONE);
                    binding.tvPriceTip.setText(s.getPrice() + "钻");
                } else {
                    binding.tvPriceTip.setVisibility(View.GONE);
                    binding.tvUnlockTip.setVisibility(View.VISIBLE);
                }
            } else {
                binding.tvPriceTip.setVisibility(View.GONE);
                binding.tvUnlockTip.setVisibility(View.GONE);
            }


//            binding.getRoot().setOnClickListener((v) -> {
//                IntentUtils.toPlayerActivity(binding.getRoot().getContext(), s.getPath());
//            });

        }

    }


}
