package com.snaphy.mapstrack.Collection;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.TrackRepository;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.snaphy.mapstrack.Services.MyRestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Ravi-Gupta on 3/10/2016.
 */
public class TrackCollection {

    MyRestAdapter restAdapter;

    private int limit = 10;

    public static Map<String, Boolean> getTrackCurrentFilterSelect() {
        return trackCurrentFilterSelect;
    }

    public static Map<String, Boolean>  trackCurrentFilterSelect = new HashMap<>();

    public static SmoothProgressBar progressBar;


    static public Map<String, Object> getEventFilter() {
        return eventFilter;
    }

    static Map<String, Object> eventFilter = new HashMap<>();


    static public Map<String, Object> getLocationFilter() {
        return locationFilter;
    }

    static Map<String, Object> locationFilter = new HashMap<>();

    static public List<Track> getEventList() {
        return eventList;
    }


    public static List<Track> eventList = new ArrayList<>();

    static public List<Track> getLocationList() {
        return locationList;
    }


    public static List<Track> locationList = new ArrayList<>();

    Context context;

    public TrackCollection(MyRestAdapter restAdapter, Context context){
        EventBus.getDefault().register(this);
        this.restAdapter = restAdapter;
        resetFilter("event");
        resetFilter("location");
        this.context = context;
        initialize("event",true, progressBar);
        initialize("location",true, progressBar);
        trackCurrentFilterSelect.put(Constants.MY_EVENTS, false);
        trackCurrentFilterSelect.put(Constants.NEAR_BY, false);
        trackCurrentFilterSelect.put(Constants.SHARED_EVENTS, false);
    }

    public void resetFilter(String type){
        if(type.equals("event")){
            //eventFilter.clear();
            eventFilter.put("limit", limit);
            eventFilter.put("skip", 0);
        }else{
            //locationFilter.clear();
            locationFilter.put("limit", limit);
            locationFilter.put("skip", 0);
        }
    }


    public void initialize(final String type, final boolean reset, final SmoothProgressBar progressBar){

        Map<String, Object> filter;
        //Now add type to filter..
        if(type.equals("event")){
            filter = eventFilter;
        }else {
            filter = locationFilter;
            //For location arrange filter desc added date..
            //{ order: 'propertyName <ASC|DESC>' }
            filter.put("order", "added DESC");
            if(BackgroundService.getCustomer() != null){
                filter.put("customerId", BackgroundService.getCustomer().getId());
                if(BackgroundService.getCustomer().getPhoneNumber() != null){
                    filter.put("friends.number", BackgroundService.getCustomer().getPhoneNumber());
                }
            }
        }

        Map<String, Object> where;
        if(filter.get("where") == null){
            where = new HashMap<>();
            filter.put("where", where);
        }else {
            where = (Map<String, Object>)filter.get("where");
        }

        //Add event or location type..
        where.put("type", type);
        //Now add where.. filter..
        filter.put("where", where);

        if(reset) {
            resetFilter(type);
            /*if(type.equals("event")) {
                eventList.clear();
            } else {
                locationList.clear();
            }*/
        } else {
            //Do Pagination
            filter.put("skip",(int)filter.get("skip")+limit);
        }

        filter.put("include", "customer");


        if(progressBar != null) {
            startProgressBar(progressBar);
            //SHOW LOADING BAR..
        }

        TrackRepository trackRepository = restAdapter.createRepository(TrackRepository.class);
        trackRepository.find(filter, new ListCallback<Track>() {
            @Override
            public void onSuccess(List<Track> objects) {
                if (progressBar != null) {
                    //CLOSE LOADING BAR
                    stopProgressBar(progressBar);
                }

                if (objects != null) {
                    if(reset) {
                        if(type.equals("event")) {
                            eventList.clear();
                        } else {
                            locationList.clear();
                        }
                    }
                    if (type.equals("event")) {
                        //setEventList(objects);
                        if (objects.size() != 0) {
                            getEventList().addAll(objects);
                        }
                        EventBus.getDefault().post(reset, Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);
                    } else {
                        // setLocationList(objects);
                        if (objects.size() != 0) {
                            getLocationList().addAll(objects);
                        }
                        EventBus.getDefault().post(reset, Constants.NOTIFY_LOCATION_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION);

                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                //Toast.makeText(context, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.v(Constants.TAG, "Error in track collection");
                if (progressBar != null) {
                    //CLOSE LOADING BAR
                    stopProgressBar(progressBar);
                }
            }
        });
    }




    @Subscriber ( tag = Constants.REQUEST_LOAD_MORE_EVENT_FROM_HOME_FRAGMENT)
    public void requestMoreEvents(SmoothProgressBar progressBar){
            initialize("event", false, progressBar);
    }

    @Subscriber ( tag = Constants.RESET_EVENTS_FROM_FILTER_FRAGMENT)
    public void initEvents(SmoothProgressBar progressBar){
        initialize("event",true, progressBar);
    }

    @Subscriber ( tag = Constants.REQUEST_LOAD_MORE_LOCATION_FROM_HOME_FRAGMENT)
    public void requestMoreLocation(SmoothProgressBar progressBar) {
        initialize("location", false, progressBar);
    }

    public void startProgressBar(SmoothProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.progressiveStart();
        }
        //OR Use Progress Dialog
    }

    public void stopProgressBar(SmoothProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
            progressBar.progressiveStop();
        }
        //OR Use Progress Dialog
    }


    public static void setFilterColor(String filterType){
        trackCurrentFilterSelect.put(Constants.MY_EVENTS, false);
        trackCurrentFilterSelect.put(Constants.NEAR_BY, false);
        trackCurrentFilterSelect.put(Constants.SHARED_EVENTS, false);

        if (filterType.equals(Constants.MY_EVENTS)) {
            trackCurrentFilterSelect.put(Constants.MY_EVENTS, true);
        }

        if (filterType.equals(Constants.NEAR_BY)) {
            trackCurrentFilterSelect.put(Constants.NEAR_BY, true);
        }

        if (filterType.equals(Constants.SHARED_EVENTS)) {
            trackCurrentFilterSelect.put(Constants.SHARED_EVENTS, true);
        }

    }





}
