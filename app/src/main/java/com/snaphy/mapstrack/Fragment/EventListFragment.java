package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.snaphy.mapstrack.Adapter.EventListAdapter;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.DividerItemDecoration;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.RecyclerItemClickListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_event_list_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.fragment_event_list_button1) Button myEventButton;
    @Bind(R.id.fragment_event_list_button2) Button sharedEventButton;
    @Bind(R.id.fragment_event_list_button3) Button nearbyEventButton;
    LinearLayoutManager layoutManager;
    EventListAdapter eventListAdapter;
    MainActivity mainActivity;

    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    /*Infinite Loading data set*/

    public EventListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        eventListAdapter = new EventListAdapter(TrackCollection.getEventList(), mainActivity);

        recyclerView.setAdapter(eventListAdapter);
        eventListAdapter.notifyDataSetChanged();

        setSelectedFilter();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (TrackCollection.eventList != null) {
                            Track track = TrackCollection.eventList.get(position);
                            mainActivity.replaceFragment(R.layout.fragment_event_info, null);
                            EventBus.getDefault().post(track, Constants.SHOW_EVENT_INFO);
                        }
                    }
                })
        );

        recyclerViewLoadMoreEventData();

        return view;
    }

    public void recyclerViewLoadMoreEventData() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    EventBus.getDefault().post(TrackCollection.progressBar, Constants.REQUEST_LOAD_MORE_EVENT_FROM_HOME_FRAGMENT);
                    loading = true;
                }
            }
        });
    }

    @Subscriber(tag = Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION)
    public void notifyEventList(boolean reset) {
        if(TrackCollection.getTrackCurrentFilterSelect() != null) {
            previousTotal = 0;
            loading = true;
            visibleThreshold = 3;
            firstVisibleItem = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
        }
        eventListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fragment_event_list_floating_button1) void openCreateEvent() {
        mainActivity.replaceFragment(R.layout.fragment_create_event, null);
    }

    @OnClick(R.id.fragment_event_list_button1) void myEventFilter() {
        mainActivity.showMyEventFilter();
        EventBus.getDefault().post(TrackCollection.progressBar, Constants.RESET_EVENTS_FROM_FILTER_FRAGMENT);

        myEventButton.setTextColor(Color.parseColor("#ed6174"));
        sharedEventButton.setTextColor(Color.parseColor("#777777"));
        nearbyEventButton.setTextColor(Color.parseColor("#777777"));
    }

    @OnClick(R.id.fragment_event_list_button2) void sharedEventFilter() {
        mainActivity.setOnlySharedEventsFilter();
        EventBus.getDefault().post(TrackCollection.progressBar, Constants.RESET_EVENTS_FROM_FILTER_FRAGMENT);

        myEventButton.setTextColor(Color.parseColor("#777777"));
        sharedEventButton.setTextColor(Color.parseColor("#ed6174"));
        nearbyEventButton.setTextColor(Color.parseColor("#777777"));
    }

    @OnClick(R.id.fragment_event_list_button3) void nearbyEventFilter() {
        mainActivity.setNearByEventFilter();
        EventBus.getDefault().post(TrackCollection.progressBar, Constants.RESET_EVENTS_FROM_FILTER_FRAGMENT);

        myEventButton.setTextColor(Color.parseColor("#777777"));
        sharedEventButton.setTextColor(Color.parseColor("#777777"));
        nearbyEventButton.setTextColor(Color.parseColor("#ed6174"));
    }

    public void setSelectedFilter() {
        if(TrackCollection.getTrackCurrentFilterSelect() != null) {
            if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.MY_EVENTS)) {

                myEventButton.setTextColor(Color.parseColor("#ed6174"));
                sharedEventButton.setTextColor(Color.parseColor("#777777"));
                nearbyEventButton.setTextColor(Color.parseColor("#777777"));

            } else if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.NEAR_BY)) {

                myEventButton.setTextColor(Color.parseColor("#777777"));
                sharedEventButton.setTextColor(Color.parseColor("#777777"));
                nearbyEventButton.setTextColor(Color.parseColor("#ed6174"));

            } else if (TrackCollection.getTrackCurrentFilterSelect().get(Constants.SHARED_EVENTS)) {

                myEventButton.setTextColor(Color.parseColor("#777777"));
                sharedEventButton.setTextColor(Color.parseColor("#ed6174"));
                nearbyEventButton.setTextColor(Color.parseColor("#777777"));

            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
