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

        mDownloadState = DONWNLOAD_STATE.DOWNLOADING;
        broadCastState(DONWNLOAD_STATE.DOWNLOADING,mProgress);

    }

    public synchronized void stop(){
        mDownloadState = DONWNLOAD_STATE.PAUSE;

        broadCastState(DONWNLOAD_STATE.PAUSE,mProgress);
    }

    public DONWNLOAD_STATE getDownloadState(){
        return mDownloadState;
    }

    @Override
    public void run() {
        while(mDownloadState==DONWNLOAD_STATE.DOWNLOADING){
            try {
                URL url = new URL(mUrl);
                URLConnection connection = url.openConnection();
                File destFile = new File(mFilePath);
                if(!destFile.exists()){
                    broadCastState(DONWNLOAD_STATE.FAIL,"dest file is not exist!");
                    return;
                }

                connection.setRequestProperty("RANGE","bytes=" + destFile.length() + "-");
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                if(destFile.length() >= fileLength){
                    broadCastState(DONWNLOAD_STATE.SUCC,100);
                    return;
                }

                // download the file
                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(mFilePath);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                long nextTimeToBroadCast = System.currentTimeMillis();
                while (mDownloadState==DONWNLOAD_STATE.DOWNLOADING &&
                        (count = input.read(data)) != -1) {
                    total += count;

                    long currentTime = System.currentTimeMillis();
                    if(nextTimeToBroadCast < currentTime){
                        mProgress = (int) (total * 100 / fileLength);
                        broadCastState(DONWNLOAD_STATE.DOWNLOADING,mProgress);

                        nextTimeToBroadCast = currentTime + 1000;
                    }


                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                if(mDownloadState == DONWNLOAD_STATE.DOWNLOADING){
                    if(destFile.length() >= fileLength){
                        broadCastState(DONWNLOAD_STATE.SUCC,100);
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

                mDownloadState = DONWNLOAD_STATE.FAIL;
                broadCastState(DONWNLOAD_STATE.FAIL,e.toString());

            }finally {

            }
        }
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
            return this.mUrl == ((DownloadTask) obj).mUrl
                    && this.mFileName == ((DownloadTask) obj).mFileName;
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
        return mFileName + "\n" + mUrl + "";
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
