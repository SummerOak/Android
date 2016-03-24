package com.example.chedifier.previewofandroidn.backgroupopt;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chedifier on 2016/3/23.
 */
public class TestJobSchedulerService extends JobService {

    private static final String TAG = TestJobSchedulerService.class.getSimpleName();

    private static int sJobId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand intent " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "on start job: " + params.getJobId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<20;i++){
                    Log.d(TAG,Thread.currentThread().getName() + "  doing something..." + i);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        NewPic$VideoActionTest.listenNewPic$Video(this);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "on stop job: " + params.getJobId());

        return true;
    }


    public static int getJobId(){
        return sJobId++;
    }
}
