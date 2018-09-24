package com.gdlife.candypie.activitys.video;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.CropKey;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.user.UserVideosAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityUserVideosBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VideoService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.FrameUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.VideoUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.gdlife.candypie.widget.common.BottomSheetDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.bean.video.UserVideosBean;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.VideoEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.PublishAlbumDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/6.
 */

public class UserVideosActivity extends BaseActivity<ActivityUserVideosBinding> {

    private User user;

    private UserVideosAdapter userVideosAdapter;

    private BottomSheetDialog videoBottomSheet;

    private Consumer<Integer> videoConsumer;

    private String videoPath;

    private VideoUtils videoUtils;

    private UserVideoActivityFrom from;

    @Inject
    VideoService videoService;

    FrameUtils frameUtils;

    private PublishAlbumDialog publishAlbumDialog;

    private boolean mReplace;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText(getString(R.string.user_videos));
        binding.rvList.setLayoutManager(new GridLayoutManager(this, 3));
//        binding.loadingView.show(true);
        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
    }

    @Override
    public void initData() {
        frameUtils = new FrameUtils();

//        DaggerUtilsComponent.builder().build().inject(this);
        DaggerServiceComponent.builder().build().inject(this);
        videoUtils = videoService.getVideoUtils();
        from = (UserVideoActivityFrom) getIntent().getExtras().get(MKey.FROM);
        user = (User) getIntent().getExtras().get(MKey.USER);

        initVideos();

//        binding.loadingView.setVisibility(View.GONE);
        binding.rvList.setVisibility(View.VISIBLE);


    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        rxObservable = RxBus.getInstance().toObserverable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread());
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
//              LogUtil.e("发送刷新用户视频集事件3", o.toString());
                if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_USERVIDEOS)) {
                    LogUtil.e("发送刷新用户视频集事件2", "发送刷新用户视频集事件2");
                    publishAlbumDialog.showAniTo100();
                    tipDialog = DialogUtils.getSuclDialog(UserVideosActivity.this, "上传成功，请等待审核", true);
                    tipDialog.show();
                    sp = 1;
                    initVideos();
                } else if (o instanceof VideoEvent.VideoUploadProgressEvent) {
                    int progress = ((VideoEvent.VideoUploadProgressEvent) o).getProgress();

                    if (publishAlbumDialog == null) {// from == UserVideoActivityFrom.REPLACE_MAIN_VIDEO && publishAlbumDialog == null
                        publishAlbumDialog = new PublishAlbumDialog.Builder(UserVideosActivity.this).create();
                        publishAlbumDialog.show();
                    }


                    publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_ing));
                    publishAlbumDialog.getBinding().pb.setProgress(progress);
                    if (progress >= 100) {
                        publishAlbumDialog.getBinding().tvLoadingText.setText(getString(R.string.upload_suc));
                        publishAlbumDialog.dismiss();
                    }
                } else if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH) || o.equals(VideoEvent.REPLACE_SUC_EVENT)) {
                    tipDialog = DialogUtils.getSuclDialog(UserVideosActivity.this, getString(R.string.upload_suc_auth_tip), true);
                    tipDialog.show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sp = 1;
                            initVideos();
                        }
                    });
                } else if (o.equals(NormalEvent.FINISH_USER_VIDEO_PAGE)) {
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


        binding.rvList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (sp + 1 > total) {
                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    binding.rvList.loadMoreComplete();
                    return;
                }
                sp = sp + 1;
                initVideos();
            }
        });
        videoConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                videoBottomSheet.dismiss();
                switch (integer) {
                    case 0:
                        if (mReplace) {
                            MValue.REPLACE_VIDEO = true;
                        }
                        IntentUtils.toAuthIndexActivity(UserVideosActivity.this, mReplace ? RecordVideoFrom.AUTH : RecordVideoFrom.USER);
                        break;
                    case 1:
                        videoUtils.toSelectVideo(UserVideosActivity.this);
                        break;
                }
            }
        };

        binding.spLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initVideos();
            }
        });
        binding.rvList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (sp >= total) {
                    binding.rvList.loadMoreComplete();
                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    return;
                }
                sp = sp + 1;
                initVideos();

            }
        });


    }

    public void chooseVideo(boolean isReplace) {

        mReplace = isReplace;

        if (isReplace) {
            MValue.REPLACE_VIDEO = true;
        }

        if (videoBottomSheet == null) {
            videoBottomSheet = DialogUtils.getAuthSkillBottomSheet(UserVideosActivity.this, videoConsumer);
        }
        videoBottomSheet.show();
//
//        IntentUtils.toAuthIndexActivity(this, isReplace ? RecordVideoFrom.AUTH : RecordVideoFrom.USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MCode.REQUEST_CODE.RECORD_VIDEO_CODE || requestCode == MCode.REQUEST_CODE.CHOOSE_VIDEO_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    doUpload(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
                } else if (type == AliyunVideoRecorder.RESULT_TYPE_CROP) {
                    videoPath = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
                    if (mReplace) {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.AUTH, null);
                    } else {
                        IntentUtils.toPlayerActivity2(this, videoPath, VideoPreviewFrom.ADD, null);
                    }
