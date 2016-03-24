package com.example.chedifier.previewofandroidn.notifications;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by chedifier on 2016/3/18.
 */
public class AutoNotifyService extends Service{

    private static final String TAG = AutoNotifyService.class.getSimpleName();

    public static final String BROADCAST_RECEIVER_ACTION = "AUTO_MSG_SERVICE_ACTION";

    public static final String ACTION_KEY = "ACTION_KEY";
    public static final int ACTION_ON_START_COMMAND = 1;
    public static final int ACTION_ON_CREATE = 2;
    public static final int ACTION_ON_DESTROY = 3;

    private Handler mHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG,"onBind " + intent);



        return null;
    }

    @Override
    public void onCreate() {

        Log.d(TAG,"onCreate ");

        super.onCreate();

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what){
                    case 0:
                        MyNotificationManager.getsInstance().sendMailNotification(
                                AutoNotifyService.this,
                                2016,
                                "邮件标题",
                                "邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容邮件内容");
                        mHandler.sendEmptyMessageDelayed(1,3000);

                        break;

                    case 1:

                        MyNotificationManager.getsInstance().sendChatNotification2(
                                AutoNotifyService.this,
                                2016,
                                "聊天标题",
                                "聊天内容聊天内容聊天内容聊天内容聊天内容聊天内容聊天内容聊天内容聊天内容");

                        mHandler.sendEmptyMessageDelayed(0,3000);

                        break;
                }

            }
        };

        sendBroadcast(new Intent(BROADCAST_RECEIVER_ACTION).putExtra(ACTION_KEY,ACTION_ON_CREATE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand intent = " + intent + " flags: " + flags
        + "  startId: " + startId);

        int rls = super.onStartCommand(intent,flags,startId);

        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(0,5000);

        sendBroadcast(new Intent(BROADCAST_RECEIVER_ACTION).putExtra(ACTION_KEY,ACTION_ON_START_COMMAND));

        return rls;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy ");

        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        sendBroadcast(new Intent(BROADCAST_RECEIVER_ACTION).putExtra(ACTION_KEY,ACTION_ON_DESTROY));

        super.onDestroy();
    }
}
