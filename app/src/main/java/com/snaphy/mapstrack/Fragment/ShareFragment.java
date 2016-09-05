package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaphy.mapstrack.Adapter.LocationShareTabAdapter;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Location shared by others or by user to others is maintained here in this fragment
 */
public class ShareFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_share_tab_layout) TabLayout tabLayout;
    @Bind(R.id.fragment_share_view_pager) ViewPager viewPager;
    MainActivity mainActivity;

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
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
        View view  =  inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);
        viewPager.setAdapter(new LocationShareTabAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
        setTextInTabLayout();
        return view;
    }

    /**
     * Set Text for Tab Layout
     */
    public void setTextInTabLayout() {
        tabLayout.getTabAt(0).setText("FOLLOWER");
        tabLayout.getTabAt(1).setText("LOCATOR");
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
