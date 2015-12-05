package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.snaphy.mapstrack.Model.ShareLocationModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Ravi-Gupta on 11/28/2015.
 */
public class ShareLocationAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    ArrayList<ShareLocationModel> locationModels = new ArrayList<ShareLocationModel>();

    public ShareLocationAdapter(ArrayList<ShareLocationModel> locationModels) {
        this.locationModels = locationModels;
    }

    class ViewHolder {
        TextView contactName;
    }

    class HeaderViewHolder {
        TextView shared;
    }

    /**
     * This method is used to inflate header layout file and display header in cart view
     * @param position {int} get position of the header
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.layout_fragment_share_header, parent, false);
            holder.shared = (TextView) convertView.findViewById(R.id.layout_fragment_share_header_textview1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        if(locationModels.get(position).getShared().equals("to")) {
            holder.shared.setText("Location shared by you");
        } else {
            holder.shared.setText("Location shared by others");
        }

        return convertView;
    }

    /**
     *
     * @param position
     * @return {long} how header should be displayed in the cart
     */
    // TODO Better Algorithm
    @Override
    public long getHeaderId(int position) {
        return locationModels.get(position).getShared().subSequence(0, 1).charAt(0);
    }

    /**
     *
     * @return {int} return size of the cart item
     */
    @Override
    public int getCount() {
        return locationModels.size();
    }

    /**
     *
     * @param i
     * @return {Object} return item at particular position
     */
    @Override
    public Object getItem(int i) {
        return locationModels.get(i);
    }

    /**
     *
     * @param i
     * @return {long} return item Id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Inflate the item view layout and diplay list of item in cart
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_fragment_share_item, viewGroup, false);
            holder.contactName = (TextView) view.findViewById(R.id.layout_fragment_share_item_textview1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.contactName.setText(locationModels.get(i).getContactName());

        return view;
    }
}
