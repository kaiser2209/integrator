package com.appmobiplus.integrador.firebase.databases;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Grupo implements Serializable {
    private String id;
    private String descricao;
    private boolean deleted;
    private List<Map<String, Object>> data;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
