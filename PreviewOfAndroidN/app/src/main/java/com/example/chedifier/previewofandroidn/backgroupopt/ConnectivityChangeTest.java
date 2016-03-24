package com.example.chedifier.previewofandroidn.backgroupopt;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

/**
 * Created by chedifier on 2016/3/23.
 */
public class ConnectivityChangeTest {

    private static final String TAG = ConnectivityChangeTest.class.getSimpleName();

    public static ConnectivityChangeReceiver sReceiver;

    public static boolean sHasRegister = false;

    public static void registerReceiver(Context ctx){
        if(sHasRegister){
            return;
        }

        if(sReceiver == null){
            sReceiver = new ConnectivityChangeReceiver();
        }

        ctx.registerReceiver(sReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        sHasRegister = true;

    }

    public static void unregisterReceiver(Context ctx){
        if(!sHasRegister){
            return;
        }

        if(sReceiver != null){
            ctx.unregisterReceiver(sReceiver);
            sReceiver = null;
            sHasRegister = false;
        }

    }

    public static void listenConnectivity(Context ctx){
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        ConnectivityManager mgr = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        mgr.registerNetworkCallback(networkRequest,new MyNetworkCallback());
    }


    public static class MyNetworkCallback extends ConnectivityManager.NetworkCallback{

        @Override
        public void onAvailable(Network network) {
            Log.d(TAG,"onAvailable " + network);

            super.onAvailable(network);
        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            Log.d(TAG,"onAvailable " + network + " ;  " + maxMsToLive);

            super.onLosing(network,maxMsToLive);
        }

        @Override
        public void onLost(Network network) {
            Log.d(TAG,"onLost " + network);

            super.onLost(network);
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            Log.d(TAG,"onCapabilitiesChanged " + network);

            super.onCapabilitiesChanged(network,networkCapabilities);
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            Log.d(TAG,"onLinkPropertiesChanged " + network);


            super.onLinkPropertiesChanged(network,linkProperties);
        }
    }

}
