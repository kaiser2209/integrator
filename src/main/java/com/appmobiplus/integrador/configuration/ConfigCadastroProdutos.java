package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Set;

public class ConfigCadastroProdutos implements Serializable {
    private String path;
    private HttpMethod method;
    private Set<Header> headers;
    private String bodyJson;

    public ConfigCadastroProdutos(String path,
                                  HttpMethod httpMethod,
                                  Set<Header> headers,
                                  String bodyJson) {
        this.path = path;
        this.method = httpMethod;
        this.headers = headers;
        this.bodyJson = bodyJson;
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

    public Set<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<Header> headers) {
        this.headers = headers;
    }

    public String getBodyJson() {
        return bodyJson;
    }

    public void setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
    }
}
