package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.snaphy.mapstrack.Adapter.HomeEventAdapter;
import com.snaphy.mapstrack.Adapter.HomeLocationAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.FetchAddressIntentService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * It is a home fragment and it contains all the elements in the home page
 * ie... two recycler views, two floating buttons
 */
public class HomeFragment extends android.support.v4.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_home_recycler_view1) RecyclerView recyclerView1;
    @Bind(R.id.fragment_home_recycler_view2) RecyclerView recyclerView2;
    @Bind(R.id.fragment_home_textview1) TextView eventText;
    @Bind(R.id.fragment_home_textview2) TextView locationText;
    @Bind(R.id.fragment_cart_floating_button1) android.support.design.widget.FloatingActionButton floatingActionButton1;
    @Bind(R.id.fragment_cart_floating_button2) android.support.design.widget.FloatingActionButton floatingActionButton2;
    HomeEventAdapter homeEventAdapter;
    HomeLocationAdapter homeLocationAdapter;
    ArrayList<EventHomeModel> eventHomeModelArrayList = new ArrayList<EventHomeModel>();
    ArrayList<LocationHomeModel> locationHomeModelArrayList = new ArrayList<LocationHomeModel>();
    MainActivity mainActivity;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<String> contacts  = new ArrayList<String>();


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEventDataInAdapter();
        setLocationDataInAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Hobo.ttf");
        eventText.setTypeface(typeface);
        locationText.setTypeface(typeface);


        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        homeEventAdapter = new HomeEventAdapter(mainActivity,eventHomeModelArrayList,mainActivity);
        homeLocationAdapter = new HomeLocationAdapter(mainActivity,locationHomeModelArrayList);

        recyclerView1.setAdapter(homeEventAdapter);
        recyclerView2.setAdapter(homeLocationAdapter);

        eventFloatingButtonClickListener();
        locationFloatingButtonClickListener();

        initializeGooglePlacesApi();
        mGoogleApiClient.connect();

        return view;
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
     * Fire when create event button is clicked
     */
    private void locationFloatingButtonClickListener() {
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_cart_floating_button1, null);
            }
        });

    }

    /**
     * Fire when create location button is clicked
     */
    private void eventFloatingButtonClickListener() {
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_cart_floating_button2, null);
            }
        });

    }

    /**
     * Data in location has been initialize from here
     */
    public void setLocationDataInAdapter() {
        locationHomeModelArrayList.add(new LocationHomeModel("Shanti Niketan", "DLF Phase 3, Gurgaon"));
        locationHomeModelArrayList.add(new LocationHomeModel("Anu Office", "Palam Vihar, Sector 25"));
        locationHomeModelArrayList.add(new LocationHomeModel("My Party", "Pitampura, Haryana"));
        locationHomeModelArrayList.add(new LocationHomeModel("Ravi House","Cyber Hub, Cyber City"));
        locationHomeModelArrayList.add(new LocationHomeModel("Sid Home", "Sahara Mall, Sikandarpur"));
    }

    /**
     * Data in event has been initialize from here
     */
    public void setEventDataInAdapter() {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        addDataToArrayList();

        eventHomeModelArrayList.add(new EventHomeModel("Ravi123","DLF Phase 3, Gurgaon","Explore public service through this popular networking and recruiting program." +
                " The Government Careers Forum will feature a keynote presentation by" +
                " Massachusetts State Representative Tackey Chan ’95, followed by round table" +
                " networking sessions for students, alumni, faculty and staff with agency" +
                " representatives","Birthday", dateFormat, contacts));


        eventHomeModelArrayList.add(new EventHomeModel("SidHome","Palam Vihar, Sector 25","Paul Romer, a prominent American economist and specialist on the theory of " +
                "growth and innovation, will discuss charter cities and their potential impact on " +
                "economic prosperity","Baby Shower", dateFormat, contacts));


        eventHomeModelArrayList.add(new EventHomeModel("RaghuMarriage","Pitampura, Haryana","Experience Virginia Woolf’s darkly elegant voice in an original stage adaptation " +
                "of four compelling short stories.","Party", dateFormat, contacts));


        eventHomeModelArrayList.add(new EventHomeModel("RobParty","Cyber Hub, Cyber City","Debra Granik '85 will screen and discuss her best-known work to date – the " +
                "Oscar-nominated film \"Winter's Bone.\"","Marriage", dateFormat, contacts));


        eventHomeModelArrayList.add(new EventHomeModel("AnuOffice", "Sahara Mall, Sikandarpur","Granik will take questions from the audience after the screening. " +
                "This event is sponsored by the Film, Television and Interactive Media Program " +
                "and the Edie and Lew Wasserman Fund.","Meeting", dateFormat, contacts));
    }

    public void addDataToArrayList() {
        contacts.add(0,"Ravi Gupta");
        contacts.add(1,"Siddarth Jain");
        contacts.add(2,"Robins Gupta");
        contacts.add(3,"Neha Jain");
        contacts.add(4,"Monika");
        contacts.add(5,"Anurag Gupta");
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
            Log.v(Constants.TAG, mAddressOutput);
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mAddressOutput != null)
                        EventBus.getDefault().postSticky(mAddressOutput);
                    }
                });

            }

        }
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
