package com.example.chedifier.previewofandroidn.backgroupopt;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;

import org.w3c.dom.Text;

/**
 * Created by chedifier on 2016/3/23.
 */
public class JobSchedulerTestActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.jobscheduleract_layout);

        ((TextView)findViewById(R.id.add_job_while_connectivity_changed)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_job_while_connectivity_changed:
                //设置一个任务，当网络有变化的时候触发


                JobScheduler jobScheduler =
                        (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

                JobInfo jbInfo = new JobInfo.Builder(TestJobSchedulerService.getJobId(),
                        new ComponentName(this,TestJobSchedulerService.class))
                        .setRequiresCharging(false)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();

                jobScheduler.schedule(jbInfo);
                break;
        }
    }
}
