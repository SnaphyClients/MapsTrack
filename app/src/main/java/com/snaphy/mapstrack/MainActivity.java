package com.snaphy.mapstrack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.EventType;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Track;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.AmazonImageRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.TrackRepository;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.snaphy.mapstrack.Database.MapsTrackDB;
import com.snaphy.mapstrack.Fragment.AboutusFragment;
import com.snaphy.mapstrack.Fragment.ContactFragment;
import com.snaphy.mapstrack.Fragment.CreateEventFragment;
import com.snaphy.mapstrack.Fragment.CreateLocationFragment;
import com.snaphy.mapstrack.Fragment.EditProfileFragment;
import com.snaphy.mapstrack.Fragment.EventInfoFragment;
import com.snaphy.mapstrack.Fragment.EventListFragment;
import com.snaphy.mapstrack.Fragment.FaqsFragment;
import com.snaphy.mapstrack.Fragment.FilterFragment;
import com.snaphy.mapstrack.Fragment.HomeFragment;
import com.snaphy.mapstrack.Fragment.LatitudeLongitudeFragment;
import com.snaphy.mapstrack.Fragment.LocationInfoFragment;
import com.snaphy.mapstrack.Fragment.LocationListFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFriendsFragment;
import com.snaphy.mapstrack.Fragment.LoginFragment;
import com.snaphy.mapstrack.Fragment.MainFragment;
import com.snaphy.mapstrack.Fragment.OTPFragment;
import com.snaphy.mapstrack.Fragment.ProfileFragment;
import com.snaphy.mapstrack.Fragment.RetryLoginFragment;
import com.snaphy.mapstrack.Fragment.SettingFragment;
import com.snaphy.mapstrack.Fragment.ShareFragment;
import com.snaphy.mapstrack.Fragment.ShowContactFragment;
import com.snaphy.mapstrack.Fragment.ShowMapFragment;
import com.snaphy.mapstrack.Fragment.TermsFragment;
import com.snaphy.mapstrack.Interface.OnFragmentChange;
import com.snaphy.mapstrack.Model.CustomContainer;
import com.snaphy.mapstrack.Model.CustomContainerRepository;
import com.snaphy.mapstrack.Model.CustomFileRepository;
import com.snaphy.mapstrack.Model.ImageModel;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.snaphy.mapstrack.Services.FetchAddressIntentService;
import com.snaphy.mapstrack.Services.MyRestAdapter;
import com.snaphy.mapstrack.Services.UnboundService;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.AccessTokenRepository;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/*import com.google.android.gms.analytics.Tracker;*/

//import com.google.android.gms.analytics.Tracker;

