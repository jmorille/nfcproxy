package eu.ttbox.nfcproxy.ui.proxy;


import android.os.Bundle;

import eu.ttbox.nfcproxy.ui.MainActivity;

public class NfcProxyCardFragment extends NfcProxyFragment {


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



}
