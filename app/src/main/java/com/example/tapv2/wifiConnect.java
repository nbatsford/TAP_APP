package com.example.tapv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class wifiConnect {
    WifiConfiguration wifiConfig = new WifiConfiguration();
    WifiEnterpriseConfig wifiEnterpriseConfig = new WifiEnterpriseConfig();
    WifiManager wifiManager;

    public boolean wifi_Connect(String user, String pass, String ssid, Context context) {


        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiEnterpriseConfig.setPassword(pass);
        Log.d("Password", "CONNECTING WITH PASSWORD: " + pass);
        wifiEnterpriseConfig.setIdentity(user);
        Log.d("username" , "CONNECTING WITH USERNAME: " + user);

        wifiEnterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        wifiConfig.enterpriseConfig = wifiEnterpriseConfig;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);


        boolean res1 = wifiManager.setWifiEnabled(true);
        int res = wifiManager.addNetwork(wifiConfig);
        Log.d("Wi-Fi Preferance", "add Network returned" + res);

        boolean disableNetwork = wifiManager.enableNetwork(res, false);
        Log.d("Wi-Fi Preferance", "enable Network returned" + disableNetwork);

        boolean saveNetwork = wifiManager.saveConfiguration();
        Log.d("Wi-Fi Preferance", "Save Config returned" + saveNetwork);

        boolean enableNetwork = wifiManager.enableNetwork(res, true);
        Log.d("Wi-Fi Preferance", "enableNetwork returned" + enableNetwork);

        if (!res1 || res == -1 || !disableNetwork || !saveNetwork || !enableNetwork) {
            //failed Connnection
            Toast.makeText(context, "Cannnot Connect to Wi-Fi", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
}
