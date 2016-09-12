package com.uc.persistentservicelib.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by chedifier on 2016/5/23.
 */
public class DaemonService extends Service {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
//    public DaemonService() {
//        super("keepAliveSvc");
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//    }
}
