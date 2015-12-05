package com.snaphy.mapstrack;

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
import com.snaphy.mapstrack.Fragment.MainFragment;
import com.snaphy.mapstrack.Fragment.ProfileFragment;
import com.snaphy.mapstrack.Fragment.SettingFragment;
import com.snaphy.mapstrack.Fragment.ShareFragment;
import com.snaphy.mapstrack.Fragment.TermsFragment;
import com.snaphy.mapstrack.Interface.OnFragmentChange;

public class MainActivity extends AppCompatActivity implements OnFragmentChange,
        MainFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener, AboutusFragment.OnFragmentInteractionListener,
        FaqsFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,
        TermsFragment.OnFragmentInteractionListener, CreateEventFragment.OnFragmentInteractionListener,
        CreateLocationFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(R.layout.fragment_main,null);
    }

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
        }
    }

    private void loadMainFragment(FragmentTransaction fragmentTransaction) {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().
                findFragmentByTag(MainFragment.TAG);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, mainFragment, MainFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openAboutUsPage(FragmentTransaction fragmentTransaction) {
        AboutusFragment aboutusFragment = (AboutusFragment) getSupportFragmentManager().
                findFragmentByTag(AboutusFragment.TAG);
        if (aboutusFragment == null) {
            aboutusFragment = AboutusFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, aboutusFragment, AboutusFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openFaqPage(FragmentTransaction fragmentTransaction) {
        FaqsFragment faqsFragment = (FaqsFragment) getSupportFragmentManager().
                findFragmentByTag(FaqsFragment.TAG);
        if (faqsFragment == null) {
            faqsFragment = FaqsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, faqsFragment, FaqsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openContactPage(FragmentTransaction fragmentTransaction) {
        ContactFragment contactFragment = (ContactFragment) getSupportFragmentManager().
                findFragmentByTag(ContactFragment.TAG);
        if (contactFragment == null) {
            contactFragment = ContactFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, contactFragment, ContactFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openTermsPage(FragmentTransaction fragmentTransaction) {
        TermsFragment termsFragment = (TermsFragment) getSupportFragmentManager().
                findFragmentByTag(TermsFragment.TAG);
        if (termsFragment == null) {
            termsFragment = TermsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, termsFragment, TermsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void createEvent(FragmentTransaction fragmentTransaction) {
        CreateEventFragment createEventFragment = (CreateEventFragment) getSupportFragmentManager().
                findFragmentByTag(CreateEventFragment.TAG);
        if (createEventFragment == null) {
            createEventFragment = CreateEventFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, createEventFragment, CreateEventFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void createLocation(FragmentTransaction fragmentTransaction) {
        CreateLocationFragment createLocationFragment = (CreateLocationFragment) getSupportFragmentManager().
                findFragmentByTag(CreateLocationFragment.TAG);
        if (createLocationFragment == null) {
            createLocationFragment = CreateLocationFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, createLocationFragment, CreateLocationFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
