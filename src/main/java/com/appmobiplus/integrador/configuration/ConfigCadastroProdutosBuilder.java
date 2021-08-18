package com.appmobiplus.integrador.configuration;

import java.util.HashSet;
import java.util.Set;

public class ConfigCadastroProdutosBuilder {
    private String path;
    private Set<Header> headers = new HashSet<>();
    private String bodyJson;

    public ConfigCadastroProdutosBuilder(String path,
                                         Set<Header> headers,
                                         String bodyJson) {
        this.path = path;
        this.headers = headers;
        this.bodyJson = bodyJson;
    }

    public ConfigCadastroProdutosBuilder setPath(String path) {
        this.path = path;
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
        return new ConfigCadastroProdutos(path, headers, bodyJson);
    }
}
