package eu.ttbox.nfcproxy.ui.proxy;

import android.os.Bundle;

import eu.ttbox.nfcproxy.ui.MainActivity;

public class NfcProxyReaderFragment extends NfcProxyFragment {

    private static final String TAG = "NfcProxyReaderFragment";

    // ===========================================================
    // Static
    // ===========================================================


    public static NfcProxyReaderFragment newInstance(int sectionNumber) {
        NfcProxyReaderFragment fragment = new NfcProxyReaderFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    // ===========================================================
    // Constructor
    // ===========================================================


    public NfcProxyReaderFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
