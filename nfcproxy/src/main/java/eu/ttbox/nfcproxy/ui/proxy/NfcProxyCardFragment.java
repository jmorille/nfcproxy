package eu.ttbox.nfcproxy.ui.proxy;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

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

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
        if (adapter != null) {
            IntentFilter intentFilter[] = {new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
            Intent destIntent = new Intent(getActivity(), getClass());
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, destIntent, 0);
            adapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilter, new String[][]{new String[]{NFCVars.ISO_PCDA_CLASS}});
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
        if (adapter != null) {
            adapter.disableForegroundDispatch(getActivity());
        }

    }




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
