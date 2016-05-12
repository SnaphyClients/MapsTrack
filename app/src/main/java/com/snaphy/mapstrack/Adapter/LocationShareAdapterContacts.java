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

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.LastUpdatedLocation;
import com.google.android.gms.maps.model.LatLng;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.Map;

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
        if(user != null){
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
                    if(user.getLastUpdatedLocation() != null){
                        if(user.getLastUpdatedLocation().getCustomer() != null){
                            Customer customer = user.getLastUpdatedLocation().getCustomer();
                            if(customer.getLastUpdatedLocation() != null){

                                double latitude;
                                try {
                                    latitude = customer.getLastUpdatedLocationLatitide();
                                } catch (Exception e) {
                                    int lat = (int)customer.getLastUpdatedLocationLatitide();
                                    latitude = (double) lat;
                                }

                                double longitude;
                                try {
                                    longitude = customer.getLastUpdatedLocationLongitude();
                                } catch (Exception e) {
                                    int longi = (int)customer.getLastUpdatedLocationLongitude();
                                    longitude = (double) longi;
                                }

                                latLng = new LatLng(latitude, longitude);
                                if(latLng != null) {
                                    EventBus.getDefault().postSticky(latLng, Constants.OPEN_MAP_FROM_LOCATION);
                                    mainActivity.replaceFragment(R.layout.fragment_map, null);
                                   /* Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," + latLng.longitude);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    mainActivity.startActivity(mapIntent);*/
                                }

                            }
                        }
                    }
                    //fetch user current location..
                    /*if(user.getContactNumber() != null){
                        if(user.getCustomer().getLastUpdatedLocation() != null){
                            latLng = new LatLng(user.getCustomer().getLastUpdatedLocationLatitide(), user.getCustomer().getLastUpdatedLocationLatitide());
                            mainActivity.replaceFragment(R.layout.fragment_map, null);
                            EventBus.getDefault().postSticky(latLng, Constants.OPEN_MAP_FROM_LOCATION);
                        }
                    }
*/
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
                        if (TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRAGMENT)) {

                            if (BackgroundService.getCustomer() != null) {
                                //Now remove from customer list..
                                if (BackgroundService.getCustomer().getLastUpdatedLocations() != null) {
                                    //Now find the shared numbers..
                                    LastUpdatedLocation lastUpdatedLocation = BackgroundService.getCustomer().getLastUpdatedLocations();
                                    if (lastUpdatedLocation.getSharedLocation() != null) {
                                        boolean found = false;
                                        for (Map<String, Object> friendsObj : lastUpdatedLocation.getSharedLocation()) {
                                            if (friendsObj.get("number") != null) {
                                                String phoneNumber = String.valueOf(friendsObj.get("number"));
                                                if (phoneNumber.equals(user.getContactNumber())) {
                                                    //remove this item..
                                                    lastUpdatedLocation.getSharedLocation().remove(friendsObj);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }

                                        if (found) {
                                            //Remove the data..
                                            lastUpdatedLocation.save(new VoidCallback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError(Throwable t) {
                                                    Log.e(Constants.TAG, t.toString() + " in  LocationShareAdapter file");
                                                }
                                            });
                                        }
                                    }
                                }
                                //Now remove from list..
                                sharedLocation.remove(user);
                                Toast.makeText(mainActivity, "User removed!", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(sharedLocation, Constants.REMOVE_LOCATION_SHARED_BY_USER);
                            }
                        }else if (TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRIENDS_FRAGMENT)){
                            if(user != null){
                                boolean found = false;
                                if(user.getLastUpdatedLocation() != null){
                                    if(BackgroundService.getCustomer() != null){
                                        if(BackgroundService.getCustomer().getPhoneNumber() != null){
                                            String phoneNumber = mainActivity.formatNumber(BackgroundService.getCustomer().getPhoneNumber());
                                            for(Map<String, Object> sharedList : user.getLastUpdatedLocation().getSharedLocation()){
                                                if(sharedList != null){
                                                    if(sharedList.get("number") != null){
                                                        String targetNumber = mainActivity.formatNumber(String.valueOf(sharedList.get("number")));
                                                        if(targetNumber.equals(phoneNumber)){
                                                            user.getLastUpdatedLocation().getSharedLocation().remove(sharedList);
                                                            found = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                            if (found) {
                                                //Remove the data..
                                                user.getLastUpdatedLocation().save(new VoidCallback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError(Throwable t) {
                                                        Log.e(Constants.TAG, t.toString() + " in  LocationShareAdapter file");
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                            //Now remove from list..
                            sharedLocation.remove(user);
                            Toast.makeText(mainActivity, "User removed!", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(sharedLocation, Constants.REMOVE_LOCATION_SHARED_BY_USER_FRIENDS);
                        }
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