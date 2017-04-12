package com.caue.splitter;

//import android.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MainPageFragment é o conteúdo da activity principal do app. Tem o objetivo de controlar o Menu Drawer e as ações realizadas em todos os fragments
 * @author Caue Polimanti
 * @version 1.0
 * Created on 9/5/2016.
 */
public class MainPageFragment extends Fragment {

    Button scanBtn;
    ConnectivityManager cm;
    NetworkInfo netInfo;

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
        Log.d("MainPageFragment", "MainPageFragment just entered in its onCreateView");

        int option = getArguments().getInt(ARG_SECTION_NUMBER);    // save the arguments to
        // restore the fragments opened on this Fragment


        // loads the fragment selected (or the restored one)
        rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        ButterKnife.bind(this, rootView);

        // setting scan QR Code action
        final Activity activity = getActivity();

        // Buttons findView
        scanBtn = (Button) rootView.findViewById(R.id.scan_qr_code_button);


        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checking connectivity
                cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getActiveNetworkInfo();

                if(netInfo != null && netInfo.isConnected()){
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt(getResources().getString(R.string.aim_to_qrcode));
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                    integrator.setOrientationLocked(true);
                } else{
                    Toast.makeText(rootView.getContext(),R.string.check_internet_connection, Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.mainPageTitle);
    }

}
