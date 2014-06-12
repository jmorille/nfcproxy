package eu.ttbox.nfcproxy.ui.proxy;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import eu.ttbox.nfcproxy.ui.MainActivity;

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



    private void doReplayTag(Tag tag, Bundle tagTransactions, Bundle pcdRequests) {

    }
}
