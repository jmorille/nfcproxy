package eu.ttbox.nfcproxy.ui.readernfc;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import eu.ttbox.nfcproxy.ui.readernfc.adapter.NfcConsoleArrayAdapter;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class NfcReaderFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private NfcConsoleArrayAdapter consoleNfc;

    // ===========================================================
    // Static
    // ===========================================================

    // TODO: Rename and change types of parameters
    public static NfcReaderFragment newInstance(String param1, String param2) {
        NfcReaderFragment fragment = new NfcReaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // ===========================================================
    // Constructor
    // ===========================================================


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NfcReaderFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Adapter
        consoleNfc = new NfcConsoleArrayAdapter(getActivity());
// new ArrayAdapter<DummyContent.DummyItem>(getActivity(),  android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS)
        // TODO: Change Adapter to display your content
        setListAdapter(consoleNfc);
    }

    // ===========================================================
    // Life Cycle
    // ===========================================================


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // ===========================================================
    // Action
    // ===========================================================


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //DummyContent.ITEMS.get(position).id
            mListener.onFragmentInteraction(consoleNfc.getItem(position).value);
        }
    }


    // ===========================================================
    // Interface
    // ===========================================================

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    // ===========================================================
    // Other
    // ===========================================================


}
