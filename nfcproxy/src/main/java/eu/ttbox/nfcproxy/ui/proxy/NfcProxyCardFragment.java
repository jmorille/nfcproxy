package eu.ttbox.nfcproxy.ui.proxy;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderBroadcastReceiver;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;
import eu.ttbox.nfcproxy.ui.MainActivity;
import eu.ttbox.nfcproxy.ui.proxy.emulator.BasicTagTechnologyWrapper;

public class NfcProxyCardFragment extends NfcProxyFragment {

    private static final String TAG = "NfcProxyCardFragment";

    // ===========================================================
    // Static
    // ===========================================================


    public static NfcProxyCardFragment newInstance(int sectionNumber) {
        NfcProxyCardFragment fragment = new NfcProxyCardFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    // ===========================================================
    // Constructor
    // ===========================================================


    public NfcProxyCardFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // ===========================================================
    // Life cycle
    // ===========================================================

    @Override
    public void onResume() {
        Log.d(TAG, "onResume start");
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onPause() {
        disableReaderMode();
        super.onPause();
    }


    // ===========================================================
    // Nfc Register Service
    // ===========================================================

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            //  KitKat :  nfc.enableReaderMode(activity, mLoyaltyCardReader, READER_FLAGS, null);
            if (!nfc.isEnabled()) {
                startNfcSettingsActivity();
            } else {
                NfcReaderCallback  mLoyaltyCardReader = null;
                enableReaderMode(activity, nfc, mLoyaltyCardReader);
            }
        }
    }


    private void enableReaderMode(Activity activity, NfcAdapter nfc, NfcReaderCallback cardReaderCallback) {
        //String[][] nfctechfilter = new String[][]{new String[]{IsoDep.class.getName()}};
        String[][] nfctechfilter =  new String[][]{new String[]{ NFCVars.ISO_PCDA_CLASS }};
        // Broadcast
       // nfcReceiver = new NfcReaderBroadcastReceiver(cardReaderCallback);
        // Register Receiver Event
       // IntentFilter nfcReceiverFilter[] = {new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
        IntentFilter nfcReceiverFilter = new IntentFilter();
        nfcReceiverFilter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        activity.registerReceiver(nfcReceiver, nfcReceiverFilter);
        // Nfc Receiver
        Intent intent = new Intent()
                .setAction(NfcReaderBroadcastReceiver.ACTION_ON_NFC_RECEIVE);
        //.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        PendingIntent nfcIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        // Enable
        nfc.enableForegroundDispatch(activity, nfcIntent, null, nfctechfilter);
        // Status
        logStatusField("Ready to read Nfc Tag....");
    }


    // ===========================================================
    // Nfc UnRegister Service
    // ===========================================================


    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        // KitKat : nfc.disableReaderMode(activity);
        disableReaderMode(activity, nfc);

    }

    private void disableReaderMode(Activity activity, NfcAdapter nfc) {
        if (nfc != null) {
            nfc.disableForegroundDispatch(activity);
        }
        if (nfcReceiver != null) {
            activity.unregisterReceiver(nfcReceiver);
        }
        logStatusField("");
    }

    // ===========================================================
    // Nfc
    // ===========================================================

    private BroadcastReceiver nfcReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO
            String action = intent.getAction();
            logNfcConsole("Nfc Broadcast", action);
            printIntentExtras(intent);
            // Get Tags
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] tagId = tag.getId();
            logNfcConsole( "Tag detected : " , NumUtil.byte2Hex(tagId));
            //

        }

        private void printIntentExtras( Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras!=null && !extras.isEmpty()) {
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    logNfcConsole("  extra " + key, "" +value);
                }
            }
        }

    };



    // ===========================================================
    // Nfc
    // ===========================================================

    private BasicTagTechnologyWrapper getBasicTagTechnologyWrapper(Tag tag ) {
        BasicTagTechnologyWrapper tagTech = null;

        Class[] supportedTags = new Class[] { IsoDep.class };
        String[] tech = tag.getTechList();

        for (String s: tech) {
            for(Class c: supportedTags) {
                if (s.equals(c.getName())) {
                    try {
                        tagTech = new BasicTagTechnologyWrapper(tag, c.getName());
                    } catch (Exception e) {
                       throw new RuntimeException("Could not Create BasicTagTechnologyWrapper : " + e.getMessage(), e);
                    }
                }
            }
        }

        return tagTech;
    }

    private void doReplayPCD(Tag tag, Bundle tagTransactions, Bundle pcdRequests) {
        BasicTagTechnologyWrapper tagTech = getBasicTagTechnologyWrapper(tag);

        // Connect The Tags
        tagTech.connect();
        boolean connected = tagTech.isConnected();
        Log.d(TAG, "isConnected: " + connected);
        if (!connected) return;

        // first store ID

        // Transmet
        byte[] tmp = new byte[] { 0x33 };
        byte[] reply = tagTech.transceive(tmp);

    }


    private void doReplayTag(Tag tag, Bundle tagTransactions, Bundle pcdRequests) {
        try {
            Class cls = Class.forName(NFCVars.ISO_PCDA_CLASS);
            Method meth = cls.getMethod("get", new Class[]{Tag.class});
            Object ipcd = meth.invoke(null, tag);
            // Connect
            meth = cls.getMethod("connect", null);
            meth.invoke(ipcd, null);
            // isConnected
            meth = cls.getMethod("isConnected", null);
            boolean connected = (Boolean) meth.invoke(ipcd, null);
            Log.d(TAG, "isConnected: " + connected);
            logNfcConsole("isConnected", String.valueOf(connected));

            // transceive
            meth = cls.getMethod("transceive", new Class[]{byte[].class});
            byte[] tmp = new byte[] { 0x33 }; // tagTransactions
            byte[] reply = (byte[]) meth.invoke(ipcd, tmp);
            logNfcConsole("reply", reply);

        } catch (Exception e) {
            throw new RuntimeException("doReplayTag : " + e.getMessage(), e);
        }

    }
}
