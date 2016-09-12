package example.chedifier.chedifier.module;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.MainActivity;
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
public class NotificationTest extends AbsModule {

    public NotificationTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("Test Notification");
        return textView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Log.i("cqx_tn", "...");

        NotificationManager mgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setAction(Intent.ACTION_VIEW); // action
        notificationIntent.setData(Uri.parse("http://www.baidu.com"));

        PendingIntent contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Notification.Builder builder = new Notification.Builder(mContext.getApplicationContext());
        builder.setTicker("ticker")
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentTitle("content title")
                .setContentText("content text")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.n_test))
                .setContentIntent(contentIntent);

        Notification n = builder.build();
        mgr.notify(100, n);
    }
}
