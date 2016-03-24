package com.example.chedifier.previewofandroidn.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 *
 * Created by chedifier on 2016/3/18.
 */
public class MyNotificationReceiver extends BroadcastReceiver{

    private static final String TAG = MyNotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive " + intent);

        MyNotificationManager.getsInstance().onNotificationIntentBack(context,intent);

    }
}
