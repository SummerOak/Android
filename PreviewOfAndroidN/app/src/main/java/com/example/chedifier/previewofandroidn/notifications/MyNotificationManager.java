package com.example.chedifier.previewofandroidn.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.chedifier.previewofandroidn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by chedifier on 2016/3/18.
 */
public class MyNotificationManager {

    private static final String TAG = MyNotificationManager.class.getSimpleName();

    private static final String NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT";

    private static final String NOTIFICATION_ACTION_REPLY = "NOTIFICATION_ACTION_REPLY";

    private static final String NOTIFICATION_ACTION_DISMISS = "NOTIFICATION_ACTION_DISMISS";

    private static final String NOTIFICATION_REPLY_TEXT_KEY = "NOTIFICATION_REPLY_TEXT_KEY";

    private static final String INTENT_EXTRA_KEY1 = "INTENT_EXTRA_KEY1";

    private static MyNotificationManager sInstance;

    private List<String> mChatHistory = new CopyOnWriteArrayList<>();

    private static final int NOTIFICATION_GROUP_ID_CHAT = 1;
    private static final int NOTIFICATION_GROUP_ID_MAIL = 2;

    private static final String NOTIFICATION_GROUP_CHAT = "com.cheidifier.notification.chat";

    private static final String NOTIFICATION_GROUP_MAIL = "com.cheidifier.notification.mail";
    private static final Map<String,Integer> sGroups = new HashMap<>();

    static {
        sGroups.put(NOTIFICATION_GROUP_CHAT,NOTIFICATION_GROUP_ID_CHAT);
        sGroups.put(NOTIFICATION_GROUP_MAIL,NOTIFICATION_GROUP_ID_MAIL);
    }

    private int mIncNotifyId = sGroups.size()+1;

    private MyNotificationManager(){

    }

    public synchronized static MyNotificationManager getsInstance(){
        if(sInstance == null){
            sInstance = new MyNotificationManager();
        }

        return sInstance;
    }