/*import com.google.android.gms.analytics.Tracker;*/

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
        OTPFragment.OnFragmentInteractionListener, FilterFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        RetryLoginFragment.OnFragmentInteractionListener, EventListFragment.OnFragmentInteractionListener,
        LocationListFragment.OnFragmentInteractionListener
{

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    static MainActivity mainActivity;
    public Tracker tracker;
    /*Push Implementation*/
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    Context context;
    MyRestAdapter restAdapter;
    MainActivity that;
    public static final int LOCATION_ALERT = 103;
    LocationManager mLocationManager;
    private static LocalInstallation installation;
    static View parentLayout;
    UnboundService testService;
    boolean isBound = false;
    static ProgressDialog progressMain;
    int count = 0;
    GoogleApiClient googleApiClient;
    public static LocalInstallation getInstallation() {
        return installation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLayout = findViewById(R.id.container);
        /*Mint.initAndStartSession(MainActivity.this, "16020362");*/
        MapsTrackDB application = (MapsTrackDB) getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
        mainActivity = this;
        that = this;
        //BackgroundService.setLoopBackAdapter(getLoopBackAdapter());
        //DONT DELETE THIS LINE..WARNING
        getLoopBackAdapter();


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Hobo.ttf");
        TextView textView1 = (TextView)findViewById(R.id.activity_main_textview0);
        TextView textView2 = (TextView)findViewById(R.id.activity_main_textview1);

        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);

        Intent myIntent = new Intent(MainActivity.this, UnboundService.class);
        bindService(myIntent, serviceConnection, Context.BIND_AUTO_CREATE);


        context = getApplicationContext();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check device for Play Services APK.
                checkPlayServices();
            }
        }, 100);
        if(isNetworkAvailable() == false) {
            Snackbar.make(parentLayout, "Internet not connected", Snackbar.LENGTH_INDEFINITE).show();
        } else {
            if (isLocationEnabled(this)) {
                checkLogin();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Seems Like location service is off,   Enable it?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String action = "com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS";
                                Intent settings = new Intent(action);
                                startActivityForResult(settings, LOCATION_ALERT);
                            }
                        }).setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).create().show();
            }
        }
        TrackCollection.setFilterColor(Constants.NEAR_BY);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UnboundService.MyBinder binder = (UnboundService.MyBinder) service;
            testService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            testService = null;
            isBound = false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public void setProgress(ProgressDialog progress) {
        progress.setIndeterminate(true);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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
                    if(object != null){
                        BackgroundService.setCustomer(object);
                        //Move to home fragment
                        moveToHome();
                    }else{
                        // you have to login first
                        BackgroundService.setCustomer(null);
                        googleLogout();
                        //Register anonymous for push service..
                        moveToLogin();
                    }

                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : checkLogin "+t.toString())
                            .build());
                    //CLOSE PROGRESS DIALOG
                    progress.dismiss();
                    // you have to login first
                    BackgroundService.setCustomer(null);
                    googleLogout();
                    //Register anonymous for push service..
                    //moveToLogin();
                    //Retry Login
                    if(t.toString().equals("org.apache.http.client.HttpResponseException: Unauthorized")) {
                        Snackbar.make(parentLayout, "Unable to connect to server", Snackbar.LENGTH_LONG).show();
                        moveToLogin();
                    } else {
                        replaceFragment(R.layout.fragment_retry_login, null);
                    }

                }
            });

        }

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



    public void moveToLogin(){
        replaceFragment(R.layout.fragment_login, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("MapsTrack")
                .setAction("")
                .build());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * This method is used to replace on fragment with teh other by calling appropiate method
     * @param id {int} Id of the fragment to be replaced (It should be unique)
     * @param object {Object} Data to be send to the fragment in anyform
     */
    @Override
    public void replaceFragment(int id, Object object) {
        if(isNetworkAvailable() == false) {
            Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show();
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (id) {
                case R.layout.fragment_main:
                    loadMainFragment(fragmentTransaction);
                    break;

                case R.layout.fragment_aboutus:
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

                case R.layout.fragment_create_event:
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

                case R.layout.fragment_retry_login:
                    retryLogin(fragmentTransaction);
                    break;
            }
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


    private void retryLogin(FragmentTransaction fragmentTransaction) {
        RetryLoginFragment retryLoginFragment = (RetryLoginFragment) getSupportFragmentManager().
                findFragmentByTag(RetryLoginFragment.TAG);
        if (retryLoginFragment == null) {
            retryLoginFragment = RetryLoginFragment.newInstance();
        } else {
            retryLoginFragment.viewMyDialog();
        }
        fragmentTransaction.replace(R.id.container, retryLoginFragment, RetryLoginFragment.TAG);
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
        } else {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(LatitudeLongitudeFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.fragment_create_event_container, latitudeLongitudeFragment, LatitudeLongitudeFragment.TAG).addToBackStack(LatitudeLongitudeFragment.TAG);
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
        } else {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(LatitudeLongitudeFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.fragment_create_location_container, latitudeLongitudeFragment, LatitudeLongitudeFragment.TAG).addToBackStack(LatitudeLongitudeFragment.TAG);
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
        final MyRestAdapter adapter = getLoopBackAdapter();
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
                //Now save the installation id with customer..
                if (BackgroundService.getCustomer() != null) {
                    //Add registration id to customer..
                    BackgroundService.getCustomer().setRegistrationId((String) id);
                    updateCustomer(BackgroundService.getCustomer());
                }

            }

            @Override
            public void onError(final Throwable t) {
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : MainActivity, Method : saveInstallation"+t.toString())
                        .build());
                Log.e(Constants.TAG, "Error saving Installation.", t);

            }
        });
    }


    public void registerInstallation(Customer customer){
        if (checkPlayServices()) {
            if (customer != null) {
                // logged in
                Log.d(Constants.TAG, "User logged in successfully");
                updateRegistration((String)customer.getId());
            } else {
                // anonymous user
                Log.d(Constants.TAG, "User not logged in ");
                updateRegistration("Anonymous User");
            }
        } else {
            Log.e(Constants.TAG, "No valid Google Play Services APK found.");
        }
    }



    public MyRestAdapter getLoopBackAdapter() {
        if (restAdapter == null) {

            restAdapter = new MyRestAdapter(
                    getApplicationContext(),
                    Constants.apiUrl);
            BackgroundService.setLoopBackAdapter(restAdapter);
        }
        return restAdapter;
    }


    public void moveToHome(){
        initializeGooglePlacesApi();
        //Now check for login...
        mGoogleApiClient.connect();
        //NOW move to home fragment..
        replaceFragment(R.layout.fragment_main, null);
        progressMain = new ProgressDialog(this);
        setProgress(progressMain);
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

            //Register for push service..
            registerInstallation(user);
        }else{
            Log.v(Constants.TAG, "Error in add Customer Method");
            //Toast.makeText(this, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    public void loadImage(final String imageUri, final ImageView imageView){
        try {
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.default_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e(Constants.TAG, "Image downloading failed. retrying.");
                            loadImage(imageUri, imageView, 0);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        } catch (Exception e) {
            Log.e(Constants.TAG, e.toString());
            Log.e(Constants.TAG, "Error downloading image.Without retrying");
        }

    }


    public void loadImage(final String imageUri,final ImageView imageView, final int retries){
        try {
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.default_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            if (retries <= 3) {
                                int track = retries + 1;
                                Log.e(Constants.TAG, "Image downloading failed. retrying." + track);
                                loadImage(imageUri, imageView, track);

                            } else {
                                Log.e(Constants.TAG, "Maximum retries of image downloading done. Cannot download image.");
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error downloading image from server.");
            Log.e(Constants.TAG, e.toString());
        }

    }


    public void fetchThumb(Map<String, Object> fileObj, final ImageView imageView){
        if(fileObj != null){
            String container = (String)fileObj.get("container");
            String file = (String)fileObj.get("name");
            fetchUrl(container, file, imageView, "thumb_");
        }

    }


    public void fetchMedium(Map<String, Object> fileObj, final ImageView imageView){
        if(fileObj != null) {
            String container = (String) fileObj.get("container");
            String file = (String) fileObj.get("name");

            fetchUrl(container, file, imageView, "medium_");
        }
    }


    public void fetchSmall(Map<String, Object> fileObj, final ImageView imageView) {
        if (fileObj != null) {
            String container = (String) fileObj.get("container");
            String file = (String) fileObj.get("name");
            fetchUrl(container, file, imageView, "small_");
        }
    }


    //Fetch url from server..
    public void loadUnsignedUrl(Map<String, Object> imageObj, final ImageView imageView){
        if(imageObj != null){
            if(imageObj.get("url") != null){
                Map<String, String> hashMap;
                try{
                    hashMap   = (Map<String, String>)imageObj.get("url");
                    if(hashMap.get("unSignedUrl") != null){
                        String unsignedUrl = hashMap.get("unSignedUrl");
                        if(unsignedUrl != null){
                            try{
                                loadImage(unsignedUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }

                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }else{
                        String defaultUrl = hashMap.get("defaultUrl");
                        if(defaultUrl != null){
                            try{
                                loadImage(Constants.baseUrl + defaultUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }
                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }
                }catch (Exception e){
                    fetchSmall(imageObj, imageView);
                }


            }
            else{
                fetchSmall(imageObj, imageView);
            }
        }else{
            fetchSmall(imageObj, imageView);
        }

    }


    //Fetch url from server..
    public void fetchUrl(String container, String file, final ImageView imageView, String prefix){
        AmazonImageRepository amazonImageRepository = getLoopBackAdapter().createRepository(AmazonImageRepository.class);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "prefix");
        hashMap.put("value", prefix);
        amazonImageRepository.getUrl(container, file, hashMap, new Adapter.JsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Map<String, Object> objectHashMap = (Map) JsonUtil.fromJson(response);
                    String defaultUrl = (String) objectHashMap.get("defaultUrl");
                    String signedUrl = (String) objectHashMap.get("signedUrl");
                    if (!signedUrl.isEmpty()) {
                        try {
                            loadImage(signedUrl, imageView);
                        } catch (Exception e) {
                            Log.e(Constants.TAG, e + "");
                        }
                    } else {
                        try {
                            loadImage(Constants.baseUrl + defaultUrl, imageView);
                        } catch (Exception e) {
                            Log.e(Constants.TAG, e + "");
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable t) {
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : MainActivity, Method : fetchUrl"+t.toString())
                        .build());
                Log.v(Constants.TAG, "Could not fetch url. Please try again later..");
            }
        });
    }



    public void uploadWithCallback(String containerName, File imageFile, final ObjectCallback<ImageModel> callback){
        Date date = new Date();
        String fileName = String.valueOf(date.getTime());
        //create a file to write bitmap data
        File file = new File(this.getCacheDir(), fileName + ".jpg");

        try{
            file.createNewFile();
            //Now converting image to bitmap..
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
            //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            byte[] bitmapdata = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //Flushing bitmap..
            bitmap.recycle();
            bitmap = null;


            CustomContainerRepository containerRepo = getLoopBackAdapter().createRepository(CustomContainerRepository.class);
            CustomFileRepository customFileRepository = getLoopBackAdapter().createRepository(CustomFileRepository.class);
            Map<String, String> objectHashMap = new HashMap<>();
            objectHashMap.put("name", containerName);
            CustomContainer container1 = containerRepo.createObject(objectHashMap);
            container1.UploadAmazon(file, new ObjectCallback<ImageModel>() {
                @Override
                public void onSuccess(ImageModel object) {
                    // object
                    callback.onSuccess(object);
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : uploadWithCallback" + t.toString())
                            .build());
                    callback.onError(t);
                }
            });
        }
        catch (IOException e){
            //TODO SHOW MESSAGE TRY AGAIN UPLOADING IMAGE..
            Log.e(Constants.TAG, e.toString());
        }

    }

    public void  uploadImageToContainer(String containerName, File imageFile, final String TAG) {
        Date date = new Date();
        String fileName = String.valueOf(date.getTime());
        //create a file to write bitmap data
        File file = new File(this.getCacheDir(), fileName + ".jpg");

        try{
            file.createNewFile();
            //Now converting image to bitmap..
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
            //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            byte[] bitmapdata = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //Flushing bitmap..
            bitmap.recycle();
            bitmap = null;


            CustomContainerRepository containerRepo = getLoopBackAdapter().createRepository(CustomContainerRepository.class);
            CustomFileRepository customFileRepository = getLoopBackAdapter().createRepository(CustomFileRepository.class);
            Map<String, String> objectHashMap = new HashMap<>();
            objectHashMap.put("name", containerName);
            CustomContainer container1 = containerRepo.createObject(objectHashMap);
            container1.UploadAmazon(file, new ObjectCallback<ImageModel>() {
                @Override
                public void onSuccess(ImageModel object) {
                    // object
                    EventBus.getDefault().post(object, TAG);
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : uploadImageToContainer"+t.toString())
                            .build());
                    Log.e(Constants.TAG, t.toString());
                    ImageModel imageModel = new ImageModel();
                    EventBus.getDefault().post(imageModel, TAG);
                }
            });
        }
        catch (IOException e){
            //TODO SHOW MESSAGE TRY AGAIN UPLOADING IMAGE..
            Log.e(Constants.TAG, e.toString());
            Log.e(Constants.TAG, "Error Downloading Image");
        }
    }


    public void updateCustomer(Customer customer){
        Map<String, ? extends Object> data = customer.convertMap();
        //Remove the password field..
        data.remove("password");
        final MainActivity that = this;
        BackgroundService.getCustomerRepository().updateAttributes((String)customer.getId(), data, new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                Log.v(Constants.TAG, "Customer Profile is Updated");
            }

            @Override
            public void onError(Throwable t) {
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : MainActivity, Method : updateCustomer"+t.toString())
                        .build());
                Log.e(Constants.TAG, t.toString());
                Log.v(Constants.TAG, "Error in update Customer Method");
                //Toast.makeText(that, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize google place api and last location of the app
     */
    private void initializeGooglePlacesApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
    }

    /**
     * When google api is connected it start intent service from this method
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
        int permissionCheck1 = ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            //http://stackoverflow.com/questions/17519198/how-to-get-the-current-location-latitude-and-longitude-in-android
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            if (!Geocoder.isPresent()) {
                return;
            }

            startIntentService();
        } else {
            mLastLocation = getLastKnownLocation();
            if(mLastLocation != null) {
                startIntentService();
            }
        }
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            int permissionCheck1 = ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCheck2 = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }

        }
        return bestLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * This method is responsible to start the service ie..FetchAddressIntentService to fetch
     * the address and display it in edittext
     */
    protected void startIntentService() {
        Intent intent = new Intent(this,FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        this.startService(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOCATION_ALERT) {
            if(isNetworkAvailable() == false) {
                Snackbar.make(parentLayout, "Internet not connected", Snackbar.LENGTH_INDEFINITE).show();
            } else {
                checkLogin();
            }

        }

    }

    @Subscriber ( tag = Constants.SEND_ERROR_IN_FINDING_LOCATION )
    public void showErrorMessage(String resultCode) {
        Log.v(Constants.TAG, "Error Fetching Address");
        Log.v(Constants.TAG, "Cannot locate you, Please try again");
        if(count < 5) {
            startIntentService();
            count++;
        } else {
            progressMain.dismiss();
            Snackbar.make(parentLayout, "Slow Connection, Please try again later", Snackbar.LENGTH_LONG).show();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Unable to locate you, try again?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startIntentService();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    /**
     * It is a class which is used to get result received from the service
     * The result is in many forms including
     * Error for no address found,time out etc...
     * Or Address, if correct address is found
     */
    @SuppressLint("ParcelCreator")
    public static class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * This method is fired when result is received from the service
         * @param resultCode
         * @param resultData
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            final String mAddressOutput;
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Log.v(Constants.TAG, mAddressOutput);
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAddressOutput != null) {
                            BackgroundService.setAddress(mAddressOutput);
                            mainActivity.startService();
                            Log.v(Constants.TAG, "From Result Receiver");
                            progressMain.dismiss();
                        }
                    }
                });

            } else if(resultCode == Constants.FAILURE_RESULT){
                Log.v(Constants.TAG, "Try again");
                // SHOW MESSAGE THAT ADDRESS CANNOT BE FETCHED AT THE MOMENT
                //startIntentService();
            }
        }
    }

    @Subscriber( tag = Constants.SEND_ADDRESS_HAS_BEEN_FOUND)
    public void getAddressFromService(String address) {
        BackgroundService.setAddress(address);
        mainActivity.startService();
        progressMain.dismiss();
        Log.v(Constants.TAG, "From EventBus");
    }



    public void deleteTrack(Track track){
        track.destroy(new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable t) {
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : MainActivity, Method : deleteTrack" + t.toString())
                        .build());
                Log.e(Constants.TAG, t.toString());
            }
        });
    }


    public void displayEventType(final TextView textView, Track track){
        if(track.getEventType() != null){
            EventType type = track.getEventType();
            addEventTypeToView(type,textView);
        }else {
            track.get__eventType(false, getLoopBackAdapter(), new ObjectCallback<EventType>() {
                @Override
                public void onSuccess(EventType object) {
                    addEventTypeToView(object, textView);
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : displayEventType" + t.toString())
                            .build());
                    Log.e(Constants.TAG, t.toString());
                    textView.setVisibility(View.GONE);
                }
            });
        }
    }


    private void addEventTypeToView(EventType type, TextView textView){
        if(type.getName() != null){
            if(!type.getName().isEmpty()){
                CharSequence eType = drawTextViewDesign("Event Type : ", type.getName());
                textView.setText(eType);
            }else{
                textView.setVisibility(View.GONE);
            }
        }else{
            textView.setVisibility(View.GONE);
        }
    }

    public void displayEventTypeName(final TextView textView, Track track){
        if(track.getEventType() != null){
            EventType type = track.getEventType();
            addEventTypeToViewName(type,textView);
        }else {
            track.get__eventType(false, getLoopBackAdapter(), new ObjectCallback<EventType>() {
                @Override
                public void onSuccess(EventType object) {
                    addEventTypeToViewName(object, textView);
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : displayEventType" + t.toString())
                            .build());
                    Log.e(Constants.TAG, t.toString());
                    textView.setVisibility(View.GONE);
                }
            });
        }
    }


    private void addEventTypeToViewName(EventType type, TextView textView){
        if(type.getName() != null){
            if(!type.getName().isEmpty()){
                textView.setText(type.getName());
            }else{
                textView.setVisibility(View.GONE);
            }
        }else{
            textView.setVisibility(View.GONE);
        }
    }

    public double CalculationByDistance(double initialLat, double initialLong,
                                        double finalLat, double finalLong) {
        Location locationA = new Location("point A");
        locationA.setLatitude(initialLat);
        locationA.setLongitude(initialLong);
        Location locationB = new Location("point B");
        locationB.setLatitude(finalLat);
        locationB.setLongitude(finalLong);
        double distance = locationA.distanceTo(locationB) ;
        return distance;
    }


    public void saveTrack(final Track track, final ProgressDialog progress){
        Map<String,Object> trackObj = (Map<String,Object>)track.convertMap();

        if(track.getId() != null){
            trackObj.put("id", track.getId());
            TrackRepository saveTrack = getLoopBackAdapter().createRepository(TrackRepository.class);
            saveTrack.updateAttributes((String) track.getId(), trackObj, new ObjectCallback<Track>() {
                @Override
                public void onSuccess(Track object) {
                    if(progress!= null){
                        progress.dismiss();
                    }
                    /*Toast.makeText(that, "Successfully created", Toast.LENGTH_SHORT).show();*/
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : saveTrack" + t.toString())
                            .build());
                    Log.e(Constants.TAG, t.toString());
                    if(progress!= null){
                        progress.dismiss();
                    }
                    /*Toast.makeText(that, "Data cannot be saved at this moment", Toast.LENGTH_SHORT).show();*/
                }
            });
        }else{
            track.save(new VoidCallback() {
                @Override
                public void onSuccess() {
                    if(progress!= null){
                        progress.dismiss();
                    }
                    /*Toast.makeText(that, "Successfully created", Toast.LENGTH_SHORT).show();*/
                }

                @Override
                public void onError(Throwable t) {
                    mainActivity.tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Exception")
                            .setAction("Fragment : MainActivity, Method : saveTrack2" + t.toString())
                            .build());
                    Log.e(Constants.TAG, t.toString());
                    if(progress!= null){
                        progress.dismiss();
                    }
                    /*Toast.makeText(that, "Data cannot be saved at this moment", Toast.LENGTH_SHORT).show();*/
                }
            });
        }
    }


    public Track saveTrack(Map<String, Object> trackObj,final ProgressDialog progress){
        TrackRepository saveTrack = getLoopBackAdapter().createRepository(TrackRepository.class);
        final boolean saveData;
        if(trackObj.get("id") == null){
            trackObj.remove("id");
            saveData = true;
        }else{
            saveData = false;
        }

        final Track tempTrack = saveTrack.createObject(trackObj);


        saveTrack.upsert(trackObj, new ObjectCallback<Track>() {
            @Override
            public void onSuccess(Track object) {

                tempTrack.setId(object.getId());
                if (saveData){
                    if(progress != null){
                        progress.dismiss();
                    }
                    Toast.makeText(that, "Successfully created", Toast.LENGTH_SHORT).show();
                }else{
                    if(progress != null){
                        progress.dismiss();
                    }
                    Toast.makeText(that, "Successfully updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                if(progress != null){
                    progress.dismiss();
                }
                Toast.makeText(that, "Data cannot be saved at this moment", Toast.LENGTH_SHORT).show();
                mainActivity.tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Exception")
                        .setAction("Fragment : MainActivity, Method : saveTrack(2Arguments)"+t.toString())
                        .build());
                Log.e(Constants.TAG, t.toString());
            }
        });

        return tempTrack;
    }


    public CharSequence drawTextViewDesign(String constant, String data) {
        SpannableString spannableString =  new SpannableString(constant);
        SpannableString spannableString2 =  new SpannableString(data);
        spannableString.setSpan(new ForegroundColorSpan(Color.rgb(237, 97, 116)),0,constant.length(),0);
        spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, constant.length(), 0);
        spannableString.setSpan(new RelativeSizeSpan(1.1f), 0, constant.length(), 0);
        CharSequence result = (TextUtils.concat(spannableString, " ", spannableString2));
        return result;
    }


    public String formatNumber(String contactNumberDataString){
        String pattern = "\\+\\d{12,12}";
        String checkZero = "^0\\d+";
        // Now create matcher object.

        contactNumberDataString = contactNumberDataString.replaceAll("\\s","");
        boolean match = contactNumberDataString.matches(pattern);
        if (!match) {
            boolean isZero = contactNumberDataString.matches(checkZero);
            if(isZero){
                String number = contactNumberDataString.substring(1);
                if(number.length() > 9){
                    number = "+91" + number;
                    contactNumberDataString = number;
                }
            }else{
                if(contactNumberDataString.length() > 9){
                    contactNumberDataString = "+91" + contactNumberDataString;
                }
            }
        }
        return contactNumberDataString;
    }

    public String parseDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("IST"));
        java.util.Date date_ = null;
        try {
            date_ = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Now parsing time..

        Log.v(Constants.TAG, date_.toString());
        String orderDay = date_.toString().substring(8, 10);
        String orderMonth = date_.toString().substring(4, 7);

        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(date_.toString());
        String orderYear = "";
        while (m.find()) {
            orderYear = m.group().toString();
        }

        //String orderYear = date_.toString().substring(30, 34);

        String actualDate = orderDay + " " + orderMonth.toUpperCase()+ " "+ orderYear;
        Log.v(Constants.TAG, actualDate);
        return actualDate;
    }



    private void startService(){
        //Now set filter for showing only nearby events at start..
        setNearByEventFilter();
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }


    public void setOnlySharedEventsFilter(){
        setNearByLocationFilter();
        Map<String, Object> where = new HashMap<>();
        if(BackgroundService.getCustomer() != null){
            //First clear the where of track collection..
            if(TrackCollection.getEventFilter() != null){
                TrackCollection.getEventFilter().put("where", where);
                if(BackgroundService.getCustomer().getPhoneNumber() != null){
                    where.put("friends.number", BackgroundService.getCustomer().getPhoneNumber());
                }
                //Now only allow status of allowed event to view..
                where.put("status", "allow");
            }
        }
        TrackCollection.setFilterColor(Constants.SHARED_EVENTS);
    }


    public void setNearByEventFilter(){
        setNearByLocationFilter();
        Map<String, Object> where = new HashMap<>();
        //Add nearby
        if(BackgroundService.getCurrentLocation() != null){
            /*
            * where: {
                location: {near: '153.536,-28'}
            }*/
            //First clear the where of track collection..
            if(TrackCollection.getEventFilter() != null){
                TrackCollection.getEventFilter().put("where", where);
                Map<String, String> near = new HashMap<>();
                near.put("near", "" + BackgroundService.getCurrentLocation().latitude+","+ BackgroundService.getCurrentLocation().longitude);
                where.put("geolocation", near);
                //Now only allow status of allowed event to view..
                where.put("status", "allow");
                where.put("isPublic", "public");
            }
        }
        TrackCollection.setFilterColor(Constants.NEAR_BY);
    }


    public void showMyEventFilter(){
        setNearByLocationFilter();
        Map<String, Object> where = new HashMap<>();
        //Add nearby
        if(BackgroundService.getCustomer() != null){
            //First clear the where of track collection..
            if(TrackCollection.getEventFilter() != null){
                TrackCollection.getEventFilter().put("where", where);
                where.put("status","allow");
                where.put("customerId", BackgroundService.getCustomer().getId());
            }
        }
        TrackCollection.setFilterColor(Constants.MY_EVENTS);
    }

    public void showMyLocationFilter(){
        Map<String, Object> where = new HashMap<>();
        //Add nearby
        if(BackgroundService.getCustomer() != null){
            //First clear the where of track collection..
            if(TrackCollection.getLocationFilter() != null){
                TrackCollection.getLocationFilter().put("where", where);
                where.put("customerId", BackgroundService.getCustomer().getId());
            }
        }
        TrackCollection.setFilterColor(Constants.MY_LOCATION);
    }

    public void setNearByLocationFilter() {
        Map<String, Object> where = new HashMap<>();
        //Add nearby
        if(BackgroundService.getCurrentLocation() != null){
            /*
            * where: {
                location: {near: '153.536,-28'}
            }*/
            //First clear the where of track collection..
            if(TrackCollection.getLocationFilter() != null){
                TrackCollection.getLocationFilter().put("where", where);
                Map<String, String> near = new HashMap<>();
                near.put("near", "" + BackgroundService.getCurrentLocation().latitude+","+ BackgroundService.getCurrentLocation().longitude);
                where.put("geolocation", near);
                //Now only allow status of allowed event to view..
                where.put("status", "allow");
                where.put("isPublic", "public");
            }
        }
    }

    public String changeToUpperCase(String source) {
        String cap = source.substring(0, 1).toUpperCase() + source.substring(1);
        return cap;
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You want to exit MapsTrack?");

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else {
            getSupportFragmentManager().popBackStack();
        }

    }



}


//ON UPDATING SDK FOLLOWING FILE SHOULD BE TAKEN INTO CONSIDERATION
//Model.java {Loopback}
//ModelRepository.java {Loopback}
//CustomModel.java {APP}