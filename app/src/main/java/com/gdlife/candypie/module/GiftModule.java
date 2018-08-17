package com.gdlife.candypie.module;

import com.gdlife.candypie.repository.GiftRepository;

import dagger.Module;
        import dagger.Provides;

@Module
public class GiftModule {

    @Provides
    public GiftRepository getGiftRepository() {
        return new GiftRepository();
    }

}
