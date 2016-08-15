package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.snaphy.mapstrack.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 8/15/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    List<Track> eventList;
    public EventListAdapter(List<Track> eventList) {
        this.eventList = eventList;
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


        eventDistance.setText("3");
        eventDistanceMetric.setText("km");

        if(track.getEventDate() != null) {
            eventTime.setText("9 AM");
        }

        if(track.getEventDate() != null) {
            eventDate.setText("15 Aug");
        }

        if(track.getName() != null) {
            eventName.setText(track.getName());
        }

        if(track.getCustomer() != null) {
            if(track.getCustomer().getFirstName() != null) {
                eventCreator.setText("by " + track.getCustomer().getFirstName());
            }
        }

        if(track.getAddress() != null) {
            eventAddress.setText(track.getAddress());
        }

        if(track.getEventType() != null) {
            if(track.getEventType().getName() != null) {
                eventType.setText(track.getEventType().getName());
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
