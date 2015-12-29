package com.snaphy.mapstrack.Collection;

import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ravi-Gupta on 12/29/2015.
 */
public class EventHomeCollection {

    ArrayList<EventHomeModel> eventHomeModelArrayList = new ArrayList<EventHomeModel>();
    Date date;

    public EventHomeCollection() {
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
        initialize();
    }

    /**
     *  Data is fetch from the server when app will start
     */
    public void initialize() {
        eventHomeModelArrayList.clear();
        date = Calendar.getInstance().getTime();
        /**
         *  TODO Fetch Data From Server
         */
        ArrayList<SelectContactModel> selectContactModels = new ArrayList<SelectContactModel>();
        selectContactModels.add(new SelectContactModel("Ravi Gupta","9873046993"));
        selectContactModels.add(new SelectContactModel("Robins Gupta","987389993"));
        selectContactModels.add(new SelectContactModel("Anurag Gupta","9873045632"));
        selectContactModels.add(new SelectContactModel("Siddarth Jain","8745646993"));
        //Remove this static code
        eventHomeModelArrayList.add(new EventHomeModel("Ravi123","DLF Phase 3, Gurgaon","Explore public service through this popular networking and recruiting program." +
                " The Government Careers Forum will feature a keynote presentation by" +
                " Massachusetts State Representative Tackey Chan ’95, followed by round table" +
                " networking sessions for students, alumni, faculty and staff with agency" +
                " representatives","Birthday", date, selectContactModels));


        eventHomeModelArrayList.add(new EventHomeModel("SidHome","Palam Vihar, Sector 25","Paul Romer, a prominent American economist and specialist on the theory of " +
                "growth and innovation, will discuss charter cities and their potential impact on " +
                "economic prosperity","Baby Shower", date, selectContactModels));


        eventHomeModelArrayList.add(new EventHomeModel("RaghuMarriage","Pitampura, Haryana","Experience Virginia Woolf’s darkly elegant voice in an original stage adaptation " +
                "of four compelling short stories.","Party", date, selectContactModels));


        eventHomeModelArrayList.add(new EventHomeModel("RobParty","Cyber Hub, Cyber City","Debra Granik '85 will screen and discuss her best-known work to date – the " +
                "Oscar-nominated film \"Winter's Bone.\"","Marriage", date, selectContactModels));


        eventHomeModelArrayList.add(new EventHomeModel("AnuOffice", "Sahara Mall, Sikandarpur","Granik will take questions from the audience after the screening. " +
                "This event is sponsored by the Film, Television and Interactive Media Program " +
                "and the Edie and Lew Wasserman Fund.","Meeting", date, selectContactModels));

        EventBus.getDefault().postSticky(eventHomeModelArrayList, EventHomeModel.onResetData);

    }

    /**
     *  When new data is added or data is changed in fragment Event this method is called
     * @param eventHomeModel
     */
    @Subscriber(tag = EventHomeModel.onSave)
    private void onSave(EventHomeModel eventHomeModel) {

        if(eventHomeModel.getId() == null) { // New Data has been added in create location fragment
            eventHomeModelArrayList.add(new EventHomeModel(eventHomeModel.getEventId(), eventHomeModel.getEventAddress(),
                    eventHomeModel.getDescription(), eventHomeModel.getType(), eventHomeModel.getDate(), eventHomeModel.getContacts()));
            /**
             *  TODO Send POST request to server
             */
            EventBus.getDefault().post(eventHomeModel, EventHomeModel.onChangeData);
        }  else { // Data has been updated in create event fragment

            for(EventHomeModel element : eventHomeModelArrayList) {
                if(element.getId() == eventHomeModel.getId()) {
                    int position = eventHomeModelArrayList.indexOf(element);
                    eventHomeModelArrayList.set(position, eventHomeModel);
                }
            }
            /**
             * TODO Send UPDATE request to server
             */
            EventBus.getDefault().postSticky(eventHomeModel, EventHomeModel.onChangeData);
        }
    }


    /**
     *  It is called when data is deleted from fragment
     * @param eventHomeModel
     */
    @Subscriber(tag = EventHomeModel.onDelete)
    private void onDelete(EventHomeModel eventHomeModel) {
        for(EventHomeModel element : eventHomeModelArrayList) {
            if(element.getId() == eventHomeModel.getId()) {
                int position = eventHomeModelArrayList.indexOf(element);
                eventHomeModelArrayList.remove(position);
            }
        }
        /**
         * TODO Send DELETE request to server
         */
        EventBus.getDefault().postSticky(eventHomeModel, EventHomeModel.onRemoveData);
    }

}
