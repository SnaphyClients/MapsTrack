package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_setting_button1) Button aboutUs;
    @Bind(R.id.fragment_setting_button2) Button faq;
    @Bind(R.id.fragment_setting_button4) Button rate;
    @Bind(R.id.fragment_setting_button5) Button contact;
    @Bind(R.id.fragment_setting_button6) Button terms;
    @Bind(R.id.fragment_setting_button3) Button share;
    MainActivity mainActivity;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        aboutusClick();
        faqClick();
        shareClick();
        rateClick();
        contactClick();
        termsClick();
        return view;
    }

    /**
     * Method to Open About us Fragment
     */
    public void aboutusClick() {
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(R.layout.fragment_aboutus,null);
            }
        });
    }

    /**
     * Method to Open FAQs Fragment
     */
    public void faqClick() {
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(R.layout.fragment_faq,null);
            }
        });
    }

    /**
     * Method to Open Share Fragment
     */
    public void shareClick() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getView(), "Share Button is clicked", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * Method to Open rate us Fragment
     */
    public void rateClick() {
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getView(), "Rate Button is clicked", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * Method to Open Contact Fragment
     */
    public void contactClick(){
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(R.layout.fragment_contact, null);
            }
        });
    }

    /**
     * Method to Open Terms and Conditions Fragment
     */
    public void termsClick() {
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(R.layout.fragment_terms, null);
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
