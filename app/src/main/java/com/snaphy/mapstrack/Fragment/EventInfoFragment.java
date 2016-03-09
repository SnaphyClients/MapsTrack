package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

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
    ImageLoader imageLoader;
    LatLng latLng;

    DateFormat dateFormat;
    MainActivity mainActivity;
    EventHomeModel eventHomeModel;
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
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        ButterKnife.bind(this, view);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return view;
    }

    @Subscriber(tag = Constants.SHOW_EVENT_INFO)
    private void showEventInfo(EventHomeModel eventHomeModel) {
        EventBus.getDefault().unregister(fragment);
        this.eventHomeModel = eventHomeModel;
        eventName.setText(eventHomeModel.getEventId());
        setContactData(eventHomeModel.getContacts());
        latLng = new LatLng(eventHomeModel.getLatLong().get("latitude"),eventHomeModel.getLatLong().get("longitude"));
        Log.v(Constants.TAG, "LatLong File = " + eventHomeModel.getLatLong().get("latitude") + eventHomeModel.getLatLong().get("longitude") + "");

        Iterator<String> valueIterator = eventHomeModel.getImageURL().values().iterator();
        String uri = "";
        while (valueIterator.hasNext()) {
            uri = uri + valueIterator.next();
        }

        Log.v(Constants.TAG, "Image File = " + uri + "");
        imageLoader.displayImage(uri, imageView);


        CharSequence eName = drawTextViewDesign("Event Name : ",this.eventHomeModel.getEventId());
        eventName2.setText(eName);

        CharSequence eType = drawTextViewDesign("Event Type : ", this.eventHomeModel.getType());
        eventType.setText(eType);

        // TODO Event date has to make correct
        //dateFormat.format(this.eventHomeModel.getDate()).toString()
        CharSequence eDate = drawTextViewDesign("Event Date : "," 26 June 2016");
        eventDate.setText(eDate);

        CharSequence eAddress = drawTextViewDesign("Event Address : ", this.eventHomeModel.getEventAddress());
        eventAddress.setText(eAddress);

        CharSequence eDescription = drawTextViewDesign("Event Description : ", this.eventHomeModel.getDescription());
        eventDescription.setText(eDescription);

        CharSequence eContact = drawTextViewDesign("Friends Invited : ", (String.valueOf(this.eventHomeModel.getContacts().size())));
        contact.setText(eContact);

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

    /**
     * Data in events has been initialize from here
     */
    public void setContactData(ArrayList<SelectContactModel> selectContactModelArrayList) {
        displayContactModelArrayList.clear();
        for(int i = 0; i<selectContactModelArrayList.size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(selectContactModelArrayList.get(i).getContactName()));
        }
    }

    @OnClick(R.id.fragment_event_info_image_button1) void backButton() {
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_event_info_button1) void editEvent() {

        EventBus.getDefault().postSticky(this.eventHomeModel, Constants.SHOW_EVENT_EDIT);
        mainActivity.replaceFragment(R.id.fragment_event_info_button1, null);
    }


    @OnClick(R.id.fragment_event_info_button2) void deleteEvent() {
        EventBus.getDefault().post(this.eventHomeModel, EventHomeModel.onDelete);
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_event_info_button3) void openMap() {
        mainActivity.replaceFragment(R.id.fragment_event_info_button3, null);
        EventBus.getDefault().postSticky(latLng, Constants.SEND_MAP_COORDINATES_EVENT);
    }

    @OnClick(R.id.fragment_event_info_button5) void addFriends() {
        mainActivity.replaceFragment(R.id.fragment_event_info_button5, null);
    }


    @OnClick(R.id.fragment_event_info_button4) void openContacts() {
        DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
        Holder holder = new ListHolder();
        showOnlyContentDialog(holder, adapter);
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
