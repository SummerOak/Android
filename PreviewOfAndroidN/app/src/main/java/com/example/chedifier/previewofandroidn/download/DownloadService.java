package com.example.chedifier.previewofandroidn.download;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chedifier on 2016/3/21.
 */
public class DownloadService extends Service implements IDownloadListener{

    private static final String TAG = DownloadService.class.getSimpleName();



    private int mFileIndex = 10000;
    private String mUrl = "http://p.gdown.baidu.com/560d7b246a5f123156ad9db1f4d2b7c55ea8d4def6a04452141cf0cf4368df619fd1de5078c52374d6b145d51f7fd1394fcb1568fb332118c0b23a6af7eb61ca05372b4a5ed2691527e292bf973bc32edc2699f2981cc41c6fcd56e25427af1562de7ad53afadf42";

    private Handler mHandler = new MyHandler(this);
    private final Messenger mMessenger = new Messenger(mHandler);

    private List<DownloadTask> mTasks = new ArrayList<>();

    @Override
    public void onCreate(){
        Log.d(TAG,"onCreate");

        mHandler.sendEmptyMessageDelayed(0,3000);
    }


    public void addDownloadTask(){


        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if(!hasPermission){
            Log.d(TAG,"sdcard permission denied!");
        }else{
            DownloadTask task = DownloadTaskMgr.getInstance().addDownloadTask("手动添加测试任务" + (++mFileIndex) + ".test",mUrl);

            if(task != null){
                task.addListener(this);
                task.start();

                mTasks.add(task);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind: " + intent);

        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        for(DownloadTask task:mTasks){
            task.removeListener(this);
        }

    }

    @Override
    public void onDownloadSucc(DownloadTask task) {

        Log.d(TAG,"onDownloadSucc task: " + task);

        addDownloadTask();
    }

    @Override
    public void onDownloadIng(DownloadTask task, int progress) {
        Log.d(TAG,"onDownloadIng task: " + task + "  progress: " + progress);
    }

    @Override
    public void onDownloadPause(DownloadTask task) {
        Log.d(TAG,"onDownloadPause task: " + task);

    }

    @Override
    public void onDownloadError(DownloadTask task) {
        Log.d(TAG,"onDownloadError task: " + task);
        task.retry();
    }


    private class MyHandler extends Handler{

        private WeakReference<DownloadService> mRefDownloadService;

        public MyHandler(DownloadService service){
            super(Looper.getMainLooper());

            mRefDownloadService = new WeakReference<DownloadService>(service);


        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){

                default:
                addDownloadTask();

            }
        }

    }
}
