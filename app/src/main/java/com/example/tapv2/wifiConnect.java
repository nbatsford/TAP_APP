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
    Context context;
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beam);
        boolean ssidMatch = false;
        spinner = (ProgressBar)findViewById(R.id.progressCon);
        final TextView status = (TextView)findViewById(R.id.textStatus);

        try {
            wait(1000);
        }catch (Exception e){}
        spinner.setVisibility(View.VISIBLE);
        status.setText("Connecting to Wi-Fi");
        boolean connected = wifi_Connect(this);
        Button retry = (Button)findViewById(R.id.buttonRetry);
        if(connected == true) {
            while (ssidMatch == false) {
                ssidMatch = getSSID();
                if (ssidMatch == true) {
                    spinner.setVisibility(View.INVISIBLE);
                    status.setText("Connected to the Wi-Fi");
                } else {
                    status.setText("Please Press Retry");
                retry.setVisibility(View.VISIBLE);
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            boolean connected = retry();
                            if (connected == true) {
                                boolean ssidMatch = getSSID();
                            } else {
                                status.setText("Unable to Connnect to Wi-Fi");
                            }
                        }
                });
                }
            }
        } else {
            retry.setVisibility(View.VISIBLE);
           retry.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                    status.setText("you've pressed Retry");
                }
            });
       }
    }*/
    Handler wait = new Handler();
    public boolean wifi_Connect(String user, String pass, Context context) {

/*        Intent get = getIntent();*/
        String SSID = "TAP";
/*        String user = get.getStringExtra("CONUSER");
        String pass = get.getStringExtra("CONPASS");*/
        wifiConfig.SSID = String.format("\"%s\"", SSID);
        wifiEnterpriseConfig.setPassword(pass);
        wifiEnterpriseConfig.setIdentity(user);

        wifiEnterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        wifiConfig.enterpriseConfig = wifiEnterpriseConfig;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);


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
            Toast.makeText(context, "Cannnot Connect to Wi-Fi", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
/*    private ProgressBar spinner;*/

/*    public boolean getSSID() {
        String ssid = wifiManager.getConnectionInfo().getSSID();
        if (ssid == "TAP") {
            return true;
        } else {
            return false;
        }
    }*/
/*    public boolean retry() {
        boolean retryConnnection = wifi_Connect(this);
        if (retryConnnection == true) {
            return true;
        } else {
            return false;
       }
    }*/
}
