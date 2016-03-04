package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/6/2015.
 * This adapter is used to diplay list of contact in the create location and create event fragments
 */
public class DisplayContactAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    ArrayList<DisplayContactModel> displayContactModelArrayList = new ArrayList<DisplayContactModel>();

    public DisplayContactAdapter(Context context, ArrayList<DisplayContactModel> displayContactModelArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.displayContactModelArrayList = displayContactModelArrayList;
    }

    /**
     * Inflate the layout file layout.display.contact here
     *
     * @return
     */
    static class ViewHolder {
        TextView textview;
        ImageButton imageButton;
    }

    @Override
    public int getCount() {
        return displayContactModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_display_contact, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageButton = (ImageButton) view.findViewById(R.id.layout_fragment_display_contact_imagebutton1);
            viewHolder.textview = (TextView) view.findViewById(R.id.layout_fragment_display_contact_textview1);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.textview.setText(displayContactModelArrayList.get(position).getContactName());
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayContactModelArrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
