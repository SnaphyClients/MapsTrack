package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 This fragment is used to display contact info in the app
 */
public class ContactFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "ContactFragment";
    @Bind(R.id.fragment_contact_imagebutton1) ImageButton backArrow;
    @Bind(R.id.fragment_contact_textview1)TextView textView;
    @Bind(R.id.fragment_contact_button1) Button contactUs;
    @Bind(R.id.fragment_contact_button2) Button emailUs;
    String text;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
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
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        setContactText();
        pressBackButton();
        return view;
    }

    public void setContactText() {
        setText();
        textView.setText(Html.fromHtml(text));
    }

    public void setText() {
        text = "<p>We would love to hear from you" +
                ".Please contact us or write us in case of any query<p/>";
    }

    @OnClick( R.id.fragment_contact_button1) void contactButton() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(Constants.APP_PHONE));
        startActivity(callIntent);
    }

    @OnClick (R.id.fragment_contact_button2) void emailButton()  {
        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + Constants.APP_MAIL));
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void pressBackButton() {
        //Back Button Click Listener
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
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
