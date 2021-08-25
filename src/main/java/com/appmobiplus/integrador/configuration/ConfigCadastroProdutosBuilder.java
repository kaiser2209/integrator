package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

public class ConfigCadastroProdutosBuilder {
    private String path;
    private HttpMethod method;
    private Set<Header> headers = new HashSet<>();
    private String bodyJson;

    public ConfigCadastroProdutosBuilder(String path,
                                         HttpMethod method,
                                         Set<Header> headers,
                                         String bodyJson) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.bodyJson = bodyJson;
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

    public ConfigCadastroProdutosBuilder setHeaders(Set<Header> headers) {
        this.headers = headers;
        return this;
    }

    public ConfigCadastroProdutosBuilder addHeader(String key, String value) {
        Header h = new HeaderBuilder().setKey(key).setValue(value).build();
        this.headers.add(h);
        return this;
    }

    public ConfigCadastroProdutos build() {
        return new ConfigCadastroProdutos(path, method, headers, bodyJson);
    }
}
