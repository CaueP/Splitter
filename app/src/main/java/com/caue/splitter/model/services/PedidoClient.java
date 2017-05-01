package com.caue.splitter.model.services;

import com.caue.splitter.model.Pedido;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by CaueGarciaPolimanti on 5/1/2017.
 */

public interface PedidoClient {
    // POST para realizar um novo pedido
    @POST("pedido/")
    Call<Pedido> realizarPedido(@Body Pedido pedido);
}
