package eu.ttbox.nfcproxy.ui.cardreader;

import android.app.Activity;
import android.app.ListFragment;
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
import android.widget.TextView;

import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.R;
import eu.ttbox.nfcproxy.service.nfc.NfcConsoleCallback;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderBroadcastReceiver;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;
import eu.ttbox.nfcproxy.service.nfc.reader.emv41.EmvCardReader;
import eu.ttbox.nfcproxy.service.nfc.reader.LoyaltyCardReader;
import eu.ttbox.nfcproxy.service.nfc.reader.navigo.NavigoCardReader;
import eu.ttbox.nfcproxy.ui.MainActivity;
import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleArrayAdapter;
import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleLine;


public class NfcReaderFragment extends ListFragment implements LoyaltyCardReader.AccountCallback, NfcConsoleCallback {

    public static final String TAG = "CardReaderFragment";


    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;


    // Service
    private NfcReaderBroadcastReceiver nfcReceiver;

    public NfcReaderCallback mLoyaltyCardReader;

    // Binding
    private TextView mStatusField;

    private NfcConsoleArrayAdapter consoleNfc;

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

    /**
     * Called when sample is created. Displays generic UI with welcome text.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adapter
        consoleNfc = new NfcConsoleArrayAdapter(getActivity());
        setListAdapter(consoleNfc);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cardreader, container, false);

        mStatusField = (TextView) v.findViewById(R.id.card_account_field);
        mStatusField.setText("Waiting...");

        mLoyaltyCardReader = new EmvCardReader(this);
//        mLoyaltyCardReader = new NavigoCardReader(this);
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
        if (activity instanceof  MainActivity) {
            MainActivity parentMain = (MainActivity)activity;
            parentMain.onSectionAttached(  getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
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
        IntentFilter nfcReceiverFilter = new IntentFilter();
        nfcReceiverFilter.addAction(NfcReaderBroadcastReceiver.ACTION_ON_NFC_RECEIVE);
        activity.registerReceiver(nfcReceiver, nfcReceiverFilter);
        // Nfc Receiver
        Intent intent = new Intent()
                .setAction(NfcReaderBroadcastReceiver.ACTION_ON_NFC_RECEIVE)
                //.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                ;
        PendingIntent nfcintent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        // Enable
        nfc.enableForegroundDispatch(activity, nfcintent, null, nfctechfilter);
        // Status
        mStatusField.setText("Ready to read Nfc Tag...."  );
    }


    // ===========================================================
    // Service Callback
    // ===========================================================

    @Override
    public void onTagDiscovered(byte[] tagId){
        String tagIdString =  NumUtil.byte2HexNoSpace(tagId);
        mStatusField.setText("Tag Discovered : " + tagIdString );
        consoleNfc.add(new NfcConsoleLine("Tag Discovered", tagIdString));
    }

    @Override
    public void onTagClose(byte[] tagId){
        String tagIdString =  NumUtil.byte2HexNoSpace(tagId);
        mStatusField.setText("Tag Close : " + tagIdString );
        consoleNfc.add(new NfcConsoleLine("Tag Close", tagIdString));
    }


    @Override
    public void onConsoleLog(String key, String value){
        consoleNfc.add(new NfcConsoleLine(key, value));
    }

 
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
    // Other
    // ===========================================================

}
