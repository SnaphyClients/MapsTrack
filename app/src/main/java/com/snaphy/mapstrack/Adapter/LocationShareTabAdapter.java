package com.snaphy.mapstrack.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.snaphy.mapstrack.Fragment.LocationShareByUserFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFriendsFragment;

/**
 * Created by Ravi-Gupta on 1/3/2016.
 */
public class LocationShareTabAdapter extends FragmentStatePagerAdapter {

    public LocationShareTabAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     *
     * @param position {int} Position of each tab
     * @return {Fragment} return fragment
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return new LocationShareByUserFragment();
            case 1 :
                return new LocationShareByUserFriendsFragment();
            default:
                return new LocationShareByUserFragment();
        }
    }

    /**
     *
     * @return {int} Number of tabs in the tab layout
     */
    @Override
    public int getCount() {
        return 2;
    }

    
}

