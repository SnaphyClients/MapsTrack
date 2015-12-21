package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaphy.mapstrack.Adapter.ShareLocationAdapter;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ShareLocationModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Location shared by others or by user to others is maintained here in this fragment
 */
public class ShareFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    ShareLocationAdapter shareLocationAdapter;
    ArrayList<ShareLocationModel> locationModels = new ArrayList<ShareLocationModel>();
    @Bind(R.id.fragment_share_listview1) se.emilsjolander.stickylistheaders.StickyListHeadersListView stickyListHeadersListView;
    MainActivity mainActivity;

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocationInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);
        shareLocationAdapter = new ShareLocationAdapter(locationModels);
        stickyListHeadersListView.setAdapter(shareLocationAdapter);
        return view;
    }

    /**
     * Data is set from here
     */
    public void setLocationInfo() {
        locationModels.add(new ShareLocationModel("to","Ravi Gupta"));
        locationModels.add(new ShareLocationModel("to","Chahat Bhandari"));
        locationModels.add(new ShareLocationModel("to","Siddharth Jain"));
        locationModels.add(new ShareLocationModel("to","Reena Mukherji"));
        locationModels.add(new ShareLocationModel("to","Robins Gupta"));
        locationModels.add(new ShareLocationModel("to","Anurag Gupta"));
        locationModels.add(new ShareLocationModel("to","Pulkit Dubey"));
        locationModels.add(new ShareLocationModel("to","Ankur Suwalka"));
        locationModels.add(new ShareLocationModel("to","Meenakshi Jain"));
        locationModels.add(new ShareLocationModel("from","Sapna Grover"));
        locationModels.add(new ShareLocationModel("from","Robins Gupta"));
        locationModels.add(new ShareLocationModel("from","Anurag Gupta"));
        locationModels.add(new ShareLocationModel("from","Pulkit Dubey"));
        locationModels.add(new ShareLocationModel("from", "Ankur Suwalka"));
        locationModels.add(new ShareLocationModel("from","Meenakshi Jain"));
        locationModels.add(new ShareLocationModel("from", "Sapna Grover"));
    }

    @OnClick(R.id.fragment_share_floating_button1) void addContactsButton() {
        mainActivity.replaceFragment(R.layout.layout_select_contact,null);
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
