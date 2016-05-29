package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.CompanyInfo;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CompanyInfoRepository;
import com.google.android.gms.analytics.HitBuilders;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * This fragment contains the methods to display frequently asked questions in the app
 */
public class FaqsFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "FaqFragment";
    @Bind(R.id.fragment_faq_imagebutton1) ImageButton backArrow;
    @Bind(R.id.fragment_faq_textview1)TextView textView;
    @Bind(R.id.fragment_faq_progressBar) SmoothProgressBar progressBar;
    MainActivity mainActivity;
    View rootview;
    CompanyInfoRepository companyInfoRepository;
    String faqs;

    public FaqsFragment() {
        // Required empty public constructor
    }

    public static FaqsFragment newInstance() {
        FaqsFragment fragment = new FaqsFragment();
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
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, view);
        rootview = view;
        textView.setMovementMethod(new ScrollingMovementMethod());
        setFaqText();
        pressBackButton();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setFaqText() {
        companyInfoRepository = mainActivity.getLoopBackAdapter().createRepository(CompanyInfoRepository.class);
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        where.put("type","faqs");
        filter.put("where", where);
        companyInfoRepository.find(filter, new ListCallback<CompanyInfo>() {
            @Override
            public void onSuccess(List<CompanyInfo> objects) {
                if (objects != null) {
                    if (objects.size() != 0) {
                        faqs = objects.get(0).getHtml().toString();
                        textView.setText(Html.fromHtml(faqs));
                        mainActivity.stopProgressBar(progressBar);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : FAQsFragment, Method : setFaqText "+t.toString())
                        .build());
                Log.e(Constants.TAG, t + "");
                mainActivity.stopProgressBar(progressBar);
                if(rootview != null) {
                    Snackbar.make(rootview, "Error fetching data", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
