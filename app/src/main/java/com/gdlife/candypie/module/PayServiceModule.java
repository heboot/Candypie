package com.gdlife.candypie.module;

import com.gdlife.candypie.serivce.PayService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heboot on 2018/3/13.
 */
@Module
public class PayServiceModule {
    @Provides
    public PayService getPayServicPe() {
        return new PayService();
    }
}
