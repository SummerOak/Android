package com.example.chedifier.previewofandroidn.backgroupopt;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;
import com.example.chedifier.previewofandroidn.aidl.ICallback;
import com.example.chedifier.previewofandroidn.aidl.IPCBean;
import com.example.chedifier.previewofandroidn.aidl.IRemoteService;
import com.example.chedifier.previewofandroidn.common.MyPermissionManager;
import com.example.chedifier.previewofandroidn.datasaver.DownloadTestActivity;
import com.example.chedifier.previewofandroidn.remote.RemoteCallbackCode;

/**
 * Created by chedifier on 2016/3/28.
 */
public class DozeTestActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTxtWakeLock;
    private TextView mTxtSheep;
    private TextView mTxtAlarm;


    private IRemoteService mRemote;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemote = IRemoteService.Stub.asInterface(service);

            if (mRemote != null) {
                Log.d(TAG, "onServiceConnected remote service bind succ!");

                try {
                    mRemote.addRemoteCallback(mRemoteCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                updateWakeLock();
                updateSheep();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected ");

            mRemote = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doze_test_layout);

        ((TextView)findViewById(R.id.network_test)).setOnClickListener(this);
        ((TextView)findViewById(R.id.guide_doze_whitelist)).setOnClickListener(this);

        mTxtWakeLock = ((TextView)findViewById(R.id.wake_lock_test));
        mTxtWakeLock.setOnClickListener(this);

        mTxtSheep = ((TextView)findViewById(R.id.count_sheep));
        mTxtSheep.setOnClickListener(this);

        mTxtAlarm = ((TextView)findViewById(R.id.set_alarm));
        mTxtAlarm.setOnClickListener(this);

        updateWakeLock();
        updateSheep();
        updateAlarm();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        if(mRemote == null){
            bindRemoteService();
        }

        updateWakeLock();
        updateSheep();
        updateAlarm();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        if(mConnection != null){
            unbindService(mConnection);
            mConnection = null;
        }

        mRemoteCallback = null;

        super.onDestroy();
    }

    private void bindRemoteService(){
        Intent it = new Intent("com.example.chedifier.previewofandroidn.remote");
        it.setPackage("com.example.chedifier.previewofandroidn");

        if(bindService(it, mConnection, Context.BIND_AUTO_CREATE)){
            Log.d(TAG,"bindService true");
        }else{
            Log.d(TAG,"bindService false");
        }
    }


    private void updateWakeLock(){
        if(mRemote != null){
            mTxtWakeLock.setEnabled(true);
            try {
                mTxtWakeLock.setText(mRemote.wakeLockLooping()?"停止WakeUp":"开始WakeUp");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            mTxtWakeLock.setEnabled(false);
        }
    }

    private void updateSheep(){
        if(mTxtSheep != null && mRemote != null){
            try{
                mTxtSheep.setText("" + mRemote.getSheepCount());
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    private void updateAlarm(){
        if(mTxtAlarm != null){
            mTxtAlarm.setText(MyAlarmManager.sAlarmLooping?"停止闹钟":"开始闹钟");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.network_test:
                startActivity(new Intent(this,DownloadTestActivity.class));
                break;
            case R.id.wake_lock_test:
                if(mRemote != null){
                    try{
                        if(mRemote.wakeLockLooping()){
                            mRemote.stopWakeLockLoop();
                        }else{
                            mRemote.startWakeLockLoop(30000);
                        }

                        updateWakeLock();
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }

                }else{
                    Log.d(TAG,"remote service is not connected! try to connect now.");
                    bindRemoteService();
                }

                break;
            case R.id.count_sheep:
                if(mRemote != null){
                    try{
                        if(mRemote.isCountingSheep()){
                            mRemote.stopCountSheep();
                        }else{
                            mRemote.countingSheep();
                        }

                        updateSheep();
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }

                }else{
                    Log.d(TAG,"remote service is not connected! try to connect now.");
                    bindRemoteService();
                }

                break;

            case R.id.set_alarm:

                if(MyAlarmManager.sAlarmLooping){
                    new MyAlarmManager(this).stopAlarm(this);
                }else{
                    new MyAlarmManager(this).startAlarm(this);
                }
                updateAlarm();

                break;

            case R.id.guide_doze_whitelist:

                PowerManager pw = (PowerManager)getSystemService(Context.POWER_SERVICE);
                if(pw.isIgnoringBatteryOptimizations(getPackageName())){
                    Log.d(TAG,"already in whitelist.");
                }else{
                    if(MyPermissionManager.checkSdcardPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)){
                        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        startActivity(intent);
                    }
                }

                break;
        }
    }

    private ICallback.Stub mRemoteCallback = new ICallback.Stub(){

        @Override
        public void onEvent(int code, IPCBean data) throws RemoteException {
            Log.d(TAG,"onEvent " + code + " data: " + data);

            switch (code){
                case RemoteCallbackCode.EVENT_COUNT_SHEEP:
                    updateSheep();
                    break;
            }
        }
    };
}
