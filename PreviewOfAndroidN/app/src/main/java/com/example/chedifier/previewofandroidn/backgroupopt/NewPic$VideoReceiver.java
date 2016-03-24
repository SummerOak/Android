package com.example.chedifier.previewofandroidn.backgroupopt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chedifier on 2016/3/23.
 */
public class NewPic$VideoReceiver extends BroadcastReceiver {

    private static final String TAG = NewPic$VideoReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"onReceive: " + intent);

    }
}
