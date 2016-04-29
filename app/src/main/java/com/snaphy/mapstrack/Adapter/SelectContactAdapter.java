/*
package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;

*/
/**
 * Created by Ravi-Gupta on 12/24/2015.
 *//*

public class SelectContactAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor data;
    private final LayoutInflater inflater;
    SelectContactModel selectContactModel;

    public SelectContactAdapter(Context context,int layout, Cursor data,String[] from,int[] to) {
        super(context,layout,data,from,to);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.data=data;
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.layout_select_contact, null);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        View row = view;
        Holder holder = null;

        final TemporaryContactDatabase temporaryContactDatabase;
        if(row == null) {

            holder.contactName = (TextView)view.findViewById(R.id.layout_fragment_show_contact_textview1);
            holder.contactNumber = (TextView)view.findViewById(R.id.layout_fragment_show_contact_textview2);
            holder.checkBox = (CheckBox) view.findViewById(R.id.layout_fragment_show_contact_checkbox1);
            temporaryContactDatabase = new TemporaryContactDatabase();
            row.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
            //holder.checkBox.setOnCheckedChangeListener(null);
            temporaryContactDatabase = new TemporaryContactDatabase();
        }


        int contactNameData = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
        int contactNumberData = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

        final String contactNameDataString = cursor.getString(contactNameData);
        final String contactNumberDataString = cursor.getString(contactNumberData);

        holder.contactName.setText(cursor.getString(contactNameData));
        holder.contactNumber.setText(cursor.getString(contactNumberData));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, contactNameDataString + contactNumberDataString + "", Toast.LENGTH_SHORT).show();

                temporaryContactDatabase.name = contactNameDataString;
                temporaryContactDatabase.number = contactNumberDataString;
                temporaryContactDatabase.save();
                selectContactModel = new SelectContactModel(null, contactNameDataString, contactNumberDataString);
                EventBus.getDefault().post(selectContactModel, Constants.ADD_CONTACTS_IN_SHARE_LOCATION);
            }
        });

    }

    static class Holder
    {
        TextView contactName;
        TextView contactNumber;
        CheckBox checkBox;
    }

}*/
