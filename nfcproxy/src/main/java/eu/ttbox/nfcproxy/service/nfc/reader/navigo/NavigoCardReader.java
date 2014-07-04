package eu.ttbox.nfcproxy.service.nfc.reader.navigo;


import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.emv.Emv41TypeEnum;
import eu.ttbox.nfcparser.emv.parser.EmvTLVList;
import eu.ttbox.nfcparser.emv.parser.TagTLV;
import eu.ttbox.nfcparser.emv.status.Emv41SWLabel;
import eu.ttbox.nfcparser.emv.status.Emv41SWLabelItem;
import eu.ttbox.nfcparser.model.CardResponse;
import eu.ttbox.nfcparser.model.StatusWord;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.service.nfc.NfcConsoleCallback;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;

/**
 * http://www.acbm.com/inedits/pass-transports-commun-secrets.html
 * https://code.google.com/p/cardpeek/wiki/Navigo
 * http://codebutler.com/announcing-farebot-for-android/
 * http://www.journaldulapin.com/2013/02/10/lire-le-contenu-cache-dun-pass-navigo-sur-un-mac/
 *  --> Code JS : https://github.com/elafargue/smart-tools/tree/master/atr/lib
 *  --> https://github.com/elafargue/smart-tools/blob/master/atr/lib/calypso/intercode2.js
 * Code Station : http://aurelienb.pagesperso-orange.fr/HTML/transports/obli_metro.htm
 */
public class NavigoCardReader implements NfcReaderCallback {

    private static final String TAG = "EmvCardReader";

    // Weak reference to prevent retain loop. consolelog is responsible for exiting
    // foreground mode before it becomes invalid (e.g. during onPause() or onStop()).
    private WeakReference<NfcConsoleCallback> consoleLog;


    // ===========================================================
    // Constructor
    // ===========================================================

    public NavigoCardReader(NfcConsoleCallback consoleLogCallback) {
        this.consoleLog = new WeakReference<NfcConsoleCallback>(consoleLogCallback);
    }


    // ===========================================================
    // Tag Connector
    // ===========================================================

