package com.caue.splitter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Caue
 * @version 1.0
 * @date 4/03/2017
 */

public class Usuario implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("txt_nome")
    private String nome;

    @Expose
    @SerializedName("txt_senha")
    private String senha;

    @Expose
    @SerializedName("nr_cpf")
    private long cpf;

    @Expose
    @SerializedName("dt_nascimento")
    private String dtNascimento;

    @Expose
    @SerializedName("txt_email")
    private String email;

    @Expose
    @SerializedName("nr_telefone")
    private long telefone;

    @Expose
    @SerializedName("conta_ativa")
    private int contaAtiva;

    // construtor


    public Usuario(int id, String nome, String senha, long cpf, String dtNascimento, String email, long telefone, int contaAtiva) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.email = email;
        this.telefone = telefone;
        this.contaAtiva = contaAtiva;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public long getCpf() {
        return cpf;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelefone() {
        return telefone;
    }

    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }

    public int getContaAtiva() {
        return contaAtiva;
    }

    public void setContaAtiva(int contaAtiva) {
        this.contaAtiva = contaAtiva;
    }

}
