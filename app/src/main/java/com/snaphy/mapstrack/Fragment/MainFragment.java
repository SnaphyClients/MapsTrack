package com.snaphy.mapstrack.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.snaphy.mapstrack.Adapter.CustomizeSearchAdapter;
import com.snaphy.mapstrack.Adapter.HomeTabLayoutAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.SearchSuggestions;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * It is the parent fragment of all the fragments, it include tab layout, search, view pager for sliding tabs
 */
public class MainFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "MainFragment";
    @Bind(R.id.fragment_main_tab_layout) TabLayout tabLayout;
    @Bind(R.id.fragment_main_view_pager) ViewPager viewPager;
    @Bind(R.id.search_view) MaterialSearchView searchView;
    @Bind(R.id.main_toolbar) android.support.v7.widget.Toolbar toolbar;
    String sugg[] = {"Birthday", "Party", "Marriage"};
    MainActivity mainActivity;
    CustomizeSearchAdapter customizeSearchAdapter;
    List<SearchSuggestions> searchSuggestionsList = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        viewPager.setAdapter(new HomeTabLayoutAdapter(mainActivity.getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        setData();
        searchSetting();
        setIconInTabLayout();
        tabLayoutListener();
        return view;
    }

    public  void setData() {
        searchSuggestionsList.add(new SearchSuggestions(true, "Marriage"));
        searchSuggestionsList.add(new SearchSuggestions(false, "Event"));
        searchSuggestionsList.add(new SearchSuggestions(true, "Party"));
        searchSuggestionsList.add(new SearchSuggestions(true, "Function"));

    }

    /**
     * All Search realted work is done in this method
     * include
     * 1. Set Cursor Icon
     * 2. Enable/Disable Voice Search
     * 3. Functions to control all search options
     */
    public void searchSetting() {
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        customizeSearchAdapter = new CustomizeSearchAdapter(getActivity(), searchSuggestionsList);
        //searchView.setSuggestions(sugg);
        searchView.setAdapter(customizeSearchAdapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchSuggestions query = (SearchSuggestions)parent.getItemAtPosition(position);
                Snackbar.make(getView(), query.getSuggestion(), Snackbar.LENGTH_SHORT).show();
                Log.v(Constants.TAG, query.getSuggestion());
                searchView.closeSearch();
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Snackbar.make(getView(), query, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(mainActivity, query, Toast.LENGTH_SHORT).show();
                Log.v(Constants.TAG, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                Snackbar.make(getView(), newText, Snackbar.LENGTH_SHORT).show();
                //Log.v(Constants.TAG, newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    /**
     * Set Icons for Tab Layout
     */
    public void setIconInTabLayout() {
        tabLayout.getTabAt(0).setIcon(R.mipmap.selected_home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.share);
        tabLayout.getTabAt(2).setIcon(R.mipmap.profile);
        tabLayout.getTabAt(3).setIcon(R.mipmap.setting);

    }

    /**
     * Selected Tab is highlighted here
     * @param position {Integer} It is the position of the tab layout elements
     */
    public void changeSelectedIconInTabLayout(int position) {
        setHasOptionsMenu(false);
        switch(position) {
            case 0 :
                tabLayout.getTabAt(0).setIcon(R.mipmap.selected_home);
                setHasOptionsMenu(true);
                break;

            case 1 :
                tabLayout.getTabAt(1).setIcon(R.mipmap.selected_share);
                setHasOptionsMenu(true);
                break;

            case 2 :
                tabLayout.getTabAt(2).setIcon(R.mipmap.selected_profile);
                setHasOptionsMenu(true);
                break;

            case 3 :
                tabLayout.getTabAt(3).setIcon(R.mipmap.selected_setting);
                setHasOptionsMenu(true);
                break;

            default:
                tabLayout.getTabAt(0).setIcon(R.mipmap.selected_home);
                setHasOptionsMenu(true);
        }
    }

    /**
     * When tab is unselected
     * @param position
     */
    public void changeUnselectedIconInTabLayout(int position) {
        switch(position) {
            case 0 :
                tabLayout.getTabAt(0).setIcon(R.mipmap.home);
                break;

            case 1 :
                tabLayout.getTabAt(1).setIcon(R.mipmap.share);
                break;

            case 2 :
                tabLayout.getTabAt(2).setIcon(R.mipmap.profile);
                break;

            case 3 :
                tabLayout.getTabAt(3).setIcon(R.mipmap.setting);
                break;

        }
    }

    /**
     * It detect when tab is changed
     */
    public void tabLayoutListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tabLayout.getSelectedTabPosition();
                changeSelectedIconInTabLayout(position);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tabLayout.getSelectedTabPosition();
                changeUnselectedIconInTabLayout(position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
    public void onAttach(Activity context) {
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * In this method search item is inflated and displayed in tollbar which act as action bar
     * @param menu {Menu} Refrence of menu folder in android
     * @param menuInflater {MenuInflater} used to inflate menu option in fragment
     *
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        getActivity().getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_filter){
            mainActivity.replaceFragment(R.layout.fragment_filter, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * In this method I have implemented back button functionality in which search button
     * might get closed when android default back button is also pressed
     */
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (searchView.isSearchOpen()) {
                        searchView.closeSearch();
                    } else {
                        getActivity().onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
    }

}