    public void sendMailNotification(Context ctx,
                                     int requestCode,
                                     String title,
                                     String content){

        PendingIntent contentIntent = PendingIntent.getActivity(
                ctx,
                requestCode,
                new Intent(ctx,NotificationTestActivity.class),
                0);

        Notification notification = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.image2)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.image3))
                .setContentTitle(title)
                .setContentInfo("NotifyContentInfo")
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setGroup(NOTIFICATION_GROUP_MAIL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .build();

        Log.d(TAG,"sendMailNotification ");
        NotificationManagerCompat.from(ctx).notify(getNotifyId(),notification);

        updateGroup(ctx);
    }

    public void sendChatNotification(Context ctx,
                                     int requestCode,
                                     String title,
                                     String content){

        //接收输入使用
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY_TEXT_KEY)
                .setLabel("chedifier1.remoteImput.label1")
                .build();

        RemoteInput remoteInput2 = new RemoteInput.Builder(NOTIFICATION_REPLY_TEXT_KEY+"2")
                .setLabel("chedifier1.remoteImput.label2")
                .build();

        PendingIntent piReply = PendingIntent.getBroadcast(
                ctx,
                requestCode,
                new Intent(ctx,MyNotificationReceiver.class).setAction(NOTIFICATION_ACTION_REPLY)
                        .putExtra(INTENT_EXTRA_KEY1,1),
                0);

        NotificationCompat.Action actReply = new NotificationCompat.Action.Builder(
                R.drawable.notification_icon,
                "回复",piReply)
                .addRemoteInput(remoteInput)
                .addRemoteInput(remoteInput2)
                .build();


        PendingIntent piDismiss = PendingIntent.getBroadcast(
                ctx,
                requestCode,
                new Intent(ctx,MyNotificationReceiver.class).setAction(NOTIFICATION_ACTION_DISMISS)
                        .putExtra(INTENT_EXTRA_KEY1,1),
                0);
        NotificationCompat.Action actDismiss = new NotificationCompat.Action.Builder(
                R.drawable.image3,
                "清除",piDismiss)
                .build();

        PendingIntent piChat = PendingIntent.getActivity(
                ctx,
                requestCode,
                new Intent(ctx,NotificationTestActivity.class),
                0);
        NotificationCompat.Action actChat = new NotificationCompat.Action.Builder(
                R.drawable.image3,
                "进入聊天",piChat)
                .build();

        //在notification消失时触发
        PendingIntent deleteIntent = PendingIntent.getBroadcast(
                ctx,
                0,
                new Intent(ctx,MyNotificationReceiver.class).setAction(NOTIFICATION_ACTION_DISMISS)
                        .putExtra(INTENT_EXTRA_KEY1,1),
                0);

        PendingIntent contentIntent = PendingIntent.getActivity(
                ctx,
                requestCode,
                new Intent(ctx,NotificationTestActivity.class),
                0);

        Notification notification = new NotificationCompat.Builder(ctx)
                .addAction(actReply)
                .addAction(actDismiss)
                .addAction(actChat)//添加重复action无效
                .setSmallIcon(R.drawable.image2)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.image3))
                .setContentTitle(title)
                .setContentInfo("NotifyContentInfo")
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setDeleteIntent(deleteIntent)
                .setAutoCancel(true)
                .setGroup(NOTIFICATION_GROUP_CHAT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .build();

        Log.d(TAG,"sendChatNotification ");
        NotificationManagerCompat.from(ctx).notify(getNotifyId(),notification);

        updateGroup(ctx);

        mChatHistory.add(content);
    }


    /**
     * 通过 android.app 下的类生成
     * @param ctx
     * @param requestCode
     * @param title
     * @param content
     */
    public void sendChatNotification2(Context ctx,
                                     int requestCode,
                                     String title,
                                     String content){

        int notifyId = getNotifyId();

        //接收输入使用
        android.app.RemoteInput remoteInput = new android.app.RemoteInput.Builder(NOTIFICATION_REPLY_TEXT_KEY)
                .setLabel("label1")
                .setChoices(new String[]{"yes","no","maybe"})//wearable上有效，手机上直接被忽略
                .build();


        android.app.RemoteInput remoteInput2 = new android.app.RemoteInput.Builder(NOTIFICATION_REPLY_TEXT_KEY+"2")
                .setLabel("chedifier1.remoteImput.label2")
                .build();

        PendingIntent piReply = PendingIntent.getBroadcast(
                ctx,
                requestCode,
                new Intent(ctx,MyNotificationReceiver.class).setAction(NOTIFICATION_ACTION_REPLY)
                        .putExtra(INTENT_EXTRA_KEY1,notifyId),
                0);

        Notification.Action actReply = new Notification.Action.Builder(
                Icon.createWithResource(ctx,R.drawable.notification_icon),
                "回复",piReply)
                .addRemoteInput(remoteInput)
                .build();


        PendingIntent piDismiss = PendingIntent.getBroadcast(
                ctx,
                requestCode,
                new Intent(ctx,MyNotificationReceiver.class).setAction(NOTIFICATION_ACTION_DISMISS)
                        .putExtra(INTENT_EXTRA_KEY1,notifyId),
                0);
        Notification.Action actDismiss = new Notification.Action.Builder(
                Icon.createWithResource(ctx,R.drawable.notification_icon),
                "清除",piDismiss)

                .addRemoteInput(remoteInput2)
                .build();

        PendingIntent piChat = PendingIntent.getActivity(
                ctx,
                requestCode,
                new Intent(ctx,NotificationTestActivity.class),
                0);
        Notification.Action actChat = new Notification.Action.Builder(
                Icon.createWithResource(ctx,R.drawable.image3),
                "进入聊天",piChat)
                .build();

        //在notification消失时触发
        PendingIntent deleteIntent = PendingIntent.getBroadcast(
                ctx,
                0,
                new Intent(ctx,MyNotificationReceiver.class).setAction(NOTIFICATION_ACTION_DISMISS)
                        .putExtra(INTENT_EXTRA_KEY1,notifyId),
                0);

        PendingIntent contentIntent = PendingIntent.getActivity(
                ctx,
                requestCode,
                new Intent(ctx,NotificationTestActivity.class),
                0);

        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(),R.layout.custom_notification_layout);

        Notification notification = new Notification.Builder(ctx)
                .addAction(actReply)
