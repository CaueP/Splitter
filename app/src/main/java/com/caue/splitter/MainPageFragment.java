package com.caue.splitter;

//import android.app.Fragment;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.OnClick;

/**
 * Created by Caue on 9/5/2016.
 */
public class MainPageFragment extends Fragment {

    View rootView = null;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static MainPageFragment newInstance(int sectionNumber) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainPageFragment(){
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
            case R.id.main_page_fragment:
                rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
                break;
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.mainPageTitle);
    }

}
