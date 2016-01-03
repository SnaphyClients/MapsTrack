package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ShareLocationModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 1/3/2016.
 */
public class LocationShareAdapterContacts  extends RecyclerView.Adapter<LocationShareAdapterContacts.ViewHolder>  {

    ArrayList<ShareLocationModel> shareLocationModelArrayList  = new ArrayList<ShareLocationModel>();
    Context context;
    static MainActivity mainActivity;
    String TAG;
    LatLng latLng;
    public LocationShareAdapterContacts(MainActivity mainActivity, ArrayList<ShareLocationModel> selectContactModels, String TAG) {
        this.shareLocationModelArrayList = selectContactModels;
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
        final ShareLocationModel shareLocationModel = shareLocationModelArrayList.get(position);

        // Set item views based on the data model
        TextView contactName = holder.contactName;
        ImageButton deleteContact = holder.deleteContact;

        contactName.setText(shareLocationModel.getContactName());

        holder.deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("",TAG);
                Log.v("",Constants.LOCATION_SHARE_BY_USER_FRAGMENT);
                if(TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRAGMENT)) {
                    EventBus.getDefault().post(shareLocationModel, Constants.DELETE_LOCATION_SHARED_BY_USER);
                } else if(TAG.equals(Constants.LOCATION_SHARE_BY_USER_FRIENDS_FRAGMENT)){
                    EventBus.getDefault().post(shareLocationModel, Constants.DELETE_LOCATION_SHARED_BY_USER_FRIENDS);
                }
            }
        });

        holder.contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLng = new LatLng(shareLocationModel.getLatLong().get("latitude"),shareLocationModel.getLatLong().get("longitude"));
                mainActivity.replaceFragment(R.layout.fragment_map, null);
                EventBus.getDefault().postSticky(latLng, Constants.OPEN_MAP_FROM_LOCATION);
            }
        });

    }

    /**
     *
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        return shareLocationModelArrayList.size();
    }

    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_fragment_display_contact_textview1) TextView contactName;
        @Bind(R.id.layout_fragment_display_contact_imagebutton1) ImageButton deleteContact;

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