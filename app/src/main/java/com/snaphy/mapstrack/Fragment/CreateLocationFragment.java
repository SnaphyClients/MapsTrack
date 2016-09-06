package com.snaphy.mapstrack.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.TrackRepository;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment is used to create location from the "Add(Plus)" button in the home fragment
 */
public class CreateLocationFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateLocationFragment";
    HashMap<String,Double> latLongHashMap = new HashMap<String, Double>();
    LatLng currentLatLng;
    static ProgressDialog progressDialog;

    @Bind(R.id.fragment_create_location_imagebutton1) ImageButton backButton;
    @Bind(R.id.fragment_create_location_edittext2) EditText locationId;
    @Bind(R.id.fragment_create_location_edittext3) EditText locationAddress;
    @Bind(R.id.fragment_create_location_textview0)
    TextView heading;

    @Bind(R.id.fragment_create_location_radio_button1) RadioButton publicRadioButton;
    @Bind(R.id.fragment_create_location_radio_button2) RadioButton privateRadioButton;
    @Bind(R.id.fragment_create_location_radio_group) RadioGroup radioGroup;
    @Bind(R.id.fragment_create_location_imagebutton2) com.github.clans.fab.FloatingActionButton showSelectedFriends;
    @Bind(R.id.fragment_create_location_button1) com.github.clans.fab.FloatingActionButton addFriends;
    @Bind(R.id.fragment_create_location_floating_button1) com.github.clans.fab.FloatingActionMenu parentFloatingMenu;

    MainActivity mainActivity;
    DialogPlus dialog;

    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    ArrayList<SelectContactModel> selectContactModelArrayList = new ArrayList<SelectContactModel>();

    static CreateLocationFragment fragment;
    Track track;
    boolean saveInProgress = false;

    ProgressDialog progress;

    public CreateLocationFragment() {
        // Required empty public constructor
    }

    public static CreateLocationFragment newInstance() {
        fragment = new CreateLocationFragment();
        EventBus.getDefault().register(fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_location, container, false);
        ButterKnife.bind(this, view);
        backButtonClickListener();
        setOnRedioCheckedListener();
        onFloatingButtonClickListener();
        return view;
    }

    public void onFloatingButtonClickListener() {
        parentFloatingMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentFloatingMenu.isOpened()) {
                    parentFloatingMenu.close(true);
                } else {
                    parentFloatingMenu.open(false);
                }
            }
        });
    }


    /**
     * Show contacts button event listener
     */
    @OnClick(R.id.fragment_create_location_imagebutton2) void openContactDialog() {
        parentFloatingMenu.close(true);
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        if(track.getFriends() != null){
            if(track.getFriends().size() == 0) {
                Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
            } else {
                DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity, track, R.id.fragment_create_location_imagebutton2);
                Holder holder = new ListHolder();
                showOnlyContentDialog(holder, adapter);
            }
        }else{
            Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Open dialog showing list of selected contacts
     * @param holder
     * @param adapter
     */
    private void showOnlyContentDialog(Holder holder, BaseAdapter adapter) {
        dialog = DialogPlus.newDialog(mainActivity)
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
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {
                        dialog.dismiss();
                    }
                })
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialog) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .setExpanded(true)
                .create();
        dialog.show();
    }

   /* @Subscriber(tag = Constants.SEND_LOCATION_LATLONG)
    private void setLatLong(LatLng latLong) {
        latLongHashMap.put("latitude",latLong.latitude);
        latLongHashMap.put("longitude", latLong.longitude);
    }*/

    @Subscriber ( tag = Constants.SEND_SELECTED_CONTACT_TO_CREATE_LOCATION_FRAGMENT)
    public void saveSelectedContacts(ArrayList<ContactModel> contactModelArrayList) {
        for(ContactModel contactModel : contactModelArrayList) {
            if(contactModel.isSelected()) {
                displayContactModelArrayList.add(new DisplayContactModel(contactModel.getContactName()));
            }
        }
    }

    public void setOnRedioCheckedListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.fragment_create_location_radio_button1) {
                    // It is public
                    disableFriendList(true);
                } else {
                    // It is private
                    disableFriendList(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.tracker.setScreenName("Create Location Screen");
        mainActivity.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(isPublic()){
            disableFriendList(true);
        }else{
            disableFriendList(false);
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    parentFloatingMenu.close(true);
                    EventBus.getDefault().post("", Constants.HIDE_MENU_OPTIONS_LOCATIONS);
                    mainActivity.onBackPressed();

                    return true;

                }

                return false;
            }
        });
        checkCreateOrEditedMode();
    }

    private void checkCreateOrEditedMode(){
        if(track == null){
            //Then in this case create a new track object..its in create mode..
            TrackRepository trackRepository = mainActivity.getLoopBackAdapter().createRepository(TrackRepository.class);
            Map<String, Object> objectMap = new HashMap<>();
            this.track = trackRepository.createObject(objectMap);
            this.track.setType("event");
        }
    }

    @Subscriber(tag = Constants.SHOW_LOCATION_EDIT)
    private void onEdit(Track track) {

        if(track != null) {
            this.track = track;
            if(track.getLocationId() != null){
                locationId.setEnabled(false);
                locationId.setText(track.getLocationId().toString());
                heading.setText(track.getLocationId().toString());
            }

            if(track.getAddress() != null) {
                locationAddress.setText(track.getAddress().toString());
            }

            if(track.getGeolocationLatitide() != 0) {
                if(track.getGeolocationLongitude() != 0) {
                    currentLatLng = new LatLng(track.getGeolocationLatitide(), track.getGeolocationLongitude());
                }
            }

            if(track.getIsPublic() != null) {
                if(track.getIsPublic().equals("public")){
                    disableFriendList(true);
                    publicRadioButton.setChecked(true);
                } else {
                    disableFriendList(false);
                    privateRadioButton.setChecked(true);
                }
            }
        }

    }



    public void disableFriendList(boolean disable){
        if(disable){
            showSelectedFriends.setVisibility(View.GONE);
            addFriends.setVisibility(View.GONE);
            parentFloatingMenu.close(true);
        }else{
            showSelectedFriends.setVisibility(View.VISIBLE);
            addFriends.setVisibility(View.VISIBLE);
            parentFloatingMenu.close(true);
        }
    }

    /**
     * When back button is pressed in fragment
     */
    private void backButtonClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFloatingMenu.close(true);
                EventBus.getDefault().post("", Constants.HIDE_MENU_OPTIONS_LOCATIONS);
                mainActivity.onBackPressed();
            }
        });
    }

    @Subscriber ( tag = Constants.SET_LATITUDE_LONGITUDE)
    public void setLatLong(LatLng latLong) {
        if(latLong != null) {
            currentLatLng = latLong;
        }
    }

    @Subscriber ( tag = Constants.UPDATE_ADDRESS_FROM_MAP )
    public void setAddressFromMap(String add) {
        locationAddress.setText(add);
        locationAddress.setFocusableInTouchMode(true);
    }

    /**
     * Contact are selected from list of avialable contact in phone contact
     */
    @OnClick(R.id.fragment_create_location_button1) void selectContact() {
        parentFloatingMenu.close(true);
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        mainActivity.replaceFragment(R.id.fragment_create_location_button1, null);
        EventBus.getDefault().postSticky(track, Constants.DISPLAY_CONTACT);
    }

    @OnClick(R.id.fragment_create_location_button5) void setLocation() {
        parentFloatingMenu.close(true);
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        progressDialog = new ProgressDialog(mainActivity);
        mainActivity.setProgress(CreateLocationFragment.progressDialog);
        mainActivity.replaceFragment(R.id.fragment_create_location_button5, null);
    }

    @OnClick(R.id.fragment_location_info_imageview1) void noImageIsPresentInLocation() {
        parentFloatingMenu.close(true);
        Toast.makeText(mainActivity, "Location doesn't have images", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fragment_create_location_edittext3) void addressClickListener() {
        parentFloatingMenu.close(true);
        if(locationAddress.getText().toString().isEmpty()) {
            locationAddress.setFocusableInTouchMode(false);
            View view1 = mainActivity.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
            progressDialog = new ProgressDialog(mainActivity);
            mainActivity.setProgress(CreateLocationFragment.progressDialog);
            mainActivity.replaceFragment(R.id.fragment_create_location_button5, null);
        } else {
            View view1 = mainActivity.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(view1.getWindowToken(), 0);
            }
        }
    }

    @Subscriber ( tag = Constants.CLOSE_DIALOG_AFTER_DELETING_LAST_CONTACT)
    public void closeDialog(String empty) {
        if(dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * When publish event button is clicked
     */
    @OnClick(R.id.fragment_create_location_button2) void publishLocation() {
        parentFloatingMenu.close(true);
        progress = new ProgressDialog(mainActivity);
        setProgress(progress);
        validateData(new ObjectCallback<Track>() {
            @Override
            public void onSuccess(Track object) {
                saveInProgress = true;
                if (saveInProgress) {
                    //Now create the event first ..
                    Map<String, Object> trackObj = (Map<String, Object>) track.convertMap();
                    if (BackgroundService.getCustomer() != null) {
                        //Now add customer ..
                        trackObj.put("customerId", BackgroundService.getCustomer().getId());
                    }


                   /* if (track.getId() != null) {
                        Toast.makeText(mainActivity, "Location updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mainActivity, "Location created successfully", Toast.LENGTH_SHORT).show();
                    }*/

                    if (track.getId() != null) {
                        trackObj.put("id", track.getId());
                    }

                    //Now save the event..
                    track = mainActivity.saveTrack(trackObj, progress);
                    track.addRelation(BackgroundService.getCustomer());

                    EventBus.getDefault().post(track, Constants.SHOW_LOCATION_INFO);
                    //On back pressed..
                    View view1 = mainActivity.getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }

                    if (track.getId() == null) {
                        //edit the location which is not saved on server
                        TrackCollection.locationList.add(track);
                    }

                    EventBus.getDefault().post(true, Constants.NOTIFY_LOCATION_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);
                    mainActivity.onBackPressed();
                    mainActivity.showMyLocationFilter();
                    EventBus.getDefault().post(TrackCollection.progressBar, Constants.RESET_LOCATION_FROM_FILTER_FRAGMENT);
                    saveInProgress = false;
                }
            }

            @Override
            public void onError(Throwable t) {
                progress.dismiss();
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : CreateLocationFragment, Method : publishLocation "+t.toString())
                        .build());
                saveInProgress = false;
            }
        });

    }

    public void setProgress(ProgressDialog progress) {
        progress.setIndeterminate(true);
        progress.setMessage("Loading...");
        progress.show();
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().post("", Constants.HIDE_MENU_OPTIONS);
        if(dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private boolean isPublic(){
        int getCheckedButtonId = radioGroup.getCheckedRadioButtonId();
        if( getCheckedButtonId == R.id.fragment_create_location_radio_button1) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean checkForSpaces(String s) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(s);
        boolean found = matcher.find();
        return found;
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

    private void validateData(final ObjectCallback<Track> callback){
        Exception t = new Exception();


        String location = locationId.getText().toString();
        String locationAdd = locationAddress.getText().toString();
        String isPublic;
        int getCheckedButtonId = radioGroup.getCheckedRadioButtonId();
        if( getCheckedButtonId == R.id.fragment_create_location_radio_button1) {
            isPublic = "public";
        }
        else {
            isPublic = "private";
        }

        track.setIsPublic(isPublic);

        if (location != null) {
            location = location.trim();
            if(location.isEmpty()){
                Toast.makeText(mainActivity, "Location Id is required", Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            } else if(location.length() >= Constants.WORD_LIMIT ) {
                Toast.makeText(mainActivity, "Location Id cannot have more than 20 characters", Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            }
            else{
                if(checkForSpaces(location.toString())) {
                    Toast.makeText(mainActivity, "Location Id could not have white spaces", Toast.LENGTH_SHORT).show();
                    callback.onError(t);
                    return;
                } else {
                    //LocationID cannot be editable..
                    if(track.getId() == null){
                        track.setLocationId(location);
                        track.setName(location);
                    }

                }

            }
        }else{
            Toast.makeText(mainActivity, "Location Id is required", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }


        if (locationAdd != null) {
            locationAdd = locationAdd.trim();
            if(locationAdd.isEmpty()){
                Toast.makeText(mainActivity, "Address is required", Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            }else{
                track.setAddress(locationAddress.getText().toString());
            }
        }else{
            Toast.makeText(mainActivity, "Address is required", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }

        if(currentLatLng == null){
            Toast.makeText(mainActivity, "You must set your location.", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }else {
            track.setGeolocation(currentLatLng.latitude, currentLatLng.longitude);
        }


        track.setType("location");
        track.setStatus("allow");



        //Also set the..
        if(track.getId() == null){
            TrackRepository trackRepository = mainActivity.getLoopBackAdapter().createRepository(TrackRepository.class);
            Map<String, Object> where = new HashMap<>();
            where.put("locationId", location);
            trackRepository.count(where, new Adapter.JsonObjectCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    if(response != null){
                        Map<String, Object> countObj  = JsonUtil.fromJson(response);
                        if(countObj.get("count") != null){
                            int count = (Integer)countObj.get("count");
                            if(count == 0){
                                callback.onSuccess(track);
                            }else {
                                Toast.makeText(mainActivity, "Location Id is already present. Please enter another one.", Toast.LENGTH_SHORT).show();
                                Exception t = new Exception();
                                callback.onError(t);
                            }
                        }else{
                            callback.onSuccess(track);
                        }
                    }else{
                        callback.onSuccess(track);
                    }

                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(mainActivity, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    callback.onError(t);
                }
            });
        }else{
            callback.onSuccess(track);
        }




    }
}
