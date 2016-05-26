package com.snaphy.mapstrack.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener{

    private OnFragmentInteractionListener mListener;
    public static String TAG = "ShowMapFragment";
    SupportMapFragment mapFragment;
    MainActivity mainActivity;
    @Bind(R.id.fragment_map_textview1) TextView distance;
    @Bind(R.id.fragment_map_textview2) TextView time;
    @Bind(R.id.fragment_map_button1)
    Button openNavigation;
    String serverKey = "AIzaSyBMKfG1V911wOn3sx3cvhx01OsDqKzOmrs";
    LatLng position;
    LatLng destination;
    LatLngBounds latLngBounds;
    GoogleMap googleMap;


    public ShowMapFragment() {
        // Required empty public constructor
    }

    public static ShowMapFragment newInstance() {
        ShowMapFragment fragment = new ShowMapFragment();
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.fragment_map_button1) void openNavigation() {
        mainActivity.onBackPressed();
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+destination.latitude+","+destination.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Subscriber(tag = Constants.SEND_MAP_COORDINATES_EVENT)
    private void setDestination(LatLng latLng) {
        EventBus.getDefault().removeStickyEvent(latLng.getClass(), Constants.SEND_MAP_COORDINATES_EVENT);
        destination = latLng;
        Log.v(Constants.TAG, "Destination = " + destination);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Subscriber(tag = Constants.SEND_MAP_COORDINATES_LOCATION)
    private void setLocationDestination(LatLng latLng) {
        EventBus.getDefault().removeStickyEvent(latLng.getClass(), Constants.SEND_MAP_COORDINATES_LOCATION);
        destination = latLng;
        Log.v(Constants.TAG, "Destination = " + destination);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Subscriber(tag = Constants.OPEN_MAP_FROM_LOCATION)
    private void setFriendsDestination(LatLng latLng) {
        EventBus.getDefault().removeStickyEvent(latLng.getClass(), Constants.OPEN_MAP_FROM_LOCATION);
        destination = latLng;
        Log.v(Constants.TAG, "Destination = "+ destination);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }


    @OnClick(R.id.fragment_map_imagebutton1) void backButton() {
        mainActivity.onBackPressed();
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;
        int permissionCheck1 = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.addMarker(new MarkerOptions().position(destination).title("End Point"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.isTrafficEnabled();
        googleMap.setTrafficEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        if(destination != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
        } else {
            Snackbar.make(getView(),"Cannot locate you at this moment, Please try again", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMyLocationChange(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        position = new LatLng(latitude, longitude);
        Log.v(Constants.TAG,"Latitude = "+latitude+" Longitude = "+longitude);
        GoogleDirection.withServerKey(serverKey)
                .from(position)
                .to(destination)
                .transitMode(TransportMode.TRANSIT)
                .language(Language.ENGLISH)
                .unit(Unit.METRIC)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.INDOOR)
                .transitMode(TransitMode.TRAIN)
                .transitMode(TransitMode.BUS)
                .transitMode(TransitMode.SUBWAY)
                .transitMode(TransitMode.RAIL)
                .transitMode(TransitMode.TRAM)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {

                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            List<Step> step = leg.getStepList();

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            distance.setText(distanceInfo.getText());
                            time.setText("( " + durationInfo.getText() + " )");

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(mainActivity, directionPositionList, 10, Color.rgb(69, 151, 255));
                            googleMap.addPolyline(polylineOptions);

                        } else {
                            //Snackbar.make(getView(), "Direction Not Found", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });

    }

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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    mainActivity.onBackPressed();

                    return true;

                }

                return false;
            }
        });
    }
}
