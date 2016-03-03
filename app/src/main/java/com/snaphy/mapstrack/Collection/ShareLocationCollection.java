package com.snaphy.mapstrack.Collection;

import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.Model.SelectContactModel;
import com.snaphy.mapstrack.Model.ShareLocationModel;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 1/3/2016.
 */
public class ShareLocationCollection {

    ArrayList<ShareLocationModel> locationSharedByUserArraylist = new ArrayList<ShareLocationModel>();
    ArrayList<ShareLocationModel> locationSharedByUserFriendsArraylist = new ArrayList<ShareLocationModel>();
    ArrayList<SelectContactModel> selectContactModelArrayList = new ArrayList<SelectContactModel>();

    public ShareLocationCollection() {
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
        initialize();
    }

    /**
     *  Data is fetch from the server when app will start
     */
    public void initialize() {
        locationSharedByUserFriendsArraylist.clear();
        locationSharedByUserArraylist.clear();

        HashMap<String,Double> latLong = new HashMap<String, Double>();
        latLong.put("latitude", 28.4591179);
        latLong.put("longitude", 77.1703644);
        /**
         *  TODO Fetch Data From Server
         */
        locationSharedByUserArraylist.add(new ShareLocationModel("1","Ravi Gupta","9873046993",latLong));
        locationSharedByUserArraylist.add(new ShareLocationModel("2","Robins Gupta","987389993",latLong));
        locationSharedByUserArraylist.add(new ShareLocationModel("3","Anurag Gupta", "9873045632",latLong));
        locationSharedByUserArraylist.add(new ShareLocationModel("4","Siddarth Jain", "8745646993",latLong));

        locationSharedByUserFriendsArraylist.add(new ShareLocationModel("1","Ravi Gupta","9873046993",latLong));
        locationSharedByUserFriendsArraylist.add(new ShareLocationModel("2","Robins Gupta","987389993", latLong));
        locationSharedByUserFriendsArraylist.add(new ShareLocationModel("3","Anurag Gupta", "9873045632", latLong));
        locationSharedByUserFriendsArraylist.add(new ShareLocationModel("4", "Siddarth Jain", "8745646993", latLong));

        EventBus.getDefault().postSticky(locationSharedByUserArraylist, Constants.INITIALIZE_LOCATION_SHARED_BY_USER);
        EventBus.getDefault().postSticky(locationSharedByUserFriendsArraylist, Constants.INITIALIZE_LOCATION_SHARED_BY_USER_FRIENDS);

    }

    /**
     *  It is called when data is deleted from fragment
     * @param shareLocationModel
     */
    @Subscriber(tag = Constants.DELETE_LOCATION_SHARED_BY_USER)
    private void onDelete(ShareLocationModel shareLocationModel) {
        for(ShareLocationModel element : locationSharedByUserArraylist) {
            if(element.getId() == shareLocationModel.getId()) {
                int position = locationSharedByUserArraylist.indexOf(element);
                locationSharedByUserArraylist.remove(position);
                break;
            }
        }
        /**
         * TODO Send DELETE request to server
         */
        EventBus.getDefault().post(shareLocationModel, Constants.REMOVE_LOCATION_SHARED_BY_USER);
    }

    /**
     *  It is called when data is deleted from fragment
     * @param shareLocationModel
     */
    @Subscriber(tag = Constants.DELETE_LOCATION_SHARED_BY_USER_FRIENDS)
    private void onDeleteFriends(ShareLocationModel shareLocationModel) {
        for(ShareLocationModel element : locationSharedByUserFriendsArraylist) {
            if(element.getId() == shareLocationModel.getId()) {
                int position = locationSharedByUserFriendsArraylist.indexOf(element);
                locationSharedByUserFriendsArraylist.remove(position);
                break;
            }
        }
        /**
         * TODO Send DELETE request to server
         */
        EventBus.getDefault().post(shareLocationModel, Constants.REMOVE_LOCATION_SHARED_BY_USER_FRIENDS);
    }

    /**
     *  When new contact is added
     * @param contactModelArrayList
     */
    @Subscriber(tag = Constants.ADD_CONTACTS_IN_SHARE_LOCATION)
    private void onSave(ArrayList<ContactModel> contactModelArrayList) {

            for(ContactModel contactModel : contactModelArrayList) {
                if(contactModel.isSelected()) {
                    selectContactModelArrayList.add(new SelectContactModel(null, contactModel.getContactName(),
                            contactModel.getContactNumber()) );
                }
            }

            /**
             *  TODO Send POST request to server
             */
            EventBus.getDefault().post(contactModelArrayList, Constants.SHOW_CONTACTS_IN_SHARE_LOCATION);
        }

    }