    @Override
    public void onTagDiscovered(Tag tag) {
        byte[] tagId = tag.getId();
        logOnTagDiscovered(tagId);
        Log.d(TAG, "New tag discovered : " + NumUtil.byte2Hex(tagId));
        log("Tag Id", tagId);
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                // Connect Tag
                isoDep.connect();
                // Read Tags
                onTagConnected(isoDep);
                // Close Tags
                isoDep.close();
                logOnTagClose(tagId);
            } catch (IOException e) {
                String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                Log.e(TAG, "Error reading nfc : " + errorMessage, e);
            }
        }
    }


    // ===========================================================
    // Constructor
    // ===========================================================

    private void onTagConnected(IsoDep isoDep) throws IOException {
        // Select Master File
        selectMasterFile(isoDep);
        selectPseDirectoryNavigo(isoDep);
    }


    private void selectMasterFile(IsoDep isoDep) throws IOException {
        String title = "[Step 0] SELECT FILE Master File (if available)";
        log("[Step 0]", "SELECT FILE Master File (if available)");
        CardResponse fcp = transceive(isoDep, "00 A4 04 00");
        byte[] recv = fcp.getData();
        StatusWord sw = fcp.getStatusWord();
    }


    private void selectPseDirectoryNavigo(IsoDep isoDep) throws IOException {
        // https://github.com/pterjan/cardpeek-navigo/tree/master/dot_cardpeek_dir/scripts
        String fileName = "1TIC.ICA";
        selectPseDirectory(isoDep, fileName);
    }


    /**
     * http://www.openscdp.org/scripts/tutorial/emv/Application%20Selection.html
     *
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    private EmvTLVList selectPseDirectory(IsoDep isoDep, String fileName) throws IOException {
        // [Step 1] Select 1PAY.SYS.DDF01 to get the PSE directory
        log("[Step 1]", "Select " + fileName + " to get the PSE directory");

        byte[] fileNameAsBytes = AscciHelper.toAsciiString2Bytes(fileName);
        String fileNameSize = NumUtil.byte2Hex(new byte[]{(byte) fileNameAsBytes.length});
        String fileNameAsHex = NumUtil.byte2Hex(fileNameAsBytes);
//        byte[] recv = transceive("00 A4 04 00 0E 31 50 41 59 2E 53 59 53 2E 44 44 46 30 31");
        String cmd = "00 A4 04 00 " + fileNameSize + " " + fileNameAsHex + " 00";

        CardResponse card = transceive(isoDep, cmd);
        byte[] recv = card.getData();

        //addText("[Step 1] Select 2PAY.SYS.DDF01 to get the PSE directory");
        //log("[Step 1] Select 2PAY.SYS.DDF01 to get the PSE directory");
        //byte[] recv = transceive("00 A4 04 00 0E 32 50 41 59 2E 53 59 53 2E 44 44 46 30 31 00");


        // Parse Pse Direcory
        // -------------------
        EmvTLVList parsedRecv = new EmvTLVList(recv);
        log(parsedRecv);

//        log("[Step 1] END");
        return parsedRecv;
    }


    // ===========================================================
    // NFC Transceive
    // ===========================================================


    protected CardResponse transceive(IsoDep tagcomm, String command) throws IOException {
        byte[] bytes = NumUtil.hex2Byte(command);
        return transceive(tagcomm, bytes);
    }

    protected CardResponse transceive(IsoDep tagcomm, byte[] bytes) throws IOException {
        Log.d(TAG, "Send: " + NumUtil.byte2Hex(bytes));
        log("Send", bytes);
        byte[] recv = tagcomm.transceive(bytes);
        Log.d(TAG, "Received: " + NumUtil.byte2Hex(bytes));
        log("Recv", recv);

        // Log Datas
        byte[] recvData = TLVParser.getData(recv);
        if (recv.length > 2) {
            Log.d(TAG, "Received: " + AscciHelper.toAsciiByte2String(bytes));
        }
        // Create CardResponse
        CardResponse res = new CardResponse(bytes, recv);
//        StatusWord sw = new StatusWord(recv[recv.length - 2], recv[recv.length - 1]);
//        res.setData(recvData);
//        res.setStatusWord(sw);


        // --> error list http://www.eftlab.co.uk/index.php/site-map/knowledge-base/118-apdu-response-list
        // Parse Error
        ArrayList<Emv41SWLabelItem> errors = Emv41SWLabel.getError(recv);
        if (!errors.isEmpty()) {
            for (Emv41SWLabelItem err : errors) {
                Log.d(TAG, "Received: " + NumUtil.byte2Hex(recv) + " ==> " + err);
                StatusWord sw = res.getStatusWord();
                log("SW " + NumUtil.byte2Hex(sw.getSw1()) + NumUtil.byte2Hex(sw.getSw2()), "(" + err.type + ") " + err.desc);
            }
        }

        return res;
    }

    // ===========================================================
    // Console
    // ===========================================================

    private void logOnTagDiscovered(byte[] tagId) {
        NfcConsoleCallback console = consoleLog.get();
        if (console != null) {
            console.onTagDiscovered(tagId);
        }
    }

    private void logOnTagClose(byte[] tagId) {
        NfcConsoleCallback console = consoleLog.get();
        if (console != null) {
            console.onTagClose(tagId);
        }
    }


    private void log(EmvTLVList parsedRecv) {
        for (Map.Entry<Integer, TagTLV> entry : parsedRecv.getMapTagsEntrySet()) {
            TagTLV tag = entry.getValue();
            byte[] tagValue = tag.getTagValue();
            // Search Label
            Emv41Enum emv = Emv41Enum.getByTag(tag);
            String keyLabel;
            String valueLabel;
            if (emv == null) {
                keyLabel = tag.toString();
                valueLabel = Emv41TypeEnum.UNNKOWN.toString(tagValue);
            } else {
                keyLabel = emv.name() + "(" +  tag.getTagIdAsHexString() + ")";
                valueLabel = emv.toString(tagValue);
            }
            log("  ", keyLabel);
            log("  ", valueLabel);

        }
    }


    private void log(String key, byte[] value) {
        log(key, NumUtil.byte2Hex(value));
    }

    private void log(String key, String value) {
        NfcConsoleCallback console = consoleLog.get();
        if (console != null) {
            console.onConsoleLog(key, value);
        }
    }


    // ===========================================================
    // Other
    // ===========================================================


}
