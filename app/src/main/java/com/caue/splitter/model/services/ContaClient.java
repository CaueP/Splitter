package com.caue.splitter.model.services;

import com.caue.splitter.model.Conta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by CaueGarciaPolimanti on 5/20/2017.
 */
public interface ContaClient {
    // GET para consultar os pedidos realizados e o total de uma comanda
    @GET("conta/{codEstabelecimento}/{codComanda}")
    Call<Conta> consultarConta(@Path("codEstabelecimento") String codEstabelecimento, @Path("codComanda") int codComanda);
}
