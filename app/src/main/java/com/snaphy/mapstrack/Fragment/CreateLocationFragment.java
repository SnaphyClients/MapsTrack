package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.activeandroid.query.Select;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment is used to create location from the "Add(Plus)" button in the home fragment
 */
public class CreateLocationFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateLocationFragment";

    @Bind(R.id.fragment_create_location_imagebutton1) ImageButton backButton;
    @Bind(R.id.fragment_create_location_edittext1) EditText locationName;
    @Bind(R.id.fragment_create_location_edittext2) EditText locationId;

    static com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocompleteTextView;
    MainActivity mainActivity;

    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    ArrayList<SelectContactModel> selectContactModelArrayList = new ArrayList<SelectContactModel>();
    List<TemporaryContactDatabase> temporaryContactDatabases;

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
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_location, container, false);
        ButterKnife.bind(this, view);

        temporaryContactDatabases = new Select().from(TemporaryContactDatabase.class).execute();

        placesAutocompleteTextView = (com.seatgeek.placesautocomplete.PlacesAutocompleteTextView) view.findViewById(R.id.fragment_create_location_edittext3);

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
        DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
        setLocationDataInAdapter();
        Holder holder = new ListHolder();
        showOnlyContentDialog(holder, adapter);
    }

    /**
     * Open dialog showing list of selected contacts
     * @param holder
     * @param adapter
     */
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

   /* public void onEvent(AddressEvent event){
        placesAutocompleteTextView.setText(event.getAddress());
    }*/

    /**
     * When back button is pressed in fragment
     */
    private void backButtonClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
    }

    /**
     * Contact are selected from list of avialable contact in phone contact
     */
    @OnClick(R.id.fragment_create_location_button1) void selectContact() {
        mainActivity.replaceFragment(R.layout.layout_select_contact, null);
    }

    /**
     * When publish event button is clicked
     */
    @OnClick(R.id.fragment_create_location_button2) void publishEvent() {

        for (int i = 0; i<temporaryContactDatabases.size(); i++) {
            selectContactModelArrayList.add(new SelectContactModel(temporaryContactDatabases.get(i).name,
                    temporaryContactDatabases.get(i).number));
        }

        LocationHomeModel locationHomeModel =  new LocationHomeModel(locationName.getText().toString(),
                placesAutocompleteTextView.getText().toString(),
                locationId.getText().toString(), selectContactModelArrayList);

        EventBus.getDefault().post(locationHomeModel, LocationHomeModel.onSave);
        TemporaryContactDatabase.deleteAll();
        //TODO check its occurance
        setLocationDataInAdapter();
        mainActivity.onBackPressed();

    }


    /**
     * Data in events has been initialize from here
     */
    public void setLocationDataInAdapter() {

        displayContactModelArrayList.clear();
        for(int i = 0; i<temporaryContactDatabases.size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(temporaryContactDatabases.get(i).name));
        }

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
