package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.snaphy.mapstrack.WordUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 8/17/2016.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    List<Track> locationList;
    MainActivity mainActivity;
    public LocationListAdapter(List<Track> locationList, MainActivity mainActivity) {
        this.locationList = locationList;
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.layout_location_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Track track = locationList.get(position);

        // Set item views based on the data model
        TextView locationDistance = holder.locationDistance;
        TextView locationDistanceMetric = holder.locationDistanceMetric;
        TextView locationName = holder.locationName;
        TextView locationCreator = holder.locationCreator;
        TextView locationAddress = holder.locationAddress;

        Typeface typeface = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/gothic_bold.ttf");

        locationDistance.setTypeface(typeface);
        locationDistanceMetric.setTypeface(typeface);
        locationName.setTypeface(typeface2);

        if(track.getGeolocationLatitide() != 0) {
            if (track.getGeolocationLongitude() != 0) {
                double distance = mainActivity.CalculationByDistance(BackgroundService.getCurrentLocation().latitude, BackgroundService.getCurrentLocation().longitude,
                        track.getGeolocationLatitide(), track.getGeolocationLongitude());
                int distanceInKm = (int)distance/1000;
                if(distanceInKm < 0 || distanceInKm == 0){
                    distanceInKm = 0;
                    locationDistance.setText(distanceInKm+"");
                    locationDistanceMetric.setText(Constants.KM);
                } else if (distanceInKm > 99) {
                    distanceInKm = 99;
                    locationDistance.setText(distanceInKm+"");
                    locationDistanceMetric.setText("+ "+Constants.KM);
                } else {
                    locationDistance.setText(distanceInKm+"");
                    locationDistanceMetric.setText("+ "+Constants.KM);
                }

            }
        }

        if(track.getLocationId() != null) {
            locationName.setText(track.getLocationId());
        } else {
            locationName.setText("");
        }

        if(track.getCustomer() != null) {
            if(track.getCustomer().getFirstName() != null) {
                locationCreator.setText("by " + WordUtils.capitalize(track.getCustomer().getFirstName()));
            }
        }

        if(track.getAddress() != null) {
            locationAddress.setText(WordUtils.capitalize(track.getAddress()));
        }


    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_location_list_textview1) TextView locationDistance;
        @Bind(R.id.layout_location_list_textview2) TextView locationDistanceMetric;
        @Bind(R.id.layout_location_list_textview3) TextView locationName;
        @Bind(R.id.layout_location_list_textview4) TextView locationCreator;
        @Bind(R.id.layout_location_list_textview5) TextView locationAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
