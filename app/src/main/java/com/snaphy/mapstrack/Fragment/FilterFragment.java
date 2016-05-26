package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static final String TAG = "FilterFragment";
    @Bind(R.id.fragment_filter_button1) Button sharedEventAndLocation;
    @Bind(R.id.fragment_filter_button2) Button myEventAndLocation;
    @Bind(R.id.fragment_filter_button3) Button nearbyEvents;
    @Bind(R.id.fragment_filter_button4) Button myLocation;
    SmoothProgressBar smoothProgressBar;
    MainActivity mainActivity;

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);
        setSelectedFilter();
        return view;
    }

    public void setSelectedFilter() {
        if(TrackCollection.getTrackCurrentFilterSelect() != null) {
            if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.MY_EVENTS)) {

                myEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_my_events_selected, 0, 0);
                sharedEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_share_events, 0, 0);
                nearbyEvents.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_events, 0, 0);
                myLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_location, 0, 0);
            } else if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.NEAR_BY)) {

                myEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_my_events, 0, 0);
                sharedEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_share_events, 0, 0);
                nearbyEvents.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_events_selected, 0, 0);
                myLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_location, 0, 0);

            } else if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.SHARED_EVENTS)) {

                myEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_my_events, 0, 0);
                sharedEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_share_events_selected, 0, 0);
                nearbyEvents.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_events, 0, 0);
                myLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_location, 0, 0);

            } else if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.MY_LOCATION)) {
                myEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_my_events, 0, 0);
                sharedEventAndLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_share_events, 0, 0);
                nearbyEvents.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_events, 0, 0);
                myLocation.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.filter_nearby_location_selected, 0, 0);
            }
        }
    }

    @OnClick(R.id.fragment_filter_button1) void sharedEventAndLocation() {
        //TODO CHANCES OF ERROR CHECK LATER..
        mainActivity.setOnlySharedEventsFilter();
        checkIfLocationIsChecked();
        EventBus.getDefault().post("", Constants.UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER);
        getActivity().onBackPressed();
    }

    @OnClick(R.id.fragment_filter_button2) void myEventAndLocation() {
        mainActivity.showMyEventFilter();
        checkIfLocationIsChecked();
        EventBus.getDefault().post("", Constants.UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER);
        getActivity().onBackPressed();
    }

    @OnClick(R.id.fragment_filter_button3) void nearbyEvents() {
        mainActivity.setNearByEventFilter();
        checkIfLocationIsChecked();
        EventBus.getDefault().post("", Constants.UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER);
        getActivity().onBackPressed();
    }

    @OnClick(R.id.fragment_filter_button4) void myLocations() {
        mainActivity.showMyLocationFilter();
        EventBus.getDefault().post("", Constants.UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER_LOCATION);
        getActivity().onBackPressed();
    }

    public void checkIfLocationIsChecked() {
            EventBus.getDefault().post("", Constants.UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER_LOCATION);
    }

    @OnClick(R.id.fragment_filter_imagebutton1) void backButton() {
        getActivity().onBackPressed();
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
