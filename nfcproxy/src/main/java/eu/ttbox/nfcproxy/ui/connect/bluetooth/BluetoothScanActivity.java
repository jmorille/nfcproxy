package eu.ttbox.nfcproxy.ui.connect.bluetooth;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import eu.ttbox.nfcproxy.R;

public class BluetoothScanActivity extends Activity {

    private static final String TAG = "BluetoothScanActivity";

    private BluetoothScanFragment bluetoothScanFragment;


    // ===========================================================
    // Constructors
    // ===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_search_device);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof  BluetoothScanFragment) {
            this.bluetoothScanFragment = (BluetoothScanFragment)fragment;
        }
    }


    // ===========================================================
    // Constructors
    // ===========================================================


}
