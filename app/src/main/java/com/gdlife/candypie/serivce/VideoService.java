package com.gdlife.candypie.serivce;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.VideoUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.UserInfoEditBean;
import com.heboot.bean.video.StsUploadCredentialsBean;
import com.heboot.event.VideoEvent;
import com.heboot.req.UploadAvatarReq;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/1/25.
 */

public class VideoService {

    private Map<String, Object> params;

    @Inject
    VideoUtils videoUtils;

//    public VideoService(VideoUtils videoUtils) {
//        this.videoUtils = videoUtils;
//    }
//
//    private VideoService() {
//    }


    {
        DaggerUtilsComponent.builder().build().inject(this);
    }


    public void doUpdateAvatar(String avatarPath) {
        UploadService.doUploadAvatar(avatarPath, new Observer<UploadAvatarReq>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(UploadAvatarReq uploadAvatarReq) {
                if (uploadAvatarReq != null) {
                    params = SignUtils.getNormalParams();
                    params.put(MKey.VIDEO_ID, MValue.CURRENT_UPDATE_AVATAR_VIDEO_ID);
                    params.put(MKey.AVATAR, JSON.toJSONString(uploadAvatarReq));
                    String sign = SignUtils.doSign(params);
                    params.put(MKey.SIGN, sign);
                    HttpClient.Builder.getGuodongServer().set_cover_video(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserInfoEditBean>() {
                        @Override
                        public void onSuccess(BaseBean<UserInfoEditBean> baseBean) {
                            if (baseBean != null && baseBean.getData() != null && baseBean.getData().getUser() != null) {
                                UserService.getInstance().setUser(baseBean.getData().getUser());
                            }
//                            RxBus.getInstance().post(UserEvent.UPDATE_PROFILE);
                            RxBus.getInstance().post(VideoEvent.UPDATE_AVATAR_SUC_EVENT);
                            if (MValue.REPLACE_VIDEO) {
                                RxBus.getInstance().post(VideoEvent.REPLACE_SUC_EVENT);
                                MValue.REPLACE_VIDEO = false;
                            }
                        }

                        @Override
                        public void onError(BaseBean<UserInfoEditBean> baseBean) {

                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("头像上传测试1>", e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 上传视频
     *
     * @param videoPath
     */
    public void doUploadVideo(String imagePath, String avatarPath, int isAuth, VideoUploadType videoUploadType, String videoPath) {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

//        Observable<BaseBean<StsUploadCredentialsBean>> getUploadParams = HttpClient.Builder.getGuodongServer().sts_upload_credentials(params)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());

        Observable<String> uploadObservable = HttpClient.Builder.getGuodongServer().sts_upload_credentials(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        RxBus.getInstance().post(VideoEvent.VIDEO_UPLOAD_ERROR_EVENT);
                    }
                })
                .flatMap(new Function<BaseBean<StsUploadCredentialsBean>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(BaseBean<StsUploadCredentialsBean> videoUploadConfigBeanBaseBean) throws Exception {
                        return videoUtils.doUpload(MAPP.mapp, videoUploadType, imagePath, videoUploadConfigBeanBaseBean.getData().getConfig(), videoPath);
                    }
                });

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

                if (isAuth == 1) {

                    if (MValue.REPLACE_VIDEO) {
                        RxBus.getInstance().post(VideoEvent.REPLACE_SUC_EVENT);
                        MValue.REPLACE_VIDEO = false;
                    } else {
                        RxBus.getInstance().post(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH);
                        if (MValue.COMMIT_UPLOAD_VIDEO) {
                            MValue.COMMIT_UPLOAD_VIDEO = false;
                        } else {
                            IntentUtils.toUserInfoActivity(MAPP.mapp, MValue.USER_INFO_TYPE_AUTH, MValue.USER_INFO_TYPE_AUTH, UserService.getInstance().getUser(), null, null);
                        }

                    }
                } else if (videoUploadType == VideoUploadType.USER && isAuth == 0) {
                    RxBus.getInstance().post(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_BY_USERVIDEOS);
                } else if (videoUploadType == VideoUploadType.SKILL) {
                    LogUtil.e("测试数据1", o.toString());
                    RxBus.getInstance().post(new VideoEvent.VideoUploadSkillEvent(o.toString()));
                }


            }

            @Override
            public void onError(Throwable e) {
                RxBus.getInstance().post(VideoEvent.VIDEO_UPLOAD_ERROR_EVENT);
            }

            @Override
            public void onComplete() {

            }
        };

        if (videoUploadType == VideoUploadType.USER) {
            if (isAuth == 1) {
//                uploadObservable.flatMap(new Function<String, ObservableSource<UploadAvatarReq>>() {
//                    @Override
//                    public ObservableSource<UploadAvatarReq> apply(String s) throws Exception {
//                        params = SignUtils.getNormalParams();
//                        params.put(MKey.VIDEO_ID, s);
//                        return UploadService.doUploadAvatar(avatarPath);
//                    }
//                }).flatMap(new Function<UploadAvatarReq, ObservableSource<BaseBean<BaseBeanEntity>>>() {
//
//                    @Override
//                    public ObservableSource<BaseBean<BaseBeanEntity>> apply(UploadAvatarReq s) throws Exception {
//                        params.put(MKey.AVATAR, JSON.toJSONString(s));
//                        if (MValue.REPLACE_VIDEO) {
//                            params.put(MKey.IS_AUTH, isAuth);
//                            params.put(MKey.COVER_VIDEO, 1);
//                        } else {
//                            params.put(MKey.IS_AUTH, 1);
//                        }
//                        String sign = SignUtils.doSign(params);
//                        params.put(MKey.SIGN, sign);
//                        LogUtil.e("videoservice", JSON.toJSONString(params));
//                        return HttpClient.Builder.getGuodongServer().upload_video(params);
//                    }
//                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(observer);

                uploadObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        params = SignUtils.getNormalParams();
                        params.put(MKey.VIDEO_ID, s);
                        if (MValue.REPLACE_VIDEO) {
                            params.put(MKey.IS_AUTH, isAuth);
                            params.put(MKey.COVER_VIDEO, 1);
                        } else {
                            params.put(MKey.IS_AUTH, 1);
                        }
                        String sign = SignUtils.doSign(params);
                        params.put(MKey.SIGN, sign);
                        LogUtil.e("videoservice", JSON.toJSONString(params));
                        HttpClient.Builder.getGuodongServer().upload_video(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
                    }
                });

            } else {
                uploadObservable.flatMap(new Function<String, ObservableSource<BaseBean<BaseBeanEntity>>>() {
                    @Override
                    public ObservableSource<BaseBean<BaseBeanEntity>> apply(String s) throws Exception {
                        params = SignUtils.getNormalParams();
                        params.put(MKey.IS_AUTH, 0);
                        params.put(MKey.VIDEO_ID, s);
                        LogUtil.e("videoservice", JSON.toJSONString(params));
                        return HttpClient.Builder.getGuodongServer().upload_video(params);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
            }
        } else if (videoUploadType == VideoUploadType.SKILL) {
            uploadObservable.subscribe(observer);
        }


//
    }

    public VideoUtils getVideoUtils() {
        return videoUtils;
    }
}
