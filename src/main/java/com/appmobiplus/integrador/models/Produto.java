package com.appmobiplus.integrador.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Produto {
    @Id
    private String ean;
    private String descricao;
    private float preco_de;
    private float preco_por;
    private String link_image;

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
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

}
