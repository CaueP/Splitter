package com.caue.splitter;

import android.content.Context;
import android.content.Intent;
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
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Mesa;
import com.caue.splitter.model.Participante;
import com.caue.splitter.model.Produto;
import com.caue.splitter.model.Usuario;
import com.caue.splitter.model.services.MesaClient;
import com.caue.splitter.model.services.UsuarioClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Cauê Garcia Polimanti
 * @version 1.0
 *          Created on 6/4/2017.
 */

public class ParticipantsFragment extends Fragment {

    private static final String TAG = "ParticipantsFragment";
    View rootView;

    //views
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ParticipantAdapter mParticipantAdapter;

    //data
    Mesa mesa = null;
    ArrayList<Participante> mListaParticipants = null;

    // criação do serviço
    MesaClient service = ServiceGenerator.createService(MesaClient.class);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_participants, container, false);

//        mListaParticipants = new ArrayList<>();

        initView();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mesa = (Mesa) bundle.getSerializable(Constants.KEY.MESA_DATA);
            carregarListaParticipantes(mesa);
        } else {
            Log.d(TAG, "Bundle com a mesa vazia");
        }

//        for (int i = 0; i < 10; i++) {
//            mListaParticipants.add(new Participante("Nome " + i, "http://www.otvfoco.com.br/wp-content/uploads/2017/04/emilly-foto-e1492962693785.jpg"));
//        }

        return rootView;
    }

    private void initView() {
        // find views
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_participants);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        try {
            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_participant);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPurplePrimary, R.color.colorPurpleBase, R.color.colorPurpleAccent);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mesa != null) carregarListaParticipantes(mesa);
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "Error on swipeRefreshLayout: " + ex.getMessage());
        }
    }

    private void setDataRecyclerViewer() {
        if (mParticipantAdapter != null) {
            mParticipantAdapter.setItems(mListaParticipants);
        } else {
            mParticipantAdapter = new ParticipantAdapter(mListaParticipants);
            mRecyclerView.setAdapter(mParticipantAdapter);
        }

    }

    private void carregarListaParticipantes(Mesa mesa) {
        Log.d(TAG, "Carregando lista de participantes");
        Call<ArrayList<Participante>> listCall = service.consultarParticipante(mesa.getCodEstabelecimento(), mesa.getNrMesa());

        // callback ao receber a resposta da API
        Callback<ArrayList<Participante>> carregaUsuarioCallback = new Callback<ArrayList<Participante>>() {

            @Override
            public void onResponse(Call<ArrayList<Participante>> call, Response<ArrayList<Participante>> response) {
                Log.d(TAG, "entered in onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "isSuccessful");
                    mListaParticipants = response.body();
                    setDataRecyclerViewer();
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    if (response.code() == 404) {    // usuario nao cadastrado
                        Log.d(TAG, "Parâmetros incorretos" + response.body());
                    }

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Participante>> call, Throwable t) {
                Log.e(TAG, "onFailure - Ocorreu um erro ao chamar a API");
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        // call
        listCall.enqueue(carregaUsuarioCallback);
    }
}