package com.appmobiplus.integrador.configuration;

import java.util.HashMap;
import java.util.Map;

public class BuscaCadProdutos {
    private int page;
    private Map<String, String> clausulas = new HashMap<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map<String, String> getClausulas() {
        return clausulas;
    }

    public void setClausulas(Map<String, String> clausulas) {
        this.clausulas = clausulas;
    }
}
