package com.gdlife.candypie.utils;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;

/**
 * Created by Heboot on 2017/7/5.
 */

public class OssUtils {

    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";



    private static OSSClient ossClient;

    private static OssUtils instance;

    public static OssUtils getInstance() {
        if (instance == null) {
            instance = new OssUtils();
            // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAInfZNVc1uyyKJ", "5bI5waSP93zwEIsri1TlkwI4u1mhjt");
            if (ossClient == null) {
                ossClient = new OSSClient(MAPP.mapp, endpoint, credentialProvider);
            }
        }
        return instance;
    }

    public void uploadFile(String objectKey, OSSCompletedCallback callback, String path) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(BuildConfig.OSSBUCKET, objectKey, path);
        ossClient.asyncPutObject(putObjectRequest, callback);
    }

}
