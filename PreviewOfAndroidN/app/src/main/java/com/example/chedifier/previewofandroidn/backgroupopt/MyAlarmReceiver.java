package com.example.chedifier.previewofandroidn.backgroupopt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by chedifier on 2016/3/28.
 */
public class MyAlarmReceiver extends BroadcastReceiver{

    private static final String TAG = MyAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive: " + intent);

        new MyAlarmManager(context).onReceiveAlarm(context,intent);

    }
}
