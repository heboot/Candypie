package com.gdlife.candypie.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.gdlife.candypie.R;

import static android.content.Context.AUDIO_SERVICE;

public class AudioUtil2 {

    private boolean playing = false;
    private boolean loaded = false;
    private float actualVolume;
    private float maxVolume;
    private float volume;
    private AudioManager audioManager;
    private SoundPool soundPool;


    private int ringingStreamId;
    private int userCaingId;
    private int servicerCallingId;
    private int moneyId;
    private int newMessageId;
    private static AudioUtil2 instance;
    /*Mny*/

    private AudioUtil2(Context context) {
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;

        // Load the sounds
        //因为在5.0上new SoundPool();被弃用 5.0上利用Builder
        //创建SoundPool
        int maxStreams = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .build();
        } else {
            soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }

        });
        //加载资源ID
        userCaingId = soundPool.load(context, R.raw.voip_calling_ring, 1);
        servicerCallingId = soundPool.load(context, R.raw.voip_called, 1);
        moneyId = soundPool.load(context, R.raw.money, 1);
        newMessageId = soundPool.load(context, R.raw.new_message, 1);
    }

    public static AudioUtil2 getInstance(Context context) {
        if (instance == null) {
            instance = new AudioUtil2(context);
        }
        return instance;
    }

    //播放用户拨打
    public void playUserCalling() {
        if (loaded) {// && !playing
            ringingStreamId = soundPool.play(userCaingId, 1, 1, 1, -1, 1f);
            playing = true;
        }
    }

    //播放服务者收到单子
    public void playServicerCalling() {
        if (loaded) {// && !playing
            ringingStreamId = soundPool.play(servicerCallingId, 1, 1, 1, -1, 1f);
            playing = true;
        }
    }

    //来钱了
    public void playMoneySound() {
        if (loaded) {// && !playing
            ringingStreamId = soundPool.play(moneyId, volume, volume, 1, 0, 1f);
//            playing = true;
        }
    }

    public void playNewMsgSound() {
        if (loaded) {//&& !playing
            ringingStreamId = soundPool.play(newMessageId, volume, volume, 1, 0, 1f);
//            playing = true;
        }
    }

    //Stop播放
    public void stopRinging() {
        if (playing) {
            soundPool.stop(ringingStreamId);
            playing = false;
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.unload(userCaingId);
            soundPool.unload(servicerCallingId);
            soundPool.unload(moneyId);
            soundPool.unload(newMessageId);
            soundPool.release();
            soundPool = null;
        }
        instance = null;
    }
}
