package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 3/3/2016.
 */
public class ShowContactAdapter extends RecyclerView.Adapter<ShowContactAdapter.ViewHolder> {

    ArrayList<ContactModel> contactModels;
    Context context;

    public ShowContactAdapter(Context context, ArrayList<ContactModel> contactModels) {
        this.contactModels = contactModels;
        this.context = context;
    }

    /**
     * Inflate the layout file layout.home.event here
     *
     * @param parent   {ViewGroup}
     * @param viewType {int}
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.layout_select_contact, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    /**
     * Set text in text fields from the data from the server
     *
     * @param holder   {ViewHolder}
     * @param position {int}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContactModel contactModel = contactModels.get(position);

        // Set item views based on the data model
        TextView contactName = holder.contactName;
        TextView contactNumber = holder.contactNumber;
        CheckBox checkBox = holder.checkBox;

        contactName.setText(contactModel.getContactName());
        contactNumber.setText(contactModel.getContactNumber());
        checkBox.setClickable(false);
        if(contactModel.isSelected()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

    }

    /**
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_fragment_show_contact_textview1) TextView contactName;
        @Bind(R.id.layout_fragment_show_contact_textview2) TextView contactNumber;
        @Bind(R.id.layout_fragment_show_contact_checkbox1) CheckBox checkBox;

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