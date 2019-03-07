package com.example.tapv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class wifiConnect extends AppCompatActivity {
    WifiConfiguration wifiConfig = new WifiConfiguration();
    WifiEnterpriseConfig wifiEnterpriseConfig = new WifiEnterpriseConfig();
    WifiManager wifiManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifistatus);
        Intent get = getIntent();
        String user = get.getStringExtra("CONUSER");
        String pass = get.getStringExtra("CONPASS");
        wifi_Connect(user, pass, "TAP", this);
    }
    public void wifi_Connect(String username, String pass,  String SSID, Context context) {
        wifiConfig.SSID = String.format("\"%s\"", SSID);
        wifiEnterpriseConfig.setPassword(pass);
        wifiEnterpriseConfig.setIdentity(username);

        wifiEnterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        wifiConfig.enterpriseConfig = wifiEnterpriseConfig;
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        boolean res1 = wifiManager.setWifiEnabled(true);
        int res = wifiManager.addNetwork(wifiConfig);
        Log.d("Wi-Fi Preferance", "add Network returned" + res );

        boolean disableNetwork = wifiManager.enableNetwork(res, false);
        Log.d("Wi-Fi Preferance", "enable Network returned" + disableNetwork);

        boolean saveNetwork = wifiManager.saveConfiguration();
        Log.d("Wi-Fi Preferance", "Save Config returned" + saveNetwork);

        boolean enableNetwork = wifiManager.enableNetwork(res,true);
        Log.d("Wi-Fi Preferance", "enableNetwork returned" + enableNetwork);

        if (!res1 || res == -1 || !disableNetwork || !saveNetwork || !enableNetwork) {
            //failed Connnection
            Toast.makeText(this, "Cannnot Connect to Wi-Fi", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Connected To Wi-Fi", Toast.LENGTH_LONG).show();
        }

    }
}
