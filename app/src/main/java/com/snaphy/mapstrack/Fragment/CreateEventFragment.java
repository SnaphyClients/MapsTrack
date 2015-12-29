package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.activeandroid.query.Select;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * This Fragment is used to create new event by clicking on "Add(Plus)" button in the home fragment
 *
 */
public class CreateEventFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CreateEventFragment";
    RecyclerView recyclerView;

    @Bind(R.id.fragment_create_event_imagebutton1) ImageButton backButton;
    @Bind(R.id.fragment_create_event_edittext3) EditText dateEdittext;
    @Bind(R.id.fragment_create_event_edittext2) EditText eventLocation;
    @Bind(R.id.fragment_create_event_edittext1) EditText eventName;
    @Bind(R.id.fragment_create_event_edittext4) EditText eventDescription;

    fr.ganfra.materialspinner.MaterialSpinner materialSpinner;
    static com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocompleteTextView;
    DisplayContactAdapter displayContactAdapter;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();
    List<TemporaryContactDatabase> temporaryContactDatabases;
    ArrayList<SelectContactModel> selectContactModelArrayList = new ArrayList<SelectContactModel>();
    MainActivity mainActivity;
    DateFormat dateFormat;
    Date date;


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

        temporaryContactDatabases = new Select().from(TemporaryContactDatabase.class).execute();
        dateFormat = new SimpleDateFormat();
        setSpinner();
        datePickerClickListener(view);
        dateEdittext.setKeyListener(null);
        selectPosition();
        return view;
    }

//    public void onEvent(AddressEvent event){
//        Toast.makeText(mainActivity,event.getAddress(),Toast.LENGTH_SHORT).show();
//        placesAutocompleteTextView.setText(event.getAddress());
//    }

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
        displayContactModelArrayList.clear();
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
                mainActivity.onBackPressed();
            }
        });
    }

    /**
     * This method is fired when any date from the date picker is selected
     * @param view
     */
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

    @OnClick(R.id.fragment_create_event_button4) void selectContact() {
        mainActivity.replaceFragment(R.layout.layout_select_contact, null);
    }

    @OnClick(R.id.fragment_create_event_button2) void publishEvent() {

        for (int i = 0; i<temporaryContactDatabases.size(); i++) {
            selectContactModelArrayList.add(new SelectContactModel(temporaryContactDatabases.get(i).name,
                    temporaryContactDatabases.get(i).number));
        }

        try {
            date = dateFormat.parse(dateEdittext.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        EventHomeModel eventHomeModel =  new EventHomeModel(eventName.getText().toString(),
                placesAutocompleteTextView.getText().toString(), eventDescription.getText().toString(),
                materialSpinner.getSelectedItem().toString(), date
                , selectContactModelArrayList);

        EventBus.getDefault().post(eventHomeModel, EventHomeModel.onSave);
        TemporaryContactDatabase.deleteAll();
        //TODO check its occurance
        setEventDataInAdapter();
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
