package com.snaphy.mapstrack.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.snaphy.mapstrack.Fragment.HomeFragment;
import com.snaphy.mapstrack.Fragment.ProfileFragment;
import com.snaphy.mapstrack.Fragment.SettingFragment;
import com.snaphy.mapstrack.Fragment.ShareFragment;

/**
 * Created by Ravi-Gupta on 11/24/2015.
 * It is a adapter that used to display and control all the fragments and tabs that are being display on
 * the home screen for example like ... home tab, settings tab etc...
 */
public class HomeTabLayoutAdapter extends FragmentPagerAdapter {


    public HomeTabLayoutAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Source :: http://stackoverflow.com/questions/30539772/android-tablayout-android-design#
     * Return Fragments in Tab Layout
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return new HomeFragment();
            case 1 :
                return new ShareFragment();
            case 2 :
                return new ProfileFragment();
            case 3 :
                return new SettingFragment();
            default:
                return new HomeFragment();
        }
    }

    /**
     *
     * @return No of Tab in the Layout
     */
    @Override
    public int getCount() {
        return 4;
    }

    /**
     * Return Title of the Tab Layout
     * @param position
     * @return
     */
  /*  @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "First Tab";
            case 1:
            default:
                return "Second Tab";
        }
    }*/
}
