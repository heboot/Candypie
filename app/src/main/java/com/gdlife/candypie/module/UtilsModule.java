package com.gdlife.candypie.module;

import com.gdlife.candypie.utils.APPUtils;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.FrameUtils;
import com.gdlife.candypie.utils.InitUtils;
import com.gdlife.candypie.utils.LocationUtils;
import com.gdlife.candypie.utils.NavUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.VideoUtils;
import com.gdlife.candypie.utils.rv.RVUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heboot on 2018/1/17.
 */
@Module
public class UtilsModule {

    @Provides
    public VideoUtils getVideoUtils() {
        return new VideoUtils();
    }

    @Singleton
    @Provides
    public InitUtils getInitUtils() {
        return new InitUtils();
    }

    @Singleton
    @Provides
    public APPUtils getAPPUtils() {
        return new APPUtils();
    }

    @Singleton
    @Provides
    public CheckUtils getCheckUtils() {
        return new CheckUtils();
    }

    @Singleton
    @Provides
    public LocationUtils getLocationUtils() {
        return new LocationUtils();
    }

    @Provides
    public PermissionUtils getPermissonUtils() {
        return new PermissionUtils();
    }

    @Singleton
    @Provides
    public FrameUtils getFrameUtils() {
        return new FrameUtils();
    }

    @Singleton
    @Provides
    public NavUtils getNAVUtils() {
        return new NavUtils();
    }

}
