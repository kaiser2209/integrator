package com.appmobiplus.integrador.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProdutoJson implements Serializable {
    private String ean;
    private String codigo;
    private String descricao;
    private float precoDe;
    private float precoPor;
    private String link_image;
    private List<ProdutoJson> sugeridos;

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPrecoDe() {
        return precoDe;
    }

    public void setPrecoDe(float precoDe) {
        this.precoDe = precoDe;
    }

    public float getPrecoPor() {
        return precoPor;
    }

    public void setPrecoPor(float precoPor) {
        this.precoPor = precoPor;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public List<ProdutoJson> getSugeridos() {
        return sugeridos;
    }

    public void setSugeridos(List<ProdutoJson> sugeridos) {
        this.sugeridos = sugeridos;
    }
}
