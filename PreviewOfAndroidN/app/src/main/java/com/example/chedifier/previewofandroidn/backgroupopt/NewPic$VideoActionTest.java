package com.example.chedifier.previewofandroidn.backgroupopt;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Camera;
import android.net.ConnectivityManager;
import android.net.Uri;

/**
 * Created by chedifier on 2016/3/23.
 */
public class NewPic$VideoActionTest {

    public static NewPic$VideoReceiver sReceiver;

    public static boolean sHasRegister = false;

    public static void registerReceiver(Context ctx){

        if(sHasRegister){
            return;
        }

        if(sReceiver == null){
            sReceiver = new NewPic$VideoReceiver();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(android.hardware.Camera.ACTION_NEW_PICTURE);
        intentFilter.addAction(android.hardware.Camera.ACTION_NEW_VIDEO);

        ctx.registerReceiver(sReceiver,intentFilter);
        sHasRegister = true;
    }

    public static void unregisterReceiver(Context ctx){
        if(!sHasRegister){
            return;
        }

        if(sReceiver != null){
            ctx.unregisterReceiver(sReceiver);
            sReceiver = null;
            sHasRegister = false;
        }

    }

    public static void listenNewPic$Video(Context context){
        JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(TestJobSchedulerService.getJobId(),
                new ComponentName(context, TestJobSchedulerService.class));

        Uri uri = Uri.parse("content://media/");
        builder.addTriggerContentUri(
                new JobInfo.TriggerContentUri(uri,
                        JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS));
        js.schedule(builder.build());
    }

}
