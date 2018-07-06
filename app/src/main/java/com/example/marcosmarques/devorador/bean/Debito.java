package com.example.marcosmarques.devorador.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Debito extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String descricao;
    @Required
    private String tipo;
    @Required
    private String qtdParcela;
    private double valorParcela;
    private double valorTotal;
    @Required
    private String diaVencimento;
    private int qtdParcelaQuitada;
    @Required
    private String status;

    public Debito() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getQtdParcela() {
        return qtdParcela;
    }

    public void setQtdParcela(String qtdParcela) {
        this.qtdParcela = qtdParcela;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(String diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public int getQtdParcelaQuitada() {
        return qtdParcelaQuitada;
    }

    public void setQtdParcelaQuitada(int qtdParcelaQuitada) {
        this.qtdParcelaQuitada = qtdParcelaQuitada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
