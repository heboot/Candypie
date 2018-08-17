package com.gdlife.candypie.utils;

import com.gdlife.candypie.serivce.ThemeService;
import com.heboot.event.VideoChatEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by heboot on 2018/2/6.
 */

public class ObserableUtils {

    public static int initValue = 0;

    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;

        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {

                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);
    }


    /**
     * 还能聊多久
     *
     * @param time
     * @return
     */
    public static Observable<String> videotime(final int time) {

        final int countTime = time;
        //第一个参数是延迟的意思 所以不要放
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, String>() {

                    @Override
                    public String apply(Long aLong) throws Exception {

                        LogUtil.e("计算可聊时间1...",aLong + ">>>>");

                        initValue = aLong.intValue();

                        if ((long) time - aLong == 120) {
                            RxBus.getInstance().post(VideoChatEvent.VIDEO_CHAT_BALANCE_TIP_EVENT);
                        } else if ((long) time - aLong == 0) {
                            RxBus.getInstance().post(VideoChatEvent.VIDEO_CHAT_END_EVENT);
                        }
                        return ThemeService.getMiL(aLong) + ":" + ThemeService.getMiR(aLong);
                    }
                })
                .take(countTime);
    }

    public static Observable<String> videotimeByGift(final int time) {

        final int countTime = time;
        //第一个参数是延迟的意思 所以不要放  第二个参数是间隔一秒执行
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, String>() {

                    @Override
                    public String apply(Long aLong) throws Exception {

                        LogUtil.e("计算可聊时间1...",aLong + ">>>>");

                        if(aLong <= initValue){
                            aLong =(long)initValue + aLong;
                        }else{
                            aLong =(long)initValue + aLong;
                            initValue = aLong.intValue();
                        }



                        if ((long) time - aLong == 120) {
                            RxBus.getInstance().post(VideoChatEvent.VIDEO_CHAT_BALANCE_TIP_EVENT);
                        } else if ((long) time - aLong == 0) {
                            RxBus.getInstance().post(VideoChatEvent.VIDEO_CHAT_END_EVENT);
                        }
                        return ThemeService.getMiL(aLong) + ":" + ThemeService.getMiR(aLong);
                    }
                })
                .take(countTime);
    }

    public static Observable<Integer> countdownByMILLISECONDS(int time) {
        if (time < 0) time = 0;

        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {

                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return aLong.intValue();
                    }
                })
                .take(countTime);

    }


    public static Observable<Long> countdownBySECONDS(long time) {
        if (time < 0) time = 0;

        final long countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {

                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);

    }


}
