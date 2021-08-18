package com.appmobiplus.integrador.configuration;

import java.util.HashSet;
import java.util.Set;

public class ConfigCustosProdutosBuilder {
    private String path;
    private Set<Header> headers = new HashSet<>();
    private String bodyJson;

    public ConfigCustosProdutosBuilder(String path,
                                         Set<Header> headers,
                                         String bodyJson) {
        this.path = path;
        this.headers = headers;
        this.bodyJson = bodyJson;
    }

    public ConfigCustosProdutosBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigCustosProdutosBuilder setHeaders(Set<Header> headers) {
        this.headers = headers;
        return this;
    }

    public ConfigCustosProdutosBuilder addHeader(String key, String value) {
        Header h = new HeaderBuilder().setKey(key).setValue(value).build();
        this.headers.add(h);
        return this;
    }

    public ConfigCustosProdutos build() {
        return new ConfigCustosProdutos(path, headers, bodyJson);
    }
}
