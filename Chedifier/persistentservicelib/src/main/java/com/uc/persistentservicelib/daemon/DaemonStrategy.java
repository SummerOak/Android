package com.uc.persistentservicelib.daemon;

import android.content.Context;

/**
 * Created by chedifier on 2016/5/21.
 */
public final class DaemonStrategy {

    /**
     * 死亡监听决策
     * @return
     */
    public static DeathMonitor monitorTypeDecision(DeathMonitor.IDeathListener l,boolean targetProcess){

        return new DeadFileLockMonitor2(l,targetProcess);

    }


    /**
     * 拉活方式决策
     * @return
     */
    public static PullUpExecutor wakeTypeDecision(Context ctx,boolean hostProc){

        if(hostProc){

            return new ReceiverPullUpExecutor(ctx,DaemonBroadCastReceiver.class.getName());
        }else{
            return new ReceiverPullUpExecutor(ctx,HostBroadCastReceiver.class.getName());
        }
    }

    /**
     * 死亡监听器
     *
     * Created by chedifier on 2016/5/20.
     */
    public static abstract class DeathMonitor {

        private IDeathListener mListener;

        /**
         * 标记是否运行在TargetService进程
         */
        protected boolean mRunInTargetProc;

        public DeathMonitor(IDeathListener listener,boolean runInTargetProc){
            mListener = listener;
            mRunInTargetProc = runInTargetProc;
        }

        public abstract void start(Context ctx,String serviceName);

        public void onDeathNative(){
            if(mListener != null){
                mListener.onDeath();
            }
        }

        public static interface IDeathListener{
            public void onDeath();
        }

    }

    /**
     * 拉活接口
     */
    public static abstract class PullUpExecutor {
        public abstract boolean wakeup();
    }
}
