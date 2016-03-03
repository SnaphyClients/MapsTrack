package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaphy.mapstrack.Adapter.LocationShareAdapterContacts;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.ShareLocationModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationShareByUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationShareByUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationShareByUserFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_location_share_by_user_recycler_view1) RecyclerView recyclerView;
    ArrayList<ShareLocationModel> shareLocationModelArrayList = new ArrayList<ShareLocationModel>();
    LocationShareAdapterContacts locationShareAdapterContacts;
    public static String TAG = "LocationShareByUserFragment";
    MainActivity mainActivity;

    public LocationShareByUserFragment() {
        // Required empty public constructor
    }


    public static LocationShareByUserFragment newInstance() {
        LocationShareByUserFragment fragment = new LocationShareByUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_location_share_by_user, container, false);
        ButterKnife.bind(this, view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        locationShareAdapterContacts = new LocationShareAdapterContacts(mainActivity, shareLocationModelArrayList, Constants.LOCATION_SHARE_BY_USER_FRAGMENT);
        recyclerView.setAdapter(locationShareAdapterContacts);
        locationShareAdapterContacts.notifyDataSetChanged();

        return view;
    }

    @Subscriber(tag = Constants.INITIALIZE_LOCATION_SHARED_BY_USER)
    private void onInitialize(ArrayList<ShareLocationModel> shareLocationModelArrayList) {

        this.shareLocationModelArrayList.clear();

        for(int i = 0; i<shareLocationModelArrayList.size(); i++) {
            this.shareLocationModelArrayList.add(new ShareLocationModel(shareLocationModelArrayList.get(i).getId(),
                    shareLocationModelArrayList.get(i).getContactName(),
                    shareLocationModelArrayList.get(i).getContactNumber(),
                    shareLocationModelArrayList.get(i).getLatLong()));
        }

        locationShareAdapterContacts.notifyDataSetChanged();
    }

    /**
     *  It is called when data is deleted from fragment
     * @param shareLocationModel
     */
    @Subscriber(tag = Constants.REMOVE_LOCATION_SHARED_BY_USER)
    private void onDelete(ShareLocationModel shareLocationModel) {
        for(ShareLocationModel element : shareLocationModelArrayList) {
            if(element.getId() == shareLocationModel.getId()) {
                int position = shareLocationModelArrayList.indexOf(element);
                shareLocationModelArrayList.remove(position);
                break;
            }
        }
        locationShareAdapterContacts.notifyDataSetChanged();
    }

    @OnClick(R.id.fragment_location_share_by_user_floating_button1) void addFriends() {
        mainActivity.replaceFragment(R.id.fragment_location_share_by_user_floating_button1, null);
    }

    @Subscriber(tag = Constants.SHOW_CONTACTS_IN_SHARE_LOCATION)
    private void onSave(ArrayList<ContactModel> contactModelArrayList) {
        for(ContactModel contactModel: contactModelArrayList) {
            if(contactModel.isSelected()) {
                shareLocationModelArrayList.add(new ShareLocationModel(null, contactModel.getContactName(),
                        contactModel.getContactNumber(), null));
            }
        }

        locationShareAdapterContacts.notifyDataSetChanged();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
