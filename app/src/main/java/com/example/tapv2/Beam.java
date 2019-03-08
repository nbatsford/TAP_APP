package com.example.tapv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;
public class Beam extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;
    private static final int MESSAGE_SENT = 1;
    wifiConnect connect = new wifiConnect();
    Activity act = new Activity();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beam);
        spinner = findViewById(R.id.progressCon);
        spinner.setVisibility(View.INVISIBLE);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC NOT AVAILIBLE", Toast.LENGTH_LONG).show();
        } else {
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this,this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Intent beam = getIntent();
        String user = "TAPUSER:" + beam.getStringExtra("STARTUSER") + "!tapusername!";
        String pass = "TAPPASSWORD:" + beam.getStringExtra("STARTPASS") + "!tappassword!";

        String textRecord = user + pass;

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
                    Intent beam = getIntent();
                    String tempuser = beam.getStringExtra("STARTUSER");
                    String temppass =beam.getStringExtra("STARTPASS");
                    final TextView status = (TextView)findViewById(R.id.textStatus);

                    boolean continueConnect = connectToWiFi(tempuser, temppass, status);
                    if (continueConnect != true) {
                        status.setText("Cannot Connnect to Wi-Fi");
                        spinner.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Cannot Connect to WiFi", Toast.LENGTH_LONG).show();
                    } else {
                        spinner.setVisibility(View.INVISIBLE);
                        status.setText("Connected To Wi-Fi");
                        Toast.makeText(getApplicationContext(),"Connected to WiFi", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
    }

    void processIntent(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES
        );

        NdefMessage msg = (NdefMessage) rawMsgs[0];
    }
    public boolean connectToWiFi(String user, String pass, TextView status) {
        spinner = (ProgressBar)findViewById(R.id.progressCon);
        spinner.setVisibility(View.VISIBLE);
        status.setText("Connecting To Wi-Fi");
        wifiConnect connect = new wifiConnect();
        boolean connected = connect.wifi_Connect(user, pass, getApplicationContext());
        if (connected != true) {
            return false;
        } else {
            return true;
        }
    }
        private ProgressBar spinner;
}
