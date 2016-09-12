package com.uc.persistentservicelib.daemon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.File;

/**
 * use process define in andoridmanifest.xml
 *
 * Created by chedifier on 2016/5/20.
 */
public class DeadFileLockMonitor extends DaemonStrategy.DeathMonitor {

    private static final String TAG = DeadFileLockMonitor.class.getSimpleName();

    private final String LOCK_DIR = "daemon";
    private final String HOST_LOCK_FILE = "host_l";
    private final String DAEMON_LOCK_FILE = "daemon_l";
    private final String HOST_TEMP_FILE = "host_t";
    private final String DAEMON_TEMP_FILE = "daemon_t";

    public DeadFileLockMonitor(IDeathListener listener,boolean runInTargetProc) {
        super(listener,runInTargetProc);
    }

    @Override
    public void start(Context ctx, final String serviceName) {

        if(mRunInTargetProc){
            ctx.startService(new Intent(ctx,DaemonService.class));

            final File indicatorDir = ctx.getDir(LOCK_DIR, Context.MODE_PRIVATE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startNative(
                            new File(indicatorDir, HOST_LOCK_FILE).getAbsolutePath(),
                            new File(indicatorDir, DAEMON_LOCK_FILE).getAbsolutePath(),
                            new File(indicatorDir, HOST_TEMP_FILE).getAbsolutePath(),
                            new File(indicatorDir, DAEMON_TEMP_FILE).getAbsolutePath());
                }
            }).start();
        }else{

            ComponentName componentName = new ComponentName(ctx.getPackageName(), serviceName);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            ctx.startService(intent);

            final File indicatorDir = ctx.getDir(LOCK_DIR, Context.MODE_PRIVATE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    startNative(
                            new File(indicatorDir, DAEMON_LOCK_FILE).getAbsolutePath(),
                            new File(indicatorDir, HOST_LOCK_FILE).getAbsolutePath(),
                            new File(indicatorDir, DAEMON_TEMP_FILE).getAbsolutePath(),
                            new File(indicatorDir, HOST_TEMP_FILE).getAbsolutePath());
                }
            }).start();
        }

    }

    protected native void startNative(String hostL,String daemonL,
                                      String hostT,String daemonT);
}
