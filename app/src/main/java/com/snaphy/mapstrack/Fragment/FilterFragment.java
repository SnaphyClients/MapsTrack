package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.snaphy.mapstrack.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private enum choice {SharedEventsAndLocation, myEventAndLocation, nearbyEevnts};

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
        sharedEventAndLocation.setTextColor(Color.CYAN);
        return view;
    }

    @OnClick(R.id.fragment_filter_button1) void sharedEventAndLocation() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.fragment_filter_button2) void myEventAndLocation() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.fragment_filter_button3) void nearbyEvents() {
        getActivity().onBackPressed();
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
