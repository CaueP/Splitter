package com.caue.splitter.controller;

import com.caue.splitter.model.callback.UsuarioService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.caue.splitter.helper.Constants;

/**
 * @author Caue
 * @version 1.0
 * @date 4/03/2017
 */

public class RestManager {

    // Service generico para a API
    private UsuarioService mUsuarioService;

    public UsuarioService getUsuarioService(){
        if(mUsuarioService == null){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mUsuarioService = retrofit.create(UsuarioService.class);
        }
        return mUsuarioService;
    }
}
