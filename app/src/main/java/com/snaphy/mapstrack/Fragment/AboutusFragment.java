package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * This fragment is located in settings frgament and used to display app info
 */
public class AboutusFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "AboutusFragment";
    @Bind(R.id.fragment_about_us_imagebutton1) ImageButton backArrow;
    @Bind(R.id.fragment_about_us_textview1)TextView textView;
    @Bind(R.id.fragment_about_us_progressBar) SmoothProgressBar progressBar;
    MainActivity mainActivity;
    View rootview;

    public AboutusFragment() {
        // Required empty public constructor
    }


    public static AboutusFragment newInstance() {
        AboutusFragment fragment = new AboutusFragment();
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
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        rootview = view;
        ButterKnife.bind(this, view);
        setAboutusText();
        pressBackButton();
        return view;
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

    private void setAboutusText() {
       /* companyInfoRepository = mainActivity.getLoopBackAdapter().createRepository(CompanyInfoRepository.class);
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        where.put("type","aboutus");
        filter.put("where", where);
        companyInfoRepository.find(filter, new ListCallback<CompanyInfo>() {
            @Override
            public void onSuccess(List<CompanyInfo> objects) {
                if (objects != null) {
                    if (objects.size() != 0) {
                        aboutus = objects.get(0).getHtml().toString();
                        textView.setText(Html.fromHtml(aboutus));
                        mainActivity.stopProgressBar(progressBar);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t + "");
                mainActivity.stopProgressBar(progressBar);
                if(rootview != null) {
                    Snackbar.make(rootview, "Error fetching data", Snackbar.LENGTH_SHORT).show();
                }
            }
        });*/
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
}
