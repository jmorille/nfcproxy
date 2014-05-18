package eu.ttbox.nfcproxy.ui.readernfc.adapter;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eu.ttbox.nfcproxy.R;

public class NfcConsoleArrayAdapter extends ArrayAdapter<NfcConsoleLine> {

    private Context mContext;

    private LayoutInflater mInflater;

    private int mLayout;



    // ===========================================================
    // Constructor
    // ===========================================================



    public NfcConsoleArrayAdapter(Context context) {
        this(context, R.layout.nfccoonsole_adpator);
    }

    public NfcConsoleArrayAdapter(Context context, int layout) {
        super(context, layout);
        this.mLayout = layout;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    // ===========================================================
    // View Init
    // ===========================================================


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NfcConsoleLine item = getItem(position);
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

    public View newView(Context context, NfcConsoleLine item, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);
        // Then populate the ViewHolder
        ViewHolder holder = new ViewHolder();
        holder.keyText = (TextView) view.findViewById(R.id.nfcconsole_key);
        holder.valueText = (TextView) view.findViewById(R.id.nfcconsole_value);
        // and store it inside the layout.
        view.setTag(holder);
        return view;
    }


    // ===========================================================
    // Binding
    // ===========================================================


    public void bindView(View view, Context context, NfcConsoleLine item){
        ViewHolder holder = (ViewHolder) view.getTag();
        // Bind Value
        holder.keyText.setText(item.key);
        holder.valueText.setText(item.value);
    }



    // ===========================================================
    // View Holder
    // ===========================================================


    static class ViewHolder {
        TextView keyText;
        TextView valueText;
    }


}
