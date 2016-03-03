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
import android.widget.ListView;

import com.snaphy.mapstrack.Adapter.ShowContactAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.RecyclerItemClickListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private OnFragmentInteractionListener mListener;
    public static String TAG = "ShowContactFragment";
    @Bind(R.id.fragment_show_contact_recycler_view1) RecyclerView recyclerView;
    ShowContactAdapter showContactAdapter;
    ArrayList<ContactModel> contactModelArrayList = new ArrayList<ContactModel>();
    TemporaryContactDatabase temporaryContactDatabase;

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
    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            R.id.layout_fragment_show_contact_textview1,
            R.id.layout_fragment_show_contact_textview2
    };
    ListView mContactsList;
    // The contact's _ID value
    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView

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
        getLoaderManager().initLoader(0, null, this);
        temporaryContactDatabase = new TemporaryContactDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_contact, container, false);
        ButterKnife.bind(this, view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        showContactAdapter = new ShowContactAdapter(mainActivity,contactModelArrayList);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContactModel contactModel = contactModelArrayList.get(position);
                        Log.v(Constants.TAG, contactModel.getContactName());
                        if(contactModel.isSelected()) {
                            contactModel.setIsSelected(false);
                            showContactAdapter.notifyDataSetChanged();
                        } else {
                            contactModel.setIsSelected(true);
                            SelectContactModel selectContactModel;
                            temporaryContactDatabase.name = contactModel.getContactName();
                            temporaryContactDatabase.number = contactModel.getContactNumber();
                            temporaryContactDatabase.save();
                            selectContactModel = new SelectContactModel(null, contactModel.getContactName(), contactModel.getContactNumber());
                            EventBus.getDefault().post(selectContactModel, Constants.ADD_CONTACTS_IN_SHARE_LOCATION);
                            showContactAdapter.notifyDataSetChanged();
                        }

                    }
                })
        );

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick(R.id.fragment_show_contact_imagebutton1) void backButton() {
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_show_contact_button1) void contactSelected() {
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
        // Put the result Cursor in the adapter for the ListView
        //Log.v(Constants.TAG, data.get + "Yess");
        while (data.moveToNext())
        {

            int contactNameData = data.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
            int contactNumberData = data.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

            final String contactNameDataString = data.getString(contactNameData);
            final String contactNumberDataString = data.getString(contactNumberData);

            ContactModel contactModel= new ContactModel();
            contactModel.setContactName(contactNameDataString);
            contactModel.setContactNumber(contactNumberDataString);
            contactModel.setIsSelected(false);
            contactModelArrayList.add(contactModel);
        }
        recyclerView.setAdapter(showContactAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //showContactAdapter.changeCursor(null);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
