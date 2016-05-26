package com.snaphy.mapstrack.Helper;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.snaphy.mapstrack.Adapter.DisplayContactAdapter;
import com.snaphy.mapstrack.Adapter.LocationShareAdapterContacts;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;

import java.util.List;

/**
 * Created by Ravi-Gupta on 3/21/2016.
 */
public class ContactMatcher implements  LoaderManager.LoaderCallbacks<Cursor>{
    MainActivity mainActivity;
    List<ContactModel> contactModels;
    DisplayContactAdapter displayContactAdapter;
    LocationShareAdapterContacts locationShareAdapterContacts;
    Cursor globalCursor;

    public ContactMatcher(MainActivity mainActivity, List<ContactModel> contactModels, DisplayContactAdapter displayContactAdapter){
        this.mainActivity = mainActivity;
        this.contactModels = contactModels;
        this.displayContactAdapter = displayContactAdapter;
        mainActivity.getSupportLoaderManager().initLoader(0, null, this);
    }

    public ContactMatcher(MainActivity mainActivity, List<ContactModel> contactModels, LocationShareAdapterContacts locationShareAdapterContacts){
        this.mainActivity = mainActivity;
        this.contactModels = contactModels;
        this.locationShareAdapterContacts = locationShareAdapterContacts;
        mainActivity.getSupportLoaderManager().initLoader(0, null, this);
    }


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



    private class FetchContact extends AsyncTask<String, Void, String> {

        public Cursor data;

        public FetchContact(Cursor data){
            this.data = data;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                while (data.moveToNext()) {
                    int contactNameData = data.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
                    int contactNumberData = data.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    final String contactNameDataString = data.getString(contactNameData);
                    String contactNumberDataString = data.getString(contactNumberData);

                    contactNumberDataString = mainActivity.formatNumber(contactNumberDataString);

                    //Now start matching..
                    for (ContactModel contactModel : contactModels) {
                        if (String.valueOf(contactModel.getContactNumber()) != null) {
                            if (!String.valueOf(contactModel.getContactNumber()).isEmpty()) {
                                String friendNumber = contactModel.getContactNumber().trim();
                                friendNumber = mainActivity.formatNumber(friendNumber);
                                if (friendNumber.equals(contactNumberDataString.toString().trim())) {
                                    if (contactNameDataString != null) {
                                        if (!contactNameDataString.isEmpty()) {
                                            contactModel.setContactName(contactNameDataString);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                data.moveToFirst();
            } catch (Exception e) {
                Log.v(Constants.TAG, e.toString());
            }
            finally {
               /* data.close();
                data = null;*/
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if(displayContactAdapter != null){
                //Now notify the adapter..
                displayContactAdapter.notifyDataSetChanged();
            }else{
                locationShareAdapterContacts.notifyDataSetChanged();
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
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
                mainActivity,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null,
                order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Now process data in another thread..
        globalCursor = data;
        AsyncTask asyncTask = new FetchContact(globalCursor);
        asyncTask.execute(new String[] {""});
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
