package com.caue.splitter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cgpolim on 11/04/2017.
 */

public class Mesa implements Serializable {

    @Expose
    @SerializedName("qrCode")
    private String qrCode;

    @Expose
    @SerializedName("nrMesa")
    private int nrMesa;

    @Expose
    @SerializedName("codEstabelecimento")
    private String codEstabelecimento;


    @Expose
    @SerializedName("qrCodeOcupado")
    private String qrCodeOcupado;

    @Expose
    @SerializedName("usuarioResponsavel")
    private String usuarioResponsavel;

    @Expose
    @SerializedName("tipoDivisao")
    private int tipoDivisao;

    //getters and setters

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getNrMesa() {
        return nrMesa;
    }

    public void setNrMesa(int nrMesa) {
        this.nrMesa = nrMesa;
    }

    public String getCodEstabelecimento() {
        return codEstabelecimento;
    }

    public void setCodEstabelecimento(String codEstabelecimento) {
        this.codEstabelecimento = codEstabelecimento;
    }

    public int getTipoDivisao() {
        return tipoDivisao;
    }

    public void setTipoDivisao(int tipoDivisao) {
        this.tipoDivisao = tipoDivisao;
    }

    public String getQrCodeOcupado() {
        return qrCodeOcupado;
    }
    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

}