package com.example.tapv2;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Parcelable;

import java.nio.charset.Charset;
import java.util.Locale;


public class beam extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private TextView mTextView;
    private NdefMessage mNdefMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beam);

        Intent beam = getIntent();

        String user = beam.getStringExtra("STARTUSER");
        String pass = beam.getStringExtra("STARTPASS");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            Toast.makeText(this, "READY TO BEAM", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC NOT ENABLED", Toast.LENGTH_LONG).show();
        }

        mNdefMessage = new NdefMessage(
                new NdefRecord[] {
                        createNewTextRecord(user.toString(), Locale.ENGLISH, true),
                        createNewTextRecord(pass.toString(), Locale.ENGLISH,true) });

    }

    public static NdefRecord createNewTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1<< 7);

        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte)status;

        System.arraycopy(langBytes, 0, data, 1 , langBytes.length);
        System.arraycopy((textBytes), 0,data,1+langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundNdefPush(this, mNdefMessage);
    }

    @Override
    public void onPause(){
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundNdefPush(this);
    }

}
