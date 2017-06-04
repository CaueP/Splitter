package com.caue.splitter.model.services;

import com.caue.splitter.model.Conta;
import com.caue.splitter.model.Pagamento;
import com.caue.splitter.model.Participante;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Cauê Garcia Polimanti
 * @version 1.0
 * Created on 6/4/2017.
 */
public interface MesaClient {
    // GET para consultar os participantes em uma mesa
    @GET("mesa/participante/{codEstabelecimento}/{nrMesa}")
    Call<ArrayList<Participante>> consultarParticipante(@Path("codEstabelecimento") String codEstabelecimento, @Path("nrMesa") int nrMesa);
}
