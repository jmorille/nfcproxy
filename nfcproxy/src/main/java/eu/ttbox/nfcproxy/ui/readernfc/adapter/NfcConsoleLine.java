package eu.ttbox.nfcproxy.ui.readernfc.adapter;


import eu.ttbox.nfcparser.utils.NumUtil;

public class NfcConsoleLine {

    public String key;

    public String value;

    public NfcConsoleLine(String key, byte[] value) {
        this(key, NumUtil.byte2Hex(value));
    }

    public NfcConsoleLine(String key, String value) {
        this.key = key;
        this.value = value;
    }


}
