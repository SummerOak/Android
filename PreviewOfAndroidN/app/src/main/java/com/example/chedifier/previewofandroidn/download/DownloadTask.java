package com.example.chedifier.previewofandroidn.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.chedifier.previewofandroidn.common.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chedifier on 2016/3/21.
 */
public class DownloadTask implements Runnable,Parcelable{

    private static final String TAG = DownloadTask.class.getSimpleName();

    private Handler mHandler;
    private List<IDownloadListener> mListeners = new ArrayList<>();


    private String mUrl;
    private String mFileName;
    private String mFilePath;
    private int mProgress;

    private int mMaxRetryTimes = 50;
    private int mRetryTimes = 0;

    private Boolean mThreadRunning = false;
    private DONWNLOAD_STATE mDownloadState = DONWNLOAD_STATE.INIT;

    protected DownloadTask(Parcel in) {
        mUrl = in.readString();
        mFileName = in.readString();
        mFilePath = in.readString();
        mProgress = in.readInt();
    }

    public static final Creator<DownloadTask> CREATOR = new Creator<DownloadTask>() {
        @Override
        public DownloadTask createFromParcel(Parcel in) {
            return new DownloadTask(in);
        }

        @Override
        public DownloadTask[] newArray(int size) {
            return new DownloadTask[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mUrl);
        parcel.writeString(mFileName);
        parcel.writeString(mFilePath);
        parcel.writeInt(mProgress);
    }

    public static enum DONWNLOAD_STATE{
        INIT,
        DOWNLOADING,
        FAIL,
        SUCC,
        PAUSE,
    }

    private DownloadTask(String saveName,String url){
        mUrl = url;
        mFileName = saveName;
        String dir = Environment.getExternalStorageDirectory() + "/chedifier/download/";
        mFilePath = dir + saveName;
        FileUtils.deleteFile(mFilePath);
        FileUtils.createFile(mFilePath);
        mProgress = 0;

        mHandler = new MyHandler(Looper.getMainLooper());
    }

    public static DownloadTask createDownloadTask(DownloadTaskMgr mgr,String saveName,String url){

        if(url == null || url.equals("")){
            return null;
        }


        if(saveName == null || saveName.equals("")){
            return null;
        }

        return new DownloadTask(saveName,url);

    }

    public synchronized void start(){

        if(mDownloadState == DONWNLOAD_STATE.DOWNLOADING){
            Log.d(TAG,"task is downloading.");
            return;
        }

        new Thread(this).start();

        mRetryTimes = 0;
        mDownloadState = DONWNLOAD_STATE.DOWNLOADING;
        broadCastState(DONWNLOAD_STATE.DOWNLOADING,mProgress);

    }

    public synchronized void retry(){

        if(mRetryTimes > mMaxRetryTimes){
            return;
        }

        if(mDownloadState == DONWNLOAD_STATE.DOWNLOADING){
            Log.d(TAG,"task is downloading.");
            return;
        }

        if(!mThreadRunning){
            new Thread(this).start();
        }

        mRetryTimes++;
        mDownloadState = DONWNLOAD_STATE.DOWNLOADING;
        broadCastState(DONWNLOAD_STATE.DOWNLOADING,mProgress);

    }

    public synchronized void stop(){
        mRetryTimes = 0;
        if(mDownloadState == DONWNLOAD_STATE.DOWNLOADING){
            mDownloadState = DONWNLOAD_STATE.PAUSE;
        }

        broadCastState(DONWNLOAD_STATE.PAUSE,mProgress);
    }

    public DONWNLOAD_STATE getDownloadState(){
        return mDownloadState;
    }


    private void lockThread(){
        synchronized (mThreadRunning){
            mThreadRunning = true;
        }

        Log.d(TAG,this + "run>>>>");
    }

    private void unLockThread(){
        synchronized (mThreadRunning){
            mThreadRunning = false;
        }

        Log.d(TAG,this + "stop||||");
    }

    @Override
    public void run() {

        lockThread();

        while(mDownloadState==DONWNLOAD_STATE.DOWNLOADING){
            InputStream input = null;
            OutputStream output = null;

            try {
                URL url = new URL(mUrl);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                File destFile = new File(mFilePath);
                if(!destFile.exists()){
                    unLockThread();
                    broadCastState(DONWNLOAD_STATE.FAIL,"dest file is not exist!");
                    return;
                }

                connection.setRequestProperty("RANGE","bytes=" + destFile.length() + "-");
                connection.connect();

                int fileLength = connection.getContentLength() + (int)destFile.length();
                Log.d(TAG,"fileLength = " + fileLength + "destFile.length(): " + destFile.length());
                if(destFile.length() >= fileLength){
                    unLockThread();
                    mProgress = 100;
                    mDownloadState = DONWNLOAD_STATE.SUCC;
                    broadCastState(DONWNLOAD_STATE.SUCC,mProgress);
                    return;
                }

                input = new BufferedInputStream(connection.getInputStream());
                output = new FileOutputStream(mFilePath,true);
                byte data[] = new byte[1024];
                long total = destFile.length();
                int count;
                long nextTimeToBroadCast = System.currentTimeMillis();
                while (mDownloadState==DONWNLOAD_STATE.DOWNLOADING &&
                        (count = input.read(data)) != -1) {
                    total += count;

                    long currentTime = System.currentTimeMillis();
                    if(nextTimeToBroadCast < currentTime){
                        mProgress = (int) (total * 100 / fileLength);
                        Log.d(TAG,"run progress: " + mProgress);
                        broadCastState(DONWNLOAD_STATE.DOWNLOADING,mProgress);

                        nextTimeToBroadCast = currentTime + 1000;
                    }


                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                output = null;
                input.close();
                input = null;

                if(mDownloadState == DONWNLOAD_STATE.DOWNLOADING){
                    if(destFile.length() >= fileLength){

                        unLockThread();
                        mProgress = 100;
                        mDownloadState = DONWNLOAD_STATE.SUCC;
                        broadCastState(DONWNLOAD_STATE.SUCC,100);
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

                unLockThread();
                mDownloadState = DONWNLOAD_STATE.FAIL;
                broadCastState(DONWNLOAD_STATE.FAIL,e.toString());

                return;
            }finally {
                if(output != null){
                    try {
                        output.flush();
                        output.close();
                        output = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(input != null){
                    try {
                        input.close();
                        input=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        unLockThread();
    }

    private boolean hasListener(IDownloadListener l){
        synchronized (mListeners){
            for(int i=0;i<mListeners.size();i++){
                if(mListeners.get(i) == l){
                    return true;
                }
            }
        }

        return false;
    }

    public void addListener(IDownloadListener l){
        synchronized (mListeners){
            if(!hasListener(l)){
                mListeners.add(l);
            }
        }
    }

    public void removeListener(IDownloadListener l){
        synchronized (mListeners){
            for(Iterator it = mListeners.iterator();it.hasNext();){
                if(it.next() == l){
                    it.remove();
                }
            }
        }
    }

    public int getProgress(){
        return mProgress;
    }

    public String getFileName(){
        return mFileName;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getFilePath(){
        return mFilePath;
    }

    private void broadCastState(DONWNLOAD_STATE state,Object data){

        synchronized (mListeners){
            for(int i=0;i<mListeners.size();i++){
                mHandler.obtainMessage(state.ordinal(),new MsgObj(mListeners.get(i),data))
                        .sendToTarget();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DownloadTask){
            return this.mUrl.equals (((DownloadTask) obj).mUrl)
                    && this.mFileName.equals(((DownloadTask) obj).mFileName);
        }

        return  false;
    }

    @Override
    public int hashCode() {
        return (mUrl!=null?mUrl.hashCode()*31:0)
                +(mFileName!=null?mFileName.hashCode():0);
    }

    @Override
    public String toString(){
        return mFileName + " " + "";
    }

    private class MyHandler extends Handler{


        public MyHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            switch (DONWNLOAD_STATE.values()[msg.what]){
                case INIT:



                    break;

                case DOWNLOADING:

                    ((MsgObj)msg.obj).l.onDownloadIng(DownloadTask.this,(Integer)(((MsgObj)msg.obj).o));

                    break;

                case FAIL:

                    ((MsgObj)msg.obj).l.onDownloadError(DownloadTask.this);

                    break;

                case SUCC:

                    ((MsgObj)msg.obj).l.onDownloadSucc(DownloadTask.this);

                    break;

                case PAUSE:

                    ((MsgObj)msg.obj).l.onDownloadPause(DownloadTask.this);

                    break;
            }
        }



    }

    private static class MsgObj{
        public MsgObj(IDownloadListener l,Object o){
            this.l = l;
            this.o = o;
        }

        public IDownloadListener l;
        public Object o;
    }

}
