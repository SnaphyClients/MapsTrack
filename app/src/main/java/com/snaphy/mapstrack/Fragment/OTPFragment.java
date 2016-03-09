package com.snaphy.mapstrack.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OTPFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static final String TAG = "OTPFragment";
    MainActivity mainActivity;
    static String OTP;
    View rootview;
    static EditText enterCodeEditText;
    @Bind(R.id.fragment_otp_verification_edittext2) EditText mobileNumber;
    @Bind(R.id.fragment_otp_verification_textview6) TextView validationFailedTextView;
    @Bind(R.id.fragment_otp_verification_button3) TextView goButton;
    @Bind(R.id.fragment_otp_verification_button2)
    Button resendCode;
    @Bind(R.id.fragment_otp_verification_progressBar) SmoothProgressBar progressBar;


    public OTPFragment() {
        // Required empty public constructor
    }

    public static OTPFragment newInstance() {
        OTPFragment fragment = new OTPFragment();
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
        View view = inflater.inflate(R.layout.fragment_ot, container, false);
        ButterKnife.bind(this, view);
        rootview = view;
        enterCodeEditText = (EditText) view.findViewById(R.id.fragment_otp_verification_edittext1);
        mainActivity.stopProgressBar(progressBar);
        addChangeListener();
        return view;
    }

    public void addChangeListener() {
        enterCodeEditText.addTextChangedListener(getTextWatcher());
    }

    public TextWatcher getTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int codeLength = start + count;

                String code = enterCodeEditText.getText().toString().trim();
                if (codeLength == 4) {

                    //mainActivity.replaceFragment(R.layout.fragment_main, null);
                }
            }//onTextChanged


            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void loginWithCode(String code){
        if(!BackgroundService.getAccessToken().isEmpty()){
            CustomerRepository customerRepository = mainActivity.getLoopBackAdapter().createRepository(CustomerRepository.class);
            customerRepository.loginWithCode(BackgroundService.getAccessToken(), code, mobileNumber.getText().toString(), new Adapter.JsonObjectCallback() {
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
                    Toast.makeText(mainActivity, "Invalid code", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick (R.id.fragment_otp_verification_button3) void requestOTP() {
        if(isPhoneValidate(mobileNumber.getText().toString())) {
            //TODO SEND OTP METHOD CALLED
            requestOtpServer(mobileNumber.getText().toString());

        } else {
            Snackbar.make(rootview, "Enter Valid Mobile Number", Snackbar.LENGTH_SHORT).show();
        }
    }



    public void requestOtpServer(String number){
        if(!BackgroundService.getAccessToken().isEmpty()){
            CustomerRepository customerRepository = mainActivity.getLoopBackAdapter().createRepository(CustomerRepository.class);
            customerRepository.requestOtp(mobileNumber.getText().toString(), new Adapter.JsonObjectCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.i(Constants.TAG, response + "");
                }

                @Override
                public void onError(Throwable t) {
                    Snackbar.make(rootview, "Enter Valid Mobile Number", Snackbar.LENGTH_SHORT).show();
                    //TODO ADD RETRY BUTTON.. CALL THIS SAME METHOD..
                }
            });
        }
    }


    public boolean isPhoneValidate(String phone) {
        boolean isPhoneValid = false;
        Pattern phonePattern = Pattern.compile("\\b\\d{10}\\b");
        Matcher phoneMatcher = phonePattern.matcher(phone);
        if (!phoneMatcher.matches()) {
            //Snackbar.make(getView(), "Enter Correct Phone Number", Snackbar.LENGTH_SHORT).show();
            isPhoneValid = false;
        } else {
            isPhoneValid = true;
        }
        return  isPhoneValid;
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
        void onFragmentInteraction(Uri uri);
    }

    public static class IncomingSms extends BroadcastReceiver {

        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();
        public Matcher m;

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v(Constants.TAG, "Broadcast is working");
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        //Log.v("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        Pattern p = Pattern.compile("\\b\\d{4}\\b");
                        m = p.matcher(message);
                        while (m.find()) {

                            OTP = m.group().toString();
                            enterCodeEditText.setText(OTP);
                            //mainActivity.replaceFragment(R.layout.fragment_place_order, null);

                        }
                        // Show Alert
                        //int duration = Toast.LENGTH_LONG;
                        //Toast toast = Toast.makeText(context,
                        //"senderNum: "+ senderNum + ", message: " + message, duration);
                        //toast.show();

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }

    }
}
