package com.example.chedifier.previewofandroidn.notifications;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by chedifier on 2016/3/18.
 */
public class ServiceHelper {

    public static final boolean isServiceRunning(Context ctx,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
