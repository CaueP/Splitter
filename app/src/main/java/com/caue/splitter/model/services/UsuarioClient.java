package com.caue.splitter.model.services;

import com.caue.splitter.model.Usuario;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Caue Polimanti
 * @version 1.0
 * created on 4/03/2017
 */

// Definição dos callbacks da REST API
public interface UsuarioClient {

    // GET para consultar um usuario
    @GET("usuario/{email}")
    Call<Usuario> getUsuario(@Path("email") String email);

    // POST para cadastrar um usuario
    @POST("usuario")
    Call<Usuario> postUsuario(@Body Usuario usuario);

    // PUT para atualizar um usuario
    @PUT("usuario/{email}")
    Call<Usuario> atualizarUsuario(@Body Usuario usuario, @Path("email") String email);

    // DELETE para desativar um usuário
    @DELETE("usuario/{email}")
    Call<ResponseBody> desativarUsuario(@Path("email") String email);
}
