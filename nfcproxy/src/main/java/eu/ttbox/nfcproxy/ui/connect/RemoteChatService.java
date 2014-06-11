package eu.ttbox.nfcproxy.ui.connect;


public interface RemoteChatService {

    public static final String EXTRAS_DEVICE_NAME = "EXTRAS_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "EXTRAS_DEVICE_ADDRESS";


    void connectByAddress(String deviceAddress, boolean secure);
}
