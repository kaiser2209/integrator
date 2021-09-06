package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class ConfigCadastroProdutos implements Serializable, ConfigBuscaProduto {
    private String path;
    private HttpMethod method;
    private BuscaCadProdutos searchParameters;
    private Map<String, String> urlParameters;

    public ConfigCadastroProdutos(String path,
                                  HttpMethod httpMethod,
                                  BuscaCadProdutos searchParameters,
                                  Map<String, String> urlParameters) {
        this.path = path;
        this.method = httpMethod;
        this.searchParameters = searchParameters;
        this.urlParameters = urlParameters;
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

    public Map<String, String> getUrlParameters() {
        return this.getUrlParameters();
    }

    public void setUrlParameters(Map<String, String> urlParameters) {
        this.urlParameters = urlParameters;
    }

    public String toString() {
        return "[Path: " + this.path + "\n" +
                "HttpMethod: " + this.method + "\n" +
                "SearchParameters: " + this.searchParameters + "]";
    }
}
