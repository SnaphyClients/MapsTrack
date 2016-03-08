package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RESULT_OK = 1;
    private OnFragmentInteractionListener mListener;
    public static String TAG = "LoginFragment";
    @Bind(R.id.fragment_login_textview0) TextView mapsText;
    @Bind(R.id.fragment_login_textview1) TextView trackText;
    private GoogleApiClient googleApiClient;
    MainActivity mainActivity;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private static final int RC_SIGN_IN = 0;
    GoogleSignInOptions gso;
    String personName;

    //Client Id
    //624660143780-1k79af7cuirj7df67p1otbnnb70adiue.apps.googleusercontent.com

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Hobo.ttf");
        mapsText.setTypeface(typeface);
        trackText.setTypeface(typeface);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.CLIENT_ID)
                .requestEmail()
                .build();

        initializeGoogleApiClient();
        return view;
    }

    @Subscriber(tag = Constants.LOGOUT)
    private void logout(String logout) {
        Log.v(Constants.TAG, "Out Logout");
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
            googleApiClient.connect();
            Log.v(Constants.TAG, "In Logout");
            mainActivity.onBackPressed();
        }
    }

    @OnClick(R.id.fragment_login_button1) void loginButton() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void initializeGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(mainActivity)
                .enableAutoManage(mainActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //BackgroundService.googleApiClient = googleApiClient;
        googleApiClient.connect();
    }

   /* public void sendTokenToServer(String token) {
        CustomerRepository customerRepository = mainActivity.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.loginWithGoogle(token, new Adapter.JsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Log.i(Constants.TAG, "Google = " + response.toString());
                    mainActivity.addUser(response);
                    //mainActivity.replaceFragment(R.layout.fragment_main, null);

                } else {
                    Log.v(Constants.TAG, "Null");
                }


            }

            @Override
            public void onError(Throwable t) {
                Snackbar.make(getView(), "Something went wrong", Snackbar.LENGTH_SHORT).show();
                Log.e(Constants.TAG, t.toString());

            }
        });
    }*/

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if(acct.getIdToken() != null) {
                //sendTokenToServer(acct.getIdToken());
            }

        } else {

        }
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


    @Override
    public void onConnectionFailed(ConnectionResult result) {

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
