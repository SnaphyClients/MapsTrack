package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RetryLoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RetryLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RetryLoginFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public static String TAG = "RetryLoginFragment";

    public RetryLoginFragment() {
        // Required empty public constructor
    }

    public void viewMyDialog() {
        showDialog();
    }

    public static RetryLoginFragment newInstance() {
        RetryLoginFragment fragment = new RetryLoginFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        showDialog();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retry_login, container, false);

        Typeface typeface = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/Hobo.ttf");
        TextView textView1 = (TextView)view.findViewById(R.id.fragment_retry_login_textview0);
        TextView textView2 = (TextView)view.findViewById(R.id.fragment_retry_login_textview1);
        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);
        return view;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
        alertDialogBuilder.setMessage("Cannot connect to server at this moment, please try again");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mainActivity.checkLogin();
                arg0.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mainActivity.onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
