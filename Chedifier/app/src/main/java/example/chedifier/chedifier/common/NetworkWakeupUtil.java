package example.chedifier.chedifier.common;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : NetworkWakeupUtil
 * <p/>
 * Creation    : 2016/7/6
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/7/6, chengqianxing, Create the file
 * ****************************************************************************
 */
public class NetworkWakeupUtil {

    private static final String TAG = NetworkWakeupUtil.class.getSimpleName();

    private static Boolean sHasWakeUp = false;

    public static void wakeupNetwork(Context ctx){

        synchronized (sHasWakeUp){
            if(sHasWakeUp){
                return;
            }

            sHasWakeUp = true;
        }

        Log.d(TAG,"wakeupNetwork");

//        BlankActivity.startSelf(ctx);

        PowerManager powerManager = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
                TAG);
        wakeLock.acquire();

        BackgroundTaskMgr.getInstance().runTask(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"release wakeLock");
                if(wakeLock != null){
                    wakeLock.release();
                }

                synchronized (sHasWakeUp){
                    sHasWakeUp = false;
                }
            }
        },10000);

    }

}
