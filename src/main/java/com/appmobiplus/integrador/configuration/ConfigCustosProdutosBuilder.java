package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

public class ConfigCustosProdutosBuilder {
    private String path;
    private HttpMethod method;
    private BuscaCadProdutos searchParameters;

    public ConfigCustosProdutosBuilder(String path,
                                       HttpMethod method,
                                       BuscaCadProdutos searchParameters) {
        this.path = path;
        this.method = method;
        this.searchParameters = searchParameters;
    }

    public ConfigCustosProdutosBuilder() {

    }

    public ConfigCustosProdutosBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigCustosProdutosBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ConfigCustosProdutosBuilder setSearchParameters(BuscaCadProdutos searchParameters) {
        this.searchParameters = searchParameters;
        return this;
    }


    public ConfigCustosProdutos build() {
        return new ConfigCustosProdutos(path, method, searchParameters);
    }
}
