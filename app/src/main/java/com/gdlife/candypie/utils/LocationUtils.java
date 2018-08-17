package com.gdlife.candypie.utils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.listener.MyLocationListener;

/**
 * Created by heboot on 2018/2/24.
 */

public class LocationUtils {

    public LocationClient mLocationClient = null;

    private MyLocationListener myListener;

    private LocationClientOption option;


    public void requestLocation() {
        initLocation();
        initOptions();
        mLocationClient.start();
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            if (myListener != null) {
                mLocationClient.unRegisterLocationListener(myListener);
                myListener = null;
            }
            mLocationClient = null;
            option = null;
        }
    }

    /**
     * 初始化组件和服务
     */
    private void initLocation() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(MAPP.mapp);
            if (myListener == null) {
                myListener = new MyLocationListener();
            }
            //声明LocationClient类
            mLocationClient.registerLocationListener(myListener);
            //注册监听函数
        }
    }

    private void initOptions() {
        option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("gcj02");

        option.setIsNeedAddress(true);
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(0);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

}
