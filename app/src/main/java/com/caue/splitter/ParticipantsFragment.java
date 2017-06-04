package com.caue.splitter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caue.splitter.controller.MenuAdapter;
import com.caue.splitter.controller.ParticipantAdapter;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Participante;
import com.caue.splitter.model.Produto;

import java.util.ArrayList;

/**
 * @author Cauê Garcia Polimanti
 * @version 1.0
 * Created on 6/4/2017.
 */

public class ParticipantsFragment extends Fragment {

    private static final String TAG = "ParticipantsFragment";
    View rootView;

    //views
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ParticipantAdapter mParticipantAdapter;

    //data
    ArrayList<Participante> mListaParticipants = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_participants, container, false);

        mListaParticipants = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mListaParticipants.add(new Participante("Nome " + i, "http://www.otvfoco.com.br/wp-content/uploads/2017/04/emilly-foto-e1492962693785.jpg"));
        }

        initView();

        return rootView;
    }

    private void initView(){
        // find views
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_participants);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        if (mParticipantAdapter != null) {
            mParticipantAdapter = null;
        }

        setDataRecyclerViewer();

        try{
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_participant);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPurplePrimary, R.color.colorPurpleBase, R.color.colorPurpleAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2300);
            }
        });
        } catch (Exception ex) {
            Log.e(TAG, "Error on swipeRefreshLayout: " + ex.getMessage());
        }
    }

    private void setDataRecyclerViewer() {
        mParticipantAdapter = new ParticipantAdapter(mListaParticipants);
        mRecyclerView.setAdapter(mParticipantAdapter);
    }
}