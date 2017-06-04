package com.caue.splitter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Classe que define o participante de uma mesa
 * @author Cauê Garcia Polimanti
 * @version 1.0
 * Created on 6/4/2017.
 */
public class Participante implements Serializable{


    @Expose
    @SerializedName("nome")
    private String nome;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("url_foto")
    private String urlFoto;

    @Expose
    @SerializedName("comanda")
    private String comanda;

    /**
     * Construtor
     * @param nome
     * @param urlFoto
     */
    public Participante(String nome, String urlFoto) {
        this.nome = nome;
        this.urlFoto = urlFoto;
    }

    // getters e setter
    public String getNome() {
        return nome;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public String getEmail() {
        return email;
    }

    public String getComanda() {
        return comanda;
    }
}
