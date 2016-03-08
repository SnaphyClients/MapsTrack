package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Database.ProfileDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.EditProfileModel;
import com.snaphy.mapstrack.R;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        List<ProfileDatabase> profileInfo = new Select().from(ProfileDatabase.class).execute();
//        name.setText(profileInfo.get(0).name);
        //email.setText(profileInfo.get(0).emailId);
        //imageLoader.displayImage(profileInfo.get(0).pictureUrl, profilePicture);
        return view;
    }

    @OnClick(R.id.fragment_profile_button1) void logoutButton() {
        EventBus.getDefault().post("logout", Constants.LOGOUT);
    }

    @OnClick(R.id.fragment_profile_button2) void editProfile() {
        EditProfileModel editProfileModel = new EditProfileModel();
        String nameArray[] = name.getText().toString().split(" ");

        if(!nameArray[0].isEmpty()) {
            editProfileModel.setFirstName(nameArray[0]);
            if(!nameArray[1].isEmpty()) {
                editProfileModel.setLastName(nameArray[1]);
            }
        }

        editProfileModel.setMobileNumber(phone.getText().toString());

        if(profilePicture.getDrawable() != null) {
            editProfileModel.setImage(profilePicture.getDrawable());
        }
        EventBus.getDefault().postSticky(editProfileModel, Constants.REQUEST_EDIT_PROFILE_FRAGMENT);
        mainActivity.replaceFragment(R.layout.fragment_edit_profile, null);
    }

    @Subscriber ( tag = Constants.RESPONSE_EDIT_PROFILE_FRAGMENT)
    public void saveEditedData(EditProfileModel editProfileModel) {
        name.setText(editProfileModel.getFirstName().toString() +" "+ editProfileModel.getLastName().toString());
        phone.setText(editProfileModel.getMobileNumber());
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
}
