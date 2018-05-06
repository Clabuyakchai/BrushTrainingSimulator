package com.example.clabuyakchai.brushtrainingsimulator.stateinternet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Clabuyakchai on 06.05.2018.
 */

public class StateInternet {
    public static boolean hasConnection(final Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiInfo != null && wifiInfo.isConnected()){
            return true;
        }

        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifiInfo != null && wifiInfo.isConnected()){
            return true;
        }

        wifiInfo = cm.getActiveNetworkInfo();
        if(wifiInfo != null && wifiInfo.isConnected()){
            return true;
        }

        return false;
    }
}
