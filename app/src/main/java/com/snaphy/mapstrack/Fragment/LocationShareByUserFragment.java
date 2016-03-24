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
import com.snaphy.mapstrack.Adapter.LocationShareAdapterContacts;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Helper.ContactMatcher;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.ShareLocationModel;
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
    //List<Customer> sharedLocation;
    List<ContactModel> sharedLocation = new ArrayList<>();

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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscriber ( tag = Constants.UPDATE_SHARED_FRIENDS_BY_USER_LIST)
    public void updateSharedList(List<ContactModel> contactModels){
        sharedLocation.clear();
        if(contactModels != null){
            if(contactModels.size() != 0){
                sharedLocation.addAll(contactModels);
                setLocation(sharedLocation);
            }
        }
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
        shareWithUser();
        return view;
    }

  /*  @Subscriber(tag = Constants.INITIALIZE_LOCATION_SHARED_BY_USER)
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
*/
    /**
     *  It is called when data is deleted from fragment
     */
    @Subscriber(tag = Constants.REMOVE_LOCATION_SHARED_BY_USER)
    private void onDelete(List<ContactModel> sharedLocation) {
        locationShareAdapterContacts.notifyDataSetChanged();
    }


    @OnClick(R.id.fragment_location_share_by_user_floating_button1) void addFriends() {
        mainActivity.replaceFragment(R.id.fragment_location_share_by_user_floating_button1, null);
    }

 /*   @Subscriber(tag = Constants.SHOW_CONTACTS_IN_SHARE_LOCATION)
    private void onSave(ArrayList<ContactModel> contactModelArrayList) {
        for(ContactModel contactModel: contactModelArrayList) {
            if(contactModel.isSelected()) {
                shareLocationModelArrayList.add(new ShareLocationModel(null, contactModel.getContactName(),
                        contactModel.getContactNumber(), null));
            }
        }

        locationShareAdapterContacts.notifyDataSetChanged();
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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


    public void shareWithUser(){
        if(BackgroundService.getCustomer() != null){
            if(BackgroundService.getCustomer().getPhoneNumber() != null){
                String phoneNumber = mainActivity.formatNumber(BackgroundService.getCustomer().getPhoneNumber());
                Map<String, Object> filter = new HashMap<>();
                Map<String, Object> where = new HashMap<>();
                where.put("customerId", BackgroundService.getCustomer().getId());
                filter.put("where", where);
                LastUpdatedLocationRepository lastUpdatedLocationRepository = mainActivity.getLoopBackAdapter().createRepository(LastUpdatedLocationRepository.class);
                lastUpdatedLocationRepository.find(filter, new ListCallback<LastUpdatedLocation>() {
                    @Override
                    public void onSuccess(List<LastUpdatedLocation> objects) {
                        if(objects != null){
                            if(objects.size() != 0){
                                LastUpdatedLocation lastUpdatedLocation = objects.get(0);
                                lastUpdatedLocation.addRelation(BackgroundService.getCustomer());
                                BackgroundService.getCustomer().addRelation(lastUpdatedLocation);

                                if(lastUpdatedLocation.getSharedLocation() != null){
                                    if(lastUpdatedLocation.getSharedLocation().size() != 0){
                                        sharedLocation.clear();
                                        for (Map<String, Object> sharedFriend : lastUpdatedLocation.getSharedLocation()) {
                                            ContactModel contactModel = new ContactModel();
                                            if(sharedFriend.get("number") != null){
                                                String formattedNumber = mainActivity.formatNumber(String.valueOf(sharedFriend.get("number")));
                                                contactModel.setContactNumber(formattedNumber);
                                                sharedLocation.add(contactModel);
                                            }
                                        }

                                        setLocation(sharedLocation);
                                    }else{
                                        sharedLocation.clear();
                                        setLocation(sharedLocation);
                                    }
                                }else{
                                    sharedLocation.clear();
                                    setLocation(sharedLocation);
                                }

                            }else{
                                sharedLocation.clear();
                                setLocation(sharedLocation);
                            }
                        }else{
                            sharedLocation.clear();
                            setLocation(sharedLocation);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        sharedLocation.clear();
                        Log.e(Constants.TAG, t.toString());
                        Log.v(Constants.TAG, "Error in Location Share By User Fragment");
                    }
                });
            }else{
                sharedLocation.clear();
                setLocation(sharedLocation);
            }
        }else{
            sharedLocation.clear();
            setLocation(sharedLocation);
        }
    }


    public void setLocation(List<ContactModel> location){
        locationShareAdapterContacts = new LocationShareAdapterContacts(mainActivity, location, Constants.LOCATION_SHARE_BY_USER_FRAGMENT);
        recyclerView.setAdapter(locationShareAdapterContacts);
        ContactMatcher contactMatcher = new ContactMatcher(mainActivity, location, locationShareAdapterContacts);
        locationShareAdapterContacts.notifyDataSetChanged();
    }




/*
    private class PopulateContact extends AsyncTask<String, Void, String> {

        public Customer user;
        public List<ContactModel> sharedFriendList;

        public PopulateContact(Customer user, List<ContactModel> sharedFriendList){
            this.user = user;
            this.sharedFriendList = sharedFriendList;
        }

        @Override
        protected String doInBackground(String... params) {
            List<Customer> location_shared = user.getLocation_shared();
            if(location_shared != null){
                if(location_shared.size() != 0){
                    for(Customer obj : location_shared){
                        if(obj.get("number") != null){
                            String number = String.valueOf(obj.get("phoneNumber"));
                            if(!number.isEmpty()){
                                ContactModel contactModel = new ContactModel();
                                contactModel.setContactNumber(number);
                                contactModel.setCustomer(obj);
                                sharedFriendList.add(contactModel);
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ContactMatcher contactMatcher = new ContactMatcher(mainActivity, sharedFriendList, locationShareAdapterContacts);
            locationShareAdapterContacts.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }*/

}
