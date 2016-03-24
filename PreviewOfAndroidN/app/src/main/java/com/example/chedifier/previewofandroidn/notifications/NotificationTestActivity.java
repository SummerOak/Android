package com.example.chedifier.previewofandroidn.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;

/**
 * Created by chedifier on 2016/3/18.
 */
public class NotificationTestActivity extends BaseActivity implements View.OnClickListener{



    private Button mBtnService;

    private BroadcastReceiver mServiceMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                int what = intent.getIntExtra(AutoNotifyService.ACTION_KEY,0);
                switch (what){
                    case AutoNotifyService.ACTION_ON_CREATE:
                    case AutoNotifyService.ACTION_ON_START_COMMAND:
                    case AutoNotifyService.ACTION_ON_DESTROY:
                        updateServiceBtn();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_test_activity);

        mBtnService = (Button)findViewById(R.id.service);
        mBtnService.setOnClickListener(this);

        updateServiceBtn();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");

        registerReceiver(mServiceMsgReceiver,new IntentFilter(AutoNotifyService.BROADCAST_RECEIVER_ACTION));

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");

        unregisterReceiver(mServiceMsgReceiver);

        super.onStop();
    }

    private void updateServiceBtn(){
        if(mBtnService != null){
            if(ServiceHelper.isServiceRunning(this, AutoNotifyService.class)){
                mBtnService.setText("停止service");
            }else{
                mBtnService.setText("启动service");
            }


        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.service:

                if(ServiceHelper.isServiceRunning(this, AutoNotifyService.class)){
                    Intent it = new Intent(this,AutoNotifyService.class);
                    stopService(it);
                }else{
                    Intent it = new Intent(this,AutoNotifyService.class);
                    startService(it);
                }

                break;
        }
    }
}
