package com.caue.splitter.model.services;

import com.caue.splitter.model.Pedido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Caue Polimanti
 * @version 2.0
 * Created on 5/1/2017.
 */
public interface PedidoClient {
    // POST para realizar um novo pedido
    @POST("pedido/")
    Call<Pedido> realizarPedido(@Body Pedido pedido);
}
