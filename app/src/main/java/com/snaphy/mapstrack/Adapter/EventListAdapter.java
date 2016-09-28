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
 * Created by Ravi-Gupta on 8/15/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    List<Track> eventList;
    MainActivity mainActivity;
    public EventListAdapter(List<Track> eventList, MainActivity mainActivity) {
        this.eventList = eventList;
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.layout_event_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Track track = eventList.get(position);

        // Set item views based on the data model
        TextView eventDistance = holder.eventDistance;
        TextView eventDistanceMetric = holder.eventDistanceMetric;
        TextView eventTime = holder.eventTime;
        TextView eventDate = holder.eventDate;
        TextView eventName = holder.eventName;
        TextView eventCreator = holder.eventCreator;
        TextView eventAddress = holder.eventAddress;
        TextView eventType = holder.eventType;

        Typeface typeface = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/gothic_bold.ttf");
        eventDistance.setTypeface(typeface);
        eventDistanceMetric.setTypeface(typeface);
        eventTime.setTypeface(typeface);
        eventName.setTypeface(typeface2);
        eventType.setTypeface(typeface);
        eventDate.setTypeface(typeface2);


        if(track.getGeolocationLatitide() != 0) {
            if (track.getGeolocationLongitude() != 0) {
                double distance = mainActivity.CalculationByDistance(BackgroundService.getCurrentLocation().latitude, BackgroundService.getCurrentLocation().longitude,
                        track.getGeolocationLatitide(), track.getGeolocationLongitude());
                int distanceInKm = (int)distance/1000;
                if(distanceInKm == 0){
                    distanceInKm = 1;
                    eventDistance.setText(distanceInKm+"");
                    eventDistanceMetric.setText("+ "+Constants.KM);
                } else if (distanceInKm > 99) {
                    distanceInKm = 99;
                    eventDistance.setText(distanceInKm+"");
                    eventDistanceMetric.setText("+ "+Constants.KM);
                } else {
                    eventDistance.setText(distanceInKm+"");
                    eventDistanceMetric.setText("+ "+Constants.KM);
                }

            }
        }




        if(track.getEventTime() != null) {
            if(!track.getEventTime().isEmpty()) {
                eventTime.setText(track.getEventTime()+"");
            } else {
                //
            }
        } else {
            //
        }


        if(track.getEventDate() != null){
            if(!track.getEventDate().isEmpty()){
                CharSequence eDate;
                try {
                    eDate = mainActivity.drawTextViewDesign("", mainActivity.parseDate(track.getEventDate()));
                } catch (Exception e) {
                    eDate = mainActivity.drawTextViewDesign("", track.getEventDate());
                }
                eventDate.setText(eDate);
            }
        }

        if(track.getName() != null) {
            eventName.setText(WordUtils.capitalize(track.getName()));
        }

        if(track.getCustomer() != null) {
            if(track.getCustomer().getFirstName() != null) {
                eventCreator.setText("by " + WordUtils.capitalize(track.getCustomer().getFirstName()));
            }
        }

        if(track.getAddress() != null) {
            eventAddress.setText(WordUtils.capitalize(track.getAddress()));
        }

        if(track.getType() != null){
            if(!track.getType().isEmpty()) {
                mainActivity.displayEventTypeName(eventType, track);
                //eventType.setText(eType);
            }
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_event_list_textview1) TextView eventDistance;
        @Bind(R.id.layout_event_list_textview2) TextView eventDistanceMetric;
        @Bind(R.id.layout_event_list_textview3) TextView eventTime;
        @Bind(R.id.layout_event_list_textview4) TextView eventDate;
        @Bind(R.id.layout_event_list_textview5) TextView eventName;
        @Bind(R.id.layout_event_list_textview6) TextView eventCreator;
        @Bind(R.id.layout_event_list_textview7) TextView eventAddress;
        @Bind(R.id.layout_event_list_textview8) TextView eventType;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
