package com.caue.splitter.model;

import android.support.annotation.IntDef;
import android.util.Log;

import com.caue.splitter.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by CaueGarciaPolimanti on 5/21/2017.
 */

public class Cartao implements Serializable {

    @Expose
    @SerializedName("nome")
    private String nomeCartao;

    @Expose
    @SerializedName("numero")
    private long numero;

    @Expose
    @SerializedName("mesValidade")
    private int mesValidade;

    @Expose
    @SerializedName("anoValidade")
    private int anoValidade;

    @Expose
    @SerializedName("CVV")
    private int CVV;

    public Cartao (String nomeCartao, long numero, int mesValidade, int anoValidade, int CVV) throws Exception{
        this.nomeCartao = nomeCartao.trim();
        this.numero = numero;
        this.mesValidade = mesValidade;
        this.anoValidade = anoValidade;
        this.CVV = CVV;

        isValid();
    }

    /**
     * Valida se os dados do cartao sao validos
     * @return status de validade do cartao
     * @throws Exception codigo de invalidade do cartao
     */
    public boolean isValid() throws Exception{

        if (nomeCartao == null || nomeCartao.equals("")) {
            throw new RuntimeException(Constants.CARTAO_EXCEPTION.NomeInvalido);
        }
        if (numero == 0) {
            throw new RuntimeException(Constants.CARTAO_EXCEPTION.NumeroInvalido);
        }
        if(mesValidade < 1 && mesValidade > 12) {
            throw new RuntimeException(Constants.CARTAO_EXCEPTION.DataValidadeInvalida);
        }
        Calendar dataAtual = GregorianCalendar.getInstance();
        Calendar dataValidade = new GregorianCalendar(anoValidade, mesValidade, 1);
        if(dataAtual.after(dataValidade)) {
            throw new RuntimeException(Constants.CARTAO_EXCEPTION.CartaoVencido);
        }
        if(CVV == 0) {
            throw new RuntimeException(Constants.CARTAO_EXCEPTION.CVVInvalido);
        }
        return true;
    }
}
