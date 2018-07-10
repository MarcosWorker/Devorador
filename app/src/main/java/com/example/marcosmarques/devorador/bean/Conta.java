package com.example.marcosmarques.devorador.bean;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Conta extends RealmObject {

    @PrimaryKey
    private String id;
    private boolean cartao;
    @Required
    private String descricao;
    private RealmList<ItemConta> itens;
    private double totalConta;
    private int diaVencimento;
    private int mesPago;

    public Conta() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCartao() {
        return cartao;
    }

    public void setCartao(boolean cartao) {
        this.cartao = cartao;
    }

    public RealmList<ItemConta> getItens() {
        return itens;
    }

    public void setItens(RealmList<ItemConta> itens) {
        this.itens = itens;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getTotalConta() {
        return totalConta;
    }

    public void setTotalConta(double totalConta) {
        this.totalConta = totalConta;
    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public int getMesPago() {
        return mesPago;
    }

    public void setMesPago(int mesPago) {
        this.mesPago = mesPago;
    }
}
