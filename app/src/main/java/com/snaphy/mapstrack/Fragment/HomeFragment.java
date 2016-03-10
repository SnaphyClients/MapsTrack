package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaphy.mapstrack.Adapter.HomeEventAdapter;
import com.snaphy.mapstrack.Adapter.HomeLocationAdapter;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.EventHomeModel;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.RecyclerItemClickListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * It is a home fragment and it contains all the elements in the home page
 * ie... two recycler views, two floating buttons
 */
public class HomeFragment extends android.support.v4.app.Fragment{

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_home_recycler_view1) RecyclerView recyclerView1;
    @Bind(R.id.fragment_home_recycler_view2) RecyclerView recyclerView2;
    @Bind(R.id.fragment_home_textview1) TextView eventText;
    @Bind(R.id.fragment_home_textview2) TextView locationText;
    @Bind(R.id.fragment_cart_floating_button1) android.support.design.widget.FloatingActionButton floatingActionButton1;
    @Bind(R.id.fragment_cart_floating_button2) android.support.design.widget.FloatingActionButton floatingActionButton2;

    HomeEventAdapter homeEventAdapter;
    HomeLocationAdapter homeLocationAdapter;

    ArrayList<EventHomeModel> eventHomeModelArrayList = new ArrayList<EventHomeModel>();
    ArrayList<LocationHomeModel> locationHomeModelArrayList = new ArrayList<LocationHomeModel>();

    static MainActivity mainActivity;
    ArrayList<String> contacts  = new ArrayList<String>();
    LinearLayoutManager layoutManager1;
    LinearLayoutManager layoutManager2;

    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    int firstVisibleItem, visibleItemCount, totalItemCount;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        TrackCollection.progressBar = (SmoothProgressBar) view.findViewById(R.id.fragment_home_progressBar);
        mainActivity.stopProgressBar(TrackCollection.progressBar);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Hobo.ttf");
        eventText.setTypeface(typeface);
        locationText.setTypeface(typeface);

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
                        EventHomeModel eventHomeModel = eventHomeModelArrayList.get(position);
                        mainActivity.replaceFragment(R.layout.fragment_event_info, null);
                        EventBus.getDefault().post(eventHomeModel, Constants.SHOW_EVENT_INFO);
                    }
                })
        );

        recyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        LocationHomeModel locationHomeModel = locationHomeModelArrayList.get(position);
                        mainActivity.replaceFragment(R.layout.fragment_location_info, null);
                        EventBus.getDefault().postSticky(locationHomeModel, Constants.SHOW_LOCATION_INFO);
                    }
                })
        );

        eventFloatingButtonClickListener();
        locationFloatingButtonClickListener();

        recyclerViewLoadMoreEventData();
        recyclerViewLoadMoreLocationData();

        return view;
    }

    /********************************************************Subscribers for event and locations**************************/


    @Subscriber (tag = Constants.NOTIFY_EVENT_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION)
    public void notifyEventList(boolean reset) {
        homeEventAdapter.notifyDataSetChanged();
    }

    @Subscriber (tag = Constants.NOTIFY_LOCATION_DATA_IN_HOME_FRAGMENT_FROM_TRACK_COLLECTION)
    public void notifyLocationList(boolean reset) {
        homeLocationAdapter.notifyDataSetChanged();
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
                    EventBus.getDefault().post( TrackCollection.progressBar, Constants.REQUEST_LOAD_MORE_EVENT_FROM_HOME_FRAGMENT);
                    loading = true;
                }
            }
        });
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

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager2.getItemCount();
                firstVisibleItem = layoutManager2.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    EventBus.getDefault().post( TrackCollection.progressBar, Constants.REQUEST_LOAD_MORE_LOCATION_FROM_HOME_FRAGMENT);
                    loading = true;
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
