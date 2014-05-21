package eu.ttbox.nfcproxy.service.nfc.reader.emv41;


import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.emv.Emv41TypeEnum;
import eu.ttbox.nfcparser.emv.generator.PdolGenerator;
import eu.ttbox.nfcparser.emv.parser.EmvTLVParser;
import eu.ttbox.nfcparser.emv.status.Emv41SWLabel;
import eu.ttbox.nfcparser.emv.status.Emv41SWLabelItem;
import eu.ttbox.nfcparser.model.CardResponse;
import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.model.StatusWord;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.emv.parser.ApplicationFileLocator;
import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.service.nfc.NfcConsoleCallback;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;

public class EmvCardReader implements NfcReaderCallback {

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
        logOnTagDiscovered(tagId);
        Log.d(TAG, "New tag discovered : " + NumUtil.byte2Hex(tagId));
        log("Reading Tag Id", tagId);
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();
                onTagConnected(isoDep);
                //Close Tag
                isoDep.close();
                logOnTagClose(tagId);
            } catch (IOException e) {
                String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                Log.e(TAG, "Error reading nfc : " + errorMessage, e);
            }
        }
    }


    // ===========================================================
    // Nfc reader
    // ===========================================================

    private void onTagConnected(IsoDep isoDep) throws IOException {
        // Select Master File
        selectMasterFile(isoDep);
        PseDirectory pseDir = selectPseDirectory(isoDep);
        // SFI data
        // Application app = readPseRecord(  isoDep,pseDir);
        // Aid  Record
        readAllAidRecord(isoDep, pseDir);

    }

    private void selectMasterFile(IsoDep isoDep) throws IOException {
        String title = "[Step 0] SELECT FILE Master File (if available)";
        log("[Step 0]", "SELECT FILE Master File (if available)");
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

        return selectPseDirectory(isoDep, fileName);
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
 //       byte[] dfName = parsedRecv.getTlvValue("84");
 //       String dfNameString = AscciHelper.toAsciiByte2String(dfName);
//        log("DF Name : " , NumUtil.byte2Hex(dfName) + " ==> " + dfNameString);
 //        log("DF Name", dfNameString);

//
//        byte[] sfi = parsedRecv.getTlvValue("88");
//        byte[] lang = parsedRecv.getTlvValue("5F2D");
//        String langValue = AscciHelper.toAsciiByte2String(lang);
//        log("Lang", langValue);
//        log("sfi", NumUtil.byte2Hex(sfi));

        PseDirectory result = new PseDirectory(parsedRecv);
//        result.lang = langValue;
//        result.dfName = dfNameString;
//        result.fsi = sfi[0];
//
//        log("[Step 1] END");
        return result;
    }


    public Application readPseRecord(IsoDep isoDep, PseDirectory pseDirectory) throws IOException {
        // TODO select in funtion of EMV_v4.3_Book_1_ICC_to_Terminal_Interface_2012060705394541.pdf page 129
        // TODO http://dexterous-programmer.blogspot.fr/2012/04/emv-transaction-step-1-application.html
        log("[Step 2]", "Send READ RECORD with 0 to find out where the record is");

        String sfi = NumUtil.byte2Hex((byte) ((pseDirectory.fsi << 3) | 4));
        String cmd = "00 B2 01 " + sfi + " 00";
        CardResponse card = transceive(isoDep, cmd);
        byte[] recv = card.getData();


        // Parse Read Pse Record
        // -------------------
        //  recv[0] == 0x70
        //  recv[1] == Lenght
        byte[] application = Arrays.copyOfRange(recv, 2, 2 + recv[1]);
        log("Application", NumUtil.byte2Hex(application));

        // Lenght of directory entry 1
        //  application[0] == 0x61
        byte lenghtDirectoryOne = application[1];
        // Application Id
        //  application[2] == 0x4F
        int appIdSize = NumUtil.getUnsignedValue(  application[3]);
        //  addText("appIdSize", NumUtil.toHexString(new byte[]{application[3]}));
        int appIdLenght = 4 + appIdSize;
        byte[] appId = Arrays.copyOfRange(application, 4, appIdLenght);
        log("App ID", NumUtil.byte2Hex(appId));

        // application[appIdLenght+1] == 0x50
        int appLabelSize = NumUtil.getUnsignedValue( application[appIdLenght + 1]);
        //  addText("appLabelSize", NumUtil.toHexString(new byte[]{appLabelSize}));
        int appLabelLenght = appIdLenght + appLabelSize + 2;

        byte[] appLabel = Arrays.copyOfRange(application, appIdLenght + 2, appLabelLenght);
        String appLabelString = AscciHelper.toAsciiByte2String(appLabel);
        // addText("App Label", NumUtil.toHexString(appLabel));
        log("App Label", appLabelString);

        Application app = new Application();
        app.appLabel = appLabelString;
        app.appid = appId;
        return app;
    }

    /**
     * http://dexterous-programmer.blogspot.fr/2012/04/emv-transaction-step-1-application.html
     */
    public void readAllAidRecord(IsoDep isoDep, PseDirectory pseDirectory) throws IOException {
        byte[] aid = pseDirectory.getAid();
        if (aid != null) {
            String aidSize = NumUtil.byte2Hex(new byte[]{(byte) aid.length});
            String aidAsHex = NumUtil.byte2Hex(aid);

            log("[Step 2]", "Select Aid " + aidAsHex);

            String cmd = "00 A4 04 00 " + aidSize + " " + aidAsHex + " 00";
            CardResponse card = transceive(isoDep, cmd);

            // TODO PayCardTLVParser
            EmvTLVParser aidResponse = new EmvTLVParser(card);
            log(aidResponse);

            byte[] pdol = aidResponse.getTlvValue(Emv41Enum.PDOL);
            if (pdol!=null) {

                ArrayList<RecvTag> parsedPdol = TLVParser.parseDataObjectList(pdol);
                for (RecvTag pdolTag : parsedPdol){
                    log("  pdol " , pdolTag.toString()  );
                }

                PdolGenerator pdolGen = new PdolGenerator(pdol);
                getGPO(isoDep, pdolGen);


            }
            if (false &&  card.isSuccess()) {

                log("[Step 3]", "Read All Aid Record of Aid " + aidAsHex);


                for (int sfi = 1; sfi <= 31; sfi++) {
                    log("Read record", "sfi " + sfi + "/" + 31);
                    for (int rec = 1; rec <= 16; rec++) {
                        byte[] readCmd = new byte[]{0x00, (byte) 0xB2, (byte) rec, (byte) ((sfi << 3) | 4), 0x00};
                        CardResponse tlv = transceive(isoDep, readCmd, false);

                        if (tlv.isSuccess()) {
                            log("    Read", "SFI " + sfi + " record #" + rec);
                            //log(tlv);
                            byte[] tlvData = tlv.getData();
                            EmvTLVParser record = new EmvTLVParser(tlvData);
                            log(record);
                        }
                    }
                }
            }
        }

    }


    public void getGPO(IsoDep isoDep, PdolGenerator pdolGen) throws IOException {
        //http://stackoverflow.com/questions/15059580/reading-emv-card-using-ppse-and-not-pse
        // http://www.acbm.com/inedits/cartes-bancaires-sans-contact.html

        log("[Step 5]", "Send GET PROCESSING OPTIONS command");


        byte[] pdolTlv = new byte[]{(byte) 0x83};
        if (pdolGen != null) {
            byte[] pdolValues = pdolGen.generatePdolRequestData();
            // Pdol TLV
            pdolTlv = new byte[pdolValues.length + 2];
            pdolTlv[0] = (byte) 0x83;
            pdolTlv[1] = (byte) pdolValues.length;
            System.arraycopy(pdolValues, 0, pdolTlv, 2, pdolValues.length);
//            pdol = "83 " + NumUtil.toHexString(new byte[] {(byte)pdolValues.length}) + " "  + NumUtil.toHexString(pdolValues);
        }
        // Lc-Data-Le
        byte[] lcDataLe = new byte[pdolTlv.length + 2];
        lcDataLe[0] = (byte) (pdolTlv.length);
        lcDataLe[lcDataLe.length - 1] = 0x00;
        System.arraycopy(pdolTlv, 0, lcDataLe, 1, pdolTlv.length);


        //       80A800000483020804
        CardResponse card = transceive(isoDep, "80 A8 00 00 " + NumUtil.byte2Hex(lcDataLe));
       if (card.isSuccess()) {
            ApplicationFileLocator afl = new ApplicationFileLocator(card.getData());
            readRecordAFL(isoDep, afl);
       }


       // return afl;
        //TODO [Step 5] = Send GET PROCESSING OPTIONS command
        // Send = 80 A8 00 00 23 83 21 32 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 02 50 00 00 00 00 00 09 78 14 06 28 00 E4 EC 9E 52 00
        // Recv = 77 81 BE 82 02 20 00 94 0C 18 01 01 01 10 03 03 00 10 01 02 00 9F 6C 02 10 00 9F 5D 06 00 00 00 02 79 28 9F 4B 81 80 80 D4 BB FE 20 C1 5A F9 B0 86 E9 41 97 E1 3B 53 8F 44 20 50 F5 01 40 14 08 1D C4 FF F6 BD 18 6A 74 21 5C 55 69 F1 B6 43 69 46 98 CF 8A 6A 07 D6 8A F4 01 93 3E 56 EE 9F 2F A9 2F 71 C8 8E C6 25 98 36 4D DD 70 21 8C EF 2B 39 7F C3 8D 4F BF 24 FA 04 89 3D B2 88 E1 AA E0 9B 1B 98 54 33 B7 44 C5 6A FD 48 81 C6 D2 1D 88 A8 A7 CC C8 3F 9B 00 EA 4F 40 0B DC 16 4D F5 F4 92 6A 86 F0 76 8E 75 9F 26 08 37 D8 86 4C 18 46 30 67 9F 36 02 01 C0 9F 10 07 06 05 0A 03 90 00 00 90 00

    }


    public void readRecordAFL(IsoDep isoDep, ApplicationFileLocator afl) throws IOException {

        log("[Step 6]", "Send READ RECORD with 0 to find out where the record is");

        for (ApplicationFileLocator.AflRecord record : afl.records) {
            int sfi = record.sfi;
            int begin = record.recordNumberBegin;
            byte[] cmd = Iso7816Commands.readRecord(begin, sfi);
            CardResponse card = transceive(  isoDep, cmd);

            if (card.isSuccess()) {
                log(card);
                EmvTLVParser parsed = new EmvTLVParser(card.getData());
                log(parsed);
            }

        }

    }

    // ===========================================================
    // NFC Transceive
    // ===========================================================


    protected CardResponse transceive(IsoDep tagcomm, String command) throws IOException {
        byte[] bytes = NumUtil.hex2Byte(command);
        return transceive(tagcomm, bytes);
    }

    protected CardResponse transceive(IsoDep tagcomm, byte[] bytes) throws IOException {
        return transceive(tagcomm, bytes, true);
    }

    protected CardResponse transceive(IsoDep tagcomm, byte[] bytes, boolean logIt) throws IOException {
        if (logIt) {
            log("Send", bytes);
        }
        byte[] recv = tagcomm.transceive(bytes);
        if (logIt) {
            log("Recv", recv);
        }
        // Log Datas
        byte[] recvData = TLVParser.getData(recv);
        if (logIt && recv.length > 2) {
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
        if ( !errors.isEmpty()) {
            for (Emv41SWLabelItem err : errors) {
               // Log.d(TAG, "Received: " + NumUtil.byte2Hex(recv) + " ==> " + err);
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
            log("  ", keyLabel);
            log("  ", valueLabel);

        }
    }

    private void log( CardResponse cardResponse) {
        log("Send", cardResponse.getCommandBytes());
        log("Recv", cardResponse.getBytes());
    }


    private void log(String key, byte[] value) {
        log(key, NumUtil.byte2Hex(value));
    }

    private void log(String key, String value) {
        Log.d(TAG, key + " = " + value);
        // Display Console
        NfcConsoleCallback console = consoleLog.get();
        if (console != null) {
            console.onConsoleLog(key, value);
        }
    }


    // ===========================================================
    // Other
    // ===========================================================


}
