package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigCadastroProdutosBuilder {
    private String path;
    private HttpMethod method;
    private BuscaCadProdutos searchParameters;
    private Map<String, String> urlParameters;

    public ConfigCadastroProdutosBuilder(String path,
                                         HttpMethod method,
                                         BuscaCadProdutos searchParameters,
                                         Map<String, String> urlParameters) {
        this.path = path;
        this.method = method;
        this.searchParameters = searchParameters;
        this.urlParameters = urlParameters;
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

    public ConfigCadastroProdutosBuilder setUrlParameters(Map<String, String> urlParameters) {
        this.urlParameters = urlParameters;
        return this;
    }

    public ConfigCadastroProdutosBuilder addUrlParameter(String key, String value) {
        this.urlParameters.put(key, value);
        return this;
    }

    public ConfigCadastroProdutos build() {
        return new ConfigCadastroProdutos(path, method, searchParameters, urlParameters);
    }
}
