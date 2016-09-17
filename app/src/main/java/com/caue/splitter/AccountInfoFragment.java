package com.caue.splitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caue.splitter.data.UserDataJson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Caue on 9/13/2016.
 */
public class AccountInfoFragment extends Fragment {
    @BindView(R.id.user_profile_picture) ImageView userPicture;
    @BindView(R.id.edittext_user_reg_name) EditText name;
    @BindView(R.id.edittext_user_reg_email) EditText email;
    @BindView(R.id.edittext_user_reg_pass) EditText password;
    @BindView(R.id.edittext_user_reg_phone) EditText phone;
    @BindView(R.id.edittext_user_reg_cpf) EditText cpf;
    @BindView(R.id.edittext_user_reg_dob) EditText dateOfBirth;
    Button btnDeleteAccount;

    HashMap userData = null;

    View rootView = null;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static AccountInfoFragment newInstance(int sectionNumber, Bundle userData) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable("UserData",(HashMap)userData.get("UserData"));
        fragment.setArguments(args);
        return fragment;
    }

    public AccountInfoFragment(){
    }

    // set the layout
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        Log.d("AccountInfoFragment", "AccountInfoFragment just entered in its onCreateView");

        // get arguments
        //int option = getArguments().getInt(ARG_SECTION_NUMBER);

        // loads the fragment selected (or the restored one)
        //switch (option){
            //case R.id.account_info_fragment:
                rootView = inflater.inflate(R.layout.fragment_account_info, container, false);
              //  break;
        //}
        ButterKnife.bind(this, rootView);

        // get user data
        userData = (HashMap) getArguments().getSerializable("UserData");
        UserDataJson user = new UserDataJson(userData);
        Log.d("UserRegActivity", user.toString());
        // set user data
        if(userData != null) {
            name.setText((String) userData.get("name"));
            email.setText((String) userData.get("login"));
            phone.setText(String.valueOf((Long)userData.get("phone")));
            cpf.setText(String.valueOf((Long)userData.get("cpf")));
            dateOfBirth.setText((String) userData.get("dateOfBirth"));
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Uri userProfilePictureUri = null;
        if(firebaseUser != null)
                userProfilePictureUri = firebaseUser.getPhotoUrl();
        if (userProfilePictureUri != null) {
            Glide.with(this)
                    .load(userProfilePictureUri)
                    .fitCenter()
                    .into(userPicture);
        }



        // findView

        btnDeleteAccount = (Button) rootView.findViewById(R.id.delete_account);

        // create mListener to do the button actions in the activities
        final OnFragmentInteractionListener mListener;
        try{
            mListener = (OnFragmentInteractionListener) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the Fragment" +
                    "forgot to implement OnFragmentInteractionListener");
        }

        btnDeleteAccount.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("AccountInfoFragment", "Delete Button Clicked");
                mListener.OnDeleteButtonCliked();
            }
        });

        return rootView;
    }

    // Interactions ocurring on AccountInfoFragment
    public interface OnFragmentInteractionListener{
        void OnDeleteButtonCliked();
    }

    // inflate the menu for the fragment and define the actions
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //if(menu.findItem(R.id.action_share) == null)
        inflater.inflate(R.menu.menu_account_info_fragment, menu);    // add action items inside fragment

        MenuItem saveItem = menu.findItem(R.id.action_save);
        Log.d("AccountInfoFragment", "Save Button Clicked");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.accountInfoTitle);
    }
}
