package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Set;

public class ConfigCustosProdutos implements Serializable, ConfigBuscaProduto {
    private String path;
    private HttpMethod method;
    private BuscaCadProdutos searchParameters;

    public ConfigCustosProdutos(String path,
                                HttpMethod method,
                                BuscaCadProdutos searchParameters) {
        this.path = path;
        this.method = method;
        this.searchParameters = searchParameters;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public BuscaCadProdutos getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(BuscaCadProdutos searchParameters) {
        this.searchParameters = searchParameters;
    }


    public String toString() {
        return "[Path: " + this.path + "\n" +
                "HttpMethod: " + this.method + "\n" +
                "SearchParameters: " + this.searchParameters + "]";
    }
}
