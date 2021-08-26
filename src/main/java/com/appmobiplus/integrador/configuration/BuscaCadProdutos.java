package com.appmobiplus.integrador.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscaCadProdutos implements Serializable {
    private int page;
    private List<Clausula> clausulas = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Clausula> getClausulas() {
        return clausulas;
    }

    public void setClausulas(List<Clausula> clausulas) {
        this.clausulas = clausulas;
    }

    public void addClausula(String campo, long valor, String operador) {
        this.clausulas.add(new Clausula(campo, valor, operador));
    }

    public void changeValue(long valor) {
        this.clausulas.get(0).setValor(valor);
    }

    public String getCampo() {
        return this.clausulas.get(0).getCampo();
    }

    public String toString() {
        return "{Page: " + this.page + "\n" +
                "Clausulas: " + this.clausulas + "}";
    }
}

class Clausula implements Serializable {
    String campo;
    long valor;
    String operador;

    public Clausula(String campo, long valor, String operador) {
        this.campo = campo;
        this.valor = valor;
        this.operador = operador;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String toString() {
        return "[Campo: " + this.campo + ", " +
                "Valor: " + this.valor + ", " +
                "Operador: " + this.operador + "]";
    }
}
