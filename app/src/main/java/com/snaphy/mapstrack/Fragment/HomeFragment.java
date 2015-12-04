package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaphy.mapstrack.Adapter.HomeEventAdapter;
import com.snaphy.mapstrack.Adapter.HomeLocationAdapter;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_home_recycler_view1) RecyclerView recyclerView1;
    @Bind(R.id.fragment_home_recycler_view2) RecyclerView recyclerView2;
    @Bind(R.id.fragment_home_textview1) TextView eventText;
    @Bind(R.id.fragment_home_textview2) TextView locationText;
    HomeEventAdapter homeEventAdapter;
    HomeLocationAdapter homeLocationAdapter;
    ArrayList<EventHomeModel> eventHomeModelArrayList = new ArrayList<EventHomeModel>();
    ArrayList<LocationHomeModel> locationHomeModelArrayList = new ArrayList<LocationHomeModel>();

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

        homeEventAdapter = new HomeEventAdapter(eventHomeModelArrayList);
        homeLocationAdapter = new HomeLocationAdapter(locationHomeModelArrayList);

        recyclerView1.setAdapter(homeEventAdapter);
        recyclerView2.setAdapter(homeLocationAdapter);
        return view;
    }

    /**
     * Data in events has been initialize from here
     */
    public void setEventDataInAdapter() {
        locationHomeModelArrayList.add(new LocationHomeModel("Shanti Niketan", "DLF Phase 3, Gurgaon"));
        locationHomeModelArrayList.add(new LocationHomeModel("Anu Office", "Palam Vihar, Sector 25"));
        locationHomeModelArrayList.add(new LocationHomeModel("My Party", "Pitampura, Haryana"));
        locationHomeModelArrayList.add(new LocationHomeModel("Ravi House","Cyber Hub, Cyber City"));
        locationHomeModelArrayList.add(new LocationHomeModel("Sid Home", "Sahara Mall, Sikandarpur"));
    }

    /**
     * Data in locatons has been initialize from here
     */
    public void setLocationDataInAdapter() {
        eventHomeModelArrayList.add(new EventHomeModel("Ravi123","DLF Phase 3, Gurgaon"));
        eventHomeModelArrayList.add(new EventHomeModel("SidHome","Palam Vihar, Sector 25"));
        eventHomeModelArrayList.add(new EventHomeModel("RaghuMarriage","Pitampura, Haryana"));
        eventHomeModelArrayList.add(new EventHomeModel("RobParty","Cyber Hub, Cyber City"));
        eventHomeModelArrayList.add(new EventHomeModel("AnuOffice","Sahara Mall, Sikandarpur"));
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
