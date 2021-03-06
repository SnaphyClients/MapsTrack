package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 3/3/2016.
 */
public class ShowContactAdapter extends RecyclerView.Adapter<ShowContactAdapter.ViewHolder> {

    Map<String, ContactModel> contactModels;
    //List of all contacts which is going to be displayed..format Number(KEY) -> Name(VALUE)
    LinkedHashMap<String, String> contactList;
    //Refer to http://stackoverflow.com/questions/18308376/using-hashmaps-in-custom-adapters-for-listview
    //Get the key array of contactList hashMap
    private String[] mKeys;
    MainActivity mainActivity;
    //Cursor cursor;

    public ShowContactAdapter(MainActivity mainActivity, Map<String, ContactModel>  contactModels, LinkedHashMap<String, String> contactList) {
        this.contactModels = contactModels;
        this.contactList = contactList;
        mKeys = this.contactList.keySet().toArray(new String[contactList.size()]);
        this.mainActivity = mainActivity;
    }

    /**
     * Inflate the layout file layout.home.event here
     *
     * @param parent   {ViewGroup}
     * @param viewType {int}
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.layout_select_contact, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    /**
     * Set text in text fields from the data from the server
     *
     * @param holder   {ViewHolder}
     * @param position {int}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(mKeys[position] != null ) {
            String key = mKeys[position];
            /*int contactNameData = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
            int contactNumberData = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);


                final String contactNameDataString = cursor.getString(contactNameData);

                String contactNumberDataString = cursor.getString(contactNumberData);*/
            final String contactNameDataString = contactList.get(key);
            String contactNumberDataString = key;

                if (contactNumberDataString != null) {
                    // Set item views based on the cursor model
                    TextView contactName = holder.contactName;
                    TextView contactNumber = holder.contactNumber;
                    CheckBox checkBox = holder.checkBox;
                    contactName.setText(contactNameDataString);
                    contactNumber.setText(contactNumberDataString);
                    checkBox.setClickable(false);
                    //String formattedNumber = mainActivity.formatNumber(contactNumberDataString);

                    //Cannot share with your own number
                    if(BackgroundService.getCustomer() != null) {
                        if(BackgroundService.getCustomer().getPhoneNumber() != null) {
                            if(contactNumberDataString.equals(BackgroundService.getCustomer().getPhoneNumber())) {
                                checkBox.setVisibility(View.GONE);
                            } else {
                                checkBox.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    //Now match it with formatted number..
                    ContactModel contactModel = contactModels.get(contactNumberDataString);
                    if (contactModel != null) {
                        if (contactModel.isSelected()) {
                            checkBox.setChecked(true);
                        } else {
                            checkBox.setChecked(false);
                        }
                    } else {
                        checkBox.setChecked(false);
                    }
                }
            }
    }

    /**
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        //return cursor.getCount();
        return contactList.size();
    }



    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_fragment_show_contact_textview1) TextView contactName;
        @Bind(R.id.layout_fragment_show_contact_textview2) TextView contactNumber;
        @Bind(R.id.layout_fragment_show_contact_checkbox1) CheckBox checkBox;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}