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

import com.snaphy.mapstrack.MainActivity;

/**
 * Created by Ravi-Gupta on 3/21/2016.
 */
public class ContactMatcher implements  LoaderManager.LoaderCallbacks<Cursor>{
    MainActivity mainActivity;
    public ContactMatcher(MainActivity mainActivity){
        this.mainActivity = mainActivity;
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
            while (data.moveToNext())
            {
                int contactNameData = data.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
                int contactNumberData = data.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
/*

            final String contactNameDataString = data.getString(contactNameData);
            final String contactNumberDataString = data.getString(contactNumberData);
            //http://stackoverflow.com/questions/3813195/regular-expression-for-indian-mobile-numbers
            //^(?:(?:\+|0{0,2})91(\s*[\-]\s*)?|[0]?)?[789]\d{9}$

            ContactModel contactModel= new ContactModel();
            contactModel.setContactName(contactNameDataString);
            contactModel.setContactNumber(contactNumberDataString);
            contactModel.setIsSelected(false);
            contactModelArrayList.add(contactModel);
            Log.v(Constants.TAG, contactModel.getContactName());
*/

            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

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
        AsyncTask asyncTask = new FetchContact(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
