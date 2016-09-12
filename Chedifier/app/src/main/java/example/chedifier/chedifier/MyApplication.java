package example.chedifier.chedifier;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Process;
import android.util.Log;

import example.chedifier.chedifier.common.SysConfigurationChangedHandler;
import example.chedifier.chedifier.test.ExternalFileTest;
import example.chedifier.chedifier.common.BackgroundTaskMgr;

/**
 * Created by chedifier on 2016/6/7.
 */
public class MyApplication extends Application {

    private final String TAG = "MyApplication";

    private static Context sAppContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

//        new Daemon(
//                "example.chedifier.chedifier.BackgroundService",
//                "example.chedifier.chedifier",
//                "example.chedifier.chedifier:svr",
//                "example.chedifier.chedifier:daemon").onAttachBaseContext(this,true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sAppContext = this;

        BackgroundTaskMgr.getInstance().init();

//        startService(new Intent(this, BackgroundService.class));

        if(ExternalFileTest.writeExternalFile(this,"external_test","test data")){
            Log.i("cqx", "write external succ.");
        }

        ExternalFileTest.readTest();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Configuration oldConfig = getResources().getConfiguration();
        Log.i(TAG, "this:" + this + " onConfigurationChanged: " + oldConfig.toString()
                + "  oldConfig.screenLayout: " + oldConfig.screenLayout
                + "  oldConfig.orientation: " + oldConfig.orientation
                + "  oldConfig.smallestScreenWidthDp"  + oldConfig.smallestScreenWidthDp);

        Log.i(TAG, "this:" + this + " onConfigurationChanged: " + newConfig.toString()
                + "  newConfig.screenLayout: " + newConfig.screenLayout
                + "  newConfig.orientation: " + newConfig.orientation
                + "  newConfig.smallestScreenWidthDp" + newConfig.smallestScreenWidthDp);

        Log.i(TAG, "onConfigurationChanged ");

        SysConfigurationChangedHandler.getInstance().handleConfigurationChange(newConfig);
    }


    public static void stopProcess(){

        android.os.Process.killProcess(Process.myPid());

    }

    public static Context getAppContext(){
        return sAppContext;
    }

}
