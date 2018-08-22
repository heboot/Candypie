package com.gdlife.candypie.utils;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.gdlife.candypie.MAPP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/3/24.
 */

public class AudioUtil {

    public static SoundPool soundPool;

    private static List<Integer> ids = new ArrayList<>();

    private static int currentPlayId;

    private static MediaPlayer mMediaPlayer;

    /**
     * 适合播放声音短，文件小
     * 可以同时播放多种音频
     * 消耗资源较小
     */
    public static void playSound(int rawId, int sx, int loop) {


        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频的数量
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(MAPP.mapp, rawId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                int id = soundPool.play(sx, 1, 1, 0, loop, 1);
                ids.add(id);
                currentPlayId = id;
            }
        });

//        try {
//            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop();
//                mMediaPlayer.release();
//            }
//        } catch (IllegalStateException ex) {
//            mMediaPlayer = null;
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
//
//        mMediaPlayer = MediaPlayer.create(MAPP.mapp, rawId);
//        if (loop == -1) {
//            mMediaPlayer.setLooping(true);
//        } else {
//            mMediaPlayer.setLooping(false);
//        }
//        mMediaPlayer.start();


    }

    public static void stop() {
//        if (mMediaPlayer != null) {
//            try {
//                mMediaPlayer.stop();
//                mMediaPlayer.release();
//            } catch (IllegalStateException ex) {
//                mMediaPlayer = null;
//                mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.release();
//                mMediaPlayer = null;
//            }
//
//        }


        if (ids != null && ids.size() > 0) {
            for (int id : ids) {
                soundPool.stop(id);
            }
        } else {
            soundPool.autoPause();
        }

        soundPool.stop(currentPlayId);
        soundPool.autoPause();
        soundPool.release();
    }


}
