package com.snaphy.mapstrack;

/**
 * Created by Ravi-Gupta on 12/8/2015.
 */
public final class Constants {
    public static final String TAG = "MapsTrack";
    public static final String CLIENT_ID = "624660143780-l2jd9j9q7iksiager7821501msoe1r8r.apps.googleusercontent.com";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.snaphy.mapstrack";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final int SET_LOCATION_UPDATE_TIMEOUT =  1000*60*5;
    //public static final int SET_LOCATION_UPDATE_TIMEOUT =  5000;

    //public static String baseUrl = "http://192.168.1.100:3000";
    public static String baseUrl = "http://104.236.76.111:3000";
    //public static String baseUrl = "http://ec2-54-209-33-191.compute-1.amazonaws.com:3000";
    public static String apiUrl = baseUrl+"/api";
    public static final String AMAZON_CLOUD_FRONT_URL = "http://do6n8ch9q1v85.cloudfront.net/";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    public static String SENDER_ID = "673699478279";
    public static String LOOPBACK_APP_ID = "mapstrack-snaphy-push-application";

    public static final String APP_MAIL = "info@mapstrack.com";
    public static final String APP_PHONE = "tel:+91-9999566751";
    public static String APP_PLAY_STORE = "com.drugcorner.slider";
    public static String APP_SHARE_TEXT = "Finding event made easy \n\n";
    public static final String CONTAINER = "snaphy-mapstrack";

    public static String ERROR_MESSAGE = "Something went wrong!";

    public static final String NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION = "NotifyEventDataInHomeFragmentFromTrackCollection";
    public static final String NOTIFY_LOCATION_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION = "NotifyLocationDataInHomeFragmentFromTrackCollection";

    public static final String REQUEST_LOAD_MORE_EVENT_FROM_HOME_FRAGMENT = "RequestLoadMoreEventFromHomeFragment";
    public static final String REQUEST_LOAD_MORE_LOCATION_FROM_HOME_FRAGMENT = "RequestLoadMoreLocationFromHomeFragment";

    public static final String INITIALIZE_BACKGROUND_SERVICE = "InitializeBackgroundService";

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

    public static final String SEND_SELECTED_CONTACT_TO_CREATE_EVENT_FRAGMENT = "SendSelectedContactToCreateEventFragment";
    public static final String SEND_SELECTED_CONTACT_TO_CREATE_LOCATION_FRAGMENT = "SendSelectedContactToCreateLocationFragmant";
    public static final String IS_LOGIN = "BackgroundServices:isLogin";
    public static final String LOGOUT = "ProfileFragment:logout";

    public static final String REQUEST_EDIT_PROFILE_FRAGMENT = "RequestEditProfileFragment";
    public static final String RESPONSE_EDIT_PROFILE_FRAGMENT = "ResponseEditProfileFragment";

    public static final String DISPLAY_CONTACT = "displayContactInContactFragment";

    public static final String UPDATE_CONTACT_NUMBER = "updateContactNumber";
    public static final String UPDATE_CONTACT_NUMBER_IN_LOCATION = "updateContactNumberInLocation";
    public static final String SET_LATITUDE_LONGITUDE = "setLatitudeLongitude";

    public static final String RESET_EVENTS_FROM_FILTER_FRAGMENT = "resetEventsFromFilterFragment";
    public static final String RESET_LOCATION_FROM_FILTER_FRAGMENT = "resetLocationFromFilterFragment";
    public static final String UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER = "updateDataInHomeFragmentFromFilter";
    public static final String UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER_LOCATION = "updateDataInHomeFragmentFromFilterLocation";


    public static final String NEAR_BY = "nearby";
    public static final String SHARED_EVENTS = "shared";
    public static final String MY_EVENTS = "myevents";
    public static final String MY_LOCATION = "myLocation";

    public static final String DISPLAY_CONTACTS_FROM_SHARED_USER_FRAGMENT = "displayContactFromSharedUserFragment";

    public static final String UPDATE_SHARED_FRIENDS_BY_USER_LIST = "UpdateSharedFriendsByUserList";

    public static final String UPDATE_ADDRESS_FROM_MAP = "UpdateAddressFromMap";
    public static final String UPDATE_IMAGE_FROM_EDITED_CREATE_EVENT = "UpdateImageFromEditedCreateEvent";

    public static final String UPDATE_EVENT_IN_HOME = "UpdateLocationInHome";
    public static final String SEND_ERROR_IN_FINDING_LOCATION = "SendErrorInFindingLocation";

    public static final String SEND_ADDRESS_HAS_BEEN_FOUND = "SendAddressHasBeenFound";
    public static final String CREATE_EVENT_FROM_LOCATION_FRAGMENT = "CreateEventFromLocation";

    public static final String HIDE_MENU_OPTIONS = "HideMenuOptions";
    public static final String HIDE_MENU_OPTIONS_LOCATIONS = "HideMenuOptionsLocations";


}
