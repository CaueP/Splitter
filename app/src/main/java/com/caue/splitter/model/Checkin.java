package com.caue.splitter.model;

import android.util.Log;

import com.caue.splitter.MainPageActivity;
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.model.services.CheckinClient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Caue
 * @version 1.0
 * @date 14/04/2017
 */

public class Checkin implements Serializable {
    @Expose
    @SerializedName("usuario")
    private Usuario usuario;

    @Expose
    @SerializedName("mesa")
    private Mesa mesa = new Mesa();

    @Expose
    @SerializedName("isPrimeiroUsuario")
    private boolean isPrimeiroUsuario;

    @Expose
    @SerializedName("isSucesso")
    private boolean isSucesso;

    @Expose
    @SerializedName("error")
    private String error;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isPrimeiroUsuario() {
        return isPrimeiroUsuario;
    }

    public void setPrimeiroUsuario(boolean primeiroUsuario) {
        isPrimeiroUsuario = primeiroUsuario;
    }

    public boolean isSucesso() {
        return isSucesso;
    }

    public void setSucesso(boolean sucesso) {
        isSucesso = sucesso;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    Checkin resposta;

    public void realizarCheckin(final MainPageActivity activity){
        Checkin checkinResponse = new Checkin();

        // criação do serviço para realizar operações na conta do usuário
        CheckinClient service = ServiceGenerator.createService(CheckinClient.class);
        Log.d("carregarUsuario", "Carregando usuario");
        // Service para baixar objeto com o usuário
        Call<Checkin> checkinCall = service.realizarCheckin(this);

        // callback ao receber a resposta da API
        Callback<Checkin> realizarCheckinCallback = new Callback<Checkin>() {

            @Override
            public void onResponse(Call<Checkin> call, Response<Checkin> response) {
                Log.d("onResponse", "entered in onResponse - Checkin");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    resposta = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    activity.responseCheckinReceived(resposta);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Checkin> call, Throwable t) {
                Log.d("onFailure","Ocorreu um erro ao chamar a API - Checkin");

            }
        };

        // call
        checkinCall.enqueue(realizarCheckinCallback);
    }
}

