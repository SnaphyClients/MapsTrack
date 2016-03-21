package com.snaphy.mapstrack.Adapter;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.snaphy.mapstrack.Helper.ContactMatcher;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 12/6/2015.
 * This adapter is used to diplay list of contact in the create location and create event fragments
 */
public class DisplayContactAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    List<ContactModel> sharedFriendList = new ArrayList<>();
    Track track;
    MainActivity mainActivity;
    DisplayContactAdapter that;

    public DisplayContactAdapter(MainActivity mainActivity, Track track) {
        that = this;
        this.track = track;
        this.mainActivity = mainActivity;
        layoutInflater = LayoutInflater.from(mainActivity);
        AsyncTask asyncTask = new PopulateContact(track, sharedFriendList);
        asyncTask.execute(new String[] {""});

    }

    /**
     * Inflate the layout file layout.display.contact here
     *
     * @return
     */
    static class ViewHolder {
        TextView textview;
        TextView name;
        ImageButton imageButton;
    }

    @Override
    public int getCount() {
        return sharedFriendList.size();
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
            viewHolder.name = (TextView) view.findViewById(R.id.layout_fragment_display_contact_textview0);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();

        }

        //Map<String, Object> friendObj = friendsList.get(position);
        final ContactModel contactModel = sharedFriendList.get(position);
        if(contactModel != null){
            //String number = (String)friendObj.get("number");
            if(contactModel.getContactNumber() != null){
                if(!String.valueOf(contactModel.getContactNumber()).isEmpty()){
                    viewHolder.textview.setText(String.valueOf(contactModel.getContactNumber()));
                    viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteFriend(position, contactModel.getContactNumber());
                            //friendsList.remove(position);
                            Toast.makeText(mainActivity, "Friend removed from list", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                }

                //TODO SET NAME HERE FOR CONTACT...
                if(contactModel.getContactName() != null){
                    if(!contactModel.getContactName().isEmpty()){
                        viewHolder.name.setText(String.valueOf(contactModel.getContactName()));                    }
                }
            }

        }


        return view;
    }



    private class PopulateContact extends AsyncTask<String, Void, String> {

        public Track track;
        public List<ContactModel> sharedFriendList;

        public PopulateContact(Track track, List<ContactModel> sharedFriendList){
            this.track = track;
            this.sharedFriendList = sharedFriendList;
        }

        @Override
        protected String doInBackground(String... params) {
            List<Map<String, Object>> friendsList = track.getFriends();
            if(friendsList != null){
                if(friendsList.size() != 0){
                    for(Map<String, Object> obj : friendsList){
                        if(obj.get("number") != null){
                            String number = String.valueOf(obj.get("number"));
                            if(!number.isEmpty()){
                                ContactModel contactModel = new ContactModel();
                                contactModel.setContactNumber(number);
                                sharedFriendList.add(contactModel);
                            }
                        }

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ContactMatcher contactMatcher = new ContactMatcher(mainActivity, sharedFriendList, that);
            notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



    public void deleteFriend(int position, String number){
        if(track != null){
            if(track.getFriends() != null){
                if(track.getFriends().size() != 0){
                    if(track.getFriends().size() > position){
                        //TODO CHECK FOR CHANCES OF ERROR .. WRONG CONTACT REMOVAL..
                        sharedFriendList.remove(position);
                        track.getFriends().remove(position);
                        //Now save data..
                        mainActivity.saveTrack(track);
                    } else {
                        Toast.makeText(mainActivity,"Unable to remove contact", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mainActivity,"Unable to remove contact", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mainActivity,"Unable to remove contact", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mainActivity,"Unable to remove contact", Toast.LENGTH_SHORT).show();
        }
    }
}
