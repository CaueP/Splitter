package com.caue.splitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Produto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaueGarciaPolimanti on 4/30/2017.
 */

public class MenuFragment extends Fragment {

    private static final String MENU_FRAGMENT_TAG = "MenuFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        Bundle bundle = getArguments();
        if(bundle != null) {
            Log.d(MENU_FRAGMENT_TAG, bundle.getString(Constants.KEY.CARDAPIO_DATA));
            ArrayList<Produto> cardapio = (ArrayList<Produto>) new Gson().fromJson(bundle.getString(Constants.KEY.CARDAPIO_DATA), ArrayList.class);
        }

        View rootView = inflater.inflate(R.layout.fragment_checkedin_menu, container, false);


        return rootView;
    }
}
