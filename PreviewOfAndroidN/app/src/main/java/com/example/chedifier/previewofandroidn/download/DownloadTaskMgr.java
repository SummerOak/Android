package com.example.chedifier.previewofandroidn.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chedifier on 2016/3/21.
 */
public class DownloadTaskMgr {
    private static final String TAG = DownloadTaskMgr.class.getSimpleName();


    private Handler mHandler;
    private List<IDownloadMgrListener> mListeners = new ArrayList<>();
    private Map<String,DownloadTask> mTasks = new HashMap<>();

    private Integer mDownloadingTaskNum = 0;

    private boolean mInited = false;


    private Messenger mService;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = new Messenger(service);

            mInited = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;

            mInited = false;
        }
    };

    private static DownloadTaskMgr sInstance;

    private DownloadTaskMgr(){
        mHandler = new MyHandler(Looper.getMainLooper());
    }

    public synchronized static DownloadTaskMgr getInstance(){
        if(sInstance == null){
            sInstance = new DownloadTaskMgr();
        }

        return  sInstance;
    }

    public void init(Context ctx){
        if(mInited){
            Log.d(TAG,"module has inited.");

            return;
        }

//        ctx.bindService(new Intent(ctx, DownloadService.class), mConnection,
//                Context.BIND_AUTO_CREATE);
    }

    private boolean hasListener(IDownloadMgrListener l){
        synchronized (mListeners){
            for(int i=0;i<mListeners.size();i++){
                if(mListeners.get(i) == l){
                    return true;
                }
            }
        }

        return false;
    }

    public void addListener(IDownloadMgrListener l){
        synchronized (mListeners){
            if(!hasListener(l)){
                mListeners.add(l);
            }
        }
    }

    public void removeListener(IDownloadMgrListener l ){
        synchronized (mListeners){
            mListeners.remove(l);
        }
    }

    public DownloadTask addDownloadTask(String saveName,String url){

        Log.d(TAG,"addDownloadTask " + saveName);

//        if(mService == null){
//            Log.d(TAG,"addDownloadTask Failed service is not started!");
//
//            return null;
//        }

        if(!hasTask(saveName,url)){
            DownloadTask task = DownloadTask.createDownloadTask(this,saveName,url);
            if(task != null){
                mTasks.put(createTaskKey(saveName,url),task);
//                Message msg = Message.obtain(null,0,task);
//                try {
//                    mService.send(msg);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }

                broadCastMsg(0,task);

                return task;
            }
        }

        return getDownloadTask(saveName,url);
    }

    public void stopAllTask(){
        synchronized (mTasks){
            List<DownloadTask> tasks = getDownloadTasks();
            for(DownloadTask task:tasks){
                task.stop();
            }
        }
    }

    public void deleteAllTask(){
        synchronized (mTasks){
            List<DownloadTask> tasks = getDownloadTasks();
            for(DownloadTask task:tasks){
                deleteTask(task);
            }
            mTasks.clear();
        }
    }

    public void deleteTask(String fileName,String url){
        String key = createTaskKey(fileName,url);
        deleteTask(mTasks.get(key));
    }

    public void deleteTask(DownloadTask task){
        if(task != null){
            task.stop();

            new File(task.getFilePath()).delete();

            synchronized (mTasks){
                mTasks.remove(createTaskKey(task.getFileName(),task.getUrl()));
            }

            broadCastMsg(1,task);
        }
    }

    public List<DownloadTask> getSortedDownloadTasks(){
        List<DownloadTask> ret = getDownloadTasks();

        Collections.sort(ret, new Comparator<DownloadTask>() {
            @Override
            public int compare(DownloadTask task, DownloadTask t1) {
                return task.getFileName().compareTo(t1.getFileName());
            }
        });

        return ret;
    }

    public List<DownloadTask> getDownloadTasks(){
        List<DownloadTask> ret = new ArrayList<>();
        ret.addAll(mTasks.values());

        return ret;
    }

    public DownloadTask getDownloadTask(String saveName,String url){
        synchronized (mTasks) {
            return mTasks.get(createTaskKey(saveName, url));
        }
    }

    public boolean hasTask(String saveName,String url){
        synchronized (mTasks) {
            return mTasks.containsKey(createTaskKey(saveName, url));
        }
    }


    public void release(Context ctx){
//        ctx.unbindService(mConnection);
    }

    public static String createTaskKey(String saveName,String url){
        return (saveName==null?"":saveName)+"|"+(url==null?"":url);
    }


    private void broadCastMsg(int msg,DownloadTask task){

        synchronized (mListeners){
            for(int i=0;i<mListeners.size();i++){
                mHandler.obtainMessage(msg,new MsgObj(mListeners.get(i),task))
                        .sendToTarget();
            }
        }
    }

    private class MyHandler extends Handler{


        public MyHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:

                    ((MsgObj)msg.obj).l.onTaskDelete((DownloadTask)(((MsgObj)msg.obj).o));

                    break;

                case 1:

                    ((MsgObj)msg.obj).l.onTaskAdd((DownloadTask)(((MsgObj)msg.obj).o));

                    break;
            }
        }
    }

    private static class MsgObj{
        public MsgObj(IDownloadMgrListener l,DownloadTask o){
            this.l = l;
            this.o = o;
        }

        public IDownloadMgrListener l;
        public DownloadTask o;
    }
}
