package example.chedifier.chedifier.test;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import example.chedifier.chedifier.MainActivity;
import example.chedifier.chedifier.R;
import example.chedifier.chedifier.common.BackgroundTaskMgr;

/**
 * Created by chedifier on 2016/6/8.
 */
public class OpenWifiDetector {

    private static final String TAG = OpenWifiDetector.class.getSimpleName();

    private static OpenWifiDetector sInstance;


    private OpenWifiDetector(){
        ;
    }

    public static synchronized OpenWifiDetector getInstance(){
        if(sInstance == null){
            sInstance = new OpenWifiDetector();
        }

        return sInstance;
    }

    public void onWifiStateChanged(Context context,final String wifiName,boolean connected){

        Log.i(TAG,"onWifiStateChanged: " + wifiName);

        final Context appContext = context.getApplicationContext();

        if(!connected){
            cancelNotify(appContext);
        }else{
            BackgroundTaskMgr.getInstance().runTask(new OpenWifiChecker(new OpenWifiChecker.IResultCallback() {
                @Override
                public void onCheckResult(OpenWifiChecker.CHECK_RESULT result) {

                    Log.i(TAG, "check result: " + result);
                    Log.i(TAG, "isOnline: " + isOnline(appContext));

                    switch (result) {
                        case OPEN_WIFI:

                            notifyOpenWifi(appContext, wifiName);

                            break;

                        default:

                            cancelNotify(appContext);

                            break;


                    }
                }
            }));
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void notifyOpenWifi(Context ctx,String wifiName){

        Log.i(TAG,"notifyOpenWifi");

        String tickerText = "wifi 已连接";
        String contentTitle = tickerText;
        String contentText = "请登录: " + wifiName;

        PendingIntent contentIntent = PendingIntent.getActivity(
                ctx,
                0,
                new Intent(ctx,MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentTitle)
                .setTicker(tickerText)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);

    }

    private void cancelNotify(Context ctx){

        Log.i(TAG,"cancelNotify");

        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(1);
    }

    private boolean isOnline(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private static class OpenWifiChecker implements Runnable{

        private static final String mUrl = "http://down4.ucweb.com/generate_204";
        private static final int TIMEOUT = 20000;

        private IResultCallback mCallback;

        public OpenWifiChecker(IResultCallback callback){
            mCallback = callback;
        }


        @Override
        public void run() {

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(mUrl).openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);
                connection.setUseCaches(false);
                int responseCode = connection.getResponseCode();

                if(200 <= responseCode && responseCode < 400 && responseCode != 204){
                    callBack(CHECK_RESULT.OPEN_WIFI);
                }else {
                    callBack(CHECK_RESULT.NOT_OPEN_WIFI);
                }
            }
            catch (SocketTimeoutException e) {
                callBack(CHECK_RESULT.TIME_OUT);
            }
            catch (Exception exception) {
                callBack(CHECK_RESULT.EXCEPTION);
                exception.printStackTrace();
            }
            finally{
                if(connection != null){
                    Log.i(TAG, "disconnect");
                    connection.disconnect();
                }
            }

        }

        private void callBack(CHECK_RESULT result){
            if(mCallback != null){
                mCallback.onCheckResult(result);
            }
        }


        public interface IResultCallback{
            void onCheckResult(CHECK_RESULT result);
        }

        public enum CHECK_RESULT{
            OPEN_WIFI,
            NOT_OPEN_WIFI,
            TIME_OUT,
            EXCEPTION,
        }
    }

}
