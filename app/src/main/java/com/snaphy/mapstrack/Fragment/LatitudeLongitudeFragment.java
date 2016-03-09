package com.snaphy.mapstrack.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.GPSTracker;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LatitudeLongitudeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LatitudeLongitudeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LatitudeLongitudeFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener{

    private OnFragmentInteractionListener mListener;
    public static String TAG = "LatitudeLongitudeFragment";
    SupportMapFragment supportMapFragment;
    GPSTracker gps;
    Marker marker;
    MainActivity mainActivity;
    GoogleMap globalGoogleMap;
    PlacesAutocompleteTextView placesAutocompleteTextView;

    public LatitudeLongitudeFragment() {
        // Required empty public constructor
    }

    public static LatitudeLongitudeFragment newInstance() {
        LatitudeLongitudeFragment fragment = new LatitudeLongitudeFragment();
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
        View view = inflater.inflate(R.layout.fragment_latitude_longitude, container, false);
        placesAutocompleteTextView = (PlacesAutocompleteTextView) view.findViewById(R.id.fragment_lat_long_place_autocomplete1);
        gps=new GPSTracker(mainActivity);
        supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_latitude_longitude_map));
        supportMapFragment.getMapAsync(this);
        setPlaces();
        return view;
    }

    @Subscriber(tag = Constants.SEND_ADDRESS_EVENT)
    private void setAddress(String address) {
         placesAutocompleteTextView.setText(address);
    }

    public void setPlaces() {
        placesAutocompleteTextView.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        Snackbar.make(getView(), place.description + "", Snackbar.LENGTH_SHORT).show();
                        setNewMarkerLocation(place.description);
                    }
                }
        );
    }

    public void setNewMarkerLocation(String address) {
        Geocoder coder = new Geocoder(mainActivity);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(address, 50);
            for(Address add : adresses){
                double longitude = add.getLongitude();
                double latitude = add.getLatitude();
                LatLng latLng = new LatLng(latitude, longitude);
                marker.setPosition(latLng);
                globalGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                globalGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
    public void onMapReady(GoogleMap googleMap) {

        double curlat=gps.getLatitude();
        double curlon=gps.getLongitude();
        globalGoogleMap = googleMap;
        LatLng currentpos=new LatLng(curlat, curlon);
        int permissionCheck1 = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.isTrafficEnabled();
        googleMap.setTrafficEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curlat, curlon), 14));

        marker=googleMap.addMarker(new MarkerOptions().position(currentpos)
                .title("MapsTrack")
                .snippet("Long press and move marker at your location")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("Marker", "Dragging");
            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                LatLng markerLocation = marker.getPosition();
                Toast.makeText(mainActivity, markerLocation.toString(), Toast.LENGTH_LONG).show();
                Log.d("Marker", "finished");
            }

            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("Marker", "Started");

            }
        });
    }

    @Override
    public void onMyLocationChange(Location location) {
        placesAutocompleteTextView.setCurrentLocation(location);
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
