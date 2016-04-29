package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
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
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "EventInfoFragment";
    @Bind(R.id.fragment_event_info_textview0) TextView eventName;
    @Bind(R.id.fragment_event_info_textview1) TextView eventName2;
    @Bind(R.id.fragment_event_info_textview2) TextView eventType;
    @Bind(R.id.fragment_event_info_textview3) TextView eventDate;
    @Bind(R.id.fragment_event_info_textview4) TextView eventAddress;
    @Bind(R.id.fragment_event_info_textview5) TextView eventDescription;
    @Bind(R.id.fragment_event_info_textview6) TextView contact;
    @Bind(R.id.fragment_event_info_imageview1) ImageView imageView;
    @Bind(R.id.fragment_event_info_button1) com.github.clans.fab.FloatingActionButton  editEventButton;
    @Bind(R.id.fragment_event_info_button2) com.github.clans.fab.FloatingActionButton  deleteEventButton;
    @Bind(R.id.fragment_event_info_button5) com.github.clans.fab.FloatingActionButton  addFriendsEventButton;
    @Bind(R.id.fragment_event_info_button4) Button moreButton;
    ImageLoader imageLoader;
    LatLng latLng;
    boolean isEventOwner = false;
    DateFormat dateFormat;
    MainActivity mainActivity;
    Track track;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    static EventInfoFragment fragment;


    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment newInstance() {
        fragment = new EventInfoFragment();
        EventBus.getDefault().register(fragment);
        return fragment;
    }



    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(fragment);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    EventBus.getDefault().post(false, Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);
                    mainActivity.onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        ButterKnife.bind(this, view);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        return view;
    }

    @Subscriber(tag = Constants.SHOW_EVENT_INFO)
    private void showEventInfo(Track track) {
        this.track = track;
        if(!track.getName().isEmpty()) {
            eventName.setText(track.getName());
        }
        //setContactData(track.getFriends());
        if(track.getGeolocationLatitide() != 0 && track.getGeolocationLongitude() != 0){
            latLng = new LatLng(track.getGeolocationLatitide(), track.getGeolocationLongitude());
        }


        if(track.getPicture() != null){
            mainActivity.loadUnsignedUrl(track.getPicture(), imageView);
        }

        if(track.getName() != null){
            if(!this.track.getName().isEmpty()) {
                CharSequence eName = mainActivity.drawTextViewDesign("Event Name : ", mainActivity.changeToUpperCase(this.track.getName()));
                eventName2.setText(eName);
            }
        }

        mainActivity.tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Event Name")
                .setAction(track.getName())
                .build());

        if(track.getType() != null){
            if(!this.track.getType().isEmpty()) {

                mainActivity.displayEventType(eventType, track);
                //eventType.setText(eType);
            }
        }

        //dateFormat.format(this.eventHomeModel.getDate()).toString()
        if(track.getEventDate() != null){
            if(!track.getEventDate().isEmpty()){
                CharSequence eDate;
                try {
                    eDate = mainActivity.drawTextViewDesign("Event Date : ", mainActivity.parseDate(track.getEventDate()));
                } catch (Exception e) {
                    eDate = mainActivity.drawTextViewDesign("Event Date : ", track.getEventDate());
                }
                eventDate.setText(eDate);
            }
        }

        if(track.getAddress() != null){
            if(!track.getAddress().isEmpty()) {
                CharSequence eAddress = mainActivity.drawTextViewDesign("Event Address : ", mainActivity.changeToUpperCase(this.track.getAddress()));
                eventAddress.setText(eAddress);
            }
        }

        if(track.getDescription() != null){
            if(!track.getDescription().isEmpty()) {
                CharSequence eDescription = mainActivity.drawTextViewDesign("Event Description : ", mainActivity.changeToUpperCase(this.track.getDescription()));
                eventDescription.setText(eDescription);
            }
        }

        if(track.getFriends() != null){
            CharSequence eContact = mainActivity.drawTextViewDesign("Friends Invited : ", (String.valueOf(track.getFriends().size())));
            contact.setText(eContact);
        } else {
            CharSequence eContact = mainActivity.drawTextViewDesign("Friends Invited : ", "0");
            contact.setText(eContact);
        }


        if(BackgroundService.getCustomer() != null){
            if(BackgroundService.getCustomer().getId() != null){
                if(track.getCustomer() != null){
                    String customerId = (String)BackgroundService.getCustomer().getId();
                    String eventOwnerId = (String)track.getCustomer().getId();
                    if(customerId.equals(eventOwnerId)){
                        isEventOwner = true;
                        showShareOption(true);
                        ifUserNotCustomer(false);
                    }else{
                        showShareOption(true);
                        ifUserNotCustomer(true);
                    }
                }else{
                    showShareOption(false);
                    ifUserNotCustomer(true);
                    return;
                }

            }else{
                showShareOption(false);
                ifUserNotCustomer(true);
            }
        }else{
            showShareOption(false);
            ifUserNotCustomer(true);
        }
    }





    @OnClick(R.id.fragment_event_info_image_button1) void backButton() {
        EventBus.getDefault().post(false, Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_event_info_button1) void editEvent() {
        mainActivity.replaceFragment(R.id.fragment_event_info_button1, null);
        EventBus.getDefault().post(track, Constants.SHOW_EVENT_EDIT);
    }

    @Subscriber
    public void changeImageFromEdit(Drawable drawable){

    }


    @OnClick(R.id.fragment_event_info_button2) void deleteEvent() {
        if(isEventOwner){
            //Now delete this event from server..
            mainActivity.deleteTrack(track);
            TrackCollection.eventList.remove(this.track);
            EventBus.getDefault().post(false, Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);
            Toast.makeText(mainActivity, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            mainActivity.onBackPressed();
        }else{
            Toast.makeText(mainActivity, "You are not authorised to delete this event", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.fragment_event_info_button3)
    void openMap() {
        mainActivity.replaceFragment(R.id.fragment_event_info_button3, null);
        EventBus.getDefault().postSticky(latLng, Constants.SEND_MAP_COORDINATES_EVENT);
    }

    @OnClick(R.id.fragment_event_info_button5) void addFriends() {
        mainActivity.replaceFragment(R.id.fragment_event_info_button5, null);
        EventBus.getDefault().postSticky(track, Constants.DISPLAY_CONTACT);
    }


    @OnClick(R.id.fragment_event_info_button4) void openContacts() {
        if(track.getFriends() != null) {
            if (track.getFriends().size() == 0) {
                Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
            } else {
                DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity, track, R.id.fragment_event_info_button4);
                Holder holder = new ListHolder();
                showOnlyContentDialog(holder, adapter);
            }
        } else {
            Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscriber ( tag = Constants.UPDATE_CONTACT_NUMBER )
    public void updateContactNumber(Track track) {
        showEventInfo(track);
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

    public void ifUserNotCustomer(boolean value) {
        if(value){
            editEventButton.setVisibility(View.GONE);
            deleteEventButton.setVisibility(View.GONE);
        }else {
            editEventButton.setVisibility(View.VISIBLE);
            deleteEventButton.setVisibility(View.VISIBLE);
        }

    }

    public void showShareOption(boolean value) {
        if(value){
            addFriendsEventButton.setVisibility(View.VISIBLE);
        }else{
            addFriendsEventButton.setVisibility(View.GONE);
        }
    }

}
