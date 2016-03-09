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
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
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
    HashMap<String,Double> latLongHashMap = new HashMap<String, Double>();

    @Bind(R.id.fragment_create_location_imagebutton1) ImageButton backButton;
    @Bind(R.id.fragment_create_location_edittext1) EditText locationName;
    @Bind(R.id.fragment_create_location_edittext2) EditText locationId;
    @Bind(R.id.fragment_create_location_edittext3) EditText locationAddress;

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

        backButtonClickListener();
        return view;
    }


    /**
     * Show contacts button event listener
     */
    @OnClick(R.id.fragment_create_location_imagebutton2) void openContactDialog() {
        DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
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

    @Subscriber(tag = Constants.SEND_ADDRESS_LOCATION)
    private void setAddress(String address) {
       // placesAutocompleteTextView.setText(address);
    }

    @Subscriber(tag = Constants.SEND_LOCATION_LATLONG)
    private void setLatLong(LatLng latLong) {
        latLongHashMap.put("latitude",latLong.latitude);
        latLongHashMap.put("longitude", latLong.longitude);
    }

    @Subscriber ( tag = Constants.SEND_SELECTED_CONTACT_TO_CREATE_LOCATION_FRAGMENT)
    public void saveSelectedContacts(ArrayList<ContactModel> contactModelArrayList) {
        for(ContactModel contactModel : contactModelArrayList) {
            if(contactModel.isSelected()) {
                displayContactModelArrayList.add(new DisplayContactModel(contactModel.getContactName()));
            }
        }
    }

    @Subscriber(tag = Constants.SHOW_LOCATION_EDIT)
    private void onEdit(LocationHomeModel locationHomeModel) {

        //TODO Update data will be called when create event fragment is called from event info
        locationName.setText(locationHomeModel.getLocationName());
        locationId.setText(locationHomeModel.getLocationId());
        locationAddress.setText(locationHomeModel.getLocationAddress());

        displayContactModelArrayList.clear();
        for(int i = 0; i<locationHomeModel.getContacts().size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(locationHomeModel.getContacts().get(i).getContactName()));
        }
    }

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
        mainActivity.replaceFragment(R.id.fragment_create_location_button1, null);
    }

    @OnClick(R.id.fragment_create_location_button5) void setLocation() {
        mainActivity.replaceFragment(R.id.fragment_create_location_button5, null);
    }

    /**
     * When publish event button is clicked
     */
    @OnClick(R.id.fragment_create_location_button2) void publishLocation() {

        temporaryContactDatabases = new Select().from(TemporaryContactDatabase.class).execute();
        for (int i = 0; i<temporaryContactDatabases.size(); i++) {
            selectContactModelArrayList.add(new SelectContactModel(null,temporaryContactDatabases.get(i).name,
                    temporaryContactDatabases.get(i).number));
        }

        LocationHomeModel locationHomeModel =  new LocationHomeModel(null,locationName.getText().toString(),
                locationAddress.getText().toString(),
                locationId.getText().toString(), selectContactModelArrayList, latLongHashMap);

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

        temporaryContactDatabases = new Select().from(TemporaryContactDatabase.class).execute();
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
