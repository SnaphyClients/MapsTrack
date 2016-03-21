package com.snaphy.mapstrack.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaphy.mapstrack.Adapter.ShowContactAdapter;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Database.TemporaryContactDatabase;
import com.snaphy.mapstrack.MainActivity;
import com.snaphy.mapstrack.Model.ContactModel;
import com.snaphy.mapstrack.R;
import com.snaphy.mapstrack.RecyclerItemClickListener;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowContactFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "ShowContactFragment";
    @Bind(R.id.fragment_show_contact_recycler_view1) RecyclerView recyclerView;
    ShowContactAdapter showContactAdapter;
    TemporaryContactDatabase temporaryContactDatabase;
    MainActivity mainActivity;



    public ShowContactFragment() {
        // Required empty public constructor
    }

    public static ShowContactFragment newInstance() {
        ShowContactFragment fragment = new ShowContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temporaryContactDatabase = new TemporaryContactDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_contact, container, false);
        ButterKnife.bind(this, view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        showContactAdapter = new ShowContactAdapter(mainActivity,mainActivity.contactModelArrayList);
        recyclerView.setAdapter(showContactAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContactModel contactModel = mainActivity.contactModelArrayList.get(position);

                        if(contactModel.isSelected()) {
                            contactModel.setIsSelected(false);
                            showContactAdapter.notifyDataSetChanged();
                        }
                        else {
                            contactModel.setIsSelected(true);
                            showContactAdapter.notifyDataSetChanged();
                            temporaryContactDatabase.name = contactModel.getContactName();
                            temporaryContactDatabase.number = contactModel.getContactNumber();
                            temporaryContactDatabase.save();

                        }

                    }
                })
        );

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick(R.id.fragment_show_contact_imagebutton1) void backButton() {
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_show_contact_button1) void contactSelected() {
        mainActivity.onBackPressed();
        EventBus.getDefault().post(mainActivity.contactModelArrayList, Constants.SEND_SELECTED_CONTACT_TO_CREATE_EVENT_FRAGMENT);
        EventBus.getDefault().post(mainActivity.contactModelArrayList, Constants.SEND_SELECTED_CONTACT_TO_CREATE_LOCATION_FRAGMENT);
        EventBus.getDefault().post(mainActivity.contactModelArrayList, Constants.ADD_CONTACTS_IN_SHARE_LOCATION);
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }




}
