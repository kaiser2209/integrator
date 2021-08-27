package com.appmobiplus.integrador.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto {
    private String ean;
    private String descricao;
    private float preco_de;
    private float preco_por;
    private String link_image;
    private Set<Produto> sugestoes = new HashSet<>();

    protected Produto(String ean,
                      String descricao,
                      float preco_de,
                      float preco_por,
                      String link_image,
                      Set<Produto> sugestoes) {
        this.ean = ean;
        this.descricao = descricao;
        this.preco_de = preco_de;
        this.preco_por = preco_por;
        this.link_image = link_image;
        this.sugestoes = sugestoes;
    }

    public Produto() {

    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public void setEan(long ean) {
        this.ean = String.valueOf(ean);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco_de() {
        return preco_de;
    }

    public void setPreco_de(float preco_de) {
        this.preco_de = preco_de;
    }

    public float getPreco_por() {
        return preco_por;
    }

    public void setPreco_por(float preco_por) {
        this.preco_por = preco_por;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public Set<Produto> getSugestoes() {
        return sugestoes;
    }

    public void setSugestoes(Set<Produto> sugestoes) {
        this.sugestoes = sugestoes;
    }

    public boolean set(String field, Object value) {
        try {
            Field f = this.getClass().getDeclaredField(field);
            f.setAccessible(true);
            if (field.equals("ean"))
                f.set(this, String.valueOf(value));
            else
                f.set(this, value);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String toString() {
        return "ean: " + ean +
                "\ndescricao: " + descricao +
                "\npreco_de: " + preco_de +
                "\npreco_por: " + preco_por +
                "\nlink_image: " + link_image +
                "\nsugestoes: " + sugestoes.toString();
    }
}