//                    doUpload(data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH));
                }
//                    IntentUtils.toVideoFrameActivity(this, data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
//                    videoService.doUploadVideo(this, imagePath, data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            ToastUtils.showToast("用户取消录制");
        }
    }

    private void doUpload(String path) {
        videoPath = path;

        try {
            Bitmap bitmap = frameUtils.getFrameByVideo(videoPath);
            String imagePath = ImageUtils.saveBitmap(bitmap);
            publishAlbumDialog = new PublishAlbumDialog.Builder(this).create();
            publishAlbumDialog.show();
            publishAlbumDialog.showAni();
            videoService.doUploadVideo(imagePath, null, 0, VideoUploadType.USER, videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVideos() {
        params = SignUtils.getNormalParams();

        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        params.put(MKey.UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().video_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserVideosBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(UserVideosBean sendSMSBeanBaseBean) {
                total = sendSMSBeanBaseBean.getTotalPages();
                pageSize = sendSMSBeanBaseBean.getPageSize();

                if (sendSMSBeanBaseBean.getSp() == 1) {
                    binding.spLayout.setRefreshing(false);
                } else {
                    binding.rvList.loadMoreComplete();
                }


                if (sendSMSBeanBaseBean != null && sendSMSBeanBaseBean.getList() != null && sendSMSBeanBaseBean.getList().size() > 0) {
                    binding.plytContainer.toMain();
                    if (userVideosAdapter == null) {
                        if (user.getUser_video() != null && user.getUser_video().getList() != null) {
                            user.getUser_video().getList().clear();
                            user.getUser_video().getList().addAll(sendSMSBeanBaseBean.getList());
                        }
                        else if (user.getUser_video() != null && user.getUser_video().getList() == null) {
                            user.getUser_video().setList(new ArrayList<>());
                            user.getUser_video().getList().addAll(sendSMSBeanBaseBean.getList());
                        } else if (user.getUser_video() == null) {
                            user.setUser_video(new UserVideosBean());
                            user.getUser_video().setList(new ArrayList<>());
                            user.getUser_video().getList().addAll(sendSMSBeanBaseBean.getList());
                        }
                        if (UserService.getInstance().getUser() == null) {
                            userVideosAdapter = new UserVideosAdapter(new WeakReference<UserVideosActivity>(UserVideosActivity.this), false, sendSMSBeanBaseBean.getList(), from.equals(UserVideoActivityFrom.REPLACE_MAIN_VIDEO), user);
                        } else {
                            userVideosAdapter = new UserVideosAdapter(new WeakReference<UserVideosActivity>(UserVideosActivity.this), user.getId().intValue() == UserService.getInstance().getUser().getId().intValue(), sendSMSBeanBaseBean.getList(), from.equals(UserVideoActivityFrom.REPLACE_MAIN_VIDEO), user);
                        }

                        binding.rvList.setAdapter(userVideosAdapter);
                    } else {
                        if (sp == 1) {
                            if (user.getUser_video() != null && user.getUser_video().getList() != null) {
                                user.getUser_video().getList().clear();
                            }
                            userVideosAdapter.getData().clear();
                        }
                        if (user.getUser_video() != null && user.getUser_video().getList() != null) {
                            user.getUser_video().getList().addAll(sendSMSBeanBaseBean.getList());
                        }
                        userVideosAdapter.getData().addAll(sendSMSBeanBaseBean.getList());
                        if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue() && sp == 1) {
                            userVideosAdapter.getData().add(0, new HomepageVideoBean());
                        }
                        userVideosAdapter.notifyDataSetChanged();
                    }

                } else {
                    binding.plytContainer.toMain();
                    if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue() && sp == 1) {
                        userVideosAdapter = new UserVideosAdapter(new WeakReference<UserVideosActivity>(UserVideosActivity.this), user.getId().intValue() == UserService.getInstance().getUser().getId().intValue(), new ArrayList<>(), from.equals(UserVideoActivityFrom.REPLACE_MAIN_VIDEO), user);
                        if (user.getId() == UserService.getInstance().getUser().getId() && sp == 1) {
                            userVideosAdapter.getData().add(0, new HomepageVideoBean());
                        }
                        binding.rvList.setAdapter(userVideosAdapter);
                    }
                }


//                binding.rvList.setVisibility(View.VISIBLE);
//                binding.rvList.setAdapter(new UserVideosAdapter(new WeakReference<UserVideosActivity>(UserVideosActivity.this), user.getId() == UserService.getInstance().getUser().getId(), sendSMSBeanBaseBean.getList()));
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<UserVideosBean> baseBeanEntity) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(UserVideosActivity.this, baseBeanEntity.getMessage(), true);
                tipDialog.show();
            }

        }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_videos;
    }
}
