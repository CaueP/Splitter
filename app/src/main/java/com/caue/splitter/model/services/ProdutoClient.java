package com.caue.splitter.model.services;

import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Produto;
import com.caue.splitter.model.Usuario;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Caue Polimanti
 * @version 1.0
 * Created on 4/30/2017.
 */
public interface ProdutoClient {
    // GET para consultar o cardapio de um estabelecimento
    @GET("cardapio/{codEstabelecimento}")
    Call<ArrayList<Produto>> getCardapio(@Path("codEstabelecimento") String codEstabelecimento);
}
