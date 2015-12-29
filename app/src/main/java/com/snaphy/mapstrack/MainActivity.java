package com.snaphy.mapstrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.snaphy.mapstrack.Fragment.AboutusFragment;
import com.snaphy.mapstrack.Fragment.ContactFragment;
import com.snaphy.mapstrack.Fragment.CreateEventFragment;
import com.snaphy.mapstrack.Fragment.CreateLocationFragment;
import com.snaphy.mapstrack.Fragment.FaqsFragment;
import com.snaphy.mapstrack.Fragment.HomeFragment;
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

public class MainActivity extends AppCompatActivity implements OnFragmentChange,
        MainFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener, AboutusFragment.OnFragmentInteractionListener,
        FaqsFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,
        TermsFragment.OnFragmentInteractionListener, CreateEventFragment.OnFragmentInteractionListener,
        CreateLocationFragment.OnFragmentInteractionListener, ShowContactFragment.OnFragmentInteractionListener,
        ShowMapFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener
{

    //TODO 1. Make all list work in home fragment
    //TODO 2. Make menu items work in home fragment
    //TODO 3. Make Login Page with google login
    //TODO 4. Make Share fragment dynamic (to delete contact list)
    //TODO 6. Open Contacts and display in app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getBaseContext(), BackgroundService.class));
        // TODO Stop service in activity on destory method if required
        replaceFragment(R.layout.fragment_main, null);
    }

   /* public void onEvent(LoginEvent event){
        if(event.isLogin()) {
            replaceFragment(R.layout.fragment_main, null);
        } else {

        }
    }*/

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

            case R.id.fragment_cart_floating_button1 :
                createEvent(fragmentTransaction);
                break;

            case R.id.fragment_cart_floating_button2:
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
        fragmentTransaction.replace(R.id.main_container, showContactFragment, ShowContactFragment.TAG).addToBackStack(null);
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



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
