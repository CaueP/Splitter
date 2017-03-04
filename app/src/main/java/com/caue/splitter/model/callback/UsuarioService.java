package com.caue.splitter.model.callback;

import com.caue.splitter.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Caue
 * @version 1.0
 * @date 4/03/2017
 */

// Definição dos callbacks da REST API
public interface UsuarioService {

    // GET para consultar um usuario
    @GET("user/{txt_email}")
    Call<Usuario> getUsuario(@Path("txt_email") String email);

    // POST para cadastrar um usuario
    @POST("user/")
    Call<Usuario> postUsuario(@Body Usuario usuario);
}
