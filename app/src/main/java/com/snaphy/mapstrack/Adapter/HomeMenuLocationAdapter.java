package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/21/2015.
 */
public class HomeMenuLocationAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    LocationHomeModel locationHomeModel;
    Context context;
    ArrayList<String> contacts = new ArrayList<String>();

    public HomeMenuLocationAdapter(Context context,  LocationHomeModel locationHomeModel) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.locationHomeModel = locationHomeModel;
    }

    static class ViewHolder {
        ImageButton shareButton;
        ImageButton editButton;
        ImageButton deleteButton;
        TextView locationName;
        TextView locationId;
        TextView address;
        ListView contactList;
    }

    @Override
    public int getCount() {
        return 1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_location_home_menu_options, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.shareButton = (ImageButton) view.findViewById(R.id.layout_location_home_menu_options_imagebutton1);
            viewHolder.editButton = (ImageButton) view.findViewById(R.id.layout_location_home_menu_options_imagebutton2);
            viewHolder.deleteButton = (ImageButton) view.findViewById(R.id.layout_location_home_menu_options_imagebutton3);
            viewHolder.locationName = (TextView) view.findViewById(R.id.layout_location_home_menu_options_textview1);
            viewHolder.locationId = (TextView) view.findViewById(R.id.layout_location_home_menu_options_textview2);
            viewHolder.address = (TextView) view.findViewById(R.id.layout_location_home_menu_options_textview3);
            viewHolder.contactList = (ListView) view.findViewById(R.id.layout_location_home_menu_options_listview);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.locationName.setText(locationHomeModel.getLocationName());
        viewHolder.locationId.setText(locationHomeModel.getLocationId());
        viewHolder.address.setText(locationHomeModel.getLocationAddress());

        for(int i = 0; i < locationHomeModel.getContacts().size(); i++) {
            contacts.add(i , locationHomeModel.getContacts().get(i).getContactName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.layout_dialog_contact, R.id.layout_fragment_dialog_contact_textview1, contacts );
        // Sets the adapter for the ListView
        viewHolder.contactList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(viewHolder.contactList);
        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
