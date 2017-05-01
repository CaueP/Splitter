package com.caue.splitter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CaueGarciaPolimanti on 4/26/2017.
 */

public class Comanda implements Serializable {

    @Expose
    @SerializedName("codComanda")
    private int codComanda;

    public int getCodComanda() {
        return codComanda;
    }

    public void setCodComanda(int codComanda) {
        this.codComanda = codComanda;
    }
}
