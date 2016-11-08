package example.chedifier.hook.ptrace;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by wxj_pc on 2016/10/2.
 */
public final class PTraceService extends Service {

    private static final String TAG = "PTraceService";

    private static final String EXT_TRACE_PID = "EXT_TRACE_PID";

    private static Object mLockForTracing = new Object();
    private static boolean sTracing = false;

    private int mTracingPid = -1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand ");
        onStart(intent,flags);

        if(isTracing()){
            Log.d(TAG,"tracing " + mTracingPid);
            return START_STICKY;
        }

        if(intent != null){
            final int pid = intent.getIntExtra(EXT_TRACE_PID,-1);

            if(pid > 0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int code = PTrace.pTrace(pid);
                        Log.d(TAG,"trace ret: " + code);

                        if(code == 0){
                            mTracingPid = pid;
                            setTracing(true);
                        }
                    }
                }).start();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy ");
        PTrace.pStop(mTracingPid);
        setTracing(false);
        super.onDestroy();
    }

    public static void setTracing(boolean val){
        synchronized (mLockForTracing){
            sTracing = val;
        }
    }

    public static boolean isTracing(){
        synchronized (mLockForTracing){
            return sTracing;
        }
    }

    public static void startPTrace(Context context,int pid){
        if(context != null){
            try {
                context.startService(new Intent(context, PTraceService.class)
                        .putExtra(EXT_TRACE_PID,pid));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void stopPTrace(Context context){
        if(context != null){
            try {
                context.stopService(new Intent(context,PTraceService.class));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
