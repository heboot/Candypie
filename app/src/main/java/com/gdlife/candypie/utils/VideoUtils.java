package com.gdlife.candypie.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.alibaba.sdk.android.vod.upload.session.VodHttpClientConfig;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
import com.aliyun.demo.crop.AliyunVideoCrop;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.serivce.UserService;
import com.heboot.bean.video.StsUploadCredentialsBean;
import com.heboot.event.VideoEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DateUtil;

import java.util.Date;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by heboot on 2018/1/17.
 */

public class VideoUtils {

    private String TAG = "VideoUtils";

    private VodHttpClientConfig vodHttpClientConfig;

    private SvideoInfo svideoInfo;

    private VodSessionCreateInfo vodSessionCreateInfo;

    private AliyunSnapVideoParam recordParam;

    protected Map<String, Object> params;


    private void initAliyunSnapVidepParam(int max, int min) {
        recordParam = new AliyunSnapVideoParam.Builder()
                .setResolutionMode(AliyunSnapVideoParam.RESOLUTION_720P)
                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)
                .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                .setBeautyLevel(80)
                .setBeautyStatus(true)
                .setCameraType(CameraType.FRONT)
                .setFlashType(FlashType.ON)
                .setNeedRecord(false)
                .setNeedClip(true)
                .setMaxDuration(max)
                .setMinDuration(min)
                .setVideoQuality(VideoQuality.HD)
                .setGop(5)

                /**
                 * 裁剪参数
                 */
//                .setMinVideoDuration(4000)
//                .setMaxVideoDuration(29 * 1000)
//                .setMinCropDuration(3000)
//                .setFrameRate(25)
//                .setCropMode(ScaleMode.PS)
//                .setSortMode(AliyunSnapVideoParam.SORT_MODE_PHOTO)
                .build();
    }
//
//

    /**
     * 去录制视频
     */

    public void toRecordVideo(Activity activity) {
        if (recordParam == null) {
            initAliyunSnapVidepParam(MValue.RECORD_VIDEO_MAX, MValue.RECORD_VIDEO_MIN);
        }
        AliyunVideoRecorder.startRecordForResult(activity, MCode.REQUEST_CODE.RECORD_VIDEO_CODE, recordParam);
    }

    public void toSelectVideo(Activity activity) {
        AliyunSnapVideoParam mCropParam = new AliyunSnapVideoParam.Builder()
//                .setFrameRate(frameRate) //设置帧率
//                .setGop(gop) //设置关键帧间隔
//                .setCropMode(cropMode) //设置裁剪模式，目前支持有黑边和无黑边两种
//                .setVideQuality(videoQulity) //设置裁剪质量
//                .setVideoBitrate(2000) //设置视频码率，如果不设置则使用视频质量videoQulity参数计算出码率
                .setCropUseGPU(true) //设置裁剪方式，是否使用gpu进行裁剪，不设置则默认使用cpu来裁剪
                .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_720P) //设置分辨率，目前支持360p，480p，540p，720p
                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)//设置裁剪比例 目前支持1:1,3:4,9:16
                .setNeedRecord(false)//设置是否需要开放录制入口]
                .setMinVideoDuration(10000) //设置过滤的视频最小长度 单位毫秒
                .setMaxVideoDuration(300 * 1000) //设置过滤的视频最大长度 单位毫秒
                .setMinCropDuration(10000) //设置视频最小裁剪时间 单位毫秒
                .setSortMode(AliyunSnapVideoParam.SORT_MODE_VIDEO)
                .build();
        AliyunVideoCrop.startCropForResult(activity, MCode.REQUEST_CODE.CHOOSE_VIDEO_CODE, mCropParam);
    }

    //
