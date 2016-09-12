package com.uc.persistentservicelib.daemon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 *
 * Created by chedifier on 2016/5/20.
 */
public class Daemon {

    private static final String TAG = Daemon.class.getSimpleName();

    private String mTargetService;

    private String mHostProcName;

    private String mTargetProcName;

    private String mDaemonProcName;

    private DaemonStrategy.DeathMonitor mDeathMonitor;

    private DaemonStrategy.PullUpExecutor mPullUpExecutor;

    private BufferedReader mBufferedReader;//release later to save time

    private DaemonStrategy.DeathMonitor.IDeathListener mDeathListener = new DaemonStrategy.DeathMonitor.IDeathListener() {
        @Override
        public void onDeath() {
            if(mPullUpExecutor != null){
                mPullUpExecutor.wakeup();
            }else{
                Log.d(TAG,"mPullUpExecutor is null");
            }
        }
    };

    /**
     *
     * @param targetServiceName 需要保活的service名称
     * @param hostProcName      主进程名称
     * @param targetProcName    需要保活的service所在进程名称
     * @param daemonProcName    守护进程名称
     */
    public Daemon(String targetServiceName,
                  String hostProcName,
                  String targetProcName,
                  String daemonProcName){
        mTargetService = targetServiceName;
        mHostProcName = hostProcName;
        mTargetProcName = targetProcName;
        mDaemonProcName = daemonProcName;
    }

    public void onAttachBaseContext(Context ctx,boolean startSvcImmediately){

        String procName = getProcessName();

        Log.d(TAG,"start>> " + procName);
        if(procName.contains(mDaemonProcName)){
            Log.d(TAG,"daemon proc");
            mDeathMonitor = DaemonStrategy.monitorTypeDecision(mDeathListener,false);
            mPullUpExecutor = DaemonStrategy.wakeTypeDecision(ctx,false);

        }else if(procName.contains(mTargetProcName)){
            Log.d(TAG,"host proc");
            mDeathMonitor = DaemonStrategy.monitorTypeDecision(mDeathListener,true);
            mPullUpExecutor = DaemonStrategy.wakeTypeDecision(ctx,true);
        }

        if(mDeathMonitor != null){
            mDeathMonitor.start(ctx,mTargetService);
        }else if(startSvcImmediately){

            ComponentName componentName = new ComponentName(ctx.getPackageName(), mTargetService);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            ctx.startService(intent);
        }

        releaseIO();
    }

    private String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            mBufferedReader = new BufferedReader(new FileReader(file));
            return mBufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * release reader IO
     */
    private void releaseIO(){
        if(mBufferedReader != null){
            try {
                mBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBufferedReader = null;
        }
    }


    static {
        System.loadLibrary("daemon_native");
    }

}
