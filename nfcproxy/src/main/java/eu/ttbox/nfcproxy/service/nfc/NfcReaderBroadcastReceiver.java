package eu.ttbox.nfcproxy.service.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;

import eu.ttbox.nfcparser.utils.NumUtil;


public class NfcReaderBroadcastReceiver  extends BroadcastReceiver {

    private static final String TAG = "NfcReaderBroadcastReceiver";

    public static final String ACTION_ON_NFC_RECEIVE = "eu.ttbox.nfcproxy.ACTION_ON_NFC_RECEIVE";

    private NfcReaderCallback nfcReaderCallback;


    public NfcReaderBroadcastReceiver(NfcReaderCallback nfcReaderCallback) {
        this.nfcReaderCallback = nfcReaderCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "Tag detected!");

        byte[] tagId = tag.getId();
        Log.d(TAG, "Tag detected : " + NumUtil.byte2Hex(tagId));

        nfcReaderCallback.onTagDiscovered(tag);
    }


}
