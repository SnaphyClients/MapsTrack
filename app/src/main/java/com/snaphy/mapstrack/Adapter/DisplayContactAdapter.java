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
    List<ContactModel> sharedFriendList;
    Track track;
    MainActivity mainActivity;

    public DisplayContactAdapter(MainActivity mainActivity, Track track) {
        sharedFriendList.clear();
        this.mainActivity = mainActivity;
        layoutInflater = LayoutInflater.from(mainActivity);


        this.track = track;
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
        if(sharedFriendList != null){
            return sharedFriendList.size();
        }else{
            return 0;
        }
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

        //Map<String, Object> friendObj = friendsList.get(position);
        final ContactModel contactModel = sharedFriendList.get(position);
        if(contactModel != null){
            //String number = (String)friendObj.get("number");
            if(contactModel.getContactNumber() != null){
                if(!contactModel.getContactNumber().isEmpty()){
                    viewHolder.textview.setText(contactModel.getContactNumber());
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
            }

        }


        return view;
    }



    private class PopulateContact extends AsyncTask<String, Void, List<ContactModel>> {

        public Track track;

        public PopulateContact(Track track){
            this.track = track;
        }

        @Override
        protected List<ContactModel> doInBackground(String... params) {
            List<ContactModel> contactModels = new ArrayList();
            List<Map<String, Object>> friendsList = track.getFriends();
            if(friendsList != null){
                if(friendsList.size() != 0){
                    for(Map<String, Object> obj : friendsList){
                        if(obj.get("number") != null){
                            String number = (String)obj.get("number");
                            if(!number.isEmpty()){
                                ContactModel contactModel = new ContactModel();
                                contactModel.setContactNumber(number);
                                contactModels.add(contactModel);
                            }
                        }

                    }
                }
            }
            return contactModels;
        }

        @Override
        protected void onPostExecute(List<ContactModel> result) {
            sharedFriendList = result;
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
