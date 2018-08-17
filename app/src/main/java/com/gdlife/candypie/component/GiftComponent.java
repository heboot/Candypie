package com.gdlife.candypie.component;

import com.gdlife.candypie.fragments.gift.GiftFragment;
import com.gdlife.candypie.module.GiftModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = GiftModule.class)
public interface GiftComponent {

    void inject(GiftFragment giftFragment);

}
