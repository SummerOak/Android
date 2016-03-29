package com.example.chedifier.previewofandroidn.directboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.chedifier.previewofandroidn.common.FileUtils;

/**
 * Created by chedifier on 2016/3/29.
 */
public class LockedBootCompleteReceiver extends BroadcastReceiver{
    private static final String TAG = LockedBootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"onReceive: " + intent);

        context.startActivity(new Intent(context,BootCompleteActivity.class));

    }
}
