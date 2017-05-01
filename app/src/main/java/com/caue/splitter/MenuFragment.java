package com.caue.splitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caue.splitter.controller.MenuAdapter;
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

    ArrayList<Produto> mListaProduto = null;
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_checkedin_menu, container, false);

        // find views
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        Bundle bundle = getArguments();
        if(bundle != null) {
            //Log.d(MENU_FRAGMENT_TAG, bundle.getString(Constants.KEY.CARDAPIO_DATA));
            //mListaProduto = (ArrayList<Produto>) new Gson().fromJson(bundle.getString(Constants.KEY.CARDAPIO_DATA), ArrayList.class);
            //mListaProduto = (ArrayList<Produto>) new Gson().fromJson(bundle.getString(Constants.KEY.CARDAPIO_DATA), ArrayList.class);
            mListaProduto = (ArrayList<Produto>) bundle.getSerializable(Constants.KEY.CARDAPIO_DATA);
            if(mMenuAdapter != null){
                mMenuAdapter = null;
            }
            setDataRecyclerViewer();
        } else{
            Log.d(MENU_FRAGMENT_TAG, "Bundle com lista de produtos vazia");
        }


        return rootView;
    }

    private void setDataRecyclerViewer() {
        mMenuAdapter = new MenuAdapter(mListaProduto);
        mRecyclerView.setAdapter(mMenuAdapter);
    }

}
