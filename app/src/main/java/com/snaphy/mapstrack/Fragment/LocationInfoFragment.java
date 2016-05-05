package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.TrackRepository;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationInfoFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "LocationInfoFragment";
    @Bind(R.id.fragment_location_info_textview0) TextView locationNameId;
    @Bind(R.id.fragment_location_info_textview2) TextView locationId;
    @Bind(R.id.fragment_location_info_textview3) TextView locationAddress;
    @Bind(R.id.fragment_location_info_textview4) TextView contacts;
    @Bind(R.id.fragment_location_info_button1) com.github.clans.fab.FloatingActionButton editLocation;
    @Bind(R.id.fragment_location_info_button3) com.github.clans.fab.FloatingActionButton deleteLocation;
    @Bind(R.id.fragment_location_info_button6) com.github.clans.fab.FloatingActionButton addFriends;
    @Bind(R.id.fragment_location_info_floating_button1) com.github.clans.fab.FloatingActionMenu floatingActionMenu;
    @Bind(R.id.fragment_location_info_button5)
    Button moreButton;
    ImageLoader imageLoader;
    LatLng latLng;
    MainActivity mainActivity;
    static LocationInfoFragment fragment;
    boolean isLocationOwner = false;
    Track track;

    public LocationInfoFragment() {
        // Required empty public constructor
    }

    public static LocationInfoFragment newInstance() {
        fragment = new LocationInfoFragment();
        EventBus.getDefault().register(fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Subscriber(tag = Constants.SHOW_LOCATION_INFO)
    private void showLocationInfo(Track track) {
        if(track != null) {
            this.track = track;
            if(track.getLocationId() != null) {
                if(!track.getLocationId().isEmpty()) {
                    locationNameId.setText(track.getLocationId().toString());
                    CharSequence lName = drawTextViewDesign("Location Name : ",track.getLocationId().toString());
                    locationId.setText(lName);
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Location Id")
                            .setAction(track.getLocationId())
                            .build());
                }
            }


            if(track.getAddress() != null) {
                if(!track.getAddress().isEmpty()) {
                    CharSequence lAddress = drawTextViewDesign("Location Address : ", mainActivity.changeToUpperCase(track.getAddress().toString()));
                    locationAddress.setText(lAddress);
                }
            }

            if(track.getGeolocationLatitide() != 0) {
                if(track.getGeolocationLongitude() != 0) {
                    latLng = new LatLng(track.getGeolocationLatitide(), track.getGeolocationLongitude());
                }
            }

            if(track.getFriends() != null){
                CharSequence eContact = mainActivity.drawTextViewDesign("Friends Invited : ", (String.valueOf(track.getFriends().size())));
                contacts.setText(eContact);
            } else {
                moreButton.setVisibility(View.GONE);
                CharSequence eContact = mainActivity.drawTextViewDesign("Friends Invited : ", "0");
                contacts.setText(eContact);
            }

            if(track.getIsPublic().equals("public")) {
                disableFriendList(true);
            } else {
                disableFriendList(false);
            }

            //Now hide options..
            showOption(track);
        }
    }

    @Subscriber ( tag = Constants.UPDATE_CONTACT_NUMBER_IN_LOCATION )
    public void updateContactNumber(Track track) {
        showLocationInfo(track);
    }



    public CharSequence drawTextViewDesign(String constant, String data) {
        SpannableString spannableString =  new SpannableString(constant);
        SpannableString spannableString2 =  new SpannableString(data);
        spannableString.setSpan(new ForegroundColorSpan(Color.rgb(63, 81, 181)),0,constant.length(),0);
        spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, constant.length(), 0);
        spannableString.setSpan(new RelativeSizeSpan(1.1f), 0, constant.length(), 0);
        CharSequence result = (TextUtils.concat(spannableString, " ", spannableString2));
        return result;
    }


    public void showOption(Track track){
        if(BackgroundService.getCustomer() != null){
            String ownerId = (String)BackgroundService.getCustomer().getId();
            if(track.getCustomer() != null){
                String creatorId = (String)track.getCustomer().getId();
                if(creatorId.equals(ownerId)){
                    isLocationOwner = true;
                    deleteLocation.setVisibility(View.VISIBLE);
                    editLocation.setVisibility(View.VISIBLE);
                }else{
                    isLocationOwner = false;
                    deleteLocation.setVisibility(View.GONE);
                    editLocation.setVisibility(View.GONE);
                }
            }else {
                isLocationOwner = false;
                deleteLocation.setVisibility(View.GONE);
                editLocation.setVisibility(View.GONE);
            }
        }else{
            isLocationOwner = false;
            deleteLocation.setVisibility(View.GONE);
            editLocation.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fragment_location_info_image_button1) void backButton() {
        floatingActionMenu.close(true);
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_location_info_button1) void editLocation() {
        floatingActionMenu.close(true);
        mainActivity.replaceFragment(R.id.fragment_location_info_button1, null);
        EventBus.getDefault().post(track, Constants.SHOW_LOCATION_EDIT);

    }


    public void disableFriendList(boolean disable){
        if(disable){
            addFriends.setVisibility(View.GONE);
        }else{
            addFriends.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.fragment_location_info_button3) void deleteLocation() {
        floatingActionMenu.close(true);
        if(isLocationOwner){
            //Now delete this event from server..
            mainActivity.deleteTrack(track);
            TrackCollection.locationList.remove(this.track);
            EventBus.getDefault().post(false, Constants.NOTIFY_LOCATION_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);
            Toast.makeText(mainActivity, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            mainActivity.onBackPressed();
        }else {
            Toast.makeText(mainActivity, "You are not authorised to delete this location", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.fragment_location_info_button2) void openMap() {
        floatingActionMenu.close(true);
       /* mainActivity.replaceFragment(R.id.fragment_location_info_button2, null);
        EventBus.getDefault().postSticky(latLng, Constants.SEND_MAP_COORDINATES_LOCATION);*/
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latLng.latitude+","+latLng.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    @OnClick(R.id.fragment_location_info_button4) void createEventFromLocation() {
        floatingActionMenu.close(true);
        if(track != null){
            TrackRepository trackRepository = mainActivity.getLoopBackAdapter().createRepository(TrackRepository.class);
            Map<String, Object> objectMap = (Map<String, Object>)track.convertMap();
            objectMap.remove("name");
            objectMap.remove("locationId");
            objectMap.put("type", "event");
            Track locationToEvent = trackRepository.createObject(objectMap);
            mainActivity.replaceFragment(R.id.fragment_location_info_button4, null);
            EventBus.getDefault().post(locationToEvent, Constants.SHOW_EVENT_EDIT);
            EventBus.getDefault().post("",Constants.CREATE_EVENT_FROM_LOCATION_FRAGMENT);
        }
    }

    @OnClick(R.id.fragment_location_info_button6) void addFriends() {
        floatingActionMenu.close(true);
        mainActivity.replaceFragment(R.id.fragment_location_info_button6, null);
        EventBus.getDefault().postSticky(track, Constants.DISPLAY_CONTACT);
    }


    @OnClick(R.id.fragment_location_info_button5) void openContacts() {
        floatingActionMenu.close(true);
        if(track.getFriends() != null) {
            if (track.getFriends().size() == 0) {
                Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
            } else {
                DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity, track, R.id.fragment_location_info_button5);
                Holder holder = new ListHolder();
                showOnlyContentDialog(holder, adapter);
            }
        } else {
            Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOnlyContentDialog(Holder holder, BaseAdapter adapter) {
        final DialogPlus dialog = DialogPlus.newDialog(mainActivity)
                .setContentHolder(holder)
                .setAdapter(adapter)
                . setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {

                    }
                })
                .setCancelable(true)
                .setExpanded(true)
                .create();
        dialog.show();
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
