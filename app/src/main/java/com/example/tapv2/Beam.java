package com.example.tapv2;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;
public class Beam extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;
    private static final int MESSAGE_SENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beam);

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
                    Intent conIntent = new Intent(Beam.this, wifiConnect .class);

                    conIntent.putExtra("CONUSER", tempuser);
                    conIntent.putExtra("CONPASS", temppass);
                    startActivity(conIntent);
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

}
