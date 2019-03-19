package com.example.tapv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;

public class Beam extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;
    private static final int MESSAGE_SENT = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beam);

        //GET NFC ADAPTER OF DEVICE
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //CHECK IF NFC IS SUPPORTED
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC NOT AVAILIBLE", Toast.LENGTH_LONG).show();
        } else {
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this,this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // GET USERNAME AND PASSWORD FROM SHARED PREFERENCES
        String tempuser = preferences.getString("USERNAME", "NO USERNAME FOUND");
        String temppass = preferences.getString("PASSWORD", "NO PASSWORD FOUND");

        //FORMAT FOR NDEF RECORD
        String user = "TAPUSER:" + tempuser + "!tapusername!";
        String pass = "TAPPASSWORD:" + temppass + "!tappassword!";
        String textRecord = user + pass;

        //CREATE TEXT RECORD FOR DISPACH
        NdefMessage msg = new NdefMessage(NdefRecord.createMime("application/com.exapmple.android.beam", textRecord.getBytes()));
        return msg;
    }
    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }

    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        Intent checkSSID = getIntent();
//        String ssid = checkSSID.getStringExtra("SSID");
//        if (ssid != null) {
//            Toast.makeText(this, "SAVED SSID" + ssid, Toast.LENGTH_LONG).show();
//            collect(ssid);
//        }
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
    }
    void processIntent(Intent intent){
        //PROCESS NDEF MESSAGE INTO READABLE STRING
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null && rawMsgs.length > 0){
            NdefMessage[] messages = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
            }
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            String payloadString = new String(msg.getRecords()[0].getPayload());
            String ssid = payloadString.substring(payloadString.indexOf("[") + 1, payloadString.indexOf("]"));
            Toast.makeText(this, ssid, Toast.LENGTH_LONG).show();
            //COLLECT ALL DETAILS FOR CONNECTING TO WI-FI
            collect(ssid);
        }
    }
    public void collect(String ssid) {
        //RETRIVE USERNAME AND PASSWORD FROM SHARED PREFERENCES
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("USERNAME", "NO USERNAME FOUND");
        String pass = preferences.getString("PASSWORD", "NO PASSWORD FOUND");
        final TextView status = (TextView)findViewById(R.id.textStatus);

        //START CONNECTING TO WI-FI
        boolean connected = connectToWiFi(ssid, user, pass, status);
        if (connected != true) {
            status.setText("Cannot Connnect to Wi-Fi");
            Toast.makeText(getApplicationContext(),"Cannot Connect to WiFi", Toast.LENGTH_LONG).show();
        } else {
            status.setText("Connected To Wi-Fi");
            Toast.makeText(getApplicationContext(),"Connected to WiFi", Toast.LENGTH_LONG).show();
        }

    }
    public boolean connectToWiFi(String ssid, String user, String pass, TextView status) {
        status.setText("Connecting To Wi-Fi");
        wifiConnect connect = new wifiConnect();
        boolean connected = connect.wifi_Connect(user, pass, ssid, getApplicationContext());
        if (connected != true) {
            return false;
        } else {
            return true;
        }
    }
}
