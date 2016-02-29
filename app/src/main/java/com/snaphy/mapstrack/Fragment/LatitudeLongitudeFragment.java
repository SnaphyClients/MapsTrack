package com.snaphy.mapstrack.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.snaphy.mapstrack.GPSTracker;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latitude_longitude, container, false);

        gps=new GPSTracker(mainActivity);
        supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_latitude_longitude_map));
        supportMapFragment.getMapAsync(this);

        return view;
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
    public void onMapReady(GoogleMap googleMap) {

        double curlat=gps.getLatitude();
        double curlon=gps.getLongitude();
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
