package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

public class ConfigCadastroProdutosBuilder {
    private String path;
    private HttpMethod method;
    private BuscaCadProdutos searchParameters;

    public ConfigCadastroProdutosBuilder(String path,
                                         HttpMethod method,
                                         BuscaCadProdutos searchParameters) {
        this.path = path;
        this.method = method;
        this.searchParameters = searchParameters;
    }

    public ConfigCadastroProdutosBuilder() {

    }

    public ConfigCadastroProdutosBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigCadastroProdutosBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ConfigCadastroProdutosBuilder setSearchParameters(BuscaCadProdutos searchParameters) {
        this.searchParameters = searchParameters;
        return this;
    }

    public ConfigCadastroProdutos build() {
        return new ConfigCadastroProdutos(path, method, searchParameters);
    }
}
