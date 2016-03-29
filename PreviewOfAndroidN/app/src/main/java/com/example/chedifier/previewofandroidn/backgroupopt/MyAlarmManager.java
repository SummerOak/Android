package com.example.chedifier.previewofandroidn.backgroupopt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * 测试闹钟
 *
 * Created by chedifier on 2016/3/29.
 */
public class MyAlarmManager {

    private static final String TAG = MyAlarmManager.class.getSimpleName();

    Intent intent1;
    PendingIntent pIntent1;
    Intent intent2;
    PendingIntent pIntent2;
    Intent intent3;
    PendingIntent pIntent3;
    Intent intent4;
    PendingIntent pIntent4;
    Intent intent5;
    PendingIntent pIntent5;
    Intent intent6;
    PendingIntent pIntent6;
    Intent intent7;
    PendingIntent pIntent7;
    Intent intent8;
    PendingIntent pIntent8;

    public static boolean sAlarmLooping = false;


    public MyAlarmManager(Context context){
        intent1 = new Intent("com.example.chedifier.previewofandroidn.alarm1");
        intent1.putExtra("REQ_CODE",1001);
        pIntent1 = PendingIntent.getBroadcast(context,1001,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        intent2 = new Intent("com.example.chedifier.previewofandroidn.alarm2");
        intent2.putExtra("REQ_CODE",1002);
        pIntent2 = PendingIntent.getBroadcast(context,1002,intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        intent3 = new Intent("com.example.chedifier.previewofandroidn.alarm3");
        intent3.putExtra("REQ_CODE",1003);
        pIntent3 = PendingIntent.getBroadcast(context,1003,intent3,PendingIntent.FLAG_UPDATE_CURRENT);

        intent4 = new Intent("com.example.chedifier.previewofandroidn.alarm4");
        intent4.putExtra("REQ_CODE",1004);
        pIntent4 = PendingIntent.getBroadcast(context,1004,intent4,PendingIntent.FLAG_UPDATE_CURRENT);

        intent5 = new Intent("com.example.chedifier.previewofandroidn.alarm5");
        intent5.putExtra("REQ_CODE",1005);
        pIntent5 = PendingIntent.getBroadcast(context,1005,intent5,PendingIntent.FLAG_UPDATE_CURRENT);

        intent6 = new Intent("com.example.chedifier.previewofandroidn.alarm6");
        intent6.putExtra("REQ_CODE",1006);
        pIntent6 = PendingIntent.getBroadcast(context,1006,intent6,PendingIntent.FLAG_UPDATE_CURRENT);

        intent7 = new Intent("com.example.chedifier.previewofandroidn.alarm7");
        intent7.putExtra("REQ_CODE",1007);
        pIntent7 = PendingIntent.getBroadcast(context,1007,intent7,PendingIntent.FLAG_UPDATE_CURRENT);

        intent8 = new Intent("com.example.chedifier.previewofandroidn.alarm8");
        intent8.putExtra("REQ_CODE",1008);
        pIntent8 = PendingIntent.getBroadcast(context,1008,intent8,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void startAlarm(Context context){
        Log.d(TAG,"startAlarm");

//        if(sAlarmLooping){
//            Log.d(TAG,"alarm is already running.");
//            return;
//        }

        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                alarm.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 20000,pIntent1),pIntent2);
//                alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1 * 1000,pIntent3);
//                alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + 5 * 60000,pIntent4);
//                alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000,pIntent5);
        alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000,pIntent6);
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10000,pIntent7);
        Log.d(TAG,"System.currentTimeMillis(): " + System.currentTimeMillis() + "  SystemClock.elapsedRealtime(): " + SystemClock.elapsedRealtime());

        sAlarmLooping = true;
    }

    public void onReceiveAlarm(Context context,Intent intent){
        Log.d(TAG,"onReceiveAlarm: " + intent);

        if(!sAlarmLooping){
            Log.d(TAG,"alarm has required stop.");
            return;
        }


        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm1")){
            alarm.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 20000,pIntent1),pIntent2);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm2")){
            alarm.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 20000,pIntent1),pIntent2);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm3")){
            alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1 * 1000,pIntent3);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm4")){
            alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5 * 60000,pIntent4);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm5")){
            alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000,pIntent5);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm6")){
            alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000,pIntent6);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm7")){
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000,pIntent7);
        }else if(intent.getAction().equals("com.example.chedifier.previewofandroidn.alarm8")){

        }
    }

    public void stopAlarm(Context context){
        Log.d(TAG,"stopAlarm");

        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent1);
        alarm.cancel(pIntent2);
        alarm.cancel(pIntent3);
        alarm.cancel(pIntent4);
        alarm.cancel(pIntent5);
        alarm.cancel(pIntent6);
        alarm.cancel(pIntent7);
        alarm.cancel(pIntent8);


        sAlarmLooping = false;
    }

}
