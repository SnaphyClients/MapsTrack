package com.snaphy.mapstrack.Collection;

import com.androidsdk.snaphy.snaphyandroidsdk.models.EventType;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.EventTypeRepository;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 3/21/2016.
 */
public class EventTypeCollection {

    public EventTypeCollection(){
    }

    private static List<EventType> eventTypeList;

    public static void getEventTypeList(final ListCallback<EventType> callback) {
        if(eventTypeList != null) {
            callback.onSuccess(eventTypeList);
        } else {
            EventTypeRepository eventTypeRepository = BackgroundService.getLoopBackAdapter().createRepository(EventTypeRepository.class);
            Map<String, Object> filter = new HashMap<>();
            filter.put("order", "name ASC");
            eventTypeRepository.find(filter, new ListCallback<EventType>() {
                @Override
                public void onSuccess(List<EventType> objects) {
                    if(objects != null) {
                        callback.onSuccess(objects);
                    } else {
                        callback.onSuccess(null);
                    }

                }

                @Override
                public void onError(Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }







}
