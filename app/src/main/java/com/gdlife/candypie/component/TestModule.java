package com.gdlife.candypie.component;

import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.TestService;
import com.gdlife.candypie.serivce.UIService;

import dagger.Module;
import dagger.Provides;

@Module
public class TestModule {

    @Provides
    public TestService getTestService(UIService service) {
        return new TestService(service);
    }


}
