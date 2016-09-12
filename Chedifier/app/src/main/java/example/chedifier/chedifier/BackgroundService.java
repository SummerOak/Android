package example.chedifier.chedifier;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import example.chedifier.chedifier.common.BackgroundTaskMgr;
import example.chedifier.chedifier.test.NetworkConnectReceiver;
import example.chedifier.chedifier.test.OpenWifiDetector;
import example.chedifier.chedifier.utils.BatteryUtils;
import example.chedifier.chedifier.utils.FileUtils;
import example.chedifier.chedifier.common.NetworkWakeupUtil;
import example.chedifier.chedifier.utils.StringUtils;

/**
 * Created by chedifier on 2016/6/7.
 */
public class BackgroundService extends Service {

    private static NetworkConnectReceiver sNetworkConnectReceiver;

    public static final String ACTION_NET_ACCESS_LOOP = "ACTION_NET_ACCESS_LOOP";

    public static final String ACTION_CHECK_OPEN_WIFI = "ACTION_CHECK_OPEN_WIFI";
    public static final String EXTRA_WIFI_SSID = "EXTRA_WIFI_SSID";
    public static final String EXTRA_WIFI_STATE = "EXTRA_WIFI_STATE";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        startForeground();


//        registerOpenWifiReceivier();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startForeground(){
        Log.i("BackgroundService","startForeground");
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Title")
                .setContentInfo("Info")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(10,notification);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if(intent != null && intent.getAction() != null){
            switch (intent.getAction()){
                case ACTION_NET_ACCESS_LOOP:

                    startNetAccessLoop(10000);

                    break;

                case ACTION_CHECK_OPEN_WIFI:

                    String wifiSSID = intent.getStringExtra(EXTRA_WIFI_SSID);
                    boolean wifiState = intent.getBooleanExtra(EXTRA_WIFI_STATE,false);
                    OpenWifiDetector.getInstance().onWifiStateChanged(this,wifiSSID,wifiState);


                    break;
            }
        }

    }

    private Boolean mRunningNetAccessLoop = false;
    private void startNetAccessLoop(final int t){

        synchronized (mRunningNetAccessLoop){
            if(mRunningNetAccessLoop){
                return;
            }

            mRunningNetAccessLoop = true;
        }

        BackgroundTaskMgr.getInstance().runTask(new Runnable() {
            @Override
            public void run() {

                File logFile = new File("/sdcard/chedifier/sy.log");
                if(logFile.exists()&&logFile.isFile()){
                    logFile.delete();
                }

                int tt = 0;
                int fail_cnt = 0;
                while(tt++ < t){

                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) new URL("http://down4.ucweb.com/wifi/zh/wifi.html").openConnection();
                        connection.setInstanceFollowRedirects(false);
                        connection.setConnectTimeout(30000);
                        connection.setReadTimeout(30000);
                        connection.setUseCaches(false);
                        int responseCode = connection.getResponseCode();

                        String log = tt + "/" + t + "responseCode: " + responseCode;
                        Log.i("net_acc_loop", log);
                        FileUtils.writeTextFile(logFile,new String[]{
                                StringUtils.getCurrentDate() + " chargeType:"
                                        + BatteryUtils.getCurrentChargeState(BackgroundService.this)
                                        + " " + log},true);
                    }
                    catch (SocketTimeoutException e) {
                        String log = tt+"/"+t+"time out!";
                        Log.e("net_acc_loop", tt+"/"+t+"time out!");
                        try {
                            FileUtils.writeTextFile(logFile, new String[]{
                                    StringUtils.getCurrentDate() + " chargeType:"
                                            + BatteryUtils.getCurrentChargeState(BackgroundService.this)
                                            + " " + log}, true);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                    catch (Exception exception) {
                        String log = tt+"/"+t+"exception!";
                        Log.e("net_acc_loop",tt+"/"+t+"exception!");
                        try {
                            FileUtils.writeTextFile(logFile, new String[]{
                                    StringUtils.getCurrentDate() + " chargeType:"
                                            + BatteryUtils.getCurrentChargeState(BackgroundService.this)
                                            + " " + log}, true);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        exception.printStackTrace();

                        fail_cnt++;

                        if(fail_cnt > 5){
                            NetworkWakeupUtil.wakeupNetwork(BackgroundService.this);
                            fail_cnt = 0;
                        }
                    }
                    finally{
                        if(connection != null){
                            connection.disconnect();
                        }
                    }


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                synchronized (mRunningNetAccessLoop){
                    mRunningNetAccessLoop = false;
                }
            }
        });

    }

    private void registerOpenWifiReceivier(){
        if(sNetworkConnectReceiver == null){
            sNetworkConnectReceiver = new NetworkConnectReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            getApplicationContext().registerReceiver(sNetworkConnectReceiver,intentFilter);
        }
    }


}
