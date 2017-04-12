package com.caue.splitter.model.services;

import com.caue.splitter.model.Checkin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by cgpolim on 11/04/2017.
 */

public interface CheckinClient {
    // POST para cadastrar um usuario
    @POST("checkin")
    Call<Checkin> realizarCheckin(@Body Checkin checkin);
}
