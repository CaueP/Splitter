package com.caue.splitter.model;

import android.util.Log;

import com.caue.splitter.CheckedInActivity;
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.model.services.PedidoClient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CaueGarciaPolimanti on 5/1/2017.
 */

public class Pedido implements Serializable {

    @Expose
    @SerializedName("codEstabelecimento")
    private String codEstabelecimento;


    @Expose
    @SerializedName("nrMesa")
    private int nrMesa;

    @Expose
    @SerializedName("cod_comanda")
    private int codComanda;

    @Expose
    @SerializedName("cod_pedido")
    private int codigo;

    @Expose
    @SerializedName("cod_produto")
    private int codProduto;

    @Expose
    @SerializedName("nome_produto")
    private String nomeProduto;

    @Expose
    @SerializedName("qtd_produto")
    private int qtdProduto;

    @Expose
    @SerializedName("link_img_produto")
    private String urlImagem;

    @Expose
    @SerializedName("val_pedido")
    private double valorTotal;

    @Expose
    @SerializedName("val_a_pagar")
    private double valorPagar;

    @Expose
    @SerializedName("cod_status_pedido")
    private int codStatusPedido;

    @Expose
    @SerializedName("txt_observacao")
    private String descObservacao;

    // getters and setters
    public int getCodigo() {
        return codigo;
    }

    public double getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(double valorPagar) {
        this.valorPagar = valorPagar;
    }

    public int getNrMesa() {
        return nrMesa;
    }

    public void setNrMesa(int nrMesa) {
        this.nrMesa = nrMesa;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQtdProduto() {
        return qtdProduto;
    }

    public void setQtdProduto(int qtdProduto) {
        this.qtdProduto = qtdProduto;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getCodStatusPedido() {
        return codStatusPedido;
    }

    public void setCodStatusPedido(int codStatusPedido) {
        this.codStatusPedido = codStatusPedido;
    }

    public String getDescObservacao() {
        return descObservacao;
    }

    public void setDescObservacao(String descObservacao) {
        this.descObservacao = descObservacao;
    }

    public String getCodEstabelecimento() {
        return codEstabelecimento;
    }

    public void setCodEstabelecimento(String codEstabelecimento) {
        this.codEstabelecimento = codEstabelecimento;
    }

    public int getCodComanda() {
        return codComanda;
    }

    public void setCodComanda(int codComanda) {
        this.codComanda = codComanda;
    }

    public int getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(int codProduto) {
        this.codProduto = codProduto;
    }

    // construtor
    public Pedido(String codEstabelecimento, int nrMesa, int codComanda, int codProduto, int qtdProduto, String descObservacao){
        this.codEstabelecimento = codEstabelecimento;
        this.nrMesa = nrMesa;
        this.codComanda = codComanda;
        this.codProduto = codProduto;
        this.qtdProduto = qtdProduto;
        this.descObservacao = descObservacao;
    }

    /**
     * Realizar o pedido de um item
     * @param activity activity que receberá a resposta da REST API
     */
    public void pedir(final CheckedInActivity activity) {
        Log.d("Pedido", "Realizar pedido");
        PedidoClient service = ServiceGenerator.createService(PedidoClient.class);
        Call<Pedido> pedidoCall = service.realizarPedido(this);

        Callback<Pedido> realizarPedidoCallback = new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                Pedido pedidoResposta;
                Log.d("onResponse", "entered in onResponse - Pedido");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    pedidoResposta = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    activity.responseRealizarPedidoReceived(pedidoResposta);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    activity.responseRealizarPedidoReceived(null);
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.d("onFailure", "Ocorreu um erro ao chamar a API - Pedido");
                activity.responseRealizarPedidoReceived(null);
            }
        };

        // call
        pedidoCall.enqueue(realizarPedidoCallback);

    }
}
