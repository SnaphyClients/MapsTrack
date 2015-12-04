package com.snaphy.mapstrack.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaphy.mapstrack.Adapter.HomeTabLayoutAdapter;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "MainFragment";
    @Bind(R.id.fragment_main_tab_layout) TabLayout tabLayout;
    @Bind(R.id.fragment_main_view_pager) ViewPager viewPager;
    MainActivity mainActivity;

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
        ButterKnife.bind(this,view);
        viewPager.setAdapter(new HomeTabLayoutAdapter(mainActivity.getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        setIconInTabLayout();
        tabLayoutListener();
        return view;
    }

    /**
     * Set Icons for Tab Layout
     */
    public void setIconInTabLayout() {
        tabLayout.getTabAt(0).setIcon(R.mipmap.home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.share);
        tabLayout.getTabAt(2).setIcon(R.mipmap.profile);
        tabLayout.getTabAt(3).setIcon(R.mipmap.setting);

    }

    /**
     * Selected Tab is highlighted here
     * @param position {Integer} It is the position of the tab layout elements
     */
    public void changeSelectedIconInTabLayout(int position) {
        switch(position) {
            case 0 :
                tabLayout.getTabAt(0).setIcon(R.mipmap.selected_home);
                break;

            case 1 :
                tabLayout.getTabAt(1).setIcon(R.mipmap.selected_share);
                break;

            case 2 :
                tabLayout.getTabAt(2).setIcon(R.mipmap.selected_profile);
                break;

            case 3 :
                tabLayout.getTabAt(3).setIcon(R.mipmap.selected_setting);
                break;

            default:
                tabLayout.getTabAt(0).setIcon(R.mipmap.selected_home);
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
}
