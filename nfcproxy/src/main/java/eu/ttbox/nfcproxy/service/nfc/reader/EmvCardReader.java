package eu.ttbox.nfcproxy.service.nfc.reader;


import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import eu.ttbox.nfcparser.emv.status.Err;
import eu.ttbox.nfcparser.emv.status.Errors;
import eu.ttbox.nfcparser.model.CardResponse;
import eu.ttbox.nfcparser.model.StatusWord;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcproxy.R;
import eu.ttbox.nfcproxy.service.nfc.NfcReaderCallback;

public class EmvCardReader  implements NfcReaderCallback {

    private static final String TAG = "EmvCardReader";

    @Override
    public void onTagDiscovered(Tag tag) {
        byte[] tagId = tag.getId();
        Log.d(TAG, "New tag discovered : " + NumUtil.byte2Hex(tagId));
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();
            } catch (IOException e) {
                String errorMessage = e.getMessage() !=null? e.getMessage() : e.getClass().getSimpleName();
                Log.e(TAG, "Error reading nfc : " + errorMessage, e);
            }
        }
    }


    private void onTagConnected( IsoDep isoDep) {

    }




    protected CardResponse transceive(IsoDep tagcomm, byte[] bytes) throws IOException {
        Log.d(TAG, "Send: " + NumUtil.byte2Hex(bytes) );
        byte[] recv = tagcomm.transceive(bytes);
        Log.d(TAG, "Received: " + NumUtil.byte2Hex(bytes) );

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
            }
        } else {
            Log.d(TAG, "Received: " + NumUtil.byte2Hex(recv));
        }

        return res;
    }
}
