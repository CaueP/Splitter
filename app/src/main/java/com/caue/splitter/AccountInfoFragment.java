package com.caue.splitter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.OnClick;

/**
 * Created by Caue on 9/13/2016.
 */
public class AccountInfoFragment extends Fragment {


    Button btnDeleteAccount;

    View rootView = null;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static AccountInfoFragment newInstance(int sectionNumber) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountInfoFragment(){
    }

    // set the layout
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        Log.d("FrontPageFragment", "FrontPageFragment just entered in its onCreateView");

        int option = getArguments().getInt(ARG_SECTION_NUMBER);    // save the arguments to
        // restore the fragments opened on this Fragment


        // loads the fragment selected (or the restored one)
        switch (option){
            case R.id.account_info_fragment:
                rootView = inflater.inflate(R.layout.fragment_account_info, container, false);
                break;
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



}
