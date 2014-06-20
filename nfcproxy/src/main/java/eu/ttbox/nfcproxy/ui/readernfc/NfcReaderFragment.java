package eu.ttbox.nfcproxy.ui.readernfc;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.R;
import eu.ttbox.nfcproxy.service.nfc.NfcConsoleCallback;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderBroadcastReceiver;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;
import eu.ttbox.nfcproxy.service.nfc.reader.LoyaltyCardReader;
import eu.ttbox.nfcproxy.service.nfc.reader.emv41.EmvCardReader;
import eu.ttbox.nfcproxy.ui.MainActivity;
import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleArrayAdapter;
import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleLine;


public class NfcReaderFragment extends Fragment implements LoyaltyCardReader.AccountCallback {

    public static final String TAG = "CardReaderFragment";


    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;


    // Service
    private NfcReaderBroadcastReceiver nfcReceiver;

    public NfcReaderCallback mLoyaltyCardReader;

    // Data
    private NfcConsoleArrayAdapter consoleNfc;

//    private SpinnerAdapter mSpinnerAdapter;
//    private  ActionBar.OnNavigationListener mOnNavigationListener;


    // Binding
    private TextView mStatusField;
    private ListView consoleLogListView;

    // ===========================================================
    // Static
    // ===========================================================


    public static NfcReaderFragment newInstance(int sectionNumber) {
        NfcReaderFragment fragment = new NfcReaderFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    // ===========================================================
    // Constructor
    // ===========================================================


    public NfcReaderFragment() {
        super();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Card Reader Selection
//        mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.ncfreader_action_list, android.R.layout.simple_spinner_dropdown_item);
//
//        mOnNavigationListener = new ActionBar.OnNavigationListener() {
//            // Get the same strings provided for the drop-down's ArrayAdapter
//            String[] strings = getResources().getStringArray(R.array.ncfreader_action_list);
//
//            @Override
//            public boolean onNavigationItemSelected(int position, long itemId) {
//                String selectItem =  strings[position];
//                return true;
//            }
//        };
//        // ActionBar
//        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
//    }

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


        // Reader
        mLoyaltyCardReader = new EmvCardReader(nfcConsoleCallback);
//        mLoyaltyCardReader = new NavigoCardReader(nfcConsoleCallback);
        //

        // Disable Android Beam and register our card reader callback
        //enableReaderMode();

        // Register fragment menu
        setHasOptionsMenu(true);
        return v;
    }

    // ===========================================================
    // Menu
    // ===========================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nfc_console, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nfc_console_clear:
                consoleNfc.clear();
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
    public void onPause() {
        disableReaderMode();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }




    // ===========================================================
    // Dialog Service
    // ===========================================================


    protected void startNfcSettingsActivity() {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
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
                enableReaderMode(activity, nfc, mLoyaltyCardReader);
            }
        }
    }


    private void enableReaderMode(Activity activity, NfcAdapter nfc, NfcReaderCallback cardReaderCallback) {
        String[][] nfctechfilter = new String[][]{new String[]{IsoDep.class.getName()}};
        // Broadcast
        nfcReceiver = new NfcReaderBroadcastReceiver(cardReaderCallback);
        // Register Receiver Event
        IntentFilter nfcReceiverFilter = new IntentFilter();
        nfcReceiverFilter.addAction(NfcReaderBroadcastReceiver.ACTION_ON_NFC_RECEIVE);
        activity.registerReceiver(nfcReceiver, nfcReceiverFilter);
        // Nfc Receiver
        Intent intent = new Intent()
                .setAction(NfcReaderBroadcastReceiver.ACTION_ON_NFC_RECEIVE);
                //.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        PendingIntent nfcIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        // Enable
        nfc.enableForegroundDispatch(activity, nfcIntent, null, nfctechfilter);
        // Status
        mStatusField.setText("Ready to read Nfc Tag....");
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
        mStatusField.setText("");
    }

    // ===========================================================
    // Service Callback
    // ===========================================================

    private NfcConsoleCallback nfcConsoleCallback = new NfcConsoleCallback() {
        @Override
        public void onTagDiscovered(byte[] tagId) {
            String tagIdString = NumUtil.byte2HexNoSpace(tagId);
            mStatusField.setText("Tag Discovered : " + tagIdString);
            logNfcConsole("Tag Discovered", tagIdString);
         }

        @Override
        public void onTagClose(byte[] tagId) {
            String tagIdString = NumUtil.byte2HexNoSpace(tagId);
            setStatusField("Tag Close : " + tagIdString);
            logNfcConsole("Tag Close", tagIdString);
        }


        @Override
        public void onConsoleLog(String key, String value) {
            logNfcConsole(key, value);
        }


    };

    @Override
    public void onAccountReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusField.setText(account);
            }
        });
    }

    // ===========================================================
    // Console Log
    // ===========================================================

    public void setStatusField(final String statusText) {
        mStatusField.setText(statusText);
        mStatusField.post(new Runnable() {
            public void run() {
                mStatusField.setText(statusText);
            }
        });
    }

    public void logNfcConsole(final String key, final byte[] value) {
        String valueString = NumUtil.byte2HexNoSpace(value);
        logNfcConsole(key, valueString);
    }

    public void logNfcConsole(final String key, final String value) {
        Activity activity =getActivity();
        if (activity==null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                consoleNfc.add(new NfcConsoleLine(key, value));
            }
        });
    }


    // ===========================================================
    // Other
    // ===========================================================


}
