package com.example.chedifier.previewofandroidn.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chedifier.previewofandroidn.aidl.ICallback;
import com.example.chedifier.previewofandroidn.aidl.IPCBean;
import com.example.chedifier.previewofandroidn.aidl.IRemoteService;
import com.example.chedifier.previewofandroidn.backgroupopt.MyAlarmManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/28.
 */
public class BackgroundService extends Service {
    private static final String TAG = BackgroundService.class.getSimpleName();

    private Handler mHandler = new MyHandler();
    public static final int ACTION_WAKE_LOCK = 1;       //每隔一段时间使用wake lock 唤醒屏幕


    private List<ICallback> mCallbacks = new ArrayList<>();


    private boolean mWakeLockRunning = false;
    private long mWakeLockT;

    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate ");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand intent = " + intent + " flags: " + flags
                + "  startId: " + startId);

        int rls = super.onStartCommand(intent,flags,startId);

        return rls;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRemote;
    }

    @Override
    public void onDestroy(){
        Log.d(TAG,"onDestroy");

        mRemote = null;

        mHandler.removeMessages(ACTION_WAKE_LOCK);

        synchronized (mCallbacks){
            mCallbacks.clear();
        }

        super.onDestroy();
    }

    private boolean addCallback(ICallback callback){
        synchronized (mCallbacks){
            if(!hasCallback(callback)){
                return mCallbacks.add(callback);
            }
        }

        return false;
    }

    private boolean hasCallback(ICallback callback){
        synchronized (mCallbacks){
            return mCallbacks.contains(callback);
        }
    }

    private void broadCastEvent(final int code, final IPCBean data){
        Log.d(TAG,"broadCastEvent " + code);

        synchronized (mCallbacks){
            for(final ICallback callback:mCallbacks){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.onEvent(code,data);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    private IRemoteService.Stub mRemote = new IRemoteService.Stub() {

        @Override
        public boolean addRemoteCallback(ICallback callback) throws RemoteException {
            return addCallback(callback);
        }

        @Override
        public void startWakeLockLoop(long t) throws RemoteException {
            Log.d(TAG,"startWakeLockLoop t " + t);

            mWakeLockT = t;

            mHandler.removeMessages(ACTION_WAKE_LOCK);
            mHandler.sendEmptyMessageDelayed(ACTION_WAKE_LOCK,mWakeLockT);
            mWakeLockRunning = true;
        }

        @Override
        public void stopWakeLockLoop() throws RemoteException {
            Log.d(TAG,"stopWakeLockLoop");

            mHandler.removeMessages(ACTION_WAKE_LOCK);
            releaseWakeLock();

            mWakeLockRunning = false;
        }

        @Override
        public boolean wakeLockLooping() throws RemoteException {
            return mWakeLockRunning;
        }

        @Override
        public void countingSheep() throws RemoteException {
            countSheep();
        }

        @Override
        public void stopCountSheep() throws RemoteException {
            BackgroundService.this.stopCountSheep();
        }

        @Override
        public boolean isCountingSheep() throws RemoteException {
            return isCountSheep();
        }

        @Override
        public int getSheepCount() throws RemoteException {
            return mSheep;
        }

    };

    private PowerManager.WakeLock mWakeLock;
    private void wakeScreenOn(){
        Log.d(TAG,"wakeScreenOn");

        if(mWakeLock == null){
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK
                    |PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "MyWakelockTag");

        }

        if(!mWakeLock.isHeld()){
            mWakeLock.acquire();
            mWakeLock.release();
        }
    }

    private void releaseWakeLock(){
        Log.d(TAG,"releaseWakeLock");
        if(mWakeLock != null && mWakeLock.isHeld()){
            mWakeLock.release();
        }
    }

    private boolean mCountSheep = false;
    private Boolean mCountThreadRunning = false;
    private int mSheep = 0;
    private void countSheep(){
        Log.d(TAG,"countSheep");

        if(!mCountSheep){
            mCountSheep = true;

            if(!mCountThreadRunning){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (mCountThreadRunning){
                            mCountThreadRunning = true;
                        }

                        while(mCountSheep){

                            IPCBean bean = new IPCBean();
                            bean.sheepNumber = ++mSheep;
                            Log.d(TAG,"countSheep run : " + mSheep);
                            broadCastEvent(RemoteCallbackCode.EVENT_COUNT_SHEEP,bean);

                            onCountSheep(mSheep);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        mCountSheep = false;

                        synchronized (mCountThreadRunning){
                            mCountThreadRunning = false;
                        }
                    }
                }).start();
            }
        }

    }

    private void stopCountSheep(){
        mCountSheep = false;
    }

    private boolean isCountSheep(){
        return mCountSheep;
    }

    class MyHandler extends Handler{

        public MyHandler(){
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){

                case ACTION_WAKE_LOCK:
                    wakeScreenOn();
                    mHandler.sendEmptyMessageDelayed(ACTION_WAKE_LOCK,mWakeLockT);
                    break;

                default:

                    break;
            }
        }
    }

    private void onCountSheep(int sheepCount){
//        if(sheepCount%20 == 0){
//            new MyAlarmManager(this).startAlarm(this);
//        }

//        if(sheepCount %30 == 0){
//            wakeScreenOn();
//        }
//
//        if(sheepCount%35 ==0){
//            new MyAlarmManager(this).startAlarm(this);
//        }
    }
}
