package com.snaphy.mapstrack.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.EventType;
import com.snaphy.mapstrack.R;

import java.util.List;

/**
 * Created by Ravi-Gupta on 3/22/2016.
 */
public class SpinnerAdapter extends ArrayAdapter<EventType> {

    List<EventType> eventTypeList;
    Context context;

    public SpinnerAdapter(Context context, int resource, List<EventType> eventTypeList) {
        super(context, resource, eventTypeList);
        this.context = context;
        this.eventTypeList = eventTypeList;
    }

    static class ViewHolder
    {
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.layout_spinner, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView)row.findViewById(R.id.layout_spinner_textview1);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        EventType eventType = eventTypeList.get(position);
        holder.textView.setText(eventType.getName().toString());

        return row;
    }
}
