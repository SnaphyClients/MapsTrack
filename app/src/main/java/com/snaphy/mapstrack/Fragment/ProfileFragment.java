package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.EditProfileModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * All Profile related work is done here
 */
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_profile_textview1) TextView name;
    @Bind(R.id.fragment_profile_textview2) TextView email;
    @Bind(R.id.fragment_profile_textview3) TextView phone;
    @Bind(R.id.fragment_profile_picture) de.hdodenhof.circleimageview.CircleImageView profilePicture;
    ImageLoader imageLoader;
    MainActivity mainActivity;
    GoogleApiClient googleApiClient;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        addProfileData();

        return view;
    }

    public void googleLogout() {
        if(BackgroundService.getGoogleApiClient() != null) {
            if (BackgroundService.getGoogleApiClient().isConnected()) {
                Auth.GoogleSignInApi.signOut(BackgroundService.getGoogleApiClient()).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.v(Constants.TAG, status.toString());
                            }
                        });
            }
        } else {
            /*gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Constants.CLIENT_ID)
                    .build();*/
            googleApiClient = new GoogleApiClient.Builder(mainActivity)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
            if (googleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.v(Constants.TAG, status.toString());
                            }
                        });
            }
        }
    }

    @OnClick(R.id.fragment_profile_button1) void logoutButton() {
        if(BackgroundService.getCustomerRepository() == null){
            CustomerRepository customerRepository = mainActivity.getLoopBackAdapter().createRepository(CustomerRepository.class);
            BackgroundService.setCustomerRepository(customerRepository);
        }
        //TODO SHOW LOADING BAR
        BackgroundService.getCustomerRepository().logout(new VoidCallback() {
            @Override
            public void onSuccess() {
                //TODO CLOSE LOADING BAR
                //Move to login fragment..
                googleLogout();
                mainActivity.stopService(new Intent(mainActivity, BackgroundService.class));
                mainActivity.moveToLogin();
            }

            @Override
            public void onError(Throwable t) {
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : ProfileFragment, Method : logoutButton "+t.toString())
                        .build());
                //TODO CLOSE LOADING BAR
                Log.e(Constants.TAG, t.toString());
                BackgroundService.getCustomerRepository().setCurrentUserId(null);
                //Snackbar.make(rootView,Constants.ERROR_MESSAGE, Snackbar.LENGTH_SHORT).show();
                mainActivity.getLoopBackAdapter().clearAccessToken();
                BackgroundService.setCustomer(null);
                mainActivity.registerInstallation(null);
                //ALSO ADD GOOGLE LOGOUT AND FACEBOOK LOGOUT HERE..
                googleLogout();
                mainActivity.stopService(new Intent(mainActivity, BackgroundService.class));
                mainActivity.moveToLogin();
                //Toast.makeText(mainActivity, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.fragment_profile_button2) void editProfile() {
        EditProfileModel editProfileModel = new EditProfileModel();
        String nameArray[] = name.getText().toString().split(" ");

        if(!nameArray[0].isEmpty()) {
            if(nameArray.length > 2){
                if(!nameArray[1].isEmpty()){
                    if(nameArray.length > 3) {
                        if(nameArray.length > 4) {
                            editProfileModel.setFirstName(nameArray[0]+" "+nameArray[1]+ " "+nameArray[2]+ " "+nameArray[3]);

                        } else {
                            editProfileModel.setFirstName(nameArray[0] + " " + nameArray[1] + " " + nameArray[2]);
                        }
                    } else {
                        editProfileModel.setFirstName(nameArray[0] + " " + nameArray[1]);
                    }
                }
            } else {
                editProfileModel.setFirstName(nameArray[0]);
            }
            if(nameArray[nameArray.length-1] != null) {
                if (!nameArray[nameArray.length - 1].isEmpty()) {
                    editProfileModel.setLastName(nameArray[nameArray.length - 1]);
                }
            }
        }

        editProfileModel.setMobileNumber(phone.getText().toString());

        if (profilePicture.getDrawable() != null) {
            editProfileModel.setImage(profilePicture.getDrawable());
        }
        EventBus.getDefault().postSticky(editProfileModel, Constants.REQUEST_EDIT_PROFILE_FRAGMENT);
        mainActivity.replaceFragment(R.layout.fragment_edit_profile, null);
    }

    @Subscriber ( tag = Constants.RESPONSE_EDIT_PROFILE_FRAGMENT)
    public void saveEditedData(EditProfileModel editProfileModel) {
        name.setText(editProfileModel.getFirstName().toString() + " " + editProfileModel.getLastName().toString());
        profilePicture.setImageDrawable(editProfileModel.getImage());
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


    public void addProfileData(){
        if(BackgroundService.getCustomer() != null){
            String userName = "";
            String userEmail = "";
            if(!BackgroundService.getCustomer().getFirstName().isEmpty()){
                userName = BackgroundService.getCustomer().getFirstName();
                if(!BackgroundService.getCustomer().getLastName().isEmpty()){
                    userName = userName + " " + BackgroundService.getCustomer().getLastName();
                }
                name.setText(userName);
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User")
                        .setAction(userName)
                        .build());
            }

            if(BackgroundService.getCustomer().getEmail() != null){
                userEmail = BackgroundService.getCustomer().getEmail();
                email.setText(userEmail);
            }

            if(BackgroundService.getCustomer().getPhoneNumber() != null){
                phone.setText(BackgroundService.getCustomer().getPhoneNumber());
            }

            if(BackgroundService.getCustomer().getProfilePic() != null){
                mainActivity.loadUnsignedUrl(BackgroundService.getCustomer().getProfilePic(), profilePicture);
            }
        }
    }
}
