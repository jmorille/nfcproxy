package eu.ttbox.nfcproxy.service.nfc;


import android.nfc.Tag;

public interface NfcReaderCallback {

    public void onTagDiscovered(Tag tag);

}
