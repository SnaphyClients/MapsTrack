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
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
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
 * {@link LocationInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationInfoFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "LocationInfoFragment";
    @Bind(R.id.fragment_location_info_textview0) TextView locationName;
    @Bind(R.id.fragment_location_info_textview1) TextView locationName2;
    @Bind(R.id.fragment_location_info_textview2) TextView locationId;
    @Bind(R.id.fragment_location_info_textview3) TextView locationAddress;
    @Bind(R.id.fragment_location_info_textview4) TextView contacts;
    ImageLoader imageLoader;
    LatLng latLng;
    MainActivity mainActivity;
    LocationHomeModel locationHomeModel;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();

    public LocationInfoFragment() {
        // Required empty public constructor
    }

    public static LocationInfoFragment newInstance() {
        LocationInfoFragment fragment = new LocationInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
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
    private void showLocationInfo(LocationHomeModel locationHomeModel) {

        this.locationHomeModel = locationHomeModel;
        locationName.setText(locationHomeModel.getLocationName());
        setContactData(locationHomeModel.getContacts());
        latLng = new LatLng(locationHomeModel.getLatLong().get("latitude"),locationHomeModel.getLatLong().get("longitude"));
        Log.v(Constants.TAG, "LatLong File = " + locationHomeModel.getLatLong().get("latitude") + locationHomeModel.getLatLong().get("longitude") + "");


        CharSequence lName = drawTextViewDesign("Location Name : ",this.locationHomeModel.getLocationName());
        locationName2.setText(lName);

        CharSequence lId = drawTextViewDesign("Location Id : ", this.locationHomeModel.getLocationId());
        locationId.setText(lId);

        CharSequence lAddress = drawTextViewDesign("Location Address : ", this.locationHomeModel.getLocationAddress());
        locationAddress.setText(lAddress);

        CharSequence lContact = drawTextViewDesign("Friends Invited : ", (String.valueOf(this.locationHomeModel.getContacts().size())));
        contacts.setText(lContact);

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
     * Data in location has been initialize from here
     */
    public void setContactData(ArrayList<SelectContactModel> selectContactModelArrayList) {
        displayContactModelArrayList.clear();
        for(int i = 0; i<selectContactModelArrayList.size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(selectContactModelArrayList.get(i).getContactName()));
        }
    }


    @OnClick(R.id.fragment_location_info_image_button1) void backButton() {
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_location_info_button1) void editLocation() {

        EventBus.getDefault().postSticky(this.locationHomeModel, Constants.SHOW_LOCATION_EDIT);
        mainActivity.replaceFragment(R.id.fragment_location_info_button1, null);
    }


    @OnClick(R.id.fragment_location_info_button3) void deleteLocation() {

        EventBus.getDefault().post(this.locationHomeModel, LocationHomeModel.onDelete);
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_location_info_button2) void openMap() {
        mainActivity.replaceFragment(R.id.fragment_location_info_button2, null);
        EventBus.getDefault().postSticky(latLng, Constants.SEND_MAP_COORDINATES_LOCATION);    }


    @OnClick(R.id.fragment_location_info_button4) void createEventFromLocation() {
        mainActivity.replaceFragment(R.id.fragment_location_info_button4, null);
        EventBus.getDefault().postSticky(this.locationHomeModel, Constants.CREATE_EVENT_FROM_LOCATION);    }

    @OnClick(R.id.fragment_location_info_button6) void addFriends() {
        mainActivity.replaceFragment(R.id.fragment_location_info_button6,null);
    }


    @OnClick(R.id.fragment_location_info_button5) void openContacts() {
        /*DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
        Holder holder = new ListHolder();
        showOnlyContentDialog(holder, adapter);*/
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
