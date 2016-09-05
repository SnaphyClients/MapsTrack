package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsdk.snaphy.snaphyandroidsdk.models.LastUpdatedLocation;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.LastUpdatedLocationRepository;
import com.google.android.gms.analytics.HitBuilders;
import com.snaphy.mapstrack.Adapter.LocationShareAdapterContacts;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Helper.ContactMatcher;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.ListCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationShareByUserFriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationShareByUserFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationShareByUserFriendsFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "LocationShareByUserFriendsFragment";
    @Bind(R.id.fragment_location_share_by_user_friends_recycler_view1) RecyclerView recyclerView;
    @Bind(R.id.fragment_location_share_by_user_friends_progressBar) com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar progressBar;
    LocationShareAdapterContacts locationShareAdapterContacts;
    MainActivity mainActivity;
    List<ContactModel> sharedFriends = new ArrayList<>();
    public LocationShareByUserFriendsFragment() {
        // Required empty public constructor
    }

    public static LocationShareByUserFriendsFragment newInstance() {
        LocationShareByUserFriendsFragment fragment = new LocationShareByUserFriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_share_by_user_friends, container, false);
        ButterKnife.bind(this, view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }



    @Subscriber(tag = Constants.REMOVE_LOCATION_SHARED_BY_USER_FRIENDS)
    private void onDelete(List<ContactModel> sharedLocation) {
        locationShareAdapterContacts.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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


    private void showFriendSharedLocation(){
        if(BackgroundService.getCustomer() != null){
            if(BackgroundService.getCustomer().getPhoneNumber() != null) {
                if (mainActivity != null) {
                    String phoneNumber = mainActivity.formatNumber(BackgroundService.getCustomer().getPhoneNumber());
                    LastUpdatedLocationRepository lastUpdatedLocationRepository = mainActivity.getLoopBackAdapter().createRepository(LastUpdatedLocationRepository.class);
                    Map<String, Object> filter = new HashMap<>();
                    Map<String, Object> where = new HashMap<>();
                    where.put("sharedLocation.number", phoneNumber);
                    filter.put("where", where);
                    filter.put("include", "customer");

                    lastUpdatedLocationRepository.find(filter, new ListCallback<LastUpdatedLocation>() {
                        @Override
                        public void onSuccess(List<LastUpdatedLocation> objects) {
                            mainActivity.stopProgressBar(progressBar);
                            if (objects != null) {
                                if (objects.size() != 0) {
                                    sharedFriends.clear();
                                    for (LastUpdatedLocation lastUpdatedLocation : objects) {
                                        if (lastUpdatedLocation != null) {
                                            ContactModel contactModel = new ContactModel();
                                            contactModel.setLastUpdatedLocation(lastUpdatedLocation);
                                            if (lastUpdatedLocation.getCustomer() != null) {
                                                if (lastUpdatedLocation.getCustomer().getPhoneNumber() != null) {
                                                    contactModel.setContactNumber(lastUpdatedLocation.getCustomer().getPhoneNumber());
                                                    sharedFriends.add(contactModel);
                                                }
                                            }
                                        }
                                    }

                                    if (sharedFriends.size() != 0) {
                                        setLocation(sharedFriends);
                                    }

                                } else {
                                    sharedFriends.clear();
                                    setLocation(sharedFriends);
                                }
                            } else {
                                sharedFriends.clear();
                                setLocation(sharedFriends);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            mainActivity.stopProgressBar(progressBar);
                            mainActivity.tracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("Exception")
                                    .setAction("Fragment : LocationShareByUserFriendsFragment, Method : showFriendsSharedLocation " + t.toString())
                                    .build());
                            sharedFriends.clear();
                            setLocation(sharedFriends);
                            Log.e(Constants.TAG, t.toString());
                        }
                    });
                }
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

    public void setLocation(List<ContactModel> location){
        locationShareAdapterContacts = new LocationShareAdapterContacts(mainActivity, location, Constants.LOCATION_SHARE_BY_USER_FRIENDS_FRAGMENT);
        recyclerView.setAdapter(locationShareAdapterContacts);
        ContactMatcher contactMatcher = new ContactMatcher(mainActivity, location, locationShareAdapterContacts);
        locationShareAdapterContacts.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            showFriendSharedLocation();
        }else{
            // fragment is no longer visible
        }
    }
}
