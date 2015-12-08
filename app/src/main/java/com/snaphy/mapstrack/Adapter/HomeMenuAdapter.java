package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.snaphy.mapstrack.R;

/**
 * Created by Ravi-Gupta on 12/7/2015.
 * It is an adapter that is responsible for displaying of menu item in the home screen when the users
 * clicks on the menu icon of a particular list view item
 */
public class HomeMenuAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    public HomeMenuAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageButton imageButton;
    }

    @Override
    public int getCount() {
        return 3;
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
            view = layoutInflater.inflate(R.layout.layout_home_menu_options, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageButton = (ImageButton) view.findViewById(R.id.layout_home_menu_options_imagebutton1);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        switch (position) {
            case 0:
                viewHolder.imageButton.setImageResource(R.mipmap.share_purple);
                break;
            case 1:
                viewHolder.imageButton.setImageResource(R.mipmap.edit_purple);
                break;
            default:
                viewHolder.imageButton.setImageResource(R.mipmap.delete_purple);
                break;
        }

        return view;
    }
}
