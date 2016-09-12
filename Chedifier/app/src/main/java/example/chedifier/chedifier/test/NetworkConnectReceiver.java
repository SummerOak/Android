package example.chedifier.chedifier.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import example.chedifier.chedifier.BackgroundService;

/**
 * Created by chedifier on 2016/6/7.
 */
public class NetworkConnectReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkConnectReceiver.class.getSimpleName();

    private static String sCurrentWifiId = "";
    private static boolean sAuthCompleted = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null){
            return;
        }


        if(intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if(state == SupplicantState.COMPLETED){
                sAuthCompleted = true;
            }
        }else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);

            if(networkInfo != null){

                if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    if(sCurrentWifiId != null && !"".equals(sCurrentWifiId)){
                        Log.i(TAG, "<<<<<wifi disconnected " + sCurrentWifiId);

//                        OpenWifiDetector.getInstance().onWifiStateChanged(context,sCurrentWifiId,false);
                        Intent it = new Intent(context,BackgroundService.class);
                        it.setAction(BackgroundService.ACTION_CHECK_OPEN_WIFI);
                        it.putExtra(BackgroundService.EXTRA_WIFI_SSID, sCurrentWifiId);
                        it.putExtra(BackgroundService.EXTRA_WIFI_STATE,false);
                        context.startService(it);

                        sCurrentWifiId = "";
                        sAuthCompleted = false;
                    }
                }else if(sAuthCompleted
                        && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED
                        && wifiInfo != null && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED){

                    //wifi 已连接上

                    String wifiId = generateWifiID(wifiInfo);
                    if(wifiId == null
                            || wifiId.equals("")
                            || wifiId.equals(sCurrentWifiId)){

                        sAuthCompleted = false;
                        return; // 对于同个wifi 的多次连接，忽略
                    }

                    sCurrentWifiId = wifiId;
                    Log.i(TAG, "wifi connected>>>>>>>>>>>> " + sCurrentWifiId);

//                    OpenWifiDetector.getInstance().onWifiStateChanged(context,wifiInfo.getSSID(),true);

                    Intent it = new Intent(context,BackgroundService.class);
                    it.setAction(BackgroundService.ACTION_CHECK_OPEN_WIFI);
                    it.putExtra(BackgroundService.EXTRA_WIFI_SSID, sCurrentWifiId);
                    it.putExtra(BackgroundService.EXTRA_WIFI_STATE,true);
                    context.startService(it);
                }
            }
        }

    }

    private String generateWifiID(WifiInfo wifiInfo){
        if(wifiInfo == null){
            return "";
        }

        return  wifiInfo.getSSID() + "_"
                + wifiInfo.getNetworkId() + "_"
                + wifiInfo.getMacAddress() + "_"
                + wifiInfo.getIpAddress();
    }

}
