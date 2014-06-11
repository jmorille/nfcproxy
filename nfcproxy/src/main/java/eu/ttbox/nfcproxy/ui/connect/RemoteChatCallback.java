package eu.ttbox.nfcproxy.ui.connect;


import android.bluetooth.BluetoothDevice;

public interface RemoteChatCallback {

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    void setState(int state);

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param deviceName The hDevice that has been connected
     */
    void connected(String deviceName);

    void connectionFailed();

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    void connectionLost();


    /**
     * Write to the connected OutStream.
     *
     * @param buffer The bytes to write
     */
    void write(byte[] buffer);

    int read(int byteCount, byte[] buffer);
}
