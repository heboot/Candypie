package com.gdlife.candypie.utils;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationsUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    private static final String id = "channel_1";

    private static final String name = "channel_name";

    private static NotificationManager notificationManager;

    public static void showNotification(String title, Intent intent, PendingIntent pendingIntent) {


        if (pendingIntent == null) {
            if (intent == null) {
                intent = new Intent(MAPP.mapp.getCurrentActivity(), MainActivity.class);
                pendingIntent = PendingIntent.getActivity(MAPP.mapp.getCurrentActivity().getApplicationContext(), 0, intent, 0);
            } else {
                pendingIntent = PendingIntent.getActivity(MAPP.mapp.getCurrentActivity().getApplicationContext(), 0, intent, 0);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            Notification notification = createNotificationChannel(title, title, pendingIntent).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            getManager().notify(1, notification);
        } else {
            Notification notification = createNotificationNormal(title, title, pendingIntent).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            getManager().notify(1, notification);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(notificationChannel);
    }

    private static NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) MAPP.mapp.getCurrentActivity().getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }


    private static Notification.Builder createNotificationChannel(String title, String content, PendingIntent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (intent == null) {
                return new Notification.Builder(MAPP.mapp.getCurrentActivity())
                        .setContentTitle(title)
                        .setContentText(content)
                        .setChannelId(id)
                        .setSmallIcon(R.drawable.app_icon)
                        .setAutoCancel(true);
            }
            return new Notification.Builder(MAPP.mapp.getCurrentActivity())
                    .setContentTitle(title)
                    .setContentIntent(intent)
                    .setContentText(content)
                    .setChannelId(id)
                    .setSmallIcon(R.drawable.app_icon)
                    .setAutoCancel(true);
        }
        if (intent == null) {
            return new Notification.Builder(MAPP.mapp.getCurrentActivity())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.app_icon)
                    .setAutoCancel(true);
        }
        return new Notification.Builder(MAPP.mapp.getCurrentActivity())
                .setContentTitle(title)
                .setContentIntent(intent)
                .setContentText(content)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true);

    }

    private static NotificationCompat.Builder createNotificationNormal(String title, String content, PendingIntent intent) {
        if (intent == null) {
            return new NotificationCompat.Builder(MAPP.mapp.getCurrentActivity())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.app_icon)
                    .setAutoCancel(true);
        }
        return new NotificationCompat.Builder(MAPP.mapp.getCurrentActivity())
                .setContentTitle(title)
                .setContentIntent(intent)
                .setContentText(content)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true);
    }

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