//                .setRemoteInputHistory(getChatHistory())
                .addAction(actDismiss)
                .addAction(actChat)//添加重复action无效
                .setSmallIcon(R.drawable.image2)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.image3))
                .setContentTitle(title)
                .setContentInfo("NotifyContentInfo")
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setDeleteIntent(deleteIntent)
//                .setCustomContentView(remoteViews)
//                .setStyle(new Notification.DecoratedCustomViewStyle())//需要保持系统Head,Action等样式时设置
                .setGroup(NOTIFICATION_GROUP_CHAT)
                .setPriority(Notification.PRIORITY_HIGH)//在通知时能在顶部弹出，也就是Heads up效果
                .setDefaults(NotificationCompat.DEFAULT_SOUND)//在通知时能在顶部弹出，也就是Heads up效果
                .build();


        Log.d(TAG,"sendChatNotification ");
        NotificationManagerCompat.from(ctx).notify(notifyId,notification);

        updateGroup(ctx);

        mChatHistory.add(content);
    }

    private String[] getChatHistory(){
        List<String> lstChatHistory = new ArrayList<>();

        for(int s = 1;s<=3;s++){
            if(mChatHistory.size()>=s){
                lstChatHistory.add("聊天历史: " + mChatHistory.get(mChatHistory.size()-s));
            }
        }

        return lstChatHistory.toArray(new String[lstChatHistory.size()]);
    }

    private void updateGroup(Context ctx){
        if(ctx == null){
            return;
        }

        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(
                Context.NOTIFICATION_SERVICE);

        final StatusBarNotification[] statusBarNotification = notificationManager.getActiveNotifications();
        Map<String,Integer> groups = new LinkedHashMap<>();
        for(StatusBarNotification v:statusBarNotification){
            String group = v.getNotification().getGroup();

            Log.d(TAG,"updateGroup group = " + group);

            if(group != null && !group.equals("")){
                if(sGroups.containsValue(v.getId())){
                    if(!groups.containsKey(group)){
                        groups.put(group,0);
                    }

                }else{
                    groups.put(group,groups.containsKey(group)?groups.get(group)+1:1);
                }
            }
        }

        for(Map.Entry<String,Integer> entry:groups.entrySet()){
            String group = entry.getKey();
            int childNum = entry.getValue();

            Log.d(TAG,"updateGroup group = " + group + "  childNum = " + childNum);

            if(childNum < 1){
                notificationManager.cancel(sGroups.get(group));
            }else{
                updateGroupSummary(ctx,group,childNum);
            }
        }
    }

    private void updateGroupSummary(Context ctx,String group,int childNum){
        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(
                Context.NOTIFICATION_SERVICE);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.image4)
                .setGroup(group)
                .setGroupSummary(true);
        final Notification notification = builder.build();
        notificationManager.notify(sGroups.get(group), notification);
    }

    public void onNotificationIntentBack(Context ctx,Intent intent){

        Log.d(TAG,"onNotificationIntentBack intent: " + intent);

        if(intent != null){
            String action = intent.getAction();
            switch (action){
                case NOTIFICATION_ACTION_REPLY:
                    String reply = getReplyTextFromIntent(intent);
                    Log.d(TAG,"onNotificationIntentBack reply: " + reply);

                    if(reply != null && !reply.equals("")){
                        mChatHistory.add(reply);
                    }

                    break;

                case NOTIFICATION_CONTENT:

                    break;
            }

            int notifyId = intent.getIntExtra(INTENT_EXTRA_KEY1,-1);
            NotificationManagerCompat.from(ctx).cancel(notifyId);

        }

    }

    private String getReplyTextFromIntent(Intent intent){
        Bundle bundle = RemoteInput.getResultsFromIntent(intent);

        if(bundle != null){
            return bundle.getString(NOTIFICATION_REPLY_TEXT_KEY);
        }

        return null;
    }


    private int getNotifyId(){

        Log.d(TAG,"getNotifyId " + mIncNotifyId);
        return ++mIncNotifyId;
    }
}
