package com.snaphy.mapstrack;

/**
 * Created by Ravi-Gupta on 12/8/2015.
 */
public final class Constants {
    public static final String TAG = "MapsTrack";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.snaphy.mapstrack";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String SHOW_EVENT_INFO = "HomeFragment:onEventClick";
    public static final String SHOW_LOCATION_INFO = "HomeFragment:onLocationClick";

    public static final String SHOW_EVENT_EDIT = "EventInfoFragment:onEdit";
    public static final String SHOW_LOCATION_EDIT = "LocationInfoFragment:onEdit";

    public static final String SEND_ADDRESS_EVENT = "HomeFragment:sendAddressEvent";
    public static final String SEND_ADDRESS_LOCATION = "HomeFragment:sendAddressLocation";

    public static final String SEND_EVENT_LATLONG = "FetchAddressIntentService:sendEventLatLong";
    public static final String SEND_LOCATION_LATLONG = "FetchAddressIntentService:sendLocationLatLong";

    public static final String SEND_MAP_COORDINATES_EVENT = "EventInfoFragment:eventDestinationCoordinates";
    public static final String SEND_MAP_COORDINATES_LOCATION = "LocationInfoFragment:locationDestinationCoordinates";

    public static final String CREATE_EVENT_FROM_LOCATION = "LocationInfoFragment:createEventFromLocation";

    public static final String INITIALIZE_LOCATION_SHARED_BY_USER = "LocationShareByUserFragment:initializeData";
    public static final String INITIALIZE_LOCATION_SHARED_BY_USER_FRIENDS = "LocationShareByUserFriendsFragment:initializeData";

    public static final String DELETE_LOCATION_SHARED_BY_USER = "LocationShareByUserFragment:deleteData";
    public static final String DELETE_LOCATION_SHARED_BY_USER_FRIENDS = "LocationShareByUserFriendsFragment:deleteData";

    public static final String REMOVE_LOCATION_SHARED_BY_USER = "LocationShareByUserFragment:removeData";
    public static final String REMOVE_LOCATION_SHARED_BY_USER_FRIENDS = "LocationShareByUserFriendsFragment:removeData";

    public static final String LOCATION_SHARE_BY_USER_FRAGMENT = "LocationShareByUserFragment";
    public static final String LOCATION_SHARE_BY_USER_FRIENDS_FRAGMENT = "LocationShareByUserFriendsFragment";

    public static final String OPEN_MAP_FROM_LOCATION = "LocationShareAdapterContacts:openMap";
    public static final String SEND_DEFAULT_LATLONG = "FetchAddressIntentService:sendDefaultLatLong";

    public static final String ADD_CONTACTS_IN_SHARE_LOCATION = "SelectContactAdapter:addContact";
    public static final String SHOW_CONTACTS_IN_SHARE_LOCATION = "ShareLocationCollection:showContact";

    public static final String IS_LOGIN = "BackgroundServices:isLogin";
    public static final String LOGOUT = "ProfileFragment:logout";

}
