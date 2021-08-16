package com.appmobiplus.integrador.configuration;

import java.util.HashSet;
import java.util.Set;

public class ProdutoBuilder {
    private String ean;
    private String descricao;
    private float preco_de;
    private float preco_por;
    private String link_image;
    private Set<Produto> sugestoes = new HashSet<>();

    public ProdutoBuilder() {

    }

    public ProdutoBuilder(String ean,
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

    public static ProdutoBuilder get() {
        return new ProdutoBuilder();
    }

    public ProdutoBuilder setEan(String ean) {
        this.ean = ean;
        return this;
    }

    public ProdutoBuilder setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ProdutoBuilder setPreco_de(float preco_de) {
        this.preco_de = preco_de;
        return this;
    }

    public ProdutoBuilder setPreco_por(float preco_por) {
        this.preco_por = preco_por;
        return this;
    }

    public ProdutoBuilder setLink_image(String link_image) {
        this.link_image = link_image;
        return this;
    }

    public ProdutoBuilder setSugestoes(Set<Produto> sugestoes) {
        this.sugestoes = sugestoes;
        return this;
    }

    public Produto build() {
        return new Produto(ean, descricao, preco_de, preco_por, link_image, sugestoes);
    }
}
