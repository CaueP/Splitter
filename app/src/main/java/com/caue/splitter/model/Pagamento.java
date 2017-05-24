package com.caue.splitter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Caue Polimanti
 * @version 2.0
 * Created on 5/24/2017.
 */
public class Pagamento implements Serializable {

    @Expose
    @SerializedName("pagamentoRealizado")
    private boolean pagamentoRealizado;

    @Expose
    @SerializedName("error")
    private String error;


    public boolean isPagamentoRealizado() {
        return pagamentoRealizado;
    }
    public String getError() {
        return error;
    }
}
