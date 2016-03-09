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
 * All terms and conditions are controlled in this fragment
 */
public class TermsFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "TermsFragment";
    @Bind(R.id.fragment_terms_imagebutton1) ImageButton backArrow;
    @Bind(R.id.fragment_terms_textview1)TextView textView;
    @Bind(R.id.fragment_terms_progressBar) SmoothProgressBar progressBar;
    View rootview;
    MainActivity mainActivity;
    CompanyInfoRepository companyInfoRepository;
    String termsContent;

    public TermsFragment() {
        // Required empty public constructor
    }

    public static TermsFragment newInstance() {
        TermsFragment fragment = new TermsFragment();
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
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        ButterKnife.bind(this,view);
        rootview = view;
        textView.setMovementMethod(new ScrollingMovementMethod());
        setTermsText();
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

    public void setTermsText() {
        companyInfoRepository = mainActivity.getLoopBackAdapter().createRepository(CompanyInfoRepository.class);
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        where.put("type","t&c");
        filter.put("where", where);
        companyInfoRepository.find(filter, new ListCallback<CompanyInfo>() {
            @Override
            public void onSuccess(List<CompanyInfo> objects) {
                if (objects != null) {
                    if (objects.size() != 0) {
                        termsContent = objects.get(0).getHtml().toString();
                        textView.setText(Html.fromHtml(termsContent));
                        mainActivity.stopProgressBar(progressBar);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t + "");
                if(rootview != null) {
                    Snackbar.make(rootview, "Error fetching data", Snackbar.LENGTH_SHORT).show();
                }
                mainActivity.stopProgressBar(progressBar);
            }
        });
    }

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