//    /**
//     * 上传
//     *
//     * @param context
//     * @param videoUploadConfigBean
//     * @param title
//     * @param imagePath
//     * @param videoPath
//     * @return
//     */
    public Observable<String> doUpload(final Context context, VideoUploadType videoUploadType, String imagePath, final StsUploadCredentialsBean.ConfigBean videoUploadConfigBean, final String videoPath) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                VODSVideoUploadClient vodsVideoUploadClient = new VODSVideoUploadClientImpl(context);
                vodsVideoUploadClient.init();

                //参数请确保存在，如不存在SDK内部将会直接将错误throw Exception
                // 文件路径保证存在之外因为Android 6.0之后需要动态获取权限，请开发者自行实现获取"文件读写权限".
                if (vodHttpClientConfig == null) {
                    initVodHttpClientConfig();
                }

                getVideoTitle(videoUploadType);

                initVodSessionCreateInfo(videoPath, imagePath, videoUploadConfigBean);

                vodsVideoUploadClient.uploadWithVideoAndImg(vodSessionCreateInfo, new VODSVideoUploadCallback() {
                    @Override
                    public void onUploadSucceed(String videoId, String imageUrl) {
                        emitter.onNext(videoId);
                        //上传成功返回视频ID和图片URL.
                        Log.e(TAG, "onUploadSucceed" + "videoId:" + videoId + "imageUrl" + imageUrl);
//                        IntentUtils.toPlayerActivity(context, videoId);
                    }

                    @Override
                    public void onUploadFailed(String code, String message) {
                        //上传失败返回错误码和message.错误码有详细的错误信息请开发者仔细阅读
                        Log.e(TAG, "onUploadFailed" + "code" + code + "message" + message);
                        emitter.onError(new Throwable(message));
                    }

                    @Override
                    public void onUploadProgress(long uploadedSize, long totalSize) {
                        //上传的进度回调,非UI线程
                        RxBus.getInstance().post(new VideoEvent.VideoUploadProgressEvent((int) (uploadedSize * 100 / totalSize)));
                        Log.d(TAG, vodsVideoUploadClient.toString() + ">>>" + this + "onUploadProgress" + uploadedSize * 100 / totalSize);
                        //                progress = uploadedSize * 100 / totalSize;
                        //                handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onSTSTokenExpried() {
                        Log.d(TAG, "onSTSTokenExpried");
                        //STS token过期之后刷新STStoken，如正在上传将会断点续传
                        //                vodsVideoUploadClient.refreshSTSToken(accessKeyId, accessKeySecret, securityToken, expriedTime);
                    }

                    @Override
                    public void onUploadRetry(String code, String message) {
                        //上传重试的提醒
                        Log.d(TAG, "onUploadRetry" + "code" + code + "message" + message);
                    }

                    @Override
                    public void onUploadRetryResume() {
                        //上传重试成功的回调.告知用户重试成功
                        Log.d(TAG, "onUploadRetryResume");
                    }
                });
            }
        });

    }

    //
//
    private void initVodSessionCreateInfo(String videoPath, String imagePath, StsUploadCredentialsBean.ConfigBean videoUploadConfigBean) {
        //构建点播上传参数(重要)
        vodSessionCreateInfo = new VodSessionCreateInfo.Builder()
                .setImagePath(imagePath)//图片地址
                .setVideoPath(videoPath)//视频地址
                .setAccessKeyId(videoUploadConfigBean.getAccessKeyId())//临时accessKeyId
                .setAccessKeySecret(videoUploadConfigBean.getAccessKeySecret())//临时accessKeySecret
                .setSecurityToken(videoUploadConfigBean.getSecurityToken())//securityToken
                .setExpriedTime(videoUploadConfigBean.getExpiration())//STStoken过期时间
//                .setRequestID(requestID)//requestID，开发者可以传将获取STS返回的requestID设置也可以不设.
                .setIsTranscode(false)//是否转码.如开启转码请AppSever务必监听服务端转码成功的通知
                .setSvideoInfo(svideoInfo)//短视频视频信息
                .setVodHttpClientConfig(vodHttpClientConfig)//网络参数
                .build();
    }

    private void getVideoTitle(VideoUploadType videoUpload) {
        if (videoUpload == VideoUploadType.USER) {
            initSvideoInfo("user_" + UserService.getInstance().getUser().getId() + "_" + DateUtil.date2Str(new Date(System.currentTimeMillis()), DateUtil.UPLOAD));
        } else if (videoUpload == VideoUploadType.SKILL) {
            initSvideoInfo("skill" + UserService.getInstance().getUser().getId() + "_" + DateUtil.date2Str(new Date(System.currentTimeMillis()), DateUtil.UPLOAD));
        }
    }

    //
    private void initVodHttpClientConfig() {
        vodHttpClientConfig = new VodHttpClientConfig.Builder()
                .setMaxRetryCount(2)//重试次数
                .setConnectionTimeout(15 * 1000)//连接超时
                .setSocketTimeout(15 * 1000)//socket超时
                .build();

    }

    private void initSvideoInfo(String title) {
        //构建短视频VideoInfo,常见的描述，标题，详情都可以设置
        svideoInfo = new SvideoInfo();
        svideoInfo.setTitle(title);
        svideoInfo.setDesc("");
        svideoInfo.setCateId(1);
    }

}
