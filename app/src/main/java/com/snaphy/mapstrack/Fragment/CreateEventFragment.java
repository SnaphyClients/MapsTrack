package com.snaphy.mapstrack.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.EventType;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.TrackRepository;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
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
import com.snaphy.mapstrack.Adapter.SpinnerAdapter;
import com.snaphy.mapstrack.Collection.EventTypeCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.ImageModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


/**
 * This Fragment is used to create new event by clicking on "Add(Plus)" button in the home fragment
 *
 */
public class CreateEventFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateEventFragment";
    RecyclerView recyclerView;
    ImageLoader imageLoader;
    EventType selectedEventType;
    File editedImageFile;
    ImageModel imageModel;
    LatLng currentLatLng;
    boolean saveInProgress = false;


    @Bind(R.id.fragment_create_event_imagebutton1) ImageButton backButton;
    @Bind(R.id.fragment_create_event_edittext3) EditText dateEdittext;
    @Bind(R.id.fragment_create_event_edittext2) EditText eventLocation;
    @Bind(R.id.fragment_create_event_edittext1) EditText eventName;
    @Bind(R.id.fragment_create_event_edittext4) EditText eventDescription;
    @Bind(R.id.fragment_event_info_imageview1) ImageView imageView;
    @Bind(R.id.fragment_create_event_radio_button1) RadioButton publicRadioButton;
    @Bind(R.id.fragment_create_event_radio_button2) RadioButton privateRadioButton;
    @Bind(R.id.fragment_create_event_radio_group) RadioGroup radioGroup;
    @Bind(R.id.fragment_event_floating_button1) com.github.clans.fab.FloatingActionMenu parentFloatingButton;

    com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner materialSpinner;
    static com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocompleteTextView;
    DisplayContactAdapter displayContactAdapter;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    List<TemporaryContactDatabase> temporaryContactDatabases;
    ArrayList<SelectContactModel> selectContactModelArrayList = new ArrayList<SelectContactModel>();
    HashMap<String,Double> latLongHashMap = new HashMap<String, Double>();
    HashMap<String,String> imageURL =  new HashMap<String, String>();
    MainActivity mainActivity;
    DateFormat dateFormat;
    Date date;
    boolean isPrivate;
    static CreateEventFragment fragment;
    Track track;
    List<EventType> eventTypeList;
    boolean fromEdited = false;

    /* Temp Variables */
    String tempEventName;
    EventType tempEventType;
    String tempDescription;
    String tempAddress;
    String tempDate;
    LatLng tempLocation;
    /* Temp Variables */





    public CreateEventFragment() {
        // Required empty public constructor
    }

    public static CreateEventFragment newInstance() {
        fragment = new CreateEventFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);
        materialSpinner = (com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner) view.findViewById(R.id.fragment_create_event_spinner1);
        //placesAutocompleteTextView = (com.seatgeek.placesautocomplete.PlacesAutocompleteTextView) view.findViewById(R.id.fragment_create_event_edittext2);
        backButtonClickListener();
        parentFloatingButton.setIconAnimated(false);

        //TODO May be clear list contact list here

        EasyImage.configuration(mainActivity)
                .setImagesFolderName("MapsTrack")
                .saveInRootPicturesDirectory()
                .setCopyExistingPicturesToPublicLocation(true);

        dateFormat = new SimpleDateFormat();
        setSpinner();
        datePickerClickListener(view);
        dateEdittext.setKeyListener(null);
        //selectPosition();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mainActivity.onBackPressed();
                    //clearFields();
                    return true;
                }
                return false;
            }
        });
        checkCreateOrEditedMode();
    }

    @Subscriber ( tag = Constants.UPDATE_ADDRESS_FROM_MAP )
    public void setAddressFromMap(String add) {
        eventLocation.setText(add);
        eventLocation.setFocusableInTouchMode(true);
    }

    @OnClick ( R.id.fragment_create_event_edittext2 ) void clickOnLocationFieldListener() {
        if(eventLocation.getText().toString().isEmpty()) {
            eventLocation.setFocusableInTouchMode(false);
            View view1 = mainActivity.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
            mainActivity.replaceFragment(R.id.fragment_create_event_button5, null);
        } else {
            View view1 = mainActivity.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(view1.getWindowToken(), 0);
            }
        }
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

    public void clearFields() {
        eventName.setText("");
        eventDescription.setText("");
        dateEdittext.setText("");
        publicRadioButton.setChecked(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(!eventName.getText().toString().isEmpty()) {
            tempEventName = eventName.getText().toString();
        }

        if(!eventLocation.getText().toString().isEmpty()) {
            tempAddress = eventLocation.getText().toString();
        }

        if(!eventDescription.getText().toString().isEmpty()) {
            tempDescription = eventDescription.getText().toString();
        }

        if(!dateEdittext.getText().toString().isEmpty()) {
            tempDate = dateEdittext.getText().toString();
        }

        if(currentLatLng != null) {
            tempLocation = currentLatLng;
        }

    }


   /* @Subscriber(tag = Constants.SEND_ADDRESS_EVENT)
    private void setAddress(String address) {
       // placesAutocompleteTextView.setText(address);
    }*/
/*
    @Subscriber(tag = Constants.SEND_EVENT_LATLONG)
    private void setLatLong(LatLng latLong) {
        latLongHashMap.put("latitude", latLong.latitude);
        latLongHashMap.put("longitude", latLong.longitude);
    }*/

    @Subscriber(tag = Constants.CREATE_EVENT_FROM_LOCATION)
    private void createEventFromLocation(LocationHomeModel locationHomeModel) {
        eventLocation.setText(locationHomeModel.getLocationAddress());
        displayContactModelArrayList.clear();
        for(int i = 0; i<locationHomeModel.getContacts().size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(locationHomeModel.getContacts().get(i).getContactName()));
        }
        //TODO Check if lat long are from location or not
        latLongHashMap.put("latitude", locationHomeModel.getLatLong().get("latitude"));
        latLongHashMap.put("longitude", locationHomeModel.getLatLong().get("longitude"));
    }

    @Subscriber(tag = Constants.SHOW_EVENT_EDIT)
    private void onEdit(Track track) {

        fromEdited = true;

        if(track != null){
            this.track = track;
            if(track.getName() != null) {
                eventName.setText(track.getName());
            }

            if(track.getDescription() != null) {
                eventDescription.setText(track.getDescription());
            }

            if(track.getAddress() != null) {
                eventLocation.setText(track.getAddress());
            }

            if(track.getEventDate() != null) {
                try{
                    dateEdittext.setText(mainActivity.parseDate(track.getEventDate()));
                }
                catch (Exception e){
                    dateEdittext.setText(track.getEventDate());
                }
            }

            if(track.getIsPublic() != null) {
                if(track.getIsPublic().equals("public")){
                    publicRadioButton.setChecked(true);
                } else {
                    privateRadioButton.setChecked(true);
                }
            }

            if(track.getEventType() != null) {
                selectedEventType = track.getEventType();
                // SET EVENT TYPE
                materialSpinner.setText(track.getEventType().getName().toString());
            }

            if(track.getPicture() != null){
                mainActivity.loadUnsignedUrl(track.getPicture(), imageView);
                editedImageFile = new File("path");
            }

            if(track.getFriends() != null) {

            }

            if(track.getGeolocationLatitide() != 0){
                if(track.getGeolocationLongitude() != 0) {
                    currentLatLng = new LatLng(track.getGeolocationLatitide(), track.getGeolocationLongitude());
                }
            }
        }
    }

    /**
     * Show contacts button event listener
     */
    @OnClick(R.id.fragment_create_event_imagebutton2) void openContactDialog() {
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        if(track.getFriends() != null){
            if(track.getFriends().size() == 0) {
                Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
            } else {
                DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity, track, R.id.fragment_create_event_imagebutton2);
                Holder holder = new ListHolder();
                showOnlyContentDialog(holder, adapter);
            }
        }else{
            Toast.makeText(mainActivity, "No Contacts Present", Toast.LENGTH_SHORT).show();
        }

    }


    @Subscriber ( tag = Constants.SEND_SELECTED_CONTACT_TO_CREATE_EVENT_FRAGMENT)
    public void saveSelectedContacts(ArrayList<ContactModel> contactModelArrayList) {
        for(ContactModel contactModel : contactModelArrayList) {
            if(contactModel.isSelected()) {
                displayContactModelArrayList.add(new DisplayContactModel(contactModel.getContactName()));
            }
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



    /**
     * Set the data in the spinner
     */
    public void setSpinner() {
        EventTypeCollection.getEventTypeList(new ListCallback<EventType>() {
            @Override
            public void onSuccess(List<EventType> objects) {
                if (objects != null) {
                    if (objects.size() != 0) {
                        eventTypeList = objects;
                        setAdapter();
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.v(Constants.TAG, t.toString());
            }
        });
    }

    private void setAdapter() {
        SpinnerAdapter adapter = new SpinnerAdapter(mainActivity, android.R.layout.simple_dropdown_item_1line, eventTypeList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setFocusable(true);
        materialSpinner.setFocusableInTouchMode(true);
        materialSpinner.setThreshold(1);
        materialSpinner.setAdapter(adapter);

        materialSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEventType = (EventType) parent.getItemAtPosition(position);
                materialSpinner.setText(((EventType) parent.getItemAtPosition(position)).getName().toString());
            }
        });
    }



    /**
     * When certain position is selected from the drop down in auto complete
     * this method is fired
     */
    /*private void selectPosition() {
        placesAutocompleteTextView.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(Place place) {
            }
        });
    }*/

    /**
     * When back button is the toolbar is pressed this method is fired
     */
    private void backButtonClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                mainActivity.onBackPressed();
            }
        });
    }

    /**
     * This method is fired when any date from the date picker is selected
     * @param view
     */
    //TODO Design DatePicker
    private void datePickerClickListener(View view) {
        dateEdittext.setText("");
        dateEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager im = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(dateEdittext.getWindowToken(), 0);
                DatePickerPopWin pickerPopWin = new DatePickerPopWin(mainActivity, 2000, 2100, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        //handler the result here
                        dateEdittext.setText(dateDesc);
                    }
                });
                pickerPopWin.showPopWin(mainActivity);
            }
        });
    }

    @OnClick(R.id.fragment_event_info_imageview1) void openGalleryFromImageView() {
       if(editedImageFile == null){
            openGalleryFolder();
        } else {
            showAlertToRemoveProfilePic();
        }
    }

    public void showAlertToRemoveProfilePic() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
        alertDialogBuilder.setMessage("Remove this profile picture?");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                imageView.setImageResource(R.drawable.default_image);
                editedImageFile = null;
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @OnClick(R.id.fragment_create_event_button1) void openGallery() {
        openGalleryFolder();
    }

    public void openGalleryFolder() {
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        int permissionCheck = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            EasyImage.openDocuments(this);
        } else {
            Nammu.askForPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    EasyImage.openGallery(mainActivity);
                }

                @Override
                public void permissionRefused() {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, mainActivity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handle the image
                editedImageFile = imageFile;
                final String uri = Uri.fromFile(imageFile).toString();
                final String decoded = Uri.decode(uri);
                Log.v(Constants.TAG, "Image File = " + uri + "");
                imageLoader.displayImage(decoded, imageView);
                //Now upload image..
                mainActivity.uploadWithCallback(Constants.CONTAINER, editedImageFile, new ObjectCallback<ImageModel>() {
                    @Override
                    public void onSuccess(ImageModel object) {
                        imageModel = object;
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(Constants.TAG, t.toString());
                    }
                });
            }

        });
    }

    @OnClick(R.id.fragment_create_event_button4) void selectContact() {
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        mainActivity.replaceFragment(R.layout.layout_select_contact, null);
        EventBus.getDefault().postSticky(track, Constants.DISPLAY_CONTACT);
    }

    @OnClick (R.id.fragment_create_event_button5) void openMap() {
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        mainActivity.replaceFragment(R.id.fragment_create_event_button5, null);
    }

    @OnClick(R.id.fragment_create_event_button2) void publishEvent() {
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

                    //Add event type..
                    if (selectedEventType != null) {
                        trackObj.put("eventTypeId", selectedEventType.getId());
                    }

                    if (track.getId() != null) {
                        Toast.makeText(mainActivity, "Event updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mainActivity, "Event created successfully", Toast.LENGTH_SHORT).show();
                    }

                    if (track.getId() != null) {
                        trackObj.put("id", track.getId());
                    }

                    track.addRelation(BackgroundService.getCustomer());
                    track.addRelation(selectedEventType);

                    //Now save the event..
                    mainActivity.saveTrack(trackObj);
                    EventBus.getDefault().post(track, Constants.SHOW_EVENT_INFO);
                    //On back pressed..
                    View view1 = mainActivity.getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }
                    mainActivity.onBackPressed();
                    if(fromEdited) {
                        EventBus.getDefault().post(imageView.getDrawable(), Constants.UPDATE_IMAGE_FROM_EDITED_CREATE_EVENT);
                    }
                    saveInProgress = false;
                }
            }

            @Override
            public void onError(Throwable t) {
                saveInProgress = false;
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Subscriber ( tag = Constants.SET_LATITUDE_LONGITUDE)
    public void setLatLong(LatLng latLong) {
        currentLatLng = latLong;
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
    public void onDestroy() {
        super.onDestroy();
        clearFields();
        EventBus.getDefault().unregister(fragment);
    }



    private void validateData(ObjectCallback<Track> callback){
        Exception t = new Exception();
        if(editedImageFile != null){
            if(imageModel == null){
                String message = "Please! image is getting uploaded";
                Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            }else{
                track.setPicture(imageModel.getHashMap());
            }
        }


        String event = eventName.getText().toString();
        String eventDesc = eventDescription.getText().toString();
        String addr = eventLocation.getText().toString();
        String date = dateEdittext.getText().toString();
        String isPublic;
        int getCheckedButtonId = radioGroup.getCheckedRadioButtonId();
        if( getCheckedButtonId == R.id.fragment_create_event_radio_button1) {
            isPublic = "public";
        }
        else {
            isPublic = "private";
        }

        track.setIsPublic(isPublic);

        if (event != null) {
            if(event.isEmpty()){
                Toast.makeText(mainActivity, "Event name is required", Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            }else{
                track.setName(event);
            }
        }else{
            Toast.makeText(mainActivity, "Event name is required", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }

        if (eventDesc != null) {
            if(eventDesc.isEmpty()){
                //DO nothing..
                /*Toast.makeText(mainActivity, "Event description is required", Toast.LENGTH_SHORT).show();
                return;*/
            }else{
                track.setDescription(eventDesc);
            }
        }else{
            //do nothing..
           /* Toast.makeText(mainActivity, "Event description is required", Toast.LENGTH_SHORT).show();
            return;*/
        }

        if (addr != null) {
            if(addr.isEmpty()){
                Toast.makeText(mainActivity, "Address is required", Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            }else{
                track.setAddress(addr);
            }
        }else{
            Toast.makeText(mainActivity, "Address is required", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }


        if (date != null) {
            if(date.isEmpty()){
                Toast.makeText(mainActivity, "Date is required", Toast.LENGTH_SHORT).show();
                callback.onError(t);
                return;
            }else{
                track.setEventDate(date);
            }
        }else{
            Toast.makeText(mainActivity, "Date is required", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }

        if (selectedEventType == null) {
            Toast.makeText(mainActivity, "Event type is required", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }


        if(currentLatLng == null){
            Toast.makeText(mainActivity, "You must set your location.", Toast.LENGTH_SHORT).show();
            callback.onError(t);
            return;
        }else{
            track.setGeolocation(currentLatLng.latitude, currentLatLng.longitude);
        }

        callback.onSuccess(track);
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
