package com.gdlife.candypie.serivce;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.OssUtils;
import com.heboot.req.UploadAvatarReq;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

import static io.reactivex.Observable.create;

/**
 * Created by heboot on 2018/3/9.
 */

public class UploadService {

    /**
     * 上传头像
     *
     * @param avatarPath 头像路径
     */
    public static void doUploadAvatar(String avatarPath, Observer<UploadAvatarReq> subscriber) {
        //头像的阿里objectKey
        String avatarObjectKey = ImageUtils.getImageUploadName(avatarPath);

        OssUtils.getInstance().uploadFile(avatarObjectKey, new OSSCompletedCallback() {
            @Override
            public void onSuccess(OSSRequest ossRequest, OSSResult ossResult) {
                int[] avatarWH = ImageUtils.getImageWH(avatarPath);
                File picFile = new File(avatarPath);
                UploadAvatarReq req = new UploadAvatarReq(".jpg", avatarWH[1], avatarWH[0], avatarObjectKey.substring(7, avatarObjectKey.length()), picFile.length());
                subscriber.onNext(req);

            }

            @Override
            public void onFailure(OSSRequest ossRequest, ClientException e, ServiceException e1) {
                subscriber.onError(e);
            }
        }, avatarPath);

    }

    public static Observable<UploadAvatarReq> doUploadAvatar(String avatarPath) {
        String avatarObjectKey = ImageUtils.getImageUploadName(avatarPath);

        return Observable.create(new ObservableOnSubscribe<UploadAvatarReq>() {
            @Override
            public void subscribe(ObservableEmitter<UploadAvatarReq> emitter) throws Exception {
                OssUtils.getInstance().uploadFile(avatarObjectKey, new OSSCompletedCallback() {
                    @Override
                    public void onSuccess(OSSRequest ossRequest, OSSResult ossResult) {
                        int[] avatarWH = ImageUtils.getImageWH(avatarPath);
                        File picFile = new File(avatarPath);
                        UploadAvatarReq req = new UploadAvatarReq(".jpg", avatarWH[1], avatarWH[0], avatarObjectKey.substring(7, avatarObjectKey.length()), picFile.length());
                        emitter.onNext(req);

                    }

                    @Override
                    public void onFailure(OSSRequest ossRequest, ClientException e, ServiceException e1) {
                        emitter.onError(e);
                    }
                }, avatarPath);
            }
        });

    }

}
