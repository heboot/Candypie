package com.gdlife.candypie.module;

import com.gdlife.candypie.serivce.VideoService;
import com.gdlife.candypie.utils.VideoUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heboot on 2018/1/25.
 */
@Module
public class VideoServiceModule {

    @Provides
    public VideoService getVideoService() {
        return new VideoService();
    }
}
