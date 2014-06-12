package eu.ttbox.nfcproxy.ui.proxy;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import eu.ttbox.nfcproxy.R;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderBroadcastReceiver;
import eu.ttbox.nfcproxy.ui.MainActivity;
import eu.ttbox.nfcproxy.ui.connect.RemoteChatCallback;
import eu.ttbox.nfcproxy.ui.connect.RemoteChatService;
import eu.ttbox.nfcproxy.ui.connect.bluetooth.BluetoothChatService;
import eu.ttbox.nfcproxy.ui.connect.bluetooth.BluetoothScanActivity;
import eu.ttbox.nfcproxy.ui.connect.bluetooth.BluetoothScanFragment;
import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleArrayAdapter;
import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleLine;

public class NfcProxyFragment extends Fragment {

    private static final String TAG = "NfcProxyFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    // Service
    private NfcReaderBroadcastReceiver nfcReceiver;
    private BluetoothChatService mChatService;

    // Data
    private NfcConsoleArrayAdapter consoleNfc;

    // Binding
    private TextView mStatusField;
    private ListView consoleLogListView;


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
        // Binding
        mStatusField = (TextView) v.findViewById(R.id.card_account_field);
        mStatusField.setText("Waiting...");

        // Console Listview
        consoleLogListView = (ListView) v.findViewById(R.id.nfc_log_console);
        consoleNfc = new NfcConsoleArrayAdapter(getActivity());
        consoleLogListView.setAdapter(consoleNfc);

        // Service
        mChatService = new BluetoothChatService(getActivity(), bluetoothChatCallback);

        // Register fragment menu
        setHasOptionsMenu(true);
        return v;
    }

    // ===========================================================
    // Console Log
    // ===========================================================

    private void setStatusField(String statusText) {
        mStatusField.setText(statusText);
    }

    private static final int UI_HANDLER_LOG_CONSOLE = 1;
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UI_HANDLER_LOG_CONSOLE:
                    consoleNfc.add((NfcConsoleLine) msg.obj);
                    break;
            }
        }
    };

    private void logNfcConsole(final String key, final String value) {
          uiHandler.obtainMessage(UI_HANDLER_LOG_CONSOLE, new NfcConsoleLine(key, value)).sendToTarget();

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                consoleNfc.add(new NfcConsoleLine(key, value));
//
//            }
//        });
    }

    // ===========================================================
    // Menu
    // ===========================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nfc_console_clear:
                consoleNfc.clear();
                return true;
            case R.id.menu_bluetooth_scan :
                scanBluetoothDevices(false);
                logNfcConsole("menu", "Scan");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // ===========================================================
    // Life Cycle
    // ===========================================================


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Title Listener
        if (activity instanceof MainActivity) {
            MainActivity parentMain = (MainActivity) activity;
            parentMain.onSectionAttached(getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        boolean isEnable =  mChatService.isEnableAdapter(this, REQUEST_ENABLE_BT);
        if (isEnable) {
            setupChat();
        }
     }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
         Log.e(TAG, "--- ON DESTROY ---");
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        startChatService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                   setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                  //  Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                  //  finish();
                }
                break;
            default:

                break;
        }
    }


    // ===========================================================
    //  Chat Service
    // ===========================================================

    private void setupChat() {
        Log.d(TAG, "setupChat()");

    }


    private synchronized void startChatService() {
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == RemoteChatCallback.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }



    private RemoteChatCallback bluetoothChatCallback = new RemoteChatCallback() {
        @Override
        public void setState(int state) {
            String stateLabel = "??";
            switch (state) {
                case  RemoteChatCallback.STATE_NONE:
                    stateLabel = "STATE_NONE";
                    break;
                case  RemoteChatCallback.STATE_LISTEN:
                    stateLabel = "STATE_LISTEN";
                    break;
                case  RemoteChatCallback.STATE_CONNECTING:
                    stateLabel = "STATE_CONNECTING";
                    break;
                case  RemoteChatCallback.STATE_CONNECTED:
                    stateLabel = "STATE_CONNECTED";
                    break;
                default:
                    stateLabel = "" + state;
                    break;

            }
            logNfcConsole("state", stateLabel);
        }

        @Override
        public void connected(String deviceName) {
            logNfcConsole("connected", deviceName);
            String msg = "Coucou to "+ deviceName;
            mChatService.write(msg.getBytes());
        }

        @Override
        public void connectionFailed() {
            logNfcConsole("connectionFailed", "");
        }

        @Override
        public void connectionLost() {
            logNfcConsole("connectionLost", "");
        }

        @Override
        public void write(byte[] buffer) {
            logNfcConsole("write", new String(buffer));
        }

        @Override
        public int read(int byteCount, byte[] buffer) {
            logNfcConsole("read", new String(buffer));
            return 0;
        }
    };


    // ===========================================================
    // Bluetooth
    // ===========================================================


    private void scanBluetoothDevices(boolean secure) {
        int requestCode = secure? REQUEST_CONNECT_DEVICE_SECURE: REQUEST_CONNECT_DEVICE_INSECURE;
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(getActivity(), BluetoothScanActivity.class);
        startActivityForResult(serverIntent, requestCode);
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(RemoteChatService.EXTRAS_DEVICE_ADDRESS);
        // Attempt to connect to the device
        mChatService.connectByAddress(address, secure);
        logNfcConsole("connectByAddress", address);
    }



    // ===========================================================
    // Other
    // ===========================================================


}
