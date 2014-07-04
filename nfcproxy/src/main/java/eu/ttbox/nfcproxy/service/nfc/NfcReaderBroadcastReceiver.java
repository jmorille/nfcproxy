package eu.ttbox.nfcproxy.service.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.util.Log;

import eu.ttbox.nfcparser.utils.NumUtil;


public class NfcReaderBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NfcReaderBroadcastReceiver";

    public static final String ACTION_ON_NFC_RECEIVE = "eu.ttbox.nfcproxy.ACTION_ON_NFC_RECEIVE";

    private NfcReaderCallback nfcReaderCallback;


    public NfcReaderBroadcastReceiver(NfcReaderCallback nfcReaderCallback) {
        this.nfcReaderCallback = nfcReaderCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "Tag detected!");

        byte[] tagId = tag.getId();
        Log.d(TAG, "Tag detected : " + NumUtil.byte2Hex(tagId));

        // Extra Intfo
        byte[] extraId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);	//optional
        printNdefMessages(intent);

        // Discovery
        nfcReaderCallback.onTagDiscovered(tag);
    }

    private void printNdefMessages(Intent intent) {
        NdefMessage[] extraNdefMsg = extraNdefMessages(intent);
        if (extraNdefMsg!=null) {
            for (NdefMessage msg : extraNdefMsg) {
                Log.d(TAG, " NdefMessages : " +  msg.toString());
            }
        }

    }

    private NdefMessage[] extraNdefMessages(Intent intent) {
        String action = intent.getAction();
        NdefMessage[] msgs = null;
      //  if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] extraNdefMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (extraNdefMsg != null) {
                msgs = new NdefMessage[extraNdefMsg.length];
                for (int i = 0; i < extraNdefMsg.length; i++) {
                    msgs[i] = (NdefMessage) extraNdefMsg[i];
                }
            }
       // }
        return msgs;
    }
}
