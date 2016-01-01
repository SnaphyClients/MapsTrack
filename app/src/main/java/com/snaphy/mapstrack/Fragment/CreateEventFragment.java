package com.snaphy.mapstrack.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.activeandroid.query.Select;
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
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    fr.ganfra.materialspinner.MaterialSpinner materialSpinner;
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


    public CreateEventFragment() {
        // Required empty public constructor
    }

    public static CreateEventFragment newInstance() {
        CreateEventFragment fragment = new CreateEventFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, view);
        materialSpinner = (fr.ganfra.materialspinner.MaterialSpinner) view.findViewById(R.id.fragment_create_event_spinner1);
        placesAutocompleteTextView = (com.seatgeek.placesautocomplete.PlacesAutocompleteTextView) view.findViewById(R.id.fragment_create_event_edittext2);
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
        selectPosition();
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
                    clearFields();
                    return true;
                }
                return false;
            }
        });
    }

    public void clearFields() {
        eventName.setText("");
        eventDescription.setText("");
        dateEdittext.setText("");
        publicRadioButton.setChecked(true);
        //TODO Spinner check item
        //TODO Clear Temporary database here

    }


    @Subscriber(tag = Constants.SEND_ADDRESS_EVENT)
    private void setAddress(String address) {
        placesAutocompleteTextView.setText(address);
    }

    @Subscriber(tag = Constants.SEND_EVENT_LATLONG)
    private void setLatLong(LatLng latLong) {
        latLongHashMap.put("latitude",latLong.latitude);
        latLongHashMap.put("longitude", latLong.longitude);
    }

    @Subscriber(tag = Constants.SHOW_EVENT_EDIT)
    private void onEdit(EventHomeModel eventHomeModel) {

        //TODO Update data will be called when create event fragment is called from event info
        eventName.setText(eventHomeModel.getEventId());
        eventDescription.setText(eventHomeModel.getDescription());
        eventLocation.setText(eventHomeModel.getEventAddress());
        dateEdittext.setText(eventHomeModel.getDate().toString());

        if(eventHomeModel.isPrivate()) {
            privateRadioButton.setChecked(true);
        } else {
            publicRadioButton.setChecked(true);
        }

        Iterator<String> valueIterator = eventHomeModel.getImageURL().values().iterator();
        String uri = "";
        while (valueIterator.hasNext()) {
            uri = uri + valueIterator.next();
        }
        Log.v(Constants.TAG, "Image File = " + uri + "");
        imageLoader.displayImage(uri, imageView);

        displayContactModelArrayList.clear();
        for(int i = 0; i<eventHomeModel.getContacts().size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(eventHomeModel.getContacts().get(i).getContactName()));
        }
    }

    /**
     * Show contacts button event listener
     */
    @OnClick(R.id.fragment_create_event_imagebutton2) void openContactDialog() {
        setEventDataInAdapter();
        DisplayContactAdapter adapter = new DisplayContactAdapter(mainActivity,displayContactModelArrayList);
        Holder holder = new ListHolder();
        showOnlyContentDialog(holder, adapter);
    }

    /**
     * Data in events has been initialize from here
     */
    public void setEventDataInAdapter() {

        temporaryContactDatabases = new Select().from(TemporaryContactDatabase.class).execute();
        for(int i = 0; i<temporaryContactDatabases.size();i++) {
            displayContactModelArrayList.add(new DisplayContactModel(temporaryContactDatabases.get(i).name));
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
        String[] ITEMS = {"Marriage", "Birthday", "Meeting", "Party", "Get Togeather", "Baby Shower"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(adapter);
    }



    /**
     * When certain position is selected from the drop down in auto complete
     * this method is fired
     */
    private void selectPosition() {
        placesAutocompleteTextView.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(Place place) {

            }
        });
    }

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

    @OnClick(R.id.fragment_create_event_button1) void openGallery() {

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

                final String uri = Uri.fromFile(imageFile).toString();
                final String decoded = Uri.decode(uri);
                Log.v(Constants.TAG, "Image File = " + uri + "");
                imageLoader.displayImage(decoded, imageView);
                imageURL.put("name", "");
                imageURL.put("container", uri);
            }

        });
    }

    @OnClick(R.id.fragment_create_event_button4) void selectContact() {
        mainActivity.replaceFragment(R.layout.layout_select_contact, null);
    }

    @OnClick(R.id.fragment_create_event_button2) void publishEvent() {

        //TODO If user is editing event and then pressing publish then it will check its id
        //if the id is not null means it is edited and then clear all back stack upto home fragment
        //FragmentManager.popBackStack(String name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        temporaryContactDatabases = new Select().from(TemporaryContactDatabase.class).execute();
        if(temporaryContactDatabases !=  null) {
            for (int i = 0; i < temporaryContactDatabases.size(); i++) {
                selectContactModelArrayList.add(new SelectContactModel(temporaryContactDatabases.get(i).name,
                        temporaryContactDatabases.get(i).number));
            }
        }

        int getCheckedButtonId = radioGroup.getCheckedRadioButtonId();
        if( getCheckedButtonId == R.id.fragment_create_event_radio_button1) {
            isPrivate = false;
        }
        else {
            isPrivate = true;
        }

        try {
            date = dateFormat.parse(dateEdittext.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        EventHomeModel eventHomeModel =  new EventHomeModel(null,eventName.getText().toString(),
                placesAutocompleteTextView.getText().toString(), eventDescription.getText().toString(),
                materialSpinner.getSelectedItem().toString(), date
                , selectContactModelArrayList,imageURL,isPrivate,latLongHashMap);

            EventBus.getDefault().post(eventHomeModel, EventHomeModel.onSave);

        TemporaryContactDatabase.deleteAll();
        clearFields();
        mainActivity.onBackPressed();
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
