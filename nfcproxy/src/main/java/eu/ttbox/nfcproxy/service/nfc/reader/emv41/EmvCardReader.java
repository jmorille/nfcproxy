package eu.ttbox.nfcproxy.service.nfc.reader.emv41;


import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.emv.Emv41TypeEnum;
import eu.ttbox.nfcparser.emv.parser.EmvTLVParser;
import eu.ttbox.nfcparser.emv.status.Err;
import eu.ttbox.nfcparser.emv.status.Errors;
import eu.ttbox.nfcparser.model.CardResponse;
import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.model.StatusWord;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.service.nfc.NfcConsoleCallback;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;

public class EmvCardReader  implements NfcReaderCallback {

    private static final String TAG = "EmvCardReader";

    // Weak reference to prevent retain loop. consolelog is responsible for exiting
    // foreground mode before it becomes invalid (e.g. during onPause() or onStop()).
    private WeakReference<NfcConsoleCallback> consoleLog;


    // ===========================================================
    // Constructor
    // ===========================================================

    public EmvCardReader(NfcConsoleCallback consoleLogCallback) {
        this.consoleLog = new WeakReference<NfcConsoleCallback>(consoleLogCallback);
    }



    // ===========================================================
    // Tag Connector
    // ===========================================================

    @Override
    public void onTagDiscovered(Tag tag) {
        byte[] tagId = tag.getId();
        Log.d(TAG, "New tag discovered : " + NumUtil.byte2Hex(tagId));
        log("Tag Id", tagId);
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();
                onTagConnected(isoDep);
                //Close Tag
                isoDep.close();
            } catch (IOException e) {
                String errorMessage = e.getMessage() !=null? e.getMessage() : e.getClass().getSimpleName();
                Log.e(TAG, "Error reading nfc : " + errorMessage, e);
            }
        }
    }


    // ===========================================================
    // Nfc reader
    // ===========================================================

    private void onTagConnected( IsoDep isoDep) throws IOException{
         // Select Master File
        selectMasterFile(isoDep);
        PseDirectory pseDir = selectPseDirectory(isoDep);
        // SFI data
        //Application app = readPseRecord(  isoDep,pseDirectory);
        // Aid  Record
        readAllAidRecord(  isoDep,pseDir);

    }

    private void selectMasterFile(IsoDep isoDep) throws IOException {
        String title = "[Step 0] SELECT FILE Master File (if available)";
        log("[Step 0]", "SELECT FILE Master File (if available)" );
        CardResponse fcp = transceive(isoDep, "00 A4 04 00");
        byte[] recv = fcp.getData();
        StatusWord sw = fcp.getStatusWord();
    }



    private PseDirectory selectPseDirectory(IsoDep isoDep) throws IOException {
        // http://dexterous-programmer.blogspot.fr/2012/04/emv-transaction-step-1-application.html
        //In case the card is an NFC card then it will have PPSE (Paypass Payment System Environment) as
        // "2PAY.SYS.DDF01" and not "1PAY.SYS.DDF01"
        //  String fileName = "1PAY.SYS.DDF01";
        String fileName = "2PAY.SYS.DDF01";
        // String fileName = "1ADDF010";
        // new ISOSelect(ISOSelect.SELECT_AID, EMV4_1.AID_1PAY_SYS_DDF01);

        return selectPseDirectory(  isoDep, fileName);
    }

    /**
     * http://www.openscdp.org/scripts/tutorial/emv/Application%20Selection.html
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private PseDirectory selectPseDirectory(IsoDep isoDep, String fileName) throws IOException {
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
        EmvTLVParser parsedRecv = new EmvTLVParser(recv);
        log(parsedRecv);

        // DF Name
//        byte[] dfName = parsedRecv.getTlvValue("84");
//        String dfNameString = AscciHelper.toAsciiByte2String(dfName);
//        log("DF Name : " + NumUtil.hex2Byte(dfName) + " ==> " + dfNameString);
//        addText("DF Name", dfNameString);
//
//
//        byte[] sfi = parsedRecv.getTlvValue("88");
//        byte[] lang = parsedRecv.getTlvValue( "5F2D");
//        String langValue = AscciHelper.toAsciiByte2String(lang);
//        addText("Lang", langValue);
//        addText("sfi", NumUtil.toHexString(sfi));
//
         PseDirectory result = new PseDirectory(parsedRecv);
//        result.lang = langValue;
//        result.dfName = dfNameString;
//        result.fsi = sfi[0];
//
//        log("[Step 1] END");
        return result;
    }




    /**
     * http://dexterous-programmer.blogspot.fr/2012/04/emv-transaction-step-1-application.html
     */
    public void readAllAidRecord(IsoDep isoDep,PseDirectory pseDirectory) throws IOException {
        byte[] aid = pseDirectory.getAid();
        if (aid != null) {
            String aidSize = NumUtil.byte2Hex(new byte[]{(byte) aid.length});
            String aidAsHex = NumUtil.byte2Hex(aid);

           log("[Step 1]", "Select Aid " + aidAsHex  );

            String cmd = "00 A4 04 00 " + aidSize + " " + aidAsHex + " 00";
            CardResponse card = transceive(isoDep, cmd);

            // TODO PayCardTLVParser
            EmvTLVParser aidResponse =  new EmvTLVParser(card);
            log(aidResponse);

            if (card.isSuccess()) {

                log("[Step 1]", "Read All Aid Record of Aid " + aidAsHex  );


                for (int sfi = 1; sfi <= 31; sfi++) {
                    for (int rec = 1; rec <= 16; rec++) {
                        byte[] readCmd = new byte[] {0x00, (byte)0xB2, (byte)rec, (byte)((sfi << 3) | 4), 0x00 };
                        CardResponse tlv = transceive(isoDep, readCmd);
                        if (tlv.isSuccess()) {
                            log("    Read", "SFI " + sfi  + " record #" + rec);
                            byte[] tlvData =  tlv.getData();
                            EmvTLVParser record = new EmvTLVParser(tlvData);
                            log(record);
                        }
                    }
                }
            }
        }

    }


    // ===========================================================
    // NFC Transceive
    // ===========================================================



    protected CardResponse transceive(IsoDep tagcomm, String command) throws IOException {
        byte[] bytes = NumUtil.hex2Byte(command);
        return  transceive(tagcomm, bytes);
    }

    protected CardResponse transceive(IsoDep tagcomm, byte[] bytes) throws IOException {
        log("Send", bytes);
        byte[] recv = tagcomm.transceive(bytes);
        log("Recv", recv);

        // Log Datas
        byte[] recvData = TLVParser.getData(recv);
        if (recv.length > 2) {
            Log.d(TAG, "Received: " + AscciHelper.toAsciiByte2String(bytes));
        }
        // Create CardResponse
        CardResponse res = new CardResponse();
        StatusWord sw = new StatusWord(recv[recv.length - 2], recv[recv.length - 1]);
        res.setData(recvData);
        res.setStatusWord(sw);


        // --> error list http://www.eftlab.co.uk/index.php/site-map/knowledge-base/118-apdu-response-list
        // Parse Error
        ArrayList<Err> errors = Errors.getError(recv);
        if (!errors.isEmpty()) {
            for (Err err : errors) {
                Log.d(TAG, "Received: " + NumUtil.byte2Hex(recv) + " ==> " + err);
                log("SW " + NumUtil.byte2Hex(sw.getSw1()) + NumUtil.byte2Hex(sw.getSw2()), "(" + err.type + ") "  + err.desc );
            }
        }

        return res;
    }

    // ===========================================================
    // Console
    // ===========================================================


    private void log(EmvTLVParser parsedRecv) {
        for (Map.Entry<RecvTag, byte[]> entry : parsedRecv.entrySet()) {
            RecvTag tag = entry.getKey();
            byte[] tagValue = entry.getValue();
            // Search Label
            Emv41Enum emv = Emv41Enum.getByTag(tag);
            String keyLabel;
            String valueLabel;
            if (emv == null) {
                keyLabel = tag.toString();
                valueLabel = Emv41TypeEnum.UNNKOWN.toString(tagValue);
            } else {
                keyLabel = emv.name() + "(" + NumUtil.byte2HexNoSpace(tag.key) + ")";
                valueLabel = emv.toString(tagValue);
            }
            log("  " ,keyLabel);
            log("  " ,valueLabel);

        }
    }


    private void log(String key, byte[] value) {
        log(key, NumUtil.byte2Hex(value));
    }

    private void log(String key, String value) {
        Log.d(TAG, key + " = " + value);
        // Display Console
        NfcConsoleCallback console = consoleLog.get();
        if (console!=null) {
            console.onConsoleLog(key, value);
        }
    }


    // ===========================================================
    // Other
    // ===========================================================


}
