package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 12/5/2015.
 * This adapter is used to show the location list in the home page of tha application.
 * Second list in the app
 */
public class HomeLocationAdapter extends RecyclerView.Adapter<HomeLocationAdapter.ViewHolder> {

    ArrayList<LocationHomeModel> locationHomeModels  = new ArrayList<LocationHomeModel>();
    Context context;
    public HomeLocationAdapter(Context context, ArrayList<LocationHomeModel> locationHomeModels) {
        this.locationHomeModels = locationHomeModels;
        this.context = context;
    }

    /**
     * Inflate the layout file layout.home.location here
     * @param parent {ViewGroup}
     * @param viewType {int}
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View locationView = inflater.inflate(R.layout.layout_home_location, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(locationView);
        return viewHolder;
    }

    /**
     * Set text in text fields from the data from the server
     * @param holder {ViewHolder}
     * @param position {int}
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LocationHomeModel locationHomeModel = locationHomeModels.get(position);

        // Set item views based on the data model
        TextView locationName = holder.locationName;
        TextView locationAddress = holder.locationAddress;

        locationName.setText(locationHomeModel.getLocationName());
        String[] shortAddress = locationHomeModel.getLocationAddress().split(",");
        locationAddress.setText(shortAddress[0]);

    }
    /**
     *
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        return locationHomeModels.size();
    }

    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_home_location_textview1) TextView locationName;
        @Bind(R.id.layout_home_location_textview2) TextView locationAddress;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void showOnlyContentDialog(Holder holder, BaseAdapter adapter) {
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setAdapter(adapter)
                . setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {

                    }
                })
                .setCancelable(true)
                .create();
        dialog.show();
    }

}
