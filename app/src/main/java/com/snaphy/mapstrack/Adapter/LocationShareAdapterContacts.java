package com.snaphy.mapstrack.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 1/3/2016.
 */
public class LocationShareAdapterContacts  extends RecyclerView.Adapter<LocationShareAdapterContacts.ViewHolder>  {

    Context context;
    static MainActivity mainActivity;
    String TAG;
    LatLng latLng;
    List<ContactModel> sharedLocation;
    public LocationShareAdapterContacts(MainActivity mainActivity, List<ContactModel> sharedLocation, String TAG) {
        this.sharedLocation = sharedLocation;
        this.mainActivity = mainActivity;
        this.TAG = TAG;
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
    }

    /**
     * Inflate the layout file layout.display.contact here
     * @param parent {ViewGroup}
     * @param viewType {int}
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.layout_display_contact, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * Set text in text fields from the data from the server
     * @param holder {ViewHolder}
     * @param position {int}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContactModel user = sharedLocation.get(position);

        // Set item views based on the data model
        TextView contactNumber = holder.contactNumber;
        ImageButton deleteContact = holder.deleteContact;
        TextView contactName = holder.contactName;
        LinearLayout linearLayout = holder.linearLayout;
        if(user.getCustomer() != null){
            if(user.getContactName() != null){
                contactName.setText(user.getContactName().toString());
            }

            if(user.getContactNumber() != null) {
                contactNumber.setText(user.getContactNumber().toString());
            }
        }


        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(user);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRAGMENT)) {
                    // Nothing can be done here
                } else if(TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRIENDS_FRAGMENT)){
                    if(user.getCustomer().getLastUpdatedLocation() != null){
                        latLng = new LatLng(user.getCustomer().getLastUpdatedLocationLatitide(), user.getCustomer().getLastUpdatedLocationLatitide());
                        mainActivity.replaceFragment(R.layout.fragment_map, null);
                        EventBus.getDefault().postSticky(latLng, Constants.OPEN_MAP_FROM_LOCATION);
                    }

                }
            }
        });

    }

    private void showDialog(final ContactModel user) {
        new AlertDialog.Builder(mainActivity)
                .setTitle("Delete Contact?")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(BackgroundService.getCustomer() != null){
                            List<String> fk = new ArrayList<String>();
                            fk.add((String)user.getCustomer().getId());
                            BackgroundService.getCustomerRepository().__disconnect__location_shared((String) BackgroundService.getCustomer().getId(), fk, new Adapter.JsonObjectCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {

                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.e(Constants.TAG, t.toString());
                                }
                            });
                            //Now remove from list..
                            sharedLocation.remove(user);
                            Toast.makeText(mainActivity, "User removed!", Toast.LENGTH_SHORT).show();
                            //TODO CALL GET NOTIFY SET CHANGE TO FRAGMENT...


                        }
                        /*if(TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRAGMENT)) {
                            EventBus.getDefault().post(shareLocationModel, Constants.DELETE_LOCATION_SHARED_BY_USER);
                        } else if(TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRIENDS_FRAGMENT)){
                            EventBus.getDefault().post(shareLocationModel, Constants.DELETE_LOCATION_SHARED_BY_USER_FRIENDS);
                        }*/
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     *
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        return sharedLocation.size();
    }

    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_fragment_display_contact_textview1) TextView contactNumber;
        @Bind(R.id.layout_fragment_display_contact_imagebutton1) ImageButton deleteContact;
        @Bind(R.id.layout_fragment_display_contact_textview0) TextView contactName;
        @Bind(R.id.layout_display_contact_linear_layout1) LinearLayout linearLayout;

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