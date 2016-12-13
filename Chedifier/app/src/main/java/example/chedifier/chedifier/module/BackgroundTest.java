package example.chedifier.chedifier.module;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.AbsModule;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : CopySelf
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class BackgroundTest extends AbsModule {

    public BackgroundTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("testBackgroundNetAcc");
        return textView;
    }

    @Override
    public void onClick(final View v) {
        //Intent intent = new Intent(this,BackgroundService.class);
        //intent.setAction(BackgroundService.ACTION_NET_ACCESS_LOOP);
        //startService(intent);


        System.out.println("loop empty list");
        ArrayList<String> list = new ArrayList<>();

        for(String s: list){
            System.out.println(s);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    Log.d("log_test","proc: " + Process.myPid() + " thread " + Thread.currentThread().getId());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("log_test","proc: " + Process.myPid() + " thread " + Thread.currentThread().getId());
                v.postDelayed(this,1000);
            }
        },1000);
    }
}
