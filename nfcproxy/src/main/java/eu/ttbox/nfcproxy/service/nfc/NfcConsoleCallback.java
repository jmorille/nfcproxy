package eu.ttbox.nfcproxy.service.nfc;


import android.nfc.Tag;

public interface NfcConsoleCallback {

    void onTagDiscovered(byte[] tagId);

    void onTagClose(byte[] tagId);

    void onConsoleLog(String key, String value);


}
