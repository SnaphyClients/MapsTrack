package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 12/4/2015.
 * This adapter is used to show the list of events in the home page of the app
 */
public class HomeEventAdapter  extends RecyclerView.Adapter<HomeEventAdapter.ViewHolder>  {

    ArrayList<EventHomeModel> eventHomeModels  = new ArrayList<EventHomeModel>();
    Context context;
    static MainActivity mainActivity;
    public HomeEventAdapter(Context context, ArrayList<EventHomeModel> eventHomeModels, MainActivity mainActivity) {
        this.eventHomeModels = eventHomeModels;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    /**
     * Inflate the layout file layout.home.event here
     * @param parent {ViewGroup}
     * @param viewType {int}
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.layout_home_event, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    /**
     * Set text in text fields from the data from the server
     * @param holder {ViewHolder}
     * @param position {int}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EventHomeModel eventHomeModel = eventHomeModels.get(position);

        // Set item views based on the data model
        TextView eventId = holder.eventId;
        TextView eventAddress = holder.eventAddress;
        ImageButton menuOption = holder.menuOption;

        eventId.setText(eventHomeModel.getEventId());
        eventAddress.setText(eventHomeModel.getEventAddress());

        menuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMenuAdapter adapter = new HomeMenuAdapter(context,eventHomeModel);
                Holder holder = new ListHolder();
                showOnlyContentDialog(holder, adapter);
            }
        });
    }

    /**
     *
     * @return number of fields in the list
     */
    @Override
    public int getItemCount() {
        return eventHomeModels.size();
    }

    /**
     * View Holder class used to display all the elements in recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout_home_event_textview1) TextView eventId;
        @Bind(R.id.layout_home_event_textview2) TextView eventAddress;
        @Bind(R.id.layout_home_event_imageButton1) ImageButton menuOption;


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
        final DialogPlus dialog = DialogPlus.newDialog(mainActivity)
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
