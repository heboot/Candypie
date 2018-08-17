package com.gdlife.candypie.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/3/26.
 */

public class NavUtils {


    public QMUIBottomSheet showNavDialog(Context context, double lat, double lon) {
        List<String> maps = new ArrayList();
        if (APPUtils.isAvilible(context, "com.baidu.BaiduMap")) {//传入指定应用包名
            maps.add("百度地图");
        }
        if (APPUtils.isAvilible(context, "com.autonavi.minimap")) {//传入指定应用包名
            maps.add("高德地图");
        }
        if (APPUtils.isAvilible(context, "com.google.android.apps.maps")) {//传入指定应用包名
            maps.add("谷歌地图");
        }



        return DialogUtils.getNAVBottomSheet(context, maps, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                if (tag.equals("百度地图")) {
                    try {
//                          intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                                "destination=latlng:" + lat + "," + lon + "|name:我的目的地" +        //终点
                                "&mode=driving&" +          //导航路线方式
                                "region=北京" +           //
                                "&src=慧医#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");


                        context.startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                } else if (tag.equals("高德地图")) {
                    Intent intent = new Intent("android.intent.action.VIEW",
                            android.net.Uri.parse("androidamap://showTraffic?sourceApplication=softname&amp;poiid=BGVIS1&amp;lat=" + lat + "&amp;lon=" + lon + "&amp;level=10&amp;dev=0"));
                    intent.setPackage("com.autonavi.minimap");
                    context.startActivity(intent);
                } else if (tag.equals("谷歌地图")) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon + ", + Sydney +Australia");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            }
        });

    }

}
