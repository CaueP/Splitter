package com.caue.splitter.model;

import android.util.Log;

import com.caue.splitter.BillPaymentFragment;
import com.caue.splitter.OrderFragment;
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.model.services.ContaClient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Caue Polimanti
 * @version 2.0
 * Created on 5/20/2017.
 */
public class Conta implements Serializable {

    private String codEstabelecimento;
    private int codComanda;
    private int nrMesa;

    @Expose
    @SerializedName("total_mesa")
    private float totalMesa;

    @Expose
    @SerializedName("total_individual")
    private float totalIndividual;

    @Expose
    @SerializedName("pedidos")
    private ArrayList<Pedido> pedidos;


    // construtor
    public Conta(String codEstabelecimento, int codComanda, int nrMesa){
        this.codEstabelecimento = codEstabelecimento;
        this.codComanda = codComanda;
        this.nrMesa = nrMesa;
    }

    public float getTotalMesa() {
        return totalMesa;
    }

    public void setTotalMesa(float totalMesa) {
        this.totalMesa = totalMesa;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public float getTotalIndividual() {
        return totalIndividual;
    }

    public void setTotalIndividual(float totalIndividual) {
        this.totalIndividual = totalIndividual;
    }


    /**
     * Consultar os pedidos e o totalMesa de uma comanda
     * @param fragment activity que receberá a resposta da REST API
     */
    public void consultar(final OrderFragment fragment) {
        Log.d("Conta", "Consultar conta");
        ContaClient service = ServiceGenerator.createService(ContaClient.class);
        Call<Conta> contaCall = service.consultarConta(this.codEstabelecimento, this.codComanda);

        Callback<Conta> consultarContaCallback = new Callback<Conta>() {
            @Override
            public void onResponse(Call<Conta> call, Response<Conta> response) {
                Conta contaResposta;
                Log.d("onResponse", "entered in onResponse - Conta (Fechar)");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    contaResposta = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    fragment.responseConsultarContaReceived(contaResposta);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    fragment.responseConsultarContaReceived(null);
                }
            }

            @Override
            public void onFailure(Call<Conta> call, Throwable t) {
                Log.d("onFailure", "Ocorreu um erro ao chamar a API - Conta - Consultar conta");
                fragment.responseConsultarContaReceived(null);
            }
        };

        // call
        contaCall.enqueue(consultarContaCallback);

    }

    /**
     * Fechar a conta
     * @param fragment activity que receberá a resposta da REST API
     */
    public void fechar(final OrderFragment fragment) {
        Log.d("Conta", "Fechar conta");
        ContaClient service = ServiceGenerator.createService(ContaClient.class);
        Call<Conta> contaCall = service.consultarConta(this.codEstabelecimento, this.codComanda);

        Callback<Conta> consultarContaCallback = new Callback<Conta>() {
            @Override
            public void onResponse(Call<Conta> call, Response<Conta> response) {
                Conta contaResposta;
                Log.d("onResponse", "entered in onResponse - Conta (Fechar)");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    contaResposta = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    fragment.responseFecharContaReceived(contaResposta);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    fragment.responseFecharContaReceived(null);
                }
            }

            @Override
            public void onFailure(Call<Conta> call, Throwable t) {
                Log.d("onFailure", "Ocorreu um erro ao chamar a API - Conta - Fechar conta");
                Log.d("onFailure", t.getMessage());
                fragment.responseFecharContaReceived(null);
            }
        };

        // call
        contaCall.enqueue(consultarContaCallback);
    }

    /**
     * Pagar a conta
     * @param fragment activity que receberá a resposta da REST API
     */
    public void pagar(final BillPaymentFragment fragment, Cartao cartao) {
        Log.d("Pedido", "Realizar pedido");
        ContaClient service = ServiceGenerator.createService(ContaClient.class);
        Call<Pagamento> pagarContaCall = service.pagarConta(this.nrMesa, this.codComanda);

        Callback<Pagamento> pagarContaCallback = new Callback<Pagamento>() {
            @Override
            public void onResponse(Call<Pagamento> call, Response<Pagamento> response) {
                Pagamento respostaPagamento;
                Log.d("onResponse", "entered in onResponse - Pedido");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    respostaPagamento = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    fragment.responsePagarContaReceived(respostaPagamento);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    fragment.responsePagarContaReceived(null);
                }
            }

            @Override
            public void onFailure(Call<Pagamento> call, Throwable t) {
                Log.d("onFailure", "Ocorreu um erro ao chamar a API - Pedido");
                fragment.responsePagarContaReceived(null);
            }
        };

        // call
        pagarContaCall.enqueue(pagarContaCallback);
    }


}
