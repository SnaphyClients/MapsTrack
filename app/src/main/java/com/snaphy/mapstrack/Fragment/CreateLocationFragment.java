package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * This fragment is used to create location from the "Add(Plus)" button in the home fragment
 */
public class CreateLocationFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateLocationFragment";
    @Bind(R.id.fragment_create_location_imagebutton1) ImageButton backButton;
    static com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocompleteTextView;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    MainActivity mainActivity;

    public CreateLocationFragment() {
        // Required empty public constructor
    }

    public static CreateLocationFragment newInstance() {
        CreateLocationFragment fragment = new CreateLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setEventDataInAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_location, container, false);
        ButterKnife.bind(this, view);
        placesAutocompleteTextView = (com.seatgeek.placesautocomplete.PlacesAutocompleteTextView) view.findViewById(R.id.fragment_create_location_edittext3);
        boolean registered = EventBus.getDefault().isRegistered(this);
        if(!registered) {
            EventBus.getDefault().registerSticky(this);
        }
        backButtonClickListener();
        selectPosition();
        return view;
    }

    /**
     * When certain position is selected from the drop down in auto complete
     * this method is fired
     */
    private void selectPosition() {
        placesAutocompleteTextView.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(Place place) {

            }
        });
    }

    /**
     * Show contacts button event listener
     */
    @OnClick(R.id.fragment_create_location_imagebutton2) void openContactDialog() {
        setEventDataInAdapter();
        DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
        Holder holder = new ListHolder();
        showOnlyContentDialog(holder, adapter);
    }

    private void showOnlyContentDialog(Holder holder, BaseAdapter adapter) {
        final DialogPlus dialog = DialogPlus.newDialog(mainActivity)
                .setContentHolder(holder)
                .setAdapter(adapter)
                . setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {

                    }
                })
                .setCancelable(true)
                .setExpanded(true)
                .create();
        dialog.show();
    }

    public void onEventMainThread(String s){
        placesAutocompleteTextView.setText(s);
    }

    private void backButtonClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
    }

    @OnClick(R.id.fragment_create_location_button1) void selectContact() {
        mainActivity.replaceFragment(R.layout.layout_select_contact, null);
    }


    /**
     * Data in events has been initialize from here
     */
    public void setEventDataInAdapter() {
        displayContactModelArrayList.add(new DisplayContactModel("Ravi Gupta"));
        displayContactModelArrayList.add(new DisplayContactModel("Siddharth Jain"));
        displayContactModelArrayList.add(new DisplayContactModel("Anurag Gupta"));
        displayContactModelArrayList.add(new DisplayContactModel("Robins Gupta"));
        displayContactModelArrayList.add(new DisplayContactModel("Jay Dixit"));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
