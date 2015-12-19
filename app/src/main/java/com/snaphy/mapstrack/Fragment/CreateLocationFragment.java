package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This fragment is used to create location from the "Add(Plus)" button in the home fragment
 */
public class CreateLocationFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateLocationFragment";
    @Bind(R.id.fragment_create_location_recycler_view1) RecyclerView recyclerView;
    @Bind(R.id.fragment_create_location_imagebutton1) ImageButton backButton;
    DisplayContactAdapter displayContactAdapter;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    MainActivity mainActivity;

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
        //setEventDataInAdapter();
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

    private void backButtonClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
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
