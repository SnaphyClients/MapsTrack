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
import com.snaphy.mapstrack.RecyclerItemClickListener;
import com.snaphy.mapstrack.Services.FetchAddressIntentService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    double latitude;
    double longitude;


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
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
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

        homeEventAdapter.notifyDataSetChanged();
        homeLocationAdapter.notifyDataSetChanged();

        recyclerView1.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        EventHomeModel eventHomeModel = eventHomeModelArrayList.get(position);
                        mainActivity.replaceFragment(R.layout.fragment_event_info, null);
                        EventBus.getDefault().postSticky(eventHomeModel, Constants.SHOW_EVENT_INFO);
                    }
                })
        );

        recyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mainActivity.replaceFragment(R.layout.fragment_location_info, null);
                    }
                })
        );

        eventFloatingButtonClickListener();
        locationFloatingButtonClickListener();

        initializeGooglePlacesApi();
        mGoogleApiClient.connect();

        return view;
    }

    /********************************************************* Event Subscribers******************************************/
    /**
     *  Event data is fetched from the server when first initialized
     * @param eventHomeModel
     */
    @Subscriber(tag = EventHomeModel.onResetData)
    private void onInitEvent(ArrayList<EventHomeModel> eventHomeModel) {
        eventHomeModelArrayList.clear();

        for(int i = 0; i<eventHomeModel.size(); i++) {
            eventHomeModelArrayList.add(new EventHomeModel(eventHomeModel.get(i).getEventId(),
                    eventHomeModel.get(i).getEventAddress(), eventHomeModel.get(i).getDescription(),
                    eventHomeModel.get(i).getType(), eventHomeModel.get(i).getDate(),eventHomeModel.get(i).getContacts()));
        }

        homeEventAdapter.notifyDataSetChanged();
    }


    /**
     *  Event data is removed on pressing delete button
     * @param eventHomeModel
     */
    @Subscriber(tag = EventHomeModel.onRemoveData)
    private void onRemoveEvent(EventHomeModel eventHomeModel) {

        for(EventHomeModel element : eventHomeModelArrayList) {

            if(element.getId() == eventHomeModel.getId()) {
                int position = eventHomeModelArrayList.indexOf(element);
                eventHomeModelArrayList.remove(position);
            }
        }
        homeEventAdapter.notifyDataSetChanged();
    }

    /**
     * When data is edited or added this method is called
     * @param eventHomeModel
     */
    @Subscriber(tag = EventHomeModel.onChangeData)
    private void onChangeEvent(EventHomeModel eventHomeModel) {

        if(eventHomeModel.getId() == null) {
            eventHomeModelArrayList.add(new EventHomeModel(eventHomeModel.getEventId(),
                    eventHomeModel.getEventAddress(), eventHomeModel.getDescription(), eventHomeModel.getType(),
                    eventHomeModel.getDate(), eventHomeModel.getContacts()));
            homeEventAdapter.notifyDataSetChanged();
        }

        else {
            for(EventHomeModel element : eventHomeModelArrayList) {

                if(element.getId() == eventHomeModel.getId()) {
                    int position = eventHomeModelArrayList.indexOf(element);
                    eventHomeModelArrayList.set(position, eventHomeModel);
                }
            }
            homeEventAdapter.notifyDataSetChanged();
        }
    }

    /********************************************************* Event Subscribers******************************************/



    /********************************************************* Location Subscribers******************************************/
    /**
     *  Location data is fetched from the server when first initialized
     * @param locationHomeModel
     */
    @Subscriber(tag = LocationHomeModel.onResetData)
    private void onInit(ArrayList<LocationHomeModel> locationHomeModel) {
        locationHomeModelArrayList.clear();

        for(int i = 0; i<locationHomeModel.size(); i++) {
            locationHomeModelArrayList.add(new LocationHomeModel(locationHomeModel.get(i).getLocationName(),
                    locationHomeModel.get(i).getLocationAddress(),
                    locationHomeModel.get(i).getLocationId(),locationHomeModel.get(i).getContacts()));
        }

        homeLocationAdapter.notifyDataSetChanged();
    }


    /**
     *  Location data is removed on pressing delete button
     * @param locationHomeModel
     */
    @Subscriber(tag = LocationHomeModel.onRemoveData)
    private void onRemove(LocationHomeModel locationHomeModel) {

        for(LocationHomeModel element : locationHomeModelArrayList) {

            if(element.getId() == locationHomeModel.getId()) {
                int position = locationHomeModelArrayList.indexOf(element);
                locationHomeModelArrayList.remove(position);
            }
        }
        homeLocationAdapter.notifyDataSetChanged();
    }

    /**
     * When data is edited or added this method is called
     * @param locationHomeModel
     */
    @Subscriber(tag = LocationHomeModel.onChangeData)
    private void onChange(LocationHomeModel locationHomeModel) {

        if(locationHomeModel.getId() == null) {
            locationHomeModelArrayList.add(new LocationHomeModel(locationHomeModel.getLocationName(),
                    locationHomeModel.getLocationAddress(), locationHomeModel.getLocationId(),
                    locationHomeModel.getContacts()));
            homeLocationAdapter.notifyDataSetChanged();
        }

        else {
            for(LocationHomeModel element : locationHomeModelArrayList) {

                if(element.getId() == locationHomeModel.getId()) {
                    int position = locationHomeModelArrayList.indexOf(element);
                    locationHomeModelArrayList.set(position, locationHomeModel);
                }
            }
            homeLocationAdapter.notifyDataSetChanged();
        }
    }

    /********************************************************* Location Subscribers******************************************/


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
            //http://stackoverflow.com/questions/17519198/how-to-get-the-current-location-latitude-and-longitude-in-android
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
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
                        //if(mAddressOutput != null)
                      //  EventBus.getDefault().postSticky(new AddressEvent(mAddressOutput));
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
