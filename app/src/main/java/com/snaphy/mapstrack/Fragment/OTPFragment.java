package com.snaphy.mapstrack.Fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.google.android.gms.analytics.HitBuilders;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OTPFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPFragment extends android.support.v4.app.Fragment{

    private OnFragmentInteractionListener mListener;
    public static final String TAG = "OTPFragment";
    MainActivity mainActivity;
    static String OTP;
    View rootview;
    static EditText enterCodeEditText;
    @Bind(R.id.fragment_otp_verification_edittext2) EditText mobileNumber;
    @Bind(R.id.fragment_otp_verification_textview6) TextView validationFailedTextView;
    @Bind(R.id.fragment_otp_verification_button3) TextView goButton;
    @Bind(R.id.fragment_otp_timer) TextView countDown;
    @Bind(R.id.fragment_otp_verification_button2) Button resendCode;
    @Bind(R.id.fragment_otp_verification_progressBar) com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar progressBar;
    ProgressDialog progress;


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
        Typeface typeface = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/gothic.ttf");
        countDown.setTypeface(typeface);
        enterCodeEditText = (EditText) view.findViewById(R.id.fragment_otp_verification_edittext1);
        mainActivity.stopProgressBar(progressBar);
        addChangeListener();
        return view;
    }

    public void setCountDown() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setVisibility(View.VISIBLE);
                countDown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                if (progress != null) {
                    progress.dismiss();
                }
                goButton.setEnabled(true);
                countDown.setText("Try Again");
            }

        }.start();
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
                    loginWithCode(code);
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
                    if (progress != null) {
                        progress.dismiss();
                    }
                    goButton.setEnabled(true);
                    if (response != null) {
                        Log.i(Constants.TAG, "Google = " + response.toString());
                        mainActivity.addUser(response);
                        //mainActivity.replaceFragment(R.layout.fragment_main, null);

                    } else {
                        //Toast.makeText(mainActivity, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                        Log.v(Constants.TAG, "Null returned from server.");
                    }
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : OTPFragment, Method : loginWithCode " + t.toString())
                            .build());
                    if (progress != null) {
                        progress.dismiss();
                    }
                    goButton.setEnabled(true);
                    Toast.makeText(mainActivity, "Invalid code", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick (R.id.fragment_otp_verification_button3) void requestOTP() {
        if(isPhoneValidate(mobileNumber.getText().toString())) {
            requestOtpServer(mobileNumber.getText().toString());
            progress = new ProgressDialog(mainActivity);
            setProgress(progress);
            setCountDown();
            goButton.setEnabled(false);
            View view1 = mainActivity.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }

        } else {
            View view1 = mainActivity.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
            Snackbar.make(rootview, "Enter Valid Mobile Number", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void setProgress(ProgressDialog progress) {
        progress.setIndeterminate(true);
        progress.setMessage("Loading...");
        progress.show();
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
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : OTPFragment, Method : requestOTPServe "+t.toString())
                            .build());
                    if(progress != null) {
                        progress.dismiss();
                    }
                    goButton.setEnabled(true);
                    Snackbar.make(rootview, "Your internet seems to be slow or not working. Please try again later", Snackbar.LENGTH_SHORT).show();
                    //TODO ADD RETRY BUTTON.. CALL THIS SAME METHOD..
                }
            });
        }
    }


    public boolean isPhoneValidate(String phone) {
        boolean isPhoneValid;
        Pattern phonePattern = Pattern.compile("\\d{10}");
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
