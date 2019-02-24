package com.example.tapv2;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class wifiConnect {
    WifiConfiguration wifiConfig = new WifiConfiguration();
    WifiManager wifiManager;

    void wifi_Connect(String username, String pass, String SSID) {
        // Clear Current Wifi Config
        wifiConfig.allowedKeyManagement.clear();

        //Set Protocols e.g  802.1x
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        //Set connection Details
        wifiConfig.SSID = SSID;
        wifiConfig.enterpriseConfig.setPassword(pass);
        wifiConfig.enterpriseConfig.setIdentity(username);
        wifiConfig.enterpriseConfig.setAnonymousIdentity(username);
        wifiConfig.status = WifiConfiguration.Status.ENABLED;

        int netId = wifiManager.addNetwork(wifiConfig);

        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }
}
