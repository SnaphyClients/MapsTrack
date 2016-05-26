package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.snaphy.mapstrack.Adapter.HomeEventAdapter;
import com.snaphy.mapstrack.Adapter.HomeLocationAdapter;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.RecyclerItemClickListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * It is a home fragment and it contains all the elements in the home page
 * ie... two recycler views, two floating buttons
 */
public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_home_recycler_view1) RecyclerView recyclerView1;
    @Bind(R.id.fragment_home_recycler_view2) RecyclerView recyclerView2;
    @Bind(R.id.fragment_home_textview1) TextView eventText;
    @Bind(R.id.fragment_home_textview2) TextView locationText;
    @Bind(R.id.fragment_home_image_filter1) ImageView imageViewFilter;
    @Bind(R.id.fragment_cart_floating_button1) android.support.design.widget.FloatingActionButton floatingActionButton1;
    @Bind(R.id.fragment_cart_floating_button2) android.support.design.widget.FloatingActionButton floatingActionButton2;
    SharedPreferences app_preferences;

    HomeEventAdapter homeEventAdapter;
    HomeLocationAdapter homeLocationAdapter;

    static MainActivity mainActivity;
    MainActivity mainActivity2;
    ArrayList<String> contacts  = new ArrayList<String>();
    LinearLayoutManager layoutManager1;
    LinearLayoutManager layoutManager2;
    private ShowcaseView showcaseView;
    private int counter = 0;
    RelativeLayout.LayoutParams lps;

    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    /*Infinite Loading data set*/

    /*Infinite Loading dataset*/
    private int previousTotal2 = 0;
    private boolean loading2 = true;
    private int visibleThreshold2 = 3;
    int firstVisibleItem2, visibleItemCount2, totalItemCount2;
    /*Infinite Loading data set*/


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity2.tracker.setScreenName("Home Screen");
        mainActivity2.tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        TrackCollection.progressBar = (fr.castorflex.android.smoothprogressbar.SmoothProgressBar) view.findViewById(R.id.fragment_home_progressBar);
        mainActivity.stopProgressBar(TrackCollection.progressBar);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Hobo.ttf");
        eventText.setTypeface(typeface);
        locationText.setTypeface(typeface);

        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        homeEventAdapter = new HomeEventAdapter(TrackCollection.getEventList());
        homeLocationAdapter = new HomeLocationAdapter(TrackCollection.getLocationList());

        recyclerView1.setAdapter(homeEventAdapter);
        recyclerView2.setAdapter(homeLocationAdapter);

        homeEventAdapter.notifyDataSetChanged();
        homeLocationAdapter.notifyDataSetChanged();

        recyclerView1.addOnItemTouchListener(
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

        recyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Track track = TrackCollection.locationList.get(position);
                        mainActivity.replaceFragment(R.layout.fragment_location_info, null);
                        EventBus.getDefault().post(track, Constants.SHOW_LOCATION_INFO);
                    }
                })
        );

        eventFloatingButtonClickListener();
        locationFloatingButtonClickListener();

        recyclerViewLoadMoreEventData();
        recyclerViewLoadMoreLocationData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Boolean isFirstTime;
        app_preferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        SharedPreferences.Editor editor = app_preferences.edit();
        isFirstTime = app_preferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            editor.putBoolean("isFirstTime", false);
            editor.commit();

            showcaseView = new ShowcaseView.Builder(getActivity())
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(eventText))
                    .setOnClickListener(this)
                    .setContentTitle("Events")
                    .setContentText("In this section you will find any events that the going near to your house")
                    .build();
            showcaseView.setButtonText("Next");
            showcaseView.setButtonPosition(lps);

        }else{

        }
    }


    /********************************************************Subscribers for event and locations**************************/


    @Subscriber (tag = Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION)
    public void notifyEventList(boolean reset) {
        if(TrackCollection.getTrackCurrentFilterSelect() != null) {
            previousTotal = 0;
            loading = true;
            visibleThreshold = 3;
            firstVisibleItem = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
        }
        homeEventAdapter.notifyDataSetChanged();
    }

    @Subscriber (tag = Constants.NOTIFY_LOCATION_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION)
    public void notifyLocationList(boolean reset) {
        homeLocationAdapter.notifyDataSetChanged();
    }

    @Subscriber ( tag = Constants.UPDATE_EVENT_IN_HOME)
    public void updateEventInHome(String dummyString) {
        homeEventAdapter.notifyDataSetChanged();
    }

    /********************************************************Subscribers for event and locations**************************/


    /**
     * Fire when create event button is clicked
     */
    private void locationFloatingButtonClickListener() {
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.layout.fragment_create_event, null);
            }
        });

    }

    /**
     * Fire when create location button is clicked
     */
    private void eventFloatingButtonClickListener() {
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.layout.fragment_create_location, null);
            }
        });

    }

    public void recyclerViewLoadMoreEventData() {
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager1.getItemCount();
                firstVisibleItem = layoutManager1.findFirstVisibleItemPosition();

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

    @Subscriber ( tag = Constants.UPDATE_DATA_IN_HOME_FRAGMENT_FROM_FILTER)
    public void setFilterData(String emptyString) {
        EventBus.getDefault().post(TrackCollection.progressBar, Constants.RESET_EVENTS_FROM_FILTER_FRAGMENT);
    }

    public void recyclerViewLoadMoreLocationData() {
        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount2 = recyclerView.getChildCount();
                totalItemCount2 = layoutManager2.getItemCount();
                firstVisibleItem2 = layoutManager2.findFirstVisibleItemPosition();

                if (loading2) {
                    if (totalItemCount2 > previousTotal2) {
                        loading2 = false;
                        previousTotal2 = totalItemCount2;
                    }
                }
                if (!loading2 && (totalItemCount2 - visibleItemCount2)
                        <= (firstVisibleItem2 + visibleThreshold2)) {
                    EventBus.getDefault().post(TrackCollection.progressBar, Constants.REQUEST_LOAD_MORE_LOCATION_FROM_HOME_FRAGMENT);
                    loading2 = true;
                }
            }
        });
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
        mainActivity2 = (MainActivity) getActivity();
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

    @Override
    public void onClick(View v) {
        switch (counter) {
            case 0:
                //showcaseView.setShowcase(new ViewTarget(floatingActionButton1), true);
                showcaseView.hide();
                showcaseView = new ShowcaseView.Builder(getActivity())
                        .withMaterialShowcase()
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .setTarget(new ViewTarget(floatingActionButton1))
                        .setOnClickListener(this)
                        .setContentTitle("Create Events")
                        .setContentText("Either a birthday or a meeting, create all type of events and share them to you friends with a button click")
                        .build();
                showcaseView.setButtonText("Next");
                showcaseView.setButtonPosition(lps);
                break;

            case 1:
                showcaseView.hide();
                showcaseView = new ShowcaseView.Builder(getActivity())
                        .withMaterialShowcase()
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .setTarget(new ViewTarget(floatingActionButton2))
                        .setOnClickListener(this)
                        .setContentTitle("Create Locations")
                        .setContentText("Difficulty in finding address, now create any location with mapstrack and share with your friends, so that they can reach your location easily")
                        .build();
                showcaseView.setButtonText("Next");
                showcaseView.setButtonPosition(lps);
                break;

            case 2:
                showcaseView.hide();
                showcaseView = new ShowcaseView.Builder(getActivity())
                        .withMaterialShowcase()
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .setTarget(new ViewTarget(locationText))
                        .setOnClickListener(this)
                        .setContentTitle("Locations")
                        .setContentText("Here you will find all the locations that you have created or your friends have share with you ")
                        .build();
                showcaseView.setButtonText("Next");
                showcaseView.setButtonPosition(lps);
                break;

            case 3:
                showcaseView.hide();
                showcaseView = new ShowcaseView.Builder(getActivity())
                        .withMaterialShowcase()
                        .setStyle(R.style.CustomShowcaseTheme3)
                        .setTarget(new ViewTarget(imageViewFilter))
                        .setOnClickListener(this)
                        .setContentTitle("Filter")
                        .setContentText("Now select events only you want to see from Nearby Events, Shared Events and My Events")
                        .build();
                showcaseView.setButtonPosition(lps);
                break;
            case 4:
                showcaseView.hide();
                break;

        }
        counter ++;
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
