package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaphy.mapstrack.Model.DisplayContactModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 12/6/2015.
 * This adapter is used to diplay list of contact in the create location and create event fragments
 */
public class DisplayContactAdapter extends RecyclerView.Adapter<DisplayContactAdapter.ViewHolder> {

    ArrayList<DisplayContactModel> displayContactModels  = new ArrayList<DisplayContactModel>();
    public DisplayContactAdapter(ArrayList<DisplayContactModel> displayContactModels ) {
        this.displayContactModels = displayContactModels;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        DisplayContactModel displayContactModel = displayContactModels.get(position);

        // Set item views based on the data model
        TextView contactName = holder.contactName;
        contactName.setText(displayContactModel.getContactName());

    }

    /**
     *
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        return displayContactModels.size();
    }

    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_fragment_display_contact_textview1) TextView contactName;



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
