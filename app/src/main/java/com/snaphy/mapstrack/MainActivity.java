package com.snaphy.mapstrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.snaphy.mapstrack.Fragment.AboutusFragment;
import com.snaphy.mapstrack.Fragment.ContactFragment;
import com.snaphy.mapstrack.Fragment.CreateEventFragment;
import com.snaphy.mapstrack.Fragment.CreateLocationFragment;
import com.snaphy.mapstrack.Fragment.EventInfoFragment;
import com.snaphy.mapstrack.Fragment.FaqsFragment;
import com.snaphy.mapstrack.Fragment.HomeFragment;
import com.snaphy.mapstrack.Fragment.LocationInfoFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFragment;
import com.snaphy.mapstrack.Fragment.LocationShareByUserFriendsFragment;
import com.snaphy.mapstrack.Fragment.LoginFragment;
import com.snaphy.mapstrack.Fragment.MainFragment;
import com.snaphy.mapstrack.Fragment.ProfileFragment;
import com.snaphy.mapstrack.Fragment.SettingFragment;
import com.snaphy.mapstrack.Fragment.ShareFragment;
import com.snaphy.mapstrack.Fragment.ShowContactFragment;
import com.snaphy.mapstrack.Fragment.ShowMapFragment;
import com.snaphy.mapstrack.Fragment.TermsFragment;
import com.snaphy.mapstrack.Interface.OnFragmentChange;
import com.snaphy.mapstrack.Services.BackgroundService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

public class MainActivity extends AppCompatActivity implements OnFragmentChange,
        MainFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener, AboutusFragment.OnFragmentInteractionListener,
        FaqsFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,
        TermsFragment.OnFragmentInteractionListener, CreateEventFragment.OnFragmentInteractionListener,
        CreateLocationFragment.OnFragmentInteractionListener, ShowContactFragment.OnFragmentInteractionListener,
        ShowMapFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        EventInfoFragment.OnFragmentInteractionListener, LocationInfoFragment.OnFragmentInteractionListener,
        LocationShareByUserFragment.OnFragmentInteractionListener, LocationShareByUserFriendsFragment.OnFragmentInteractionListener
{

    //TODO 1. Make Contacts Selected if they are selected and show invite button
    //TODO 2. On Publish Event remove all fragment from back stack
    //TODO 5. Delete Button in contacts in events and location
    //TODO 4. Implement Search
    //TODO 6. Clear all the fields in CreateEvent Fragment and CreateLocation

    //TODO 3. Validation and Verify Number


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
        startService(new Intent(getBaseContext(), BackgroundService.class));
        // TODO Stop service in activity on destory method if required

    }

    @Subscriber(tag = Constants.IS_LOGIN)
    private void isLogin(boolean login) {
        if(login == true){
            Log.v(Constants.TAG, "I am Login");
            replaceFragment(R.layout.fragment_main, null);
        } else {
            replaceFragment(R.layout.fragment_login, null);
        }
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




    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
