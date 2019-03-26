package com.example.tapv2;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class wifiCheck {

        public boolean connectionCheck (String ssid, Context context){

            Log.d("ATTEMPTING SSID", "CHECKING CONNECTION TO " + ssid);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo;

            wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                String connSSID = wifiInfo.getSSID();
                Log.d("CONNNECTED SSID", connSSID);
                if (connSSID.equals('"' + ssid + '"')) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
