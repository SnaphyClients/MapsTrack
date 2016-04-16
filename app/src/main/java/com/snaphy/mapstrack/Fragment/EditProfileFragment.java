package com.snaphy.mapstrack.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.EditProfileModel;
import com.snaphy.mapstrack.Model.ImageModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.Services.BackgroundService;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public static String TAG = "EditProfileFragment";
    @Bind(R.id.fragment_edit_profile_image_button0) ImageButton backButton;
    @Bind(R.id.fragment_edit_profile_picture) CircleImageView imageView;
    @Bind(R.id.fragment_edit_profile_edittext1) EditText firstName;
    @Bind(R.id.fragment_edit_profile_edittext2) EditText lastName;
    @Bind(R.id.fragment_edit_profile_edittext3) EditText mobileNumber;
    @Bind(R.id.fragment_edit_profile_button1)
    Button saveButton;
    File editedImage;
    View rootview;



    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        rootview = view;
        saveButtonClickListener();
        imageViewClickListener();
        backButtonClickListener();
        EasyImage.configuration(mainActivity)
                .setImagesFolderName("MapsTrack")
                .saveInRootPicturesDirectory()
                .setCopyExistingPicturesToPublicLocation(true);
        return view;
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

    public boolean validate() {
        // Implement validate
        boolean isValidate = false;
        if(!isNameValid(firstName.getText().toString())) {
            Snackbar.make(rootview, "Invalid First Name", Snackbar.LENGTH_SHORT).show();
        } else if(!isNameValid(lastName.getText().toString())) {
            Snackbar.make(rootview, "Invalid Last Name", Snackbar.LENGTH_SHORT).show();
        }  else {
            isValidate = true;
        }
        return isValidate;
    }

    boolean isNameValid(String name) {
        return name.matches("^[a-zA-Z ]{3,30}$");
    }

    public void saveButtonClickListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                EditProfileModel editProfileModel = new EditProfileModel();
                editProfileModel.setFirstName(firstName.getText().toString());
                editProfileModel.setLastName(lastName.getText().toString());
                editProfileModel.setImage(imageView.getDrawable());
                EventBus.getDefault().post(editProfileModel, Constants.RESPONSE_EDIT_PROFILE_FRAGMENT);
                uploadImageAndUpdateData();
                mainActivity.onBackPressed();

            }
        });
    }


    public void uploadImageAndUpdateData(){
        BackgroundService.getCustomer().setFirstName(firstName.getText().toString());
        BackgroundService.getCustomer().setLastName(lastName.getText().toString());
        if(editedImage != null){
            mainActivity.uploadWithCallback(Constants.CONTAINER, editedImage, new ObjectCallback<ImageModel>() {
                @Override
                public void onSuccess(ImageModel object) {
                    if(object != null){
                        BackgroundService.getCustomer().setProfilePic(object.getHashMap());
                    }

                    //Now save the data..
                    saveData();

                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constants.TAG, t.toString());
                }
            });
        }else{
            //just update the data..
            saveData();
        }



    }


    private void saveData(){
        mainActivity.updateCustomer(BackgroundService.getCustomer());
    }






    public void imageViewClickListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage();
            }
        });

    }

    public void backButtonClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

    }

    @OnClick(R.id.fragment_edit_profile_edittext3) void changeMobileNumber() {
        //mainActivity.replaceFragment(R.id.fragment_edit_profile_edittext3, null);
    }

    @Subscriber ( tag = Constants.REQUEST_EDIT_PROFILE_FRAGMENT)
    public void fetchProfile(EditProfileModel editProfileModel) {
        if(!editProfileModel.getFirstName().isEmpty()) {
            firstName.setText(editProfileModel.getFirstName());
            if(!editProfileModel.getLastName().isEmpty()) {
                lastName.setText(editProfileModel.getLastName());
            }
        }

        /*if(!editProfileModel.getMobileNumber().isEmpty()) {
            mobileNumber.setText(editProfileModel.getMobileNumber());
        }*/

        if(editProfileModel.getImage() != null) {
            imageView.setImageDrawable(editProfileModel.getImage());
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setImage() {

        int permissionCheck = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            EasyImage.openDocuments(this);

        } else {
            Nammu.askForPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    EasyImage.openGallery(mainActivity);
                }

                @Override
                public void permissionRefused() {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, mainActivity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handle the image

                final String uri = Uri.fromFile(imageFile).toString();
                String decoded = Uri.decode(uri);
                Glide.with(mainActivity).load(decoded).into(imageView);
                editedImage = imageFile;
            }

        });
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
