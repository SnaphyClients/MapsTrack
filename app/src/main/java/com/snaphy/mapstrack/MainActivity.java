package com.snaphy.mapstrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.snaphy.mapstrack.Fragment.AboutusFragment;
import com.snaphy.mapstrack.Fragment.ContactFragment;
import com.snaphy.mapstrack.Fragment.CreateEventFragment;
import com.snaphy.mapstrack.Fragment.CreateLocationFragment;
import com.snaphy.mapstrack.Fragment.EditProfileFragment;
import com.snaphy.mapstrack.Fragment.EventInfoFragment;
import com.snaphy.mapstrack.Fragment.FaqsFragment;
import com.snaphy.mapstrack.Fragment.FilterFragment;
import com.snaphy.mapstrack.Fragment.HomeFragment;
import com.snaphy.mapstrack.Fragment.LatitudeLongitudeFragment;
import com.snaphy.mapstrack.Fragment.LocationInfoFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFriendsFragment;
import com.snaphy.mapstrack.Fragment.LoginFragment;
import com.snaphy.mapstrack.Fragment.MainFragment;
import com.snaphy.mapstrack.Fragment.OTPFragment;
import com.snaphy.mapstrack.Fragment.ProfileFragment;
import com.snaphy.mapstrack.Fragment.SettingFragment;
import com.snaphy.mapstrack.Fragment.ShareFragment;
import com.snaphy.mapstrack.Fragment.ShowContactFragment;
import com.snaphy.mapstrack.Fragment.ShowMapFragment;
import com.snaphy.mapstrack.Fragment.TermsFragment;
import com.snaphy.mapstrack.Interface.OnFragmentChange;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.AccessTokenRepository;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainActivity extends AppCompatActivity implements OnFragmentChange,
        MainFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener, AboutusFragment.OnFragmentInteractionListener,
        FaqsFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,
        TermsFragment.OnFragmentInteractionListener, CreateEventFragment.OnFragmentInteractionListener,
        CreateLocationFragment.OnFragmentInteractionListener, ShowContactFragment.OnFragmentInteractionListener,
        ShowMapFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        EventInfoFragment.OnFragmentInteractionListener, LocationInfoFragment.OnFragmentInteractionListener,
        LocationShareByUserFragment.OnFragmentInteractionListener, LocationShareByUserFriendsFragment.OnFragmentInteractionListener,
        LatitudeLongitudeFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        OTPFragment.OnFragmentInteractionListener, FilterFragment.OnFragmentInteractionListener
{

    //TODO 1. Make Contacts Selected if they are selected and show invite button
    //TODO 2. On Publish Event remove all fragment from back stack
    //TODO 5. Delete Button in contacts in events and location
    //TODO 4. Implement Search
    //TODO 6. Clear all the fields in CreateEvent Fragment and CreateLocation

    //TODO 3. Validation and Verify Number


    /*Push Implementation*/
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    Context context;
    RestAdapter restAdapter;
    private static LocalInstallation installation;
    public static LocalInstallation getInstallation() {
        return installation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BackgroundService.setLoopBackAdapter(getLoopBackAdapter());
        //DONT DELETLE THIS LINE..WARNING
        getLoopBackAdapter();

        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
        startService(new Intent(getBaseContext(), BackgroundService.class));
        // TODO Stop service in activity on destory method if required
        context = getApplicationContext();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check device for Play Services APK.
                checkPlayServices();
            }
        }, 100);

        //Now check for login...
        checkLogin();
    }


    public void setProgress(ProgressDialog progress) {
        progress.setIndeterminate(true);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }


    public void checkLogin(){
        if(BackgroundService.getCustomerRepository() == null){
            CustomerRepository customerRepository = getLoopBackAdapter().createRepository(CustomerRepository.class);
            BackgroundService.setCustomerRepository(customerRepository);
        }

        // later in one of the Activity classes
        Customer current = BackgroundService.getCustomerRepository().getCachedCurrentUser();

        if (current != null) {

            BackgroundService.setCustomer(current);
            //Move to home fragment
            moveToHome();
        } else {
            //SHOW PROGRESS DIALOG
            final ProgressDialog progress = new ProgressDialog(this);
            setProgress(progress);
            BackgroundService.getCustomerRepository().findCurrentUser(new ObjectCallback<Customer>() {
                @Override
                public void onSuccess(Customer object) {
                    //CLOSE PROGRESS DIALOG
                    progress.dismiss();
                    BackgroundService.setCustomer(object);
                    //Move to home fragment
                    moveToHome();
                }

                @Override
                public void onError(Throwable t) {
                    //CLOSE PROGRESS DIALOG
                    progress.dismiss();
                    // you have to login first
                    BackgroundService.setCustomer(null);
                    moveToLogin();
                }
            });

        }

    }


    public void moveToLogin(){
        replaceFragment(R.layout.fragment_login, null);
    }






    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //EventBus.getDefault().unregister(this);
    }

    /**
     * This method is used to replace on fragment with teh other by calling appropiate method
     * @param id {int} Id of the fragment to be replaced (It should be unique)
     * @param object {Object} Data to be send to the fragment in anyform
     */
    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.layout.fragment_main:
                loadMainFragment(fragmentTransaction);
                break;

            case R.layout.fragment_aboutus :
                openAboutUsPage(fragmentTransaction);
                break;

            case R.layout.fragment_faq:
                openFaqPage(fragmentTransaction);
                break;

            case R.layout.fragment_contact:
                openContactPage(fragmentTransaction);
                break;

            case R.layout.fragment_terms:
                openTermsPage(fragmentTransaction);
                break;

            case R.layout.fragment_create_event :
                createEvent(fragmentTransaction);
                break;

            case R.layout.fragment_create_location:
                createLocation(fragmentTransaction);
                break;

            case R.layout.layout_select_contact:
                selectContact(fragmentTransaction);
                break;

            case R.layout.fragment_map:
                openMap(fragmentTransaction);
                break;

            case R.layout.fragment_login:
                isLogin(fragmentTransaction);
                break;

            case R.layout.fragment_event_info:
                openEventInfo(fragmentTransaction);
                break;

            case R.layout.fragment_location_info:
                openLocationInfo(fragmentTransaction);
                break;

            case R.id.fragment_event_info_button3:
                openMapFromEventInfo(fragmentTransaction);
                break;

            case R.id.fragment_event_info_button1:
                openEditFromEventInfo(fragmentTransaction);
                break;

            case R.id.fragment_location_info_button1:
                openEditFromLocationInfo(fragmentTransaction);
                break;

            case R.id.fragment_location_info_button2:
                openMapFromLocationInfo(fragmentTransaction);
                break;

            case R.id.fragment_location_info_button4:
                createEventFromLocation(fragmentTransaction);
                break;

            case R.id.fragment_create_location_button1:
                selectContactForLocation(fragmentTransaction);
                break;

            case R.id.fragment_location_share_by_user_floating_button1:
                selectContactForShareLocation(fragmentTransaction);
                break;

            case R.id.fragment_create_event_button5:
                openLatitudeLongitudeSelectionMap(fragmentTransaction);
                break;

            case R.id.fragment_create_location_button5:
                openLatitudeLongitudeSelectionMapFromLocation(fragmentTransaction);
                break;

            case R.layout.fragment_edit_profile:
                openEditProfile(fragmentTransaction);
                break;

            case R.id.fragment_edit_profile_edittext3:
                openOTPFromEditProfile(fragmentTransaction);
                break;

            case R.layout.fragment_ot:
                openVerifyOTP(fragmentTransaction);
                break;

            case R.layout.fragment_filter:
                openFilterFragment(fragmentTransaction);
                break;

            case R.id.fragment_location_info_button6:
                openAddFriendsFromLocationInfo(fragmentTransaction);
                break;

            case R.id.fragment_event_info_button5:
                openAddFriendsFromEventInfo(fragmentTransaction);
                break;
        }
    }

    /**
     *  Main Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void loadMainFragment(FragmentTransaction fragmentTransaction) {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().
                findFragmentByTag(MainFragment.TAG);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, mainFragment, MainFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  About us Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openAboutUsPage(FragmentTransaction fragmentTransaction) {
        AboutusFragment aboutusFragment = (AboutusFragment) getSupportFragmentManager().
                findFragmentByTag(AboutusFragment.TAG);
        if (aboutusFragment == null) {
            aboutusFragment = AboutusFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, aboutusFragment, AboutusFragment.TAG
        ).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  FAQs Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openFaqPage(FragmentTransaction fragmentTransaction) {
        FaqsFragment faqsFragment = (FaqsFragment) getSupportFragmentManager().
                findFragmentByTag(FaqsFragment.TAG);
        if (faqsFragment == null) {
            faqsFragment = FaqsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, faqsFragment, FaqsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  Contact Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openContactPage(FragmentTransaction fragmentTransaction) {
        ContactFragment contactFragment = (ContactFragment) getSupportFragmentManager().
                findFragmentByTag(ContactFragment.TAG);
        if (contactFragment == null) {
            contactFragment = ContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, contactFragment, ContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  Terms Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openTermsPage(FragmentTransaction fragmentTransaction) {
        TermsFragment termsFragment = (TermsFragment) getSupportFragmentManager().
                findFragmentByTag(TermsFragment.TAG);
        if (termsFragment == null) {
            termsFragment = TermsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, termsFragment, TermsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  Create Event Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void createEvent(FragmentTransaction fragmentTransaction) {
        CreateEventFragment createEventFragment = (CreateEventFragment) getSupportFragmentManager().
                findFragmentByTag(CreateEventFragment.TAG);
        if (createEventFragment == null) {
            createEventFragment = CreateEventFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, createEventFragment, CreateEventFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  Create Location Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void createLocation(FragmentTransaction fragmentTransaction) {
        CreateLocationFragment createLocationFragment = (CreateLocationFragment) getSupportFragmentManager().
                findFragmentByTag(CreateLocationFragment.TAG);
        if (createLocationFragment == null) {
            createLocationFragment = CreateLocationFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, createLocationFragment, CreateLocationFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  Select Contact Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void selectContact(FragmentTransaction fragmentTransaction) {
        ShowContactFragment showContactFragment = (ShowContactFragment) getSupportFragmentManager().
                findFragmentByTag(ShowContactFragment.TAG);
        if (showContactFragment == null) {
            showContactFragment = ShowContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_create_event_container, showContactFragment, ShowContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     *  Map Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openMap(FragmentTransaction fragmentTransaction) {
        ShowMapFragment showMapFragment = (ShowMapFragment) getSupportFragmentManager().
                findFragmentByTag(ShowMapFragment.TAG);
        if (showMapFragment == null) {
            showMapFragment = ShowMapFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, showMapFragment, ShowMapFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Login Fragment is open from here
    * @param fragmentTransaction {FragmentTransaction}
    */
    private void isLogin(FragmentTransaction fragmentTransaction) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().
                findFragmentByTag(LoginFragment.TAG);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, loginFragment, LoginFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Event Info Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openEventInfo(FragmentTransaction fragmentTransaction) {
        EventInfoFragment eventInfoFragment = (EventInfoFragment) getSupportFragmentManager().
                findFragmentByTag(EventInfoFragment.TAG);
        if (eventInfoFragment == null) {
            eventInfoFragment = EventInfoFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, eventInfoFragment, EventInfoFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Location Info Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openLocationInfo(FragmentTransaction fragmentTransaction) {
        LocationInfoFragment locationInfoFragment = (LocationInfoFragment) getSupportFragmentManager().
                findFragmentByTag(LocationInfoFragment.TAG);
        if (locationInfoFragment == null) {
            locationInfoFragment = LocationInfoFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, locationInfoFragment, LocationInfoFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Map Fragment from Event Info is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openMapFromEventInfo(FragmentTransaction fragmentTransaction) {
        ShowMapFragment showMapFragment = (ShowMapFragment) getSupportFragmentManager().
                findFragmentByTag(ShowMapFragment.TAG);
        if (showMapFragment == null) {
            showMapFragment = ShowMapFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_info_container, showMapFragment, ShowMapFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Create Event from Event Info is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openEditFromEventInfo(FragmentTransaction fragmentTransaction) {

        CreateEventFragment createEventFragment = (CreateEventFragment) getSupportFragmentManager().
                findFragmentByTag(CreateEventFragment.TAG);
        if (createEventFragment == null) {
            createEventFragment = CreateEventFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_info_container, createEventFragment, CreateEventFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Map Fragment from Location Info is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openMapFromLocationInfo(FragmentTransaction fragmentTransaction) {
        ShowMapFragment showMapFragment = (ShowMapFragment) getSupportFragmentManager().
                findFragmentByTag(ShowMapFragment.TAG);
        if (showMapFragment == null) {
            showMapFragment = ShowMapFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_location_info_container, showMapFragment, ShowMapFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Create Location from Location Info is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void openEditFromLocationInfo(FragmentTransaction fragmentTransaction) {

        CreateLocationFragment createLocationFragment = (CreateLocationFragment) getSupportFragmentManager().
                findFragmentByTag(CreateLocationFragment.TAG);
        if (createLocationFragment == null) {
            createLocationFragment = CreateLocationFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_location_info_container, createLocationFragment, CreateLocationFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**  Create Event from Location Info is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void createEventFromLocation(FragmentTransaction fragmentTransaction) {

        CreateEventFragment createEventFragment = (CreateEventFragment) getSupportFragmentManager().
                findFragmentByTag(CreateEventFragment.TAG);
        if (createEventFragment == null) {
            createEventFragment = CreateEventFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_location_info_container, createEventFragment, CreateEventFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Open contact for location
     * @param fragmentTransaction
     */
    private void selectContactForLocation(FragmentTransaction fragmentTransaction) {
        ShowContactFragment showContactFragment = (ShowContactFragment) getSupportFragmentManager().
                findFragmentByTag(ShowContactFragment.TAG);
        if (showContactFragment == null) {
            showContactFragment = ShowContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_create_location_container, showContactFragment, ShowContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openAddFriendsFromEventInfo(FragmentTransaction fragmentTransaction) {
        ShowContactFragment showContactFragment = (ShowContactFragment) getSupportFragmentManager().
                findFragmentByTag(ShowContactFragment.TAG);
        if (showContactFragment == null) {
            showContactFragment = ShowContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_info_container, showContactFragment, ShowContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openAddFriendsFromLocationInfo(FragmentTransaction fragmentTransaction) {
        ShowContactFragment showContactFragment = (ShowContactFragment) getSupportFragmentManager().
                findFragmentByTag(ShowContactFragment.TAG);
        if (showContactFragment == null) {
            showContactFragment = ShowContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_location_info_container, showContactFragment, ShowContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }



    /**
     * Open Contact for location sharing
     * @param fragmentTransaction
     */
    private void selectContactForShareLocation(FragmentTransaction fragmentTransaction) {
        ShowContactFragment showContactFragment = (ShowContactFragment) getSupportFragmentManager().
                findFragmentByTag(ShowContactFragment.TAG);
        if (showContactFragment == null) {
            showContactFragment = ShowContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, showContactFragment, ShowContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * Open Contact for location sharing
     * @param fragmentTransaction
     */
    private void openLatitudeLongitudeSelectionMap(FragmentTransaction fragmentTransaction) {
        LatitudeLongitudeFragment latitudeLongitudeFragment = (LatitudeLongitudeFragment) getSupportFragmentManager().
                findFragmentByTag(LatitudeLongitudeFragment.TAG);
        if (latitudeLongitudeFragment == null) {
            latitudeLongitudeFragment = LatitudeLongitudeFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_create_event_container, latitudeLongitudeFragment, LatitudeLongitudeFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Open Contact for location sharing
     * @param fragmentTransaction
     */
    private void openLatitudeLongitudeSelectionMapFromLocation(FragmentTransaction fragmentTransaction) {
        LatitudeLongitudeFragment latitudeLongitudeFragment = (LatitudeLongitudeFragment) getSupportFragmentManager().
                findFragmentByTag(LatitudeLongitudeFragment.TAG);
        if (latitudeLongitudeFragment == null) {
            latitudeLongitudeFragment = LatitudeLongitudeFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_create_location_container, latitudeLongitudeFragment, LatitudeLongitudeFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openEditProfile(FragmentTransaction fragmentTransaction) {
        EditProfileFragment editProfileFragment = (EditProfileFragment) getSupportFragmentManager().
                findFragmentByTag(EditProfileFragment.TAG);
        if (editProfileFragment == null) {
            editProfileFragment = EditProfileFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, editProfileFragment, EditProfileFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void  openOTPFromEditProfile(FragmentTransaction fragmentTransaction) {
        OTPFragment otpFragment = (OTPFragment) getSupportFragmentManager().
                findFragmentByTag(OTPFragment.TAG);
        if (otpFragment == null) {
            otpFragment = OTPFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.fragment_edit_profile_container, otpFragment, OTPFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void  openVerifyOTP(FragmentTransaction fragmentTransaction) {
        OTPFragment otpFragment = (OTPFragment) getSupportFragmentManager().
                findFragmentByTag(OTPFragment.TAG);
        if (otpFragment == null) {
            otpFragment = OTPFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, otpFragment, OTPFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openFilterFragment(FragmentTransaction fragmentTransaction) {
        FilterFragment filterFragment = (FilterFragment) getSupportFragmentManager().
                findFragmentByTag(FilterFragment.TAG);
        if (filterFragment == null) {
            filterFragment = FilterFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, filterFragment, FilterFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void startProgressBar(SmoothProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.progressiveStart();
        }
        //OR Use Progress Dialog
    }

    public void stopProgressBar(SmoothProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
            progressBar.progressiveStop();
        }
        //OR Use Progress Dialog
    }



    /**
     * Updates the registration for push notifications.
     */
    public void updateRegistration(String userId) {
        gcm = GoogleCloudMessaging.getInstance(this);
        final RestAdapter adapter = getLoopBackAdapter();
        // 2. Create LocalInstallation instance
        final LocalInstallation installation =  new LocalInstallation(context, adapter);

        // 3. Update Installation properties that were not pre-filled
        /*TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        LOOPBACK_APP_ID = mngr.getDeviceId();*/
        // Enter the id of the LoopBack Application
        installation.setAppId(Constants.LOOPBACK_APP_ID);
       /* Log.i(TAG, LOOPBACK_APP_ID);*/
        // Substitute a real id of the user logged in this application
        installation.setUserId(userId);

        // 4. Check if we have a valid GCM registration id
        if (installation.getDeviceToken() != null) {
            // 5a. We have a valid GCM token, all we need to do now
            //     is to save the installation to the server
            saveInstallation(installation);
        } else {
            // 5b. We don't have a valid GCM token. Get one from GCM
            // and save the installation afterwards.
            registerInBackground(installation);
        }
    }

    /**
     * Checks the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(Constants.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID in the provided LocalInstallation
     */
    private void registerInBackground(final LocalInstallation installation) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                try {
                    final String regid = gcm.register(Constants.SENDER_ID);
                    installation.setDeviceToken(regid);
                    return "Device registered, registration ID=" + regid;
                } catch (final IOException ex) {
                    Log.e(Constants.TAG, "GCM registration failed.", ex);
                    return "Cannot register with GCM:" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
            }

            @Override
            protected void onPostExecute(final String msg) {

                saveInstallation(installation);
            }
        }.execute(null, null, null);
    }

    /**
     * Saves the Installation to the LoopBack server and reports the result.
     * @param installation_
     */
    void saveInstallation(final LocalInstallation installation_) {
        installation_.save(new VoidCallback() {

            @Override
            public void onSuccess() {
                final Object id = installation_.getId();
                installation = installation_;
                final String msg = "Installation saved with id " + id;
                Log.i(Constants.TAG, msg);


            }

            @Override
            public void onError(final Throwable t) {
                Log.e(Constants.TAG, "Error saving Installation.", t);

            }
        });
    }


    public void registerInstallation(Customer customer){
        if (checkPlayServices()) {
            if (customer != null) {
                // logged in
                Log.d(Constants.TAG, "User logged in successfully");
                updateRegistration((String) customer.getId());
            } else {
                // anonymous user
                Log.d(Constants.TAG, "User not logged in ");
                updateRegistration("Anonymous retailer");
            }
        } else {
            Log.e(Constants.TAG, "No valid Google Play Services APK found.");
        }
    }



    public RestAdapter getLoopBackAdapter() {
        if (restAdapter == null) {

            restAdapter = new RestAdapter(
                    getApplicationContext(),
                    Constants.apiUrl);
            BackgroundService.setLoopBackAdapter(restAdapter);
        }
        return restAdapter;
    }


    public void moveToHome(){
        //NOW move to home fragment..
        replaceFragment(R.layout.fragment_main, null);
    }


    /**
     * AddUser method for adding user once the user is successfully signed in
     * @param  response
     */
    public void addUser(JSONObject response){

        if(response != null){
            Map<String, Object> responseMap = JsonUtil.fromJson(response);
            Map<String, Object> accessTokenMap = new HashMap<>();
            accessTokenMap.put("id", responseMap.get("id"));
            accessTokenMap.put("ttl", responseMap.get("ttl"));
            AccessTokenRepository accessTokenRepository = getLoopBackAdapter().createRepository(AccessTokenRepository.class);


            JSONObject userJson = response.optJSONObject("user");
            Log.i(Constants.TAG, userJson.toString());
            if(BackgroundService.getCustomerRepository() == null){
                CustomerRepository customerRepository = getLoopBackAdapter().createRepository(CustomerRepository.class);
                BackgroundService.setCustomerRepository(customerRepository);
            }
            Customer user = userJson != null
                    ? BackgroundService.getCustomerRepository().createObject(JsonUtil.fromJson(userJson))
                    : null;
            accessTokenMap.put("userId", user.getId());
            AccessToken accessToken = accessTokenRepository.createObject(accessTokenMap);
            getLoopBackAdapter().setAccessToken(accessToken.getId().toString());
            BackgroundService.getCustomerRepository().setCurrentUserId(accessToken.getUserId());
            BackgroundService.getCustomerRepository().setCachedCurrentUser(user);

            //Now set the customer repo
            //BackgroundService.setCustomerRepository(BackgroundService.getCustomerRepository());
            //Now add Customer to BackgroundService.
            BackgroundService.setCustomer(user);

            //Now move to home fragment finally..
            moveToHome();
        }else{
            Toast.makeText(this, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
