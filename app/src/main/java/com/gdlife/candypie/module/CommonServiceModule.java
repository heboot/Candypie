package com.gdlife.candypie.module;

import com.gdlife.candypie.serivce.DownloadService;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.serivce.UIService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heboot on 2018/3/14.
 */
@Module
public class CommonServiceModule {

    @Provides
    public UIService getUIService() {
        return new UIService();
    }

    @Provides
    public OrderService getOrderService() {
        return new OrderService();
    }

    @Provides
    public ServerService getServerService() {
        return new ServerService();
    }

    @Provides
    public DownloadService getDownloadService() {
        return new DownloadService();
    }


}
