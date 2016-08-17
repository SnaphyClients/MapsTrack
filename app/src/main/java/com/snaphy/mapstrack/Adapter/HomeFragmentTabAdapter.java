package com.snaphy.mapstrack.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.snaphy.mapstrack.Fragment.EventListFragment;
import com.snaphy.mapstrack.Fragment.LocationListFragment;
import com.snaphy.mapstrack.Fragment.ShareFragment;

/**
 * Created by Ravi-Gupta on 8/15/2016.
 */
public class HomeFragmentTabAdapter extends FragmentStatePagerAdapter {

    public HomeFragmentTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return new EventListFragment();
            case 1 :
                return new LocationListFragment();
            case 2:
                return new ShareFragment();
            default:
                return new EventListFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
