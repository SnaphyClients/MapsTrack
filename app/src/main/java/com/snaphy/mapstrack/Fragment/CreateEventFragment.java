package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.FetchAddressIntentService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * This Fragment is used to create new event by clicking on "Add(Plus)" button in the home fragment
 *
 */
public class CreateEventFragment extends android.support.v4.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateEventFragment";
    RecyclerView recyclerView;
    @Bind(R.id.fragment_create_event_imagebutton1) ImageButton backButton;
    @Bind(R.id.fragment_create_event_edittext3) EditText dateEdittext;
    fr.ganfra.materialspinner.MaterialSpinner materialSpinner;
    static com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocompleteTextView;
    DisplayContactAdapter displayContactAdapter;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    MainActivity mainActivity;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private GoogleApiClient mGoogleApiClient;



    public CreateEventFragment() {
        // Required empty public constructor
    }

    public static CreateEventFragment newInstance() {
        CreateEventFragment fragment = new CreateEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);

        materialSpinner = (fr.ganfra.materialspinner.MaterialSpinner) view.findViewById(R.id.fragment_create_event_spinner1);
        placesAutocompleteTextView = (com.seatgeek.placesautocomplete.PlacesAutocompleteTextView) view.findViewById(R.id.fragment_create_event_edittext2);
        initializeGooglePlacesApi();
        mGoogleApiClient.connect();
        backButtonClickListener();
        setSpinner();
        datePickerClickListener(view);
        dateEdittext.setKeyListener(null);
        selectPosition();
        return view;
    }

    /**
     * Show contacts button event listener
     */
    @OnClick(R.id.fragment_create_event_imagebutton2) void openContactDialog() {
        setEventDataInAdapter();
        DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
        Holder holder = new ListHolder();
        showOnlyContentDialog(holder, adapter);
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

    /**
     * Set the data in the spinner
     */
    public void setSpinner() {
        String[] ITEMS = {"Marriage", "Birthday", "Meeting", "Party", "Get Togeather", "Baby Shower"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(adapter);
    }

    /**
     * Initialize google place api and last location of the app
     */
    private void initializeGooglePlacesApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
     * When back button is the toolbar is pressed this method is fired
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
     * This method is fired when any date from the date picker is selected
     * @param view
     */
    private void datePickerClickListener(View view) {
        dateEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager im = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(dateEdittext.getWindowToken(), 0);
                DatePickerPopWin pickerPopWin = new DatePickerPopWin(mainActivity, 2000, 2100, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        //handler the result here
                        dateEdittext.setText(dateDesc);
                    }
                });
                pickerPopWin.showPopWin(mainActivity);
            }
        });
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
     * When google api is connected it start intent service from this method
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                return;
            }

            startIntentService();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * This method is responsible to start the service ie..FetchAddressIntentService to fetch
     * the address and display it in edittext
     */
    protected void startIntentService() {
        Intent intent = new Intent(getActivity(),FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        mainActivity.startService(intent);
    }



    /**
     * It is a class which is used to get result received from the service
     * The result is in many forms including
     * Error for no address found,time out etc...
     * Or Address, if correct address is found
     */
    public static class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * This method is fired when result is received from the service
         * @param resultCode
         * @param resultData
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            final String mAddressOutput;
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Log.v(Constants.TAG,mAddressOutput);
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        placesAutocompleteTextView.setText(mAddressOutput);
                    }
                });

            }

        }
    }

    @OnClick(R.id.fragment_create_event_button4) void selectFriends() {
        mainActivity.replaceFragment(R.layout.layout_select_contact,null);
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
