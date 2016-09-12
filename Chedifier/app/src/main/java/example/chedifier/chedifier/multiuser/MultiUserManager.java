package example.chedifier.chedifier.multiuser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import example.chedifier.chedifier.MyApplication;
import example.chedifier.chedifier.common.SecondaryActivity;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : MultiUserManager
 * <p/>
 * Creation    : 2016/9/3
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/3, chengqianxing, Create the file
 * ****************************************************************************
 */
public class MultiUserManager {

    private static final String TAG = "MultiUserManager";


    private static class Holder{
        public static MultiUserManager sInstance = new MultiUserManager();
    }

    private MultiUserManager(){
        ;
    }

    public static MultiUserManager getInstance(){
        return Holder.sInstance;
    }

    public void registerUserChangeListener(Context context){
        if(context == null){
            return;
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_STARTED");
        intentFilter.addAction("android.intent.action.USER_STARTING");
        intentFilter.addAction("android.intent.action.USER_STOPPING");
        intentFilter.addAction("android.intent.action.USER_STOPPED");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction(Intent.ACTION_USER_FOREGROUND);
        intentFilter.addAction(Intent.ACTION_USER_BACKGROUND);

        context.registerReceiver(new UserStateReceiver(),intentFilter);

    }


    private class UserStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int user = intent.getExtras().getInt( "android.intent.extra.user_handle" );
            Log.i(TAG,"user = " + user + "UserStateReceiver onReceive " + intent);

            if (Intent.ACTION_USER_BACKGROUND.equals(intent.getAction())) {
//                MyApplication.stopProcess();
                context.startActivity(new Intent(context, SecondaryActivity.class));
            }
        }
    }

}
