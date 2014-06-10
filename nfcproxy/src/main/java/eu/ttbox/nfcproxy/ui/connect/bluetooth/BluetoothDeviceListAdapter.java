package eu.ttbox.nfcproxy.ui.connect.bluetooth;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import eu.ttbox.nfcproxy.R;

public class BluetoothDeviceListAdapter extends ArrayAdapter<BluetoothDevice> {


    private Context mContext;

    private LayoutInflater mInflater;

    private int mLayout;



    // ===========================================================
    // Constructor
    // ===========================================================

    public BluetoothDeviceListAdapter(Context context) {
        this(context, R.layout.bluetooth_device_list_item);
    }

    public BluetoothDeviceListAdapter(Context context, int layout) {
        super(context, layout);
        this.mLayout = layout;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // ===========================================================
    // View Init
    // ===========================================================


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BluetoothDevice item = getItem(position);
        // Create View
        View v;
        if (convertView == null) {
            v = newView(mContext, item, parent);
        } else {
            v = convertView;
        }
        bindView(v, mContext, item);
        return v;
    }



    public View newView(Context context, BluetoothDevice item, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);
        // Then populate the ViewHolder
        ViewHolder holder = new ViewHolder();
        holder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
        holder.deviceName = (TextView) view.findViewById(R.id.device_name);
        // and store it inside the layout.
        view.setTag(holder);
        return view;
    }


    // ===========================================================
    // Binding
    // ===========================================================

    public void bindView(View view, Context context, BluetoothDevice item){
        ViewHolder holder = (ViewHolder) view.getTag();
        // Bind Value
        // Device Name
        final String deviceName = item.getName();
        if (deviceName != null && deviceName.length() > 0) {
            holder.deviceName.setText(deviceName);
        } else {
            holder.deviceName.setText(R.string.unknown_device);

        }
        // Device Address
        holder.deviceAddress.setText(item.getAddress());
    }



    // ===========================================================
    // View Holder
    // ===========================================================

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

}
