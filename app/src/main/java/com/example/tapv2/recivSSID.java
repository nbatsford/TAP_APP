package com.example.tapv2;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.util.Log;
import android.widget.Toast;

public class recivSSID {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilter;
    private String[][] mNFCTechLists;

    public void reciv(Context context) {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (mNfcAdapter != null) {
            Toast.makeText(context, "Ready to Read", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,"NFC not Enabled", Toast.LENGTH_LONG).show();
        }

        mPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilter = new IntentFilter[] {ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] {NfcF.class.getName()}};
    }
}
