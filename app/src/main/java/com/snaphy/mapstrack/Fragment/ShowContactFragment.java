package com.snaphy.mapstrack.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.google.android.gms.analytics.HitBuilders;
import com.snaphy.mapstrack.Adapter.ShowContactAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.RecyclerItemClickListener;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowContactFragment extends android.support.v4.app.Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private Map<String, ContactModel> contactModelMap = new HashMap<>();
    private OnFragmentInteractionListener mListener;
    public static String TAG = "ShowContactFragment";
    @Bind(R.id.fragment_show_contact_recycler_view1) RecyclerView recyclerView;
    ShowContactAdapter showContactAdapter;
    Cursor globalCursor;
    Track track;
    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

    private static final int CONTACT_ID_INDEX = 0;
    private static final int LOOKUP_KEY_INDEX = 1;

    // Defines the text expression
    @SuppressLint("InlinedApi")
    private static final String SELECTION =

    ContactsContract.CommonDataKinds.Email.ADDRESS + " LIKE ? " + "AND " +
            /*
             * Searches for a MIME type that matches
             * the value of the constant
             * Email.CONTENT_ITEM_TYPE. Note the
             * single quotes surrounding Email.CONTENT_ITEM_TYPE.
             */
    ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";
    // Defines a variable for the search string
    private String mSearchString;
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };
    String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

    MainActivity mainActivity;



    public ShowContactFragment() {
        // Required empty public constructor
    }

    public static ShowContactFragment newInstance() {
        ShowContactFragment fragment = new ShowContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_contact, container, false);
        ButterKnife.bind(this, view);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) view.findViewById(R.id.fast_scroller);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(recyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(globalCursor.moveToPosition(position)) {
                            int contactNameData = globalCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
                            int contactNumberData = globalCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String contactNumberDataString = globalCursor.getString(contactNumberData);
                            final String contactNameDataString = globalCursor.getString(contactNameData);

                            String formatNumber = mainActivity.formatNumber(contactNumberDataString);
                            ContactModel contactModel = contactModelMap.get(formatNumber);

                            if(contactModel != null){
                                //Remove the model..
                                contactModelMap.remove(formatNumber);
                            }else{
                                contactModel = new ContactModel();
                                contactModel.setContactNumber(formatNumber);
                                contactModel.setContactName(contactNameDataString);
                                contactModel.setIsSelected(true);
                                //Now add it to contact model map..
                                contactModelMap.put(formatNumber, contactModel);
                            }

                            showContactAdapter.notifyDataSetChanged();
                        }
                    }
                })
        );

        return view;
    }



    @Subscriber( tag = Constants.DISPLAY_CONTACTS_FROM_SHARED_USER_FRAGMENT )
    public void showSelectedContactsFromSharedFragment(List<ContactModel> contactModelList) {
        EventBus.getDefault().removeStickyEvent(contactModelList.getClass(), Constants.DISPLAY_CONTACTS_FROM_SHARED_USER_FRAGMENT);
        for(ContactModel contactModel : contactModelList){
            if(contactModel != null){
                if(contactModel.getContactNumber() != null){
                    contactModel.setIsSelected(true);
                    contactModelMap.put(contactModel.getContactNumber(), contactModel);
                }
            }
        }
        //Now call the contact list..
        getLoaderManager().initLoader(0, null, this);
    }



    @Subscriber( tag = Constants.DISPLAY_CONTACT )
    public void showSelectedContacts(Track track) {
        this.track = track;
        EventBus.getDefault().removeStickyEvent(track.getClass(), Constants.DISPLAY_CONTACT);
        if(track.getFriends()!= null){
            if(track.getFriends().size() != 0){
                for(Map<String, Object> numberObj : track.getFriends()){
                    if(numberObj.get("number") != null){
                        String formattedNumber = mainActivity.formatNumber(String.valueOf(numberObj.get("number")));
                        ContactModel contactModel = new ContactModel();
                        contactModel.setContactNumber(formattedNumber);
                        contactModel.setIsSelected(true);
                        //NOW ADD THIS TO MAIN MODEL..
                        contactModelMap.put(formattedNumber, contactModel);

                    }
                }
            }
        }
        //Now call the contact list..
        getLoaderManager().initLoader(0, null, this);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick(R.id.fragment_show_contact_imagebutton1) void backButton() {
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_show_contact_button1) void contactSelected() {
        if(track != null){
            if(track.getType().equals("event")){
                updateFriendsList(track);
                EventBus.getDefault().post(track, Constants.UPDATE_CONTACT_NUMBER);
            }else {
                updateFriendsList(track);
                EventBus.getDefault().post(track, Constants.UPDATE_CONTACT_NUMBER_IN_LOCATION);
            }
        }else{
            addCustomerToSharedList();
        }
    }



    private void addCustomerToSharedList(){
        List<Map<String, Object>>  friendList = new ArrayList<>();
        List<ContactModel> contactModels  = new ArrayList<>();
        for(String key: contactModelMap.keySet()){
            Map<String, Object> number = new HashMap<>();
            number.put("number", mainActivity.formatNumber(key));
            friendList.add(number);
            contactModels.add(contactModelMap.get(key));
        }

        if(BackgroundService.getCustomer() != null){
            if(BackgroundService.getCustomer().getLastUpdatedLocations() != null){
                //Set new friend list..
                BackgroundService.getCustomer().getLastUpdatedLocations().setSharedLocation(friendList);
                BackgroundService.getCustomer().getLastUpdatedLocations().save(new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        mainActivity.tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Exception")
                                .setAction(t.toString())
                                .build());
                        Log.e(Constants.TAG, t.toString() + " in ShowContactFragment");
                    }
                });
            }
        }

        EventBus.getDefault().post(contactModels, Constants.UPDATE_SHARED_FRIENDS_BY_USER_LIST);

        Toast.makeText(mainActivity, "Shared location list updated!", Toast.LENGTH_SHORT).show();
        //Now go back..
        mainActivity.onBackPressed();
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        mSelectionArgs[0] = "%" + "" + "%";

        // Starts the query
        return new CursorLoader(
                getActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null,
                order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        globalCursor = data;
        showContactAdapter = new ShowContactAdapter(mainActivity, contactModelMap, data);
        recyclerView.setAdapter(showContactAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }


    @Override
    public void onDestroy() {
        super.onResume();
        if(globalCursor != null) {
            globalCursor.close();
            globalCursor = null;
        }
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Uri uri);
    }


    private void updateFriendsList(Track track){
        List<Map<String, Object>>  friendList = new ArrayList<>();
        for(String key: contactModelMap.keySet()){
            Map<String, Object> number = new HashMap<>();
            number.put("number", key);
            friendList.add(number);
        }

        //Now update friend contact list..
        track.setFriends(friendList);
        if(track.getId() != null){
            //Now  save to server..
            mainActivity.saveTrack(track);
        }
        Toast.makeText(mainActivity, "Friends list updated!", Toast.LENGTH_SHORT).show();
        //Now go back..
        mainActivity.onBackPressed();
    }

}
