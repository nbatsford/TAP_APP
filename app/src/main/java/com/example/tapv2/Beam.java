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
import android.os.AsyncTask;
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
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
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

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        //PROCESS NDEF MESSAGE INTO READABLE STRING
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null && rawMsgs.length > 0) {
            NdefMessage[] messages = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
            }
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            String payloadSSID = new String(msg.getRecords()[0].getPayload());
            String payloadLoc = new String(msg.getRecords()[1].getPayload());
            String ssid = payloadSSID.substring(payloadSSID.indexOf("[") + 1, payloadSSID.indexOf("]"));
            //COLLECT ALL DETAILS FOR CONNECTING TO WI-FI
            collect(ssid, payloadLoc);
        }
    }

    public void collect(String ssid, String Loc) {
        //RETRIVE USERNAME AND PASSWORD FROM SHARED PREFERENCES
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("USERNAME", "NO USERNAME FOUND");
        String pass = preferences.getString("PASSWORD", "NO PASSWORD FOUND");
        SharedPreferences.Editor saveDetails = preferences.edit();
        final TextView status = findViewById(R.id.textStatus);
        saveDetails.putString("SSID", ssid);
        //START CONNECTING TO WI-FI
        boolean connected = connectToWiFi(ssid, user, pass, status);
        if (connected != true) {
            status.setText("Cannot Connnect to Wi-Fi");
            Toast.makeText(getApplicationContext(), "Cannot Connect to WiFi", Toast.LENGTH_LONG).show();
        } else {
            status.setText("Connecting...");
            class check implements Runnable {
                private String str;
                private Context conty;
                private TextView status;
                private ProgressBar spinner;

                check(String ssid, Context context, TextView textView) {
                    str = ssid;
                    conty = context;
                    status = textView;

                }

                wifiCheck connSSID = new wifiCheck();

                public void run() {
                    status.setText("Connecting....");
                    spinner = (ProgressBar) findViewById(R.id.proCon);
                    spinner.setVisibility(View.VISIBLE);
                    int maxRetry = 5;
                    int retryCount = 0;
                    wifiCheck check = new wifiCheck();
                    while (true)
                        try {
                            boolean rightConn = connSSID.connectionCheck(str, conty);
                            if (rightConn == true) {
                                break;
                            }
                        } catch (Exception e) {
                            if (retryCount > maxRetry) {
                                throw new RuntimeException("Could not get SSID");
                            }
                            retryCount++;
                            continue;
                        }
/*                    spinner.setVisibility(View.GONE);*/
                    status.setText("Connected to Wi-Fi");
                    spinner.setVisibility(View.INVISIBLE);
                    Thread.currentThread().interrupt();
                }
            }
            Thread thread = new Thread(new check(ssid, this, status));
            thread.start();
            saveLoc(Loc);
            Toast.makeText(this, "Location saved", Toast.LENGTH_LONG).show();
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

    public void saveLoc(String Loc){
        String file_Name = "past_locations.txt";
        FileOutputStream outputStream = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String Formattedloc = Loc.substring(Loc.indexOf("[") + 1, Loc.indexOf("]"));
        try {
            LocalDateTime time = LocalDateTime.now();
            outputStream = openFileOutput(file_Name, MODE_APPEND);
            outputStream.write(Formattedloc.getBytes());
            String now = " " + dtf.format(time);
            outputStream.write(now.getBytes());
            outputStream.write("\n".getBytes());
            Toast.makeText(this, "Saved To " + getFilesDir() + "/" + file_Name, Toast.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
