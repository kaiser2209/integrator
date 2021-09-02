package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class ConfigCadastroProdutoBuilder {
    private HttpMethod method;
    private String path;
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> body = new HashMap<>();
    private DataType dataType;

    public ConfigCadastroProdutoBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ConfigCadastroProdutoBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigCadastroProdutoBuilder setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ConfigCadastroProdutoBuilder addParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    public ConfigCadastroProdutoBuilder setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public ConfigCadastroProdutoBuilder addHeader(String key, String value) {
        this.header.put(key, value);
        return this;
    }

    public ConfigCadastroProdutoBuilder setBody(Map<String, String> body) {
        this.body = body;
        return this;
    }

    public ConfigCadastroProdutoBuilder addBody(String key, String value) {
        this.body.put(key, value);
        return this;
    }

    public ConfigCadastroProdutoBuilder setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public ConfigCadastroProduto build() {
        return new ConfigCadastroProduto(method, path, parameters, header, body, dataType);
    }

}
