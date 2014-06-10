package eu.ttbox.nfcproxy.ui.connect.bluetooth;


import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import eu.ttbox.nfcproxy.R;
import eu.ttbox.nfcproxy.ui.MainActivity;

public class BluetoothScanFragment extends Fragment {

    private static final String TAG = "BluetoothConnectFragment";


    public static final String EXTRAS_DEVICE_NAME = "EXTRAS_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "EXTRAS_DEVICE_ADDRESS";


    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private BluetoothDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;


    private ListView newDevicesListView;


    private boolean mScanning;
    private Handler mHandler;

    // ===========================================================
    // Static
    // ===========================================================


    public static BluetoothScanFragment newInstance(int sectionNumber) {
        Log.d(TAG, "BluetoothConnectFragment.newInstance : sectionNumber=" + sectionNumber);
        BluetoothScanFragment fragment = new BluetoothScanFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    // ===========================================================
    // Constructor
    // ===========================================================


    public BluetoothScanFragment() {
        super();
    }

    /**
     * Called when sample is created. Displays generic UI with welcome text.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        // Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            // Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            //  finish();
            //  return;
        }

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bluetooth_search_device, container, false);
        newDevicesListView = (ListView) v.findViewById(R.id.bluetooth_list_devices);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register fragment menu
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Title Listener
        if (activity instanceof MainActivity) {
            MainActivity parentMain = (MainActivity) activity;
            parentMain.onSectionAttached(getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
        }
//        // Register Callback Listener
//        if (activity instanceof  OnFragmentInteractionListener) {
//            try {
//                mListener = (OnFragmentInteractionListener) activity;
//            } catch (ClassCastException e) {
//                throw new ClassCastException(activity.toString()
//                        + " must implement OnFragmentInteractionListener");
//            }
//        }
    }

    // ===========================================================
    // Life Cycle
    // ===========================================================

    @Override
    public void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new BluetoothDeviceListAdapter(getActivity());
        newDevicesListView.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }


    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    @Override
    public void onDestroy() {
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            getActivity().finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ===========================================================
    // Menu
    // ===========================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_bluetooth_stop).setVisible(false);
            menu.findItem(R.id.menu_bluetooth_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_bluetooth_stop).setVisible(true);
            menu.findItem(R.id.menu_bluetooth_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bluetooth_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_bluetooth_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }


    // ===========================================================
    // Bluetooth Select
    // ===========================================================
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {
            final BluetoothDevice device = mLeDeviceListAdapter.getItem(position);
            if (device == null) return;
            final Intent intent = new Intent();
            intent.putExtra(EXTRAS_DEVICE_NAME, device.getName());
            intent.putExtra(EXTRAS_DEVICE_ADDRESS, device.getAddress());
            if (mScanning) {
                scanLeDevice(false);
            }
//        startActivity(intent);
           getActivity().setResult(Activity.RESULT_OK, intent);
           getActivity().finish();
        }

    };
    // ===========================================================
    // Bluetooth Search
    // ===========================================================


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.cancelDiscovery();
                    getActivity().invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            // Indicate scanning in the title
            getActivity().setProgressBarIndeterminateVisibility(true);
            getActivity().setTitle(R.string.bluetooth_scanning);

            // If we're already discovering, stop it
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }

            // Request discover from BluetoothAdapter
            mBluetoothAdapter.startDiscovery();


        } else {
            mScanning = false;
            mBluetoothAdapter.cancelDiscovery();
        }
        getActivity().invalidateOptionsMenu();
    }


    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Bluetooth Receiver onReceive" + action);
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                Log.d(TAG, "Device to add : " + device);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "Device added : " + device);
                    mLeDeviceListAdapter.add(device);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                getActivity().setTitle(R.string.select_bluetooth_device);
//                if (mNewDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = getResources().getText(R.string.none_found).toString();
//                    mNewDevicesArrayAdapter.add(noDevices);
//                }
            }
        }
    };


    // ===========================================================
    // Other
    // ===========================================================

}
