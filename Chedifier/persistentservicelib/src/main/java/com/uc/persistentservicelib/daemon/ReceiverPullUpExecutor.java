package com.uc.persistentservicelib.daemon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 通过 receiver 拉活
 * Created by chedifier on 2016/5/21.
 */
public class ReceiverPullUpExecutor extends DaemonStrategy.PullUpExecutor {

    private IBinder mRemote;
    private Parcel mBroadcastData;

    public ReceiverPullUpExecutor(Context ctx, String rcvName){
        initAmsBinder();
        initBroadcastParcel(ctx,rcvName);
    }

    @Override
    public boolean wakeup() {
        try {
            if(mRemote == null || mBroadcastData == null){
                Log.e("Daemon", "REMOTE IS NULL or PARCEL IS NULL !!!");
                return false;
            }
            mRemote.transact(14, mBroadcastData, null, 0);//BROADCAST_INTENT_TRANSACTION = 0x00000001 + 13
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initAmsBinder(){
        Class<?> activityManagerNative;
        try {
            activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object amn = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Field mRemoteField = amn.getClass().getDeclaredField("mRemote");
            mRemoteField.setAccessible(true);
            mRemote = (IBinder) mRemoteField.get(amn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("Recycle")// when process dead, we should save time to restart and kill self, don`t take a waste of time to recycle
    private void initBroadcastParcel(Context context,String rcvName){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context.getPackageName(), rcvName);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);



        mBroadcastData = Parcel.obtain();
        mBroadcastData.writeInterfaceToken("android.app.IActivityManager");
        mBroadcastData.writeStrongBinder(null);
        intent.writeToParcel(mBroadcastData, 0);
        mBroadcastData.writeString(intent.resolveTypeIfNeeded(context.getContentResolver()));
        mBroadcastData.writeStrongBinder(null);
        mBroadcastData.writeInt(Activity.RESULT_OK);
        mBroadcastData.writeString(null);
        mBroadcastData.writeBundle(null);
        mBroadcastData.writeString(null);
        mBroadcastData.writeInt(-1);
        mBroadcastData.writeInt(0);
        mBroadcastData.writeInt(0);
        mBroadcastData.writeInt(0);
    }

}
