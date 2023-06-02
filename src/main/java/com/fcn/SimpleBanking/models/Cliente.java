package com.fcn.SimpleBanking.models;

import java.math.BigDecimal;

public class Cliente {
    private String nome;
    private boolean Exclusive;
    private BigDecimal saldo;
    private String nConta;
    private String dtNascimento;

    // Construtor da classe
    public Cliente(String nome, boolean planoExclusive, BigDecimal saldo, String numeroConta, String dataNascimento) {
        this.nome = nome;
        this.Exclusive = planoExclusive;
        this.saldo = saldo;
        this.nConta = numeroConta;
        this.dtNascimento = dataNascimento;
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isPlanoExclusive() {
        return Exclusive;
    }

    public void setPlanoExclusive(boolean planoExclusive) {
        this.Exclusive = planoExclusive;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumeroConta() {
        return nConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.nConta = numeroConta;
    }

    public String getDataNascimento() {
        return dtNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dtNascimento = dataNascimento;
    }
}