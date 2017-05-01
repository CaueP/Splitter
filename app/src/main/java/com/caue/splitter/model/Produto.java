package com.caue.splitter.model;

import android.util.Log;

import com.caue.splitter.CheckedInActivity;
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.model.services.ProdutoClient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe que representa os produtos disponíveis para venda em um estabelecimento
 * @author Caue Polimanti
 * @version 1.0
 * Created on 4/30/2017.
 */
public class Produto implements Serializable {
    @Expose
    @SerializedName("cod_produto")
    private int codigo;

    @Expose
    @SerializedName("nome_produto")
    private String nome;

    @Expose
    @SerializedName("cod_tp_alimento")
    private int codTipoAlimento;

    @Expose
    @SerializedName("dsc_produto")
    private String descricao;

    @Expose
    @SerializedName("val_produto")
    private double valor;

    @Expose
    @SerializedName("link_img_produto")
    private String urlImagem;

    // getters e setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodTipoAlimento() {
        return codTipoAlimento;
    }

    public void setCodTipoAlimento(int codTipoAlimento) {
        this.codTipoAlimento = codTipoAlimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public void obterCardapio(String codEstabelecimento, final CheckedInActivity activity){
        // criação do serviço para realizar operações de produto
        ProdutoClient service = ServiceGenerator.createService(ProdutoClient.class);
        Log.d("carregarUsuario", "Carregando usuario");
        // Service para consultar a lista de produtos para um estabelecimento
        Call<ArrayList<Produto>> checkinCall = service.getCardapio(codEstabelecimento);

        // callback ao receber a resposta da API
        Callback<ArrayList<Produto>> consultarCardapioCallback = new Callback<ArrayList<Produto>>() {

            @Override
            public void onResponse(Call<ArrayList<Produto>> call, Response<ArrayList<Produto>> response) {
                ArrayList<Produto> cardapioResposta;
                Log.d("onResponse", "entered in onResponse - Checkin");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    cardapioResposta = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    activity.responseCardapioReceived(cardapioResposta);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Produto>> call, Throwable t) {
                Log.d("onFailure", "Ocorreu um erro ao chamar a API - Checkin");
            }
        };

        // call
        checkinCall.enqueue(consultarCardapioCallback);
    }
}
