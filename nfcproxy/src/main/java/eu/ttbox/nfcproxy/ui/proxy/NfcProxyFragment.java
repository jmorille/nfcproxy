package eu.ttbox.nfcproxy.ui.proxy;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.ttbox.nfcproxy.R;
import eu.ttbox.nfcproxy.ui.MainActivity;

public class NfcProxyFragment extends Fragment {


    // ===========================================================
    // Static
    // ===========================================================


    public static NfcProxyFragment newInstance(int sectionNumber) {
        NfcProxyFragment fragment = new NfcProxyFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    // ===========================================================
    // Constructor
    // ===========================================================


    public NfcProxyFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cardreader, container, false);

        return v;
    }


    // ===========================================================
    // Menu
    // ===========================================================



    // ===========================================================
    // Life Cycle
    // ===========================================================


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Title Listener
        if (activity instanceof  MainActivity) {
            MainActivity parentMain = (MainActivity)activity;
            parentMain.onSectionAttached(  getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
        }
    }



    // ===========================================================
    // Other
    // ===========================================================



}
